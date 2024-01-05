package autotravel;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.packet.PacketRequestTravel;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.container.travel.TravelContainer;
import necesse.inventory.container.travel.TravelDir;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = TravelContainer.class, name = "getTravelDir", arguments = {PlayerMob.class})
public class getTravelDirPatch {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onEnter(@Advice.Argument(0) PlayerMob player) {
        try {
            int triggerBuffer = 128;
            //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() was called");

            if (player.isRiding()) {
                //System.out.println("AutoTravel.canTravelPatch - Player is riding");
                if ((((player.getX() < triggerBuffer) ? 1 : 0) & ((player.getY() < triggerBuffer) ? 1 : 0)) != 0) {
                    //return TravelDir.NorthWest;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned NorthWest");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.NorthWest));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX() - 1, player.getLevel().getIslandY() - 1, player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if ((((player.getX() < triggerBuffer) ? 1 : 0) & ((player.getY() > (player.getLevel()).height * 32 - triggerBuffer) ? 1 : 0)) != 0) {
                    //return TravelDir.SouthWest;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned SouthWest");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.SouthWest));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX() - 1, player.getLevel().getIslandY() + 1, player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if ((((player.getX() > (player.getLevel()).width * 32 - triggerBuffer) ? 1 : 0) & ((player.getY() < triggerBuffer) ? 1 : 0)) != 0) {
                    //return TravelDir.NorthEast;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned NorthEast");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.NorthEast));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX() + 1, player.getLevel().getIslandY() - 1, player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if ((((player.getX() > (player.getLevel()).width * 32 - triggerBuffer) ? 1 : 0) & ((player.getY() > (player.getLevel()).height * 32 - triggerBuffer) ? 1 : 0)) != 0) {
                    //return TravelDir.SouthEast;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned SouthEast");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.SouthEast));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX() + 1, player.getLevel().getIslandY() + 1, player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if (player.getX() < triggerBuffer) {
                    //return TravelDir.West;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned West");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.West));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX() - 1, player.getLevel().getIslandY(), player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if (player.getX() > (player.getLevel()).width * 32 - triggerBuffer) {
                    //return TravelDir.East;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned East");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.East));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX() + 1, player.getLevel().getIslandY(), player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if (player.getY() < triggerBuffer) {
                    //return TravelDir.North;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned North");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.North));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX(), player.getLevel().getIslandY() - 1, player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                } else if (player.getY() > (player.getLevel()).width * 32 - triggerBuffer) {
                    //return TravelDir.South;
                    //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir() returned South");
                    if (player.getClient() != null)
                        player.getClient().network.sendPacket(new PacketRequestTravel(TravelDir.South));
                    else {
                        player.getServerClient().changeIsland(player.getLevel().getIslandX(), player.getLevel().getIslandY() + 1, player.getLevel().getIslandDimension());
                        (player.getServerClient()).newStats.island_travels.increment(1);
                    }
                    return true;
                }

                if (player.getLevel().isCave) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        return false;
    }
}
