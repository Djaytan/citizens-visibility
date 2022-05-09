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

package fr.voltariuss.bukkit.citizens_visibility.plugin;

import co.aikar.commands.PaperCommandManager;
import fr.voltariuss.bukkit.citizens_visibility.controller.command.CitizensVisibilityCommand;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.PlayerService;
import java.util.Objects;
import java.util.stream.StreamSupport;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CommandRegister {

  private final PaperCommandManager paperCommandManager;
  private final PlayerService playerService;
  private final CitizensVisibilityCommand citizensVisibilityCommand;

  @Inject
  public CommandRegister(
      @NotNull PaperCommandManager paperCommandManager,
      @NotNull PlayerService playerService,
      @NotNull CitizensVisibilityCommand citizensVisibilityCommand) {
    this.paperCommandManager = paperCommandManager;
    this.playerService = playerService;
    this.citizensVisibilityCommand = citizensVisibilityCommand;
  }

  public void registerCommands() {
    paperCommandManager.registerCommand(citizensVisibilityCommand);
  }

  public void registerCommandCompletions() {
    paperCommandManager
        .getCommandCompletions()
        .registerAsyncCompletion(
            "cv_players",
            context -> {
              long size = Long.parseLong(context.getConfig("size", "100"));
              return playerService.fetchAll().stream()
                  .limit(size)
                  .map(Player::playerName)
                  .filter(Objects::nonNull)
                  .toList();
            });

    paperCommandManager
        .getCommandCompletions()
        .registerAsyncCompletion(
            "cv_citizens_id",
            context -> {
              long size = Long.parseLong(context.getConfig("size", "100"));
              return StreamSupport.stream(
                      CitizensAPI.getNPCRegistry().sorted().spliterator(), false)
                  .limit(size)
                  .map(NPC::getId)
                  .map(npcId -> Integer.toString(npcId))
                  .toList();
            });
  }
}
