package railevator;

import necesse.engine.network.Packet;
import necesse.engine.network.packet.PacketChangeObject;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.TileRegistry;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.util.TeleportResult;
import necesse.entity.TileDamageType;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.PortalObjectEntity;
import necesse.inventory.container.travel.TravelContainer;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

public class RailevatorDownObjectEntity<T extends TravelContainer> extends PortalObjectEntity {
    private int railevatorDownID;

    private int railevatorUpID;

    private Rectangle collision;

    public int resetTime;

    private long hardResetTime;

    public RailevatorDownObjectEntity(String type, Level level, int x, int y, int dimension, int railevatorDownID, int railevatorUpID, Rectangle collision) {
        super(level, type, x, y, level.getIdentifier(), x, y);
        LevelIdentifier identifier = level.getIdentifier();
        if (identifier.isIslandPosition())
            this.destinationIdentifier = new LevelIdentifier(identifier.getIslandX(), identifier.getIslandY(), dimension);
        this.railevatorDownID = railevatorDownID;
        this.railevatorUpID = railevatorUpID;
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
        if (getLevel().isCave) {
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

            int tileX = this.destinationTileX;
            int tileY = this.destinationTileY;
            Level targetLevel = client.getServer().world.getLevel(getDestinationIdentifier());

            if (playerMob.isRiding() && playerMob.getCollision().intersects(getCollision())) {
                if (targetLevel.getObjectID(tileX, tileY) != this.railevatorUpID) {
                    GameObject railevatorUp = ObjectRegistry.getObject(this.railevatorUpID);
                    GameObject obj = targetLevel.getObject(tileX, tileY);
                    if (!obj.isRock && !obj.isGrass)
                        targetLevel.entityManager.doDamageOverride(tileX, tileY, obj.objectHealth, TileDamageType.Object);
                    railevatorUp.placeObject(targetLevel, this.destinationTileX, this.destinationTileY, 0);
                    if ((targetLevel.getTile(tileX, tileY)).isLiquid)
                        targetLevel.setTile(tileX, tileY, TileRegistry.dirtID);
                    if (getServer() != null)
                        getServer().network.sendToClientsWithTile((Packet)new PacketChangeObject(targetLevel, tileX, tileY, this.railevatorUpID), targetLevel, this.destinationTileX, this.destinationTileY);
                }

                playerMob.refreshSpawnTime();
                this.hardResetTime = getWorldEntity().getTime() + this.resetTime;
                teleportClientToAroundDestination((playerMob.getX()/32) - getX(), (playerMob.getY()/32) - getY(), client, level -> {
                    client.newStats.ladders_used.increment(1);
                    runClearMobs(client);
                    return true;
                });
            }
        }
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

    private static Level asLevel (Object o) {
        return (o instanceof Level) ? (Level) o : null;
    }
}
