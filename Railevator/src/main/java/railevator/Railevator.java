package railevator;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.item.Item;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

import java.awt.*;

@ModEntry
public class Railevator {
    public void init() {
        System.out.println("Loading Railevator");
        RailevatorDownObject.registerRailevatorPair("railevator", -1, new Color(107, 103, 97), Item.Rarity.NORMAL, 10);
    }

    public void postInit() {
        Recipes.registerModRecipe(new Recipe(
                "railevatordown",
                1,
                RecipeTechRegistry.NONE,
                new Ingredient[]{
                        new Ingredient("ironbar", 25)
                }
        ));
    }
}