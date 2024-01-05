package recyclemod;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.*;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

@ModEntry
public class RecycleMod {

    public void init() {
        /*
        for (int i = 0; i < ItemRegistry.getItems().size(); i++) {
            String item = ItemRegistry.getItems().get(i).type + "," +
                    ItemRegistry.getItems().get(i).getID() + "," +
                    ItemRegistry.getItems().get(i).getStringID() + ",";
            if (ItemRegistry.getItems().get(i).getNewLocalization() != null) {
                item += ItemRegistry.getItems().get(i).getNewLocalization().translate() + ",";
            }
            else {
                item += "null,";
            }
            System.out.println(item);
        }
        */
        System.out.println("Loading Recycle Mod");
    }

    public void initResources() {

    }

    public void postInit() {
        //region Tools
        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("sickle", 1)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("shears", 1)
                }
        ));
        //endregion

        // Picks/Shovels/Axes
        //region Copper
        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperaxe", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("coppershovel", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperpickaxe", 1)
                }
        ));
        //endregion

        //region Iron
        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironaxe", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironshovel", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironpickaxe", 1)
                }
        ));
        //endregion

        //region Gold
        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldaxe", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldshovel", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldpickaxe", 1)
                }
        ));
        //endregion

        //region FrostShard
        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostaxe", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostshovel", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostpickaxe", 1)
                }
        ));
        //endregion

        // Weapons
        //region Copper
        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("coppersword", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperspear", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperbow", 1)
                }
        ));

        // Mod Compats
        try {
            Recipes.registerModRecipe(new Recipe(
                    "copperbar",
                    5,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("copperdagger", 1)
                    }
            ));
        } catch (Exception e) {

        }
        try {
            Recipes.registerModRecipe(new Recipe(
                    "copperbar",
                    56,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("mmt_coppersingularity", 1)
                    }
            ));
        } catch (Exception e) {

        }
        //endregion

        //region Iron
        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironsword", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                10,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("irongreatsword", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironspear", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironbow", 1)
                }
        ));

        // Mod Compats
        try {
            Recipes.registerModRecipe(new Recipe(
                    "ironbar",
                    5,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("irondagger", 1)
                    }
            ));
        } catch (Exception e) {

        }
        try {
            Recipes.registerModRecipe(new Recipe(
                    "ironbar",
                    56,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("mmt_ironsingularity", 1)
                    }
            ));
        } catch (Exception e) {

        }
        //endregion

        //region Gold
        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldsword", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldglaive", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldspear", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldbow", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldgreatbow", 1)
                }
        ));

        // Mod Compats
        try {
            Recipes.registerModRecipe(new Recipe(
                    "goldbar",
                    5,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("golddagger", 1)
                    }
            ));
        } catch (Exception e) {

        }
        try {
            Recipes.registerModRecipe(new Recipe(
                    "goldbar",
                    56,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("mmt_goldsingularity", 1)
                    }
            ));
        } catch (Exception e) {

        }
        //endregion

        //region FrostShard
        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostsword", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                10,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostgreatsword", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostspear", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostglaive", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                5,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostbow", 1)
                }
        ));

        // Mod Compats
        try {
            Recipes.registerModRecipe(new Recipe(
                    "frostshard",
                    5,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("frostdagger", 1)
                    }
            ));
        } catch (Exception e) {

        }
        try {
            Recipes.registerModRecipe(new Recipe(
                    "frostshard",
                    56,
                    RecipeTechRegistry.FORGE,
                    new Ingredient[]{
                            new Ingredient("mmt_frostsingularity", 1)
                    }
            ));
        } catch (Exception e) {

        }
        //endregion

        // Armors
        //region Copper
        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperhelmet", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperchestplate", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "copperbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("copperboots", 1)
                }
        ));
        //endregion

        //region Iron
        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironhelmet", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironchestplate", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "ironbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("ironboots", 1)
                }
        ));
        //endregion

        //region Gold
        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldcrown", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldhelmet", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldchestplate", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "goldbar",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("goldboots", 1)
                }
        ));
        //endregion

        //region FrostShard
        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frosthelmet", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frosthood", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                6,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frosthat", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                8,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostchestplate", 1)
                }
        ));

        Recipes.registerModRecipe(new Recipe(
                "frostshard",
                4,
                RecipeTechRegistry.FORGE,
                new Ingredient[]{
                        new Ingredient("frostboots", 1)
                }
        ));
        //endregion
    }
}
