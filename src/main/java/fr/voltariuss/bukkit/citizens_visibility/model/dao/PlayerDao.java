package fr.voltariuss.bukkit.citizens_visibility.model.dao;

import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import java.util.UUID;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PlayerDao extends JpaDao<Player, UUID> {

  /**
   * Constructor.
   *
   * @param sessionFactory The session factory.
   */
  @Inject
  public PlayerDao(
      @NotNull SessionFactory sessionFactory) {
    super(sessionFactory);
  }
}
