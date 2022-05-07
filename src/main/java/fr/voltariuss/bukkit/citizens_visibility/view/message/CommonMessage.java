package fr.voltariuss.bukkit.citizens_visibility.view.message;

import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
}
