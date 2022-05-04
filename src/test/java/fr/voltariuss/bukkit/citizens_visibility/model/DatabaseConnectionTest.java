package fr.voltariuss.bukkit.citizens_visibility.model;

import com.google.inject.Guice;
import com.google.inject.Injector;
import fr.voltariuss.bukkit.citizens_visibility.guice.test.GuiceGeneralTestModule;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import java.util.Random;
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
  void givenInMemoryDatabaseConnection_thenExpectToExistAndBeOpened() {
    Assert.assertNotNull(sessionFactory);
    Assert.assertTrue(sessionFactory.isOpen());
  }

  @Test
  void givenRandomCitizenVisibility_whenPersisted_thenFindGiveBackTheSameObjectWithIdSet() {
    Random random = new Random();

    CitizenVisibility initCv = new CitizenVisibility(UUID.randomUUID());
    initCv.citizenId(random.nextInt());
    initCv.isCitizenVisible(random.nextBoolean());

    Session session1 = sessionFactory.openSession();
    Transaction tx = session1.beginTransaction();
    session1.persist(initCv);
    tx.commit();
    session1.close();

    Session session2 = sessionFactory.openSession();
    @Nullable CitizenVisibility retrievedCv = session2.get(CitizenVisibility.class, 1L);
    session2.close();

    Assert.assertNotNull(retrievedCv);
    Assert.assertEquals(1L, retrievedCv.id());
    Assert.assertEquals(initCv.playerUuid(), retrievedCv.playerUuid());
    Assert.assertEquals(initCv.citizenId(), retrievedCv.citizenId());
    Assert.assertEquals(initCv.isCitizenVisible(), retrievedCv.isCitizenVisible());
  }
}
