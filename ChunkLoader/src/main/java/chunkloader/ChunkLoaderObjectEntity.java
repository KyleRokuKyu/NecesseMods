package chunkloader;

import necesse.entity.objectEntity.PortalObjectEntity;
import necesse.inventory.container.travel.TravelContainer;
import necesse.level.maps.Level;

public class ChunkLoaderObjectEntity<T extends TravelContainer> extends PortalObjectEntity {

    public ChunkLoaderObjectEntity(String type, Level level, int x, int y) {
        super(level, type, x, y, level.getIdentifier(), x, y);
    }

    public void serverTick() {
        getLevel().unloadLevelBuffer = 0;
    }
}
