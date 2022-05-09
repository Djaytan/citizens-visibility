package fr.voltariuss.bukkit.citizens_visibility.model.dao;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.DefaultCitizenVisibility;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

@Singleton
public class DefaultCitizenVisibilityDao extends JpaDao<DefaultCitizenVisibility, Long> {

  /**
   * Constructor.
   *
   * @param sessionFactory The session factory.
   */
  @Inject
  protected DefaultCitizenVisibilityDao(@NotNull SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  public @NotNull Optional<DefaultCitizenVisibility> findByCitizenId(int citizenId) {
    return executeQueryTransaction(
            session ->
                session
                    .createQuery(
                        "SELECT dcv FROM DefaultCitizenVisibility dcv WHERE dcv.citizenId ="
                            + " :citizenId",
                        DefaultCitizenVisibility.class)
                    .setParameter("citizenId", citizenId))
        .stream()
        .findFirst();
  }
}
