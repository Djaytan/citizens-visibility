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

package fr.voltariuss.bukkit.citizens_visibility.controller.implementation;

import fr.voltariuss.bukkit.citizens_visibility.controller.api.MessageController;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.parameter.MessageType;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

@Singleton
public class MessageControllerImpl implements MessageController {

  private final ConsoleCommandSender consoleCommandSender;
  private final Executor mainThreadExecutor;
  private final MiniMessage miniMessage;
  private final ResourceBundle resourceBundle;
  private final Server server;

  @Inject
  public MessageControllerImpl(
      @NotNull ConsoleCommandSender consoleCommandSender,
      @NotNull Executor mainThreadExecutor,
      @NotNull MiniMessage miniMessage,
      @NotNull ResourceBundle resourceBundle,
      @NotNull Server server) {
    this.consoleCommandSender = consoleCommandSender;
    this.mainThreadExecutor = mainThreadExecutor;
    this.miniMessage = miniMessage;
    this.resourceBundle = resourceBundle;
    this.server = server;
  }

  @Override
  public void sendInfoMessage(@NotNull Audience audience, @NotNull Component message) {
    sendMessage(audience, MessageType.INFO, message);
  }

  @Override
  public void sendSuccessMessage(@NotNull Audience audience, @NotNull Component message) {
    sendMessage(audience, MessageType.SUCCESS, message);
  }

  @Override
  public void sendFailureMessage(@NotNull Audience audience, @NotNull Component message) {
    sendMessage(audience, MessageType.FAILURE, message);
  }

  @Override
  public void sendWarningMessage(@NotNull Audience audience, @NotNull Component message) {
    sendMessage(audience, MessageType.WARNING, message);
  }

  @Override
  public void sendErrorMessage(@NotNull Audience audience, @NotNull Component message) {
    sendMessage(audience, MessageType.ERROR, message);
  }

  @Override
  public void sendRawMessage(@NotNull Audience audience, @NotNull Component message) {
    sendMessage(audience, MessageType.RAW, message);
  }

  public void sendConsoleMessage(@NotNull Component message) {
    CompletableFuture.runAsync(() -> consoleCommandSender.sendMessage(message), mainThreadExecutor);
  }

  @Override
  public void broadcastMessage(@NotNull Component message) {
    Audience onlinePlayers = Audience.audience(server.getOnlinePlayers());
    sendMessage(onlinePlayers, MessageType.BROADCAST, message);
  }

  private void sendMessage(
      @NotNull Audience audience, @NotNull MessageType messageType, @NotNull Component message) {
    Component formattedMessage = formatMessage(messageType, message);

    CompletableFuture.runAsync(
        () ->
            audience.sendMessage(formattedMessage, net.kyori.adventure.audience.MessageType.SYSTEM),
        mainThreadExecutor);
  }

  private @NotNull Component formatMessage(
      @NotNull MessageType messageType, @NotNull Component message) {
    String messageFormatKey =
        switch (messageType) {
          case INFO -> "citizen_visibility.common.message.format.info";
          case SUCCESS -> "citizen_visibility.common.message.format.success";
          case FAILURE -> "citizen_visibility.common.message.format.failure";
          case WARNING -> "citizen_visibility.common.message.format.warning";
          case ERROR -> "citizen_visibility.common.message.format.error";
          case BROADCAST -> "citizen_visibility.common.message.format.broadcast";
          case DEBUG -> "citizen_visibility.common.message.format.debug";
          case RAW -> "";
        };

    if (messageFormatKey.isEmpty()) {
      return message;
    }

    return miniMessage
        .deserialize(
            resourceBundle.getString(messageFormatKey),
            TagResolver.resolver(Placeholder.component("cv_message_content", message)))
        .decoration(TextDecoration.ITALIC, false);
  }
}
