package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * A citizen visibility service which manage visibility state of citizens. This class is viable in
 * context of use with the <a href="https://www.spigotmc.org/resources/citizens.13811/">Minecraft
 * Citizens plugin</a> where citizens are identified by Integer IDs.
 *
 * <p>A given citizen can be hide by calling {@link #hideCitizen(UUID, int) hideCitizen} method by
 * sharing its ID. One or several players' UUIDs can be specified. The opposite behavior is as well
 * possible with the {@link #showCitizen(UUID, int) showCitizen} method.
 *
 * <p>When the need to know if a given citizen is visible or not for a player, all it's necessary to
 * do is to call the {@link #fetch(UUID, int) fetch} method and check the {@link
 * CitizenVisibility#isCitizenVisible()} value.
 *
 * @author Voltariuss
 */
// TODO: manage case of default citizen visibility when new player connect
// TODO: possibility to only change the default visibility for a citizen without impacting players'
public interface CitizenVisibilityService {

  /**
   * Searches and provides the citizen's visibility for a given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen's ID.
   * @return The optional citizen visibility.
   */
  @NotNull
  Optional<CitizenVisibility> fetch(@NotNull UUID playerUuid, int citizenId);

  /**
   * Searches and provides the citizen's visibility for all registered players.
   *
   * @param citizenId The citizen ID.
   * @return The list of citizen's visibility of each registered player.
   */
  @NotNull
  List<CitizenVisibility> fetchAll(int citizenId);

  /**
   * Registers default visibilities of NPCs for the specified player.
   *
   * <p>By default, a default value not override an existing one. However, it's possible to force
   * the default value anyway with the <code>forceDefault</code> parameter.
   *
   * @param playerUuid The player's UUID.
   * @param forceDefault Specify whether a default value must override an existing one or not.
   */
  void registerDefaultVisibilities(@NotNull UUID playerUuid, boolean forceDefault);

  /**
   * Hides the targeted citizen for the given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen.
   */
  void hideCitizen(@NotNull UUID playerUuid, int citizenId);

  /**
   * Hides the targeted citizen for all registered {@link Player}s.
   *
   * @param citizenId The targeted citizen.
   */
  void hideCitizenForAllPlayers(int citizenId);

  /**
   * Shows the targeted citizen for the given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen.
   */
  void showCitizen(@NotNull UUID playerUuid, int citizenId);

  /**
   * Shows the targeted citizen for all registered {@link Player}s.
   *
   * @param citizenId The targeted citizen.
   */
  void showCitizenForAllPlayers(int citizenId);

  /**
   * Checks if the specified citizen is visible for the given player.
   *
   * @param playerUuid The player's UUID.
   * @param citizenId The citizen's ID.
   * @return <code>true</code> if the citizen is visible for the given player, <code>false</code>
   *     otherwise.
   */
  boolean isCitizenVisibleForPlayer(@NotNull UUID playerUuid, int citizenId);
}
