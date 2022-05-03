package fr.voltariuss.bukkit.citizens_visibility.guice.mock;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Named;
import javax.inject.Singleton;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;

public class GuiceGeneralMockModule extends AbstractModule {

  @Provides
  @Singleton
  @Named("jdbcUrl")
  public @NotNull String provideJdbcUrl() {
    return "";
  }

  @Provides
  @Singleton
  @Named("debugMode")
  public boolean provideDebugMode() {
    return false;
  }

  @Provides
  @Singleton
  public @NotNull SessionFactory provideSessionFactory() {
    return Mockito.mock(SessionFactory.class);
  }
}
