package railevator;

import java.awt.*;
import java.util.List;
import necesse.engine.localization.Localization;
import necesse.engine.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.LadderUpObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.objectEntity.PortalObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.ObjectHoverHitbox;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

class RailevatorUpObject extends GameObject {
    public GameTexture texture;

    public int railevatorDownObjectID = -1;

    public final String textureName;

    public final int targetDestination;

    protected RailevatorUpObject(String textureName, int targetDestination, Color mapColor) {
        this.textureName = textureName;
        this.targetDestination = targetDestination;
        this.mapColor = mapColor;
        this.drawDamage = false;
        this.toolType = ToolType.UNBREAKABLE;
        this.isLightTransparent = true;
        this.lightLevel = 75;
    }

    public void loadTextures() {
        super.loadTextures();
        this.texture = GameTexture.fromFile("objects/" + this.textureName + "up");
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX) - this.texture.getWidth() / 2 + 16;
        int drawY = camera.getTileDrawY(tileY) - this.texture.getHeight() / 2 + 16;
        final TextureDrawOptionsEnd options = this.texture.initDraw().section(0, this.texture.getWidth(), 0, this.texture.getHeight()).light(light).pos(drawX, drawY);
        list.add(new LevelSortedDrawable(this, tileX, tileY) {
            public int getSortY() {
                return 16;
            }

            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }

    public List<ObjectHoverHitbox> getHoverHitboxes(Level level, int tileX, int tileY) {
        List<ObjectHoverHitbox> list = super.getHoverHitboxes(level, tileX, tileY);
        list.add(new ObjectHoverHitbox(tileX, tileY, 0, -20, 32, 20));
        return list;
    }

    public String canPlace(Level level, int x, int y, int rotation) {
        if (!level.isIslandPosition())
            return "notisland";
        if (level.getIslandDimension() != this.targetDestination)
            return "notcave";
        return null;
    }

    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return (ObjectEntity)new RailevatorUpObjectEntity(this.textureName + "up", level, x, y, 0, this.railevatorDownObjectID, (this.texture == null) ? null : new GameSprite(this.texture, 0, 0, 32), new Rectangle(-8, -8, 48, 48));
    }
}
