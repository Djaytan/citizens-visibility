package fr.voltariuss.bukkit.citizens_visibility.controller.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Name;
import co.aikar.commands.annotation.Subcommand;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.CitizenVisibilityController;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Singleton
@CommandAlias("citizensvisibility|cv")
@CommandPermission("citizens.visibility")
public class CitizensVisibilityCommand extends BaseCommand {

  private final CitizenVisibilityController citizenVisibilityController;

  @Inject
  public CitizensVisibilityCommand(
      @NotNull CitizenVisibilityController citizenVisibilityController) {
    this.citizenVisibilityController = citizenVisibilityController;
  }

  @Subcommand("hide")
  @CommandCompletion("@cv_citizens_id @cv_players")
  @CommandPermission("citizens.visibility.hide")
  public void onHide(
      @NotNull CommandSender sender,
      @Name("citizen_id") int citizenId,
      @NotNull @Name("player_name") String playerName) {
    citizenVisibilityController.hideCitizen(sender, playerName, citizenId);
  }

  @Subcommand("hideall")
  @CommandCompletion("@cv_citizens_id")
  @CommandPermission("citizens.visibility.hideall")
  public void onHideAll(@NotNull CommandSender sender, @Name("citizen_id") int citizenId) {
    citizenVisibilityController.hideCitizenForAllPlayers(sender, citizenId);
  }

  @Subcommand("show")
  @CommandCompletion("@cv_citizens_id @cv_players")
  @CommandPermission("citizens.visibility.show")
  public void onShow(
      @NotNull CommandSender sender,
      @Name("citizen_id") int citizenId,
      @NotNull @Name("player_name") String playerName) {
    citizenVisibilityController.showCitizen(sender, playerName, citizenId);
  }

  @Subcommand("showall")
  @CommandCompletion("@cv_citizens_id")
  @CommandPermission("citizens.visibility.showall")
  public void onShowAll(@NotNull CommandSender sender, @Name("citizen_id") int citizenId) {
    citizenVisibilityController.showCitizenForAllPlayers(sender, citizenId);
  }

  @HelpCommand
  public void onHelp(
      @NotNull CommandSender sender, @NotNull @Name("sub_command") CommandHelp commandHelp) {
    commandHelp.showHelp();
  }
}
