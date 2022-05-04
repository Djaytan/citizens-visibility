package fr.voltariuss.bukkit.citizens_visibility.model.service.implementation;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.service.api.CitizenVisibilityService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Singleton;
import org.apache.commons.lang.NotImplementedException;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityServiceImpl implements CitizenVisibilityService {

  @Override
  public Optional<CitizenVisibility> find(@NotNull UUID playerUuid, int citizenId) {
    throw new NotImplementedException();
  }

  @Override
  public void hideCitizen(@NotNull UUID playerUuid, int citizenId) {
    throw new NotImplementedException();
  }

  @Override
  public void hideCitizen(@NotNull List<UUID> playersUuids, int citizenId) {
    throw new NotImplementedException();
  }

  @Override
  public void showCitizen(@NotNull UUID playerUuid, int citizenId) {
    throw new NotImplementedException();
  }

  @Override
  public void showCitizen(@NotNull List<UUID> playersUuids, int citizenId) {
    throw new NotImplementedException();
  }
}
