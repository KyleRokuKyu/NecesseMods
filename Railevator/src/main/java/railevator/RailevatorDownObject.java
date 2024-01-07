package railevator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import necesse.engine.localization.Localization;
import necesse.engine.network.Packet;
import necesse.engine.network.packet.PacketChangeObject;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.entity.TileDamageType;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.LadderDownObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.objectEntity.PortalObjectEntity;
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
import necesse.level.maps.hudManager.HudDrawElement;
import necesse.level.maps.hudManager.floatText.ChatBubbleText;
import necesse.level.maps.light.GameLight;

public class RailevatorDownObject extends GameObject {
    public GameTexture texture;

    public int railevatorUpObjectID = -1;

    public final String textureName;

    public final int destinationDimension;

    protected RailevatorDownObject(String textureName, int destinationDimension, Color mapColor, Item.Rarity rarity) {
        this.textureName = textureName;
        this.destinationDimension = destinationDimension;
        this.mapColor = mapColor;
        this.rarity = rarity;
        this.displayMapTooltip = true;
        this.drawDamage = false;
        this.toolType = ToolType.ALL;
        this.isLightTransparent = true;
    }

    public void loadTextures() {
        super.loadTextures();
        this.texture = GameTexture.fromFile("objects/" + this.textureName + "down");
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX) - this.texture.getWidth() / 2 + 16;
        int tileDrawY = camera.getTileDrawY(tileY);
        int drawY = tileDrawY - this.texture.getHeight() / 2;
        TextureDrawOptionsEnd textureDrawOptionsEnd1 = this.texture.initDraw().sprite(0, 0, 32).light(light).pos(drawX, tileDrawY);
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

    public String canPlace(Level level, int x, int y, int rotation) {
        String error = super.canPlace(level, x, y, rotation);
        if (error != null)
            return error;
        if (!level.isIslandPosition())
            return "notisland";
        if (level.getIslandDimension() != 0)
            return "notsurface";
        return null;
    }

    public void attemptPlace(Level level, int x, int y, PlayerMob player, String error) {
        if (level.isClient() && error.equals("notsurface"))
            (player.getLevel()).hudManager.addElement((HudDrawElement)new ChatBubbleText((Mob)player, Localization.translate("misc", "laddernotsurface")));
    }

    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return (ObjectEntity)new RailevatorDownObjectEntity(this.textureName + "down", level, x, y, this.destinationDimension, getID(), this.railevatorUpObjectID, new Rectangle(-2, -2, 37, 48));
    }

    public void onDestroyed(Level level, int x, int y, ServerClient client, ArrayList<ItemPickupEntity> itemsDropped) {
        if (level.isServer()) {
            ObjectEntity objectEntity = level.entityManager.getObjectEntity(x, y);
            if (objectEntity instanceof PortalObjectEntity) {
                PortalObjectEntity portal = (PortalObjectEntity)objectEntity;
                if ((level.getServer()).world.levelExists(portal.getDestinationIdentifier())) {
                    Level nextLevel = (level.getServer()).world.getLevel(portal.getDestinationIdentifier());
                    if (nextLevel.getObjectID(portal.destinationTileX, portal.destinationTileY) == this.railevatorUpObjectID) {
                        nextLevel.setObject(portal.destinationTileX, portal.destinationTileY, 0);
                        (level.getServer()).network.sendToClientsWithTile((Packet)new PacketChangeObject(level, portal.destinationTileX, portal.destinationTileY, 0), nextLevel, portal.destinationTileX, portal.destinationTileY);
                    }
                }
            }
        }
        super.onDestroyed(level, x, y, client, itemsDropped);
    }

    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips out = super.getItemTooltips(item, perspective);
        out.add(Localization.translate("itemtooltip", getStringID() + "tip"));
        return out;
    }

    public static int[] registerRailevatorPair(String textureName, int destinationDimension, Color debrisColor, Item.Rarity rarity, int itemBrokerValue) {
        RailevatorDownObject downObject = new RailevatorDownObject(textureName, destinationDimension, debrisColor, rarity);
        RailevatorUpObject upObject = new RailevatorUpObject(textureName, destinationDimension, debrisColor);
        int downObjectID = ObjectRegistry.registerObject(textureName + "down", downObject, itemBrokerValue, true);
        int upObjectID = ObjectRegistry.registerObject(textureName + "up", upObject, 0.0F, false);
        downObject.railevatorUpObjectID = upObjectID;
        upObject.railevatorDownObjectID = downObjectID;
        return new int[] { downObjectID, upObjectID };
    }
}