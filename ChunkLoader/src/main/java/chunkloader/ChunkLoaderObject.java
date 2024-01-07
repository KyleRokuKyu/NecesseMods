package chunkloader;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import necesse.engine.GameEvents;
import necesse.engine.events.GameEvent;
import necesse.engine.events.loot.ObjectLootTableDropsEvent;
import necesse.engine.localization.Localization;
import necesse.engine.network.server.ServerClient;
import necesse.engine.tickManager.TickManager;
import necesse.entity.Entity;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.pickup.ItemPickupEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObject;
import necesse.level.maps.light.GameLight;
import necesse.level.maps.multiTile.MultiTile;

public class ChunkLoaderObject extends GameObject {
    public GameTexture texture;

    public final String textureName;

    protected ChunkLoaderObject(String textureName, Color mapColor, Item.Rarity rarity) {
        this.textureName = textureName;
        this.mapColor = mapColor;
        this.rarity = rarity;
        this.displayMapTooltip = true;
        this.drawDamage = false;
        this.toolType = ToolType.ALL;
        this.isLightTransparent = true;
    }

    public void loadTextures() {
        super.loadTextures();
        this.texture = GameTexture.fromFile("objects/" + this.textureName);
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX) - this.texture.getWidth() / 2 + 16;
        int tileDrawY = camera.getTileDrawY(tileY);
        int drawY = tileDrawY - this.texture.getHeight() / 2;
        //TextureDrawOptionsEnd textureDrawOptionsEnd1 = this.texture.initDraw().sprite(0, 0, 32).light(light).pos(drawX, tileDrawY);
        final TextureDrawOptionsEnd objOptions = this.texture.initDraw().section(0, this.texture.getWidth(), 0, this.texture.getHeight()).light(light).pos(drawX, drawY);
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            public int getSortY() {
                return 20;
            }

            public void draw(TickManager tickManager) {
                objOptions.draw();
            }
        });
        tileList.add(tm -> objOptions.draw());
    }

    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX) - this.texture.getWidth() / 2 + 16;
        int tileDrawY = camera.getTileDrawY(tileY);
        int drawY = tileDrawY - this.texture.getHeight() / 2 + 16;
        this.texture.initDraw().sprite(0, 0, 32).alpha(alpha).draw(drawX, tileDrawY);
        this.texture.initDraw().section(0, this.texture.getWidth(), 0, this.texture.getHeight()).alpha(alpha).draw(drawX, drawY);
    }

    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new ChunkLoaderObjectEntity<>(this.textureName, level, x, y);
    }

    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips out = super.getItemTooltips(item, perspective);
        out.add(Localization.translate("itemtooltip", getStringID() + "tip"));
        return out;
    }

    public void placeObject(Level level, int x, int y, int rotation) {
        level.setObject(x, y, getID());
        level.setObjectRotation(x, y, (byte)rotation);
        if (level.isServer()) {
            ObjectEntity objectEntity = getNewObjectEntity(level, x, y);
            if (objectEntity != null)
                level.entityManager.objectEntities.add(objectEntity);
        }
        MultiTile multiTile = getMultiTile(rotation);
        if (multiTile.isMaster)
            multiTile.streamOtherObjects(x, y).forEach(e -> (e.value).placeObject(level, e.tileX, e.tileY, rotation));

        ChunkLoader.AddChunkLoader(level, getCurrentObjectEntity(level, x, y).getUniqueID());
    }

    public void onDestroyed(Level level, int x, int y, ServerClient client, ArrayList<ItemPickupEntity> itemsDropped) {
        ObjectLootTableDropsEvent dropsEvent = null;
        if (itemsDropped != null) {
            ArrayList<InventoryItem> drops = getDroppedItems(level, x, y);
            GameEvents.triggerEvent((GameEvent)(dropsEvent = new ObjectLootTableDropsEvent(new LevelObject(level, x, y), new Point(x * 32 + 16, y * 32 + 16), drops)));
            if (dropsEvent.dropPos != null && dropsEvent.drops != null)
                for (InventoryItem item : dropsEvent.drops) {
                    ItemPickupEntity droppedItem = item.getPickupEntity(level, dropsEvent.dropPos.x, dropsEvent.dropPos.y);
                    level.entityManager.pickups.add(droppedItem);
                    itemsDropped.add(droppedItem);
                }
        }
        if (client != null)
            client.newStats.objects_mined.increment(1);
        if (!level.isServer())
            spawnDestroyedParticles(level, x, y);
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(x, y);
        level.setObject(x, y, 0);
        if (objectEntity != null) {
            objectEntity.onObjectDestroyed(this, client, itemsDropped);
            objectEntity.remove();
        }

        ChunkLoader.RemoveChunkLoader(getCurrentObjectEntity(level, x, y).getUniqueID());
    }
}