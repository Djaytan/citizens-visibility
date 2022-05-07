package fr.voltariuss.bukkit.citizens_visibility.plugin;

import fr.voltariuss.bukkit.citizens_visibility.CitizensVisibilityRuntimeException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

public class SQLiteInitializer {

  public static void init(@NotNull Path pluginDataSourcePath) {
    Path sqliteDatabaseFile = pluginDataSourcePath.resolve("data.db");

    try {
      if (Files.notExists(pluginDataSourcePath)) {
        Files.createDirectory(pluginDataSourcePath);
      }

      if (Files.notExists(sqliteDatabaseFile)) {
        Files.createFile(sqliteDatabaseFile);
      }
    } catch (IOException e) {
      throw new CitizensVisibilityRuntimeException("Unable to create SQLite database file", e);
    }
  }
}
