package fr.voltariuss.bukkit.citizens_visibility.controller.implementation;

import com.google.common.base.Preconditions;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.CitizenVisibilityController;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.MessageController;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import fr.voltariuss.bukkit.citizens_visibility.view.message.CitizenVisibilityMessage;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.kyori.adventure.audience.Audience;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityControllerImpl implements CitizenVisibilityController {

  private final CitizenVisibilityMessage citizenVisibilityMessage;
  private final CitizenVisibilityService citizenVisibilityService;
  private final MessageController messageController;

  @Inject
  public CitizenVisibilityControllerImpl(
      @NotNull CitizenVisibilityMessage citizenVisibilityMessage,
      @NotNull CitizenVisibilityService citizenVisibilityService,
      @NotNull MessageController messageController) {
    this.citizenVisibilityMessage = citizenVisibilityMessage;
    this.citizenVisibilityService = citizenVisibilityService;
    this.messageController = messageController;
  }

  @Override
  public void hideCitizen(
      @NotNull Audience audience, @NotNull OfflinePlayer player, int citizenId) {
    Preconditions.checkNotNull(audience);
    Preconditions.checkNotNull(player);

    citizenVisibilityService.hideCitizen(player.getUniqueId(), citizenId);

    messageController.sendInfoMessage(
        audience,
        citizenVisibilityMessage.visibilityToggled(player.getUniqueId(), citizenId, false));
  }

  @Override
  public void hideCitizenForAllPlayers(@NotNull Audience audience, int citizenId) {
    Preconditions.checkNotNull(audience);

    citizenVisibilityService.hideCitizenForAllPlayers(citizenId);

    messageController.sendInfoMessage(
        audience, citizenVisibilityMessage.visibilityToggledForAllPlayers(citizenId, false));
  }

  @Override
  public void showCitizen(
      @NotNull Audience audience, @NotNull OfflinePlayer player, int citizenId) {
    Preconditions.checkNotNull(audience);
    Preconditions.checkNotNull(player);

    citizenVisibilityService.showCitizen(player.getUniqueId(), citizenId);

    messageController.sendInfoMessage(
        audience,
        citizenVisibilityMessage.visibilityToggled(player.getUniqueId(), citizenId, true));
  }

  @Override
  public void showCitizenForAllPlayers(@NotNull Audience audience, int citizenId) {
    Preconditions.checkNotNull(audience);

    citizenVisibilityService.showCitizenForAllPlayers(citizenId);

    messageController.sendInfoMessage(
        audience, citizenVisibilityMessage.visibilityToggledForAllPlayers(citizenId, true));
  }
}
