package fr.voltariuss.bukkit.citizens_visibility.model;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.voltariuss.bukkit.citizens_visibility.guice.test.GuiceGeneralTestModule;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import java.util.UUID;
import junit.framework.Assert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Database connection")
class DatabaseConnectionTest {

  private SessionFactory sessionFactory;

  @BeforeEach
  void setUp() {
    Injector injector = Guice.createInjector(new GuiceGeneralTestModule());
    sessionFactory = injector.getInstance(SessionFactory.class);
  }

  @AfterEach
  void tearDown() {
    sessionFactory.close();
    sessionFactory = null;
  }

  @Test
  void connectDatabase() {
    Assert.assertNotNull(sessionFactory);
    Assert.assertTrue(sessionFactory.isOpen());
  }

  @Test
  void requestDatabase() {
    UUID playerUuid = UUID.randomUUID();
    CitizenVisibility citizenVisibility = new CitizenVisibility(playerUuid);
    citizenVisibility.npcId(1);
    citizenVisibility.isNpcVisible(true);

    Session session1 = sessionFactory.openSession();
    Transaction tx = session1.beginTransaction();
    session1.persist(citizenVisibility);
    tx.commit();
    session1.close();

    Session session2 = sessionFactory.openSession();
    @Nullable
    CitizenVisibility retrievedCitizenVisibility = session2.get(CitizenVisibility.class, 1L);
    session2.close();

    Assert.assertNotNull(retrievedCitizenVisibility);
    Assert.assertEquals(1L, retrievedCitizenVisibility.id());
    Assert.assertEquals(playerUuid, retrievedCitizenVisibility.playerUuid());
    Assert.assertEquals(1, retrievedCitizenVisibility.npcId());
    Assert.assertTrue(retrievedCitizenVisibility.isNpcVisible());
  }
}
