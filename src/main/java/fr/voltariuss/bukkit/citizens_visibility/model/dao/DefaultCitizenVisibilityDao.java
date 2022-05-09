package fr.voltariuss.bukkit.citizens_visibility.model.dao;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.DefaultCitizenVisibility;
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
  protected DefaultCitizenVisibilityDao(
      @NotNull SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
