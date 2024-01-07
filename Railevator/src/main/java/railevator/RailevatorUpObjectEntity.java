package railevator;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

import necesse.engine.commands.serverCommands.GetTeamServerCommand;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.Packet;
import necesse.engine.network.packet.PacketChangeObject;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.tickManager.TickManager;
import necesse.engine.util.ComputedFunction;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.util.TeleportResult;
import necesse.entity.TileDamageType;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.PortalObjectEntity;
import necesse.gfx.gameTexture.GameSprite;
import necesse.gfx.gameTooltips.GameTooltips;
import necesse.gfx.gameTooltips.StringTooltips;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;

public class RailevatorUpObjectEntity extends PortalObjectEntity {
    private int railevatorDownID;

    private Rectangle collision;

    private GameSprite mapSprite;

    public int resetTime;

    private long hardResetTime;

    public RailevatorUpObjectEntity(String type, Level level, int x, int y, int dimension, int ladderDownID, GameSprite mapSprite, Rectangle collision) {
        super(level, type, x, y, level.getIdentifier(), x, y);
        LevelIdentifier identifier = level.getIdentifier();
        if (identifier.isIslandPosition())
            this.destinationIdentifier = new LevelIdentifier(identifier.getIslandX(), identifier.getIslandY(), 0);
        this.railevatorDownID = ladderDownID;
        this.mapSprite = mapSprite;
        this.collision = collision;
        this.resetTime = 100;
    }

    public Rectangle getCollision() {
        return new Rectangle(getX() * 32 + this.collision.x, getY() * 32 + this.collision.y, this.collision.width, this.collision.height);
    }

    public void clientTick() {
        checkCollision();
    }

    public void serverTick() {
        if (!getLevel().isCave) {
            remove();
        }
        checkCollision();
    }

    private void checkCollision() {
        if (getWorldEntity().getTime() <= this.hardResetTime)
            return;

        ArrayList<PlayerMob> playersInRange = getLevel().entityManager.players.getInRegionRangeByTile(getX(), getY(), 1);
        for (PlayerMob playerMob : playersInRange) {
            if (playerMob.getRemainingSpawnInvincibilityTime() > 0) {
                continue;
            }

            ServerClient client = getServer() != null ? playerMob.getServerClient() : getClient().getLocalServer().getLocalServerClient();
            if (playerMob.isRiding() && playerMob.getCollision().intersects(getCollision())) {
                playerMob.refreshSpawnTime();
                this.hardResetTime = getWorldEntity().getTime() + this.resetTime;
                Level oldLevel = getLevel();

                teleportClientToAroundDestination((playerMob.getX()/32) - getX(), (playerMob.getY()/32) - getY(), client, level -> {
                    client.newStats.ladders_used.increment(1);
                    if (level.getObjectID(this.destinationTileX, this.destinationTileY) != this.railevatorDownID) {
                        oldLevel.setObject(getTileX(), getTileY(), 0);
                        getServer().network.sendToClientsWithTile((Packet) new PacketChangeObject(level, getTileX(), getTileY(), 0), oldLevel, getTileX(), getTileY());
                    }
                    return true;
                });
                runClearMobs(client);
            }
        }
    }

    public boolean shouldDrawOnMap() {
        return true;
    }

    public Rectangle drawOnMapBox() {
        return new Rectangle(-12, -12, 24, 24);
    }

    public void drawOnMap(TickManager tickManager, int x, int y) {
        this.mapSprite.initDraw().size(24, 24).draw(x - 12, y - 12);
    }

    public GameTooltips getMapTooltips() {
        return (GameTooltips)new StringTooltips(getObject().getDisplayName());
    }

    protected void teleportClientToAroundDestination(int tileOffsetX, int tileOffsetY, ServerClient client, Predicate<Level> validCheck) {
        teleportClientToAroundDestination(tileOffsetX, tileOffsetY, client, (Function<LevelIdentifier, Level>)null, validCheck);
    }

    protected void teleportClientToAroundDestination(int tileOffsetX, int tileOffsetY, ServerClient client, Function<LevelIdentifier, Level> generator, Predicate<Level> validCheck) {
        client.changeLevelCheck(getDestinationIdentifier(), generator, level -> {
            boolean isValid = (validCheck == null || level != null);
            if (!isValid)
                return new TeleportResult(false, null);
            Point teleportPos = new Point((this.destinationTileX - tileOffsetX) * 32 + 16, (this.destinationTileY - tileOffsetY) * 32 + 16);
            return new TeleportResult(true, teleportPos);
        }, true);
    }

    public static Level asLevel(Object o) {
        return (o instanceof Level) ? (Level) o : null;
    }
}