package fr.voltariuss.bukkit.citizens_visibility.controller.api;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

public interface CitizenVisibilityController {

  void hideCitizen(@NotNull Audience audience, @NotNull String playerName, int citizenId);

  void hideCitizenForAllPlayers(@NotNull Audience audience, int citizenId);

  void showCitizen(@NotNull Audience audience, @NotNull String playerName, int citizenId);

  void showCitizenForAllPlayers(@NotNull Audience audience, int citizenId);
}
