package fr.voltariuss.bukkit.citizens_visibility.controller;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.HelpCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

@CommandAlias("citizensvisibility|cv")
public class CitizensVisibilityCommand extends BaseCommand {

  @HelpCommand
  public void onHelp(@NotNull CommandSender sender, @NotNull CommandHelp commandHelp) {
    commandHelp.showHelp();
  }
}
