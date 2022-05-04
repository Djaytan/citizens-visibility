package fr.voltariuss.bukkit.citizens_visibility.model.service.api;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
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
   * @return An optional citizen's visibility instance.
   */
  @NotNull Optional<CitizenVisibility> find(@NotNull UUID playerUuid, int citizenId);

  /**
   * Hides the targeted citizen for the given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen.
   */
  void hideCitizen(@NotNull UUID playerUuid, int citizenId);

  /**
   * Hides the targeted citizen for the given players' UUIDs.
   *
   * @param playersUuids The concerning players' UUIDs.
   * @param citizenId The targeted citizen.
   */
  void hideCitizen(@NotNull List<UUID> playersUuids, int citizenId);

  /**
   * Shows the targeted citizen for the given player's UUID.
   *
   * @param playerUuid The concerning player's UUID.
   * @param citizenId The targeted citizen.
   */
  void showCitizen(@NotNull UUID playerUuid, int citizenId);

  /**
   * Shows the targeted citizen for the given players' UUIDs.
   *
   * @param playersUuids The concerning players' UUIDs.
   * @param citizenId The targeted citizen.
   */
  void showCitizen(@NotNull List<UUID> playersUuids, int citizenId);
}
