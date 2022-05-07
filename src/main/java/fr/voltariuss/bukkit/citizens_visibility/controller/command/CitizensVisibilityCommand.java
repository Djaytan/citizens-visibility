package fr.voltariuss.bukkit.citizens_visibility.controller.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import fr.voltariuss.bukkit.citizens_visibility.controller.api.CitizenVisibilityController;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@Singleton
@CommandAlias("citizensvisibility|cv")
public class CitizensVisibilityCommand extends BaseCommand {

  private final CitizenVisibilityController citizenVisibilityController;

  @Inject
  public CitizensVisibilityCommand(
      @NotNull CitizenVisibilityController citizenVisibilityController) {
    this.citizenVisibilityController = citizenVisibilityController;
  }

  @Subcommand("hide")
  public void onHide(@NotNull CommandSender sender, int citizenId, @NotNull String playerName) {
    citizenVisibilityController.hideCitizen(sender, playerName, citizenId);
  }

  @Subcommand("hideall")
  public void onHideAll(@NotNull CommandSender sender, int citizenId) {
    citizenVisibilityController.hideCitizenForAllPlayers(sender, citizenId);
  }

  @Subcommand("show")
  public void onShow(@NotNull CommandSender sender, int citizenId, @NotNull String playerName) {
    citizenVisibilityController.showCitizen(sender, playerName, citizenId);
  }

  @Subcommand("showall")
  public void onShowAll(@NotNull CommandSender sender, int citizenId) {
    citizenVisibilityController.showCitizenForAllPlayers(sender, citizenId);
  }

  @HelpCommand
  public void onHelp(@NotNull CommandSender sender, @NotNull CommandHelp commandHelp) {
    commandHelp.showHelp();
  }
}
