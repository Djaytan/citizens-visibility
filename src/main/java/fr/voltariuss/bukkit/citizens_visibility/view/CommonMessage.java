package fr.voltariuss.bukkit.citizens_visibility.view;

import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CommonMessage {

  private final MiniMessage miniMessage;
  private final ResourceBundle resourceBundle;

  @Inject
  public CommonMessage(@NotNull MiniMessage miniMessage, @NonNull ResourceBundle resourceBundle) {
    this.miniMessage = miniMessage;
    this.resourceBundle = resourceBundle;
  }

  public @NotNull Component unexpectedError() {
    return miniMessage.deserialize(
        resourceBundle.getString("citizen_visibility.common.fail.unexpected_error"));
  }

  public @NotNull Component playerNotFound(String playerName) {
    return miniMessage
        .deserialize(
            resourceBundle.getString("citizen_visibility.common.fail.player_not_found"),
            TagResolver.resolver(Placeholder.unparsed("cv_player_name", playerName)))
        .decoration(TextDecoration.ITALIC, false);
  }
}
