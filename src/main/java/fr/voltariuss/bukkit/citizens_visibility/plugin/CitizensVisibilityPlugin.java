package fr.voltariuss.bukkit.citizens_visibility.plugin;

import fr.voltariuss.bukkit.citizens_visibility.RemakeBukkitLogger;
import fr.voltariuss.bukkit.citizens_visibility.plugin.guice.GuiceInjector;
import javax.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

public class CitizensVisibilityPlugin extends JavaPlugin {

  @Inject private CommandRegister commandRegister;
  @Inject private ListenerRegister listenerRegister;
  @Inject private RemakeBukkitLogger logger;

  @Override
  public void onEnable() {
    logger.info("Guice injection");
    GuiceInjector.inject(this);

    logger.info("Commands registration");
    commandRegister.registerCommands();
    commandRegister.registerCommandCompletions();

    logger.info("Listeners registration");
    listenerRegister.registerListeners();

    logger.info("Citizens-Visibility successfully enabled!");
  }
}
