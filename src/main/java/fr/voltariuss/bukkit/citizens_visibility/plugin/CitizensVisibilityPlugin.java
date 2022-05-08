package fr.voltariuss.bukkit.citizens_visibility.plugin;

import fr.voltariuss.bukkit.citizens_visibility.plugin.guice.GuiceInjector;
import javax.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

public class CitizensVisibilityPlugin extends JavaPlugin {

  @Inject private CommandRegister commandRegister;
  @Inject private EventListenerRegister eventListenerRegister;
  @Inject private PacketListenerRegister packetListenerRegister;

  @Override
  public void onEnable() {
    // TODO: wait Citizens enabling (and potentially other dependencies as well..)

    getSLF4JLogger().info("SQLite database initialization");
    SQLiteInitializer.init(getDataFolder().toPath());

    getSLF4JLogger().info("Guice injection");
    GuiceInjector.inject(this);

    getSLF4JLogger().info("Commands registration");
    commandRegister.registerCommands();
    commandRegister.registerCommandCompletions();

    getSLF4JLogger().info("Event listeners registration");
    eventListenerRegister.registerListeners();

    getSLF4JLogger().info("Packet listeners registration");
    packetListenerRegister.registerListeners();

    getSLF4JLogger().info("Citizens-Visibility successfully enabled!");
  }
}
