package chunkloader;

import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.RecipeTechRegistry;
import necesse.engine.util.LevelIdentifier;
import necesse.inventory.item.Item;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import necesse.level.maps.Level;

import java.awt.*;
import java.io.*;

@ModEntry
public class ChunkLoader {

    public static String chunkLoaders = "";
    public static String filename;

    public void init() {
        System.out.println("Loading ChunkLoader");
        ObjectRegistry.registerObject("chunkloader", new ChunkLoaderObject("chunkloader", new Color(107, 103, 97), Item.Rarity.NORMAL), 2, true);
    }

    public void postInit() {
        Recipes.registerModRecipe(new Recipe(
                "chunkloader",
                1,
                RecipeTechRegistry.NONE,
                new Ingredient[]{
                        new Ingredient("goldbar", 10),
                        new Ingredient("frostshard", 10)
                }
        ));
    }

    public static void AddChunkLoader (Level level, int uuid) {
        if (chunkLoaders == null)
            chunkLoaders = "";

        String levelInfo = uuid + "|" + level.getIdentifier().getIslandX() + "|" + level.getIdentifier().getIslandY() + "|" + level.getIdentifier().getIslandDimension();
        chunkLoaders = levelInfo + "\n";
        try {
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(file, true); // true for appending
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(levelInfo);
            bufferedWriter.newLine(); // Optional: add a new line after appended text

            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Text has been appended to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the file.");
            e.printStackTrace();
        }
    }

    public static void RemoveChunkLoader (int uuid) {
        if (chunkLoaders == null || chunkLoaders == "") {
            chunkLoaders = "";
            return;
        }

        String newChunkLoaders = "";
        for (String chunkloader : chunkLoaders.split("\n")) {
            if (Integer.parseInt(chunkloader.split("\\|")[0]) != uuid) {
                newChunkLoaders += chunkloader + "\n";
            }
        }
        chunkLoaders = newChunkLoaders;

        try {
            File file = new File(filename);
            FileWriter fileWriter = new FileWriter(file, false); // true for appending
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(chunkLoaders);

            bufferedWriter.close();
            fileWriter.close();

            System.out.println("Text has been appended to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the file.");
            e.printStackTrace();
        }
    }
}