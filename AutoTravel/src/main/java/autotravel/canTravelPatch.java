package autotravel;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.engine.state.MainGame;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.container.travel.TravelContainer;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = MainGame.class, name = "canTravel", arguments = {Client.class, PlayerMob.class})
public class canTravelPatch {
    @Advice.OnMethodExit
    static void onExit(@Advice.Argument(1) PlayerMob player, @Advice.Return(readOnly = false) boolean canTravel) {
        try {
            //System.out.println("AutoTravel.canTravelPatch - MainGame.canTravel() was called");
            if (player.isRiding()) {
                //System.out.println("AutoTravel.canTravelPatch - player.isRiding() = true");
                //System.out.println("AutoTravel.canTravelPatch - TravelContainer.getTravelDir(player) = " + (TravelContainer.getTravelDir(player) != null));
                canTravel = (TravelContainer.getTravelDir(player) != null);
            }
        }
        catch (Exception e) {
            //System.out.println(e.getMessage());
        }
    }
}