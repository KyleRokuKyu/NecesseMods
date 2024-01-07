package chunkloader;

import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.network.server.Server;
import necesse.engine.util.LevelIdentifier;
import necesse.engine.world.WorldEntity;
import net.bytebuddy.asm.Advice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@ModConstructorPatch(target = WorldEntity.class, arguments = {Server.class})
public class ChunkLoaderPatchWorldInitServer {
    @Advice.OnMethodExit
    static void onExit(@Advice.Argument(0) Server server) {
        if (server == null || server.world == null || server.world.worldEntity == null)
            return;
        try {
            String directoryPath = "ChunkLoaders";

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (directory.mkdir()) {
                    System.out.println("ChunkLoader Directory created successfully.");
                } else {
                    System.out.println("Failed to create the ChunkLoader directory.");
                }
            }

            ChunkLoader.filename = "ChunkLoaders/" + server.world.getUniqueID() + "_chunkLoadedLevels.txt";
            File file = new File(ChunkLoader.filename);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            StringBuilder text = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                text.append(line).append("\n");
            }

            bufferedReader.close();
            fileReader.close();

            System.out.println("Text loaded from file:\n" + text.toString());

            for (String chunkLoaderData : text.toString().split("\n")) {
                LevelIdentifier levelID = new LevelIdentifier(Integer.parseInt(chunkLoaderData.split("\\|")[1]), Integer.parseInt(chunkLoaderData.split("\\|")[2]), Integer.parseInt(chunkLoaderData.split("\\|")[3]));
                server.world.getLevel(levelID);
            }

            ChunkLoader.chunkLoaders = text.toString();
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
    }
}