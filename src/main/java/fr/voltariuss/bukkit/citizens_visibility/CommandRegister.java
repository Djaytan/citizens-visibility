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

package fr.voltariuss.bukkit.citizens_visibility;

import co.aikar.commands.PaperCommandManager;
import fr.voltariuss.bukkit.citizens_visibility.controller.CitizensVisibilityCommand;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CommandRegister {

  private final PaperCommandManager paperCommandManager;
  private final Server server;
  private final CitizensVisibilityCommand citizensVisibilityCommand;

  @Inject
  public CommandRegister(
      @NotNull PaperCommandManager paperCommandManager,
      @NotNull Server server,
      @NotNull CitizensVisibilityCommand citizensVisibilityCommand) {
    this.paperCommandManager = paperCommandManager;
    this.server = server;
    this.citizensVisibilityCommand = citizensVisibilityCommand;
  }

  public void registerCommands() {
    paperCommandManager.registerCommand(citizensVisibilityCommand);
  }

  public void registerCommandCompletions() {
    paperCommandManager
        .getCommandCompletions()
        .registerAsyncCompletion(
            "allplayers",
            context -> {
              long size = Long.parseLong(context.getConfig("size", "100"));
              return Arrays.stream(server.getOfflinePlayers())
                  .limit(size)
                  .map(OfflinePlayer::getName)
                  .toList();
            });
  }
}
