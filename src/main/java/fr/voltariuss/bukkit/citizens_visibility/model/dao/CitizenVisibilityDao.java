package fr.voltariuss.bukkit.citizens_visibility.model.dao;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CitizenVisibilityDao extends JpaDao<CitizenVisibility, Long> {

  @Inject
  public CitizenVisibilityDao(@NotNull SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public @NotNull CompletableFuture<Optional<CitizenVisibility>> find(
      @NotNull UUID playerUuid, int citizenId) {
    return executeQueryTransaction(
            session ->
                session
                    .createQuery(
                        "SELECT cv FROM CitizenVisibility cv WHERE cv.playerUuid = :playerUuid AND"
                            + " cv.citizenId = :citizenId",
                        CitizenVisibility.class)
                    .setParameter("playerUuid", playerUuid)
                    .setParameter("citizenId", citizenId))
        .thenApplyAsync(resultList -> resultList.stream().findFirst());
  }

  public @NotNull CompletableFuture<List<CitizenVisibility>> findByCitizenId(int citizenId) {
    return executeQueryTransaction(
        session ->
            session
                .createQuery(
                    "SELECT cv FROM CitizenVisibility cv WHERE cv.citizenId = :citizenId",
                    CitizenVisibility.class)
                .setParameter("citizenId", citizenId));
  }
}
