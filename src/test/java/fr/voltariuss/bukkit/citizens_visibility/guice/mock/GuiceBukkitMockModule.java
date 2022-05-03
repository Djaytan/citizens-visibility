/*
 * Copyright (c) 2022 - Loïc DUBOIS-TERMOZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.voltariuss.bukkit.citizens_visibility.guice.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;
import org.slf4j.Logger;

public class GuiceBukkitMockModule extends AbstractModule {

  @Provides
  @Singleton
  public @NotNull Plugin providePlugin() {
    return Mockito.mock(Plugin.class);
  }

  @Provides
  @Singleton
  public @NotNull JavaPlugin provideJavaPlugin() {
    return Mockito.mock(JavaPlugin.class);
  }

  @Provides
  @Singleton
  public @NotNull Logger provideLogger() {
    return Mockito.mock(Logger.class);
  }

  @Provides
  @Singleton
  public @NotNull PluginManager providePluginManager() {
    return Mockito.mock(PluginManager.class);
  }

  @Provides
  @Singleton
  public @NotNull ServicesManager provideServicesManager() {
    return Mockito.mock(ServicesManager.class);
  }

  @Provides
  @Singleton
  public @NotNull Server provideServer() {
    return Mockito.mock(Server.class);
  }

  @Provides
  @Singleton
  public @NotNull ItemFactory provideItemFactory() {
    return Mockito.mock(ItemFactory.class);
  }

  @Provides
  @Singleton
  public @NotNull ConsoleCommandSender provideConsoleCommandSender() {
    return Mockito.mock(ConsoleCommandSender.class);
  }

  @Provides
  @Singleton
  public @NotNull BukkitScheduler provideBukkitScheduler() {
    return Mockito.mock(BukkitScheduler.class);
  }

  @Provides
  @Singleton
  public @NotNull ScoreboardManager provideScoreboardManager() {
    return Mockito.mock(ScoreboardManager.class);
  }

  @Provides
  @Singleton
  public @NotNull StructureManager provideStructureManager() {
    return Mockito.mock(StructureManager.class);
  }
}
