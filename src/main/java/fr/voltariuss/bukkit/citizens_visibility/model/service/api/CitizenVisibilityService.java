package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
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
 * do is to call the {@link #find(UUID, int) find} method and check the {@link
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
  Optional<CitizenVisibility> find(@NotNull UUID playerUuid, int citizenId);

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
   * @return A response which specify whether the action has been realized successfully or not and
   *     the update citizen visibility value.
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
}
