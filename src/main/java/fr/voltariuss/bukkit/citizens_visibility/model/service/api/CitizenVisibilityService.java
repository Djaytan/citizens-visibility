package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.CitizenVisibilityFetchResponse;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.response.CitizenVisibilityResponse;
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
public interface CitizenVisibilityService {

  /**
   * Searches and provides the citizen's visibility for a given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen's ID.
   * @return A response which may contains the sought value and specify whether the action has been
   *     realized successfully or not.
   */
  @NotNull
  CitizenVisibilityFetchResponse find(@NotNull UUID playerUuid, int citizenId);

  /**
   * Hides the targeted citizen for the given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen.
   * @return A response which specify whether the action has been realized successfully or not.
   */
  @NotNull
  CitizenVisibilityResponse hideCitizen(@NotNull UUID playerUuid, int citizenId);

  /**
   * Hides the targeted citizen for all registered {@link Player}s.
   *
   * @param citizenId The targeted citizen.
   * @return A response which specify whether the action has been realized successfully or not.
   */
  @NotNull
  CitizenVisibilityResponse hideCitizenForAllPlayers(int citizenId);

  /**
   * Shows the targeted citizen for the given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen.
   * @return A response which specify whether the action has been realized successfully or not.
   */
  @NotNull
  CitizenVisibilityResponse showCitizen(@NotNull UUID playerUuid, int citizenId);

  /**
   * Shows the targeted citizen for all registered {@link Player}s.
   *
   * @param citizenId The targeted citizen.
   * @return A response which specify whether the action has been realized successfully or not.
   */
  @NotNull
  CitizenVisibilityResponse showCitizenForAllPlayers(int citizenId);
}
