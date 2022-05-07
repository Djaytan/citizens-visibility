package fr.voltariuss.bukkit.citizens_visibility.model.dao;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.converter.UUIDConverter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityDao extends JpaDao<CitizenVisibility, Long> {

  private final UUIDConverter uuidConverter;

  @Inject
  public CitizenVisibilityDao(
      @NotNull SessionFactory sessionFactory, @NotNull UUIDConverter uuidConverter) {
    super(sessionFactory);
    this.uuidConverter = uuidConverter;
  }

  public @NotNull Optional<CitizenVisibility> find(@NotNull UUID playerUuid, int citizenId) {
    return executeQueryTransaction(
            session ->
                session
                    .createQuery(
                        "SELECT cv FROM CitizenVisibility cv WHERE cv.playerUuid = :playerUuid AND"
                            + " cv.citizenId = :citizenId",
                        CitizenVisibility.class)
                    .setParameter("playerUuid", uuidConverter.convertToDatabaseColumn(playerUuid))
                    .setParameter("citizenId", citizenId))
        .findFirst();
  }

  public @NotNull List<CitizenVisibility> findByCitizenId(int citizenId) {
    return executeQueryTransaction(
            session ->
                session
                    .createQuery(
                        "SELECT cv FROM CitizenVisibility cv WHERE cv.citizenId = :citizenId",
                        CitizenVisibility.class)
                    .setParameter("citizenId", citizenId))
        .toList();
  }
}
