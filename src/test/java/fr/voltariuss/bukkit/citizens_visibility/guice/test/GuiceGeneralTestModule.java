/*
 * Copyright (c) 2022 - Loïc DUBOIS-TERMOZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.voltariuss.bukkit.citizens_visibility.guice.test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import fr.voltariuss.bukkit.citizens_visibility.CitizensVisibilityRuntimeException;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import javax.inject.Named;
import javax.inject.Singleton;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.JDBC;
import org.sqlite.SQLiteDataSource;
import org.sqlite.hibernate.dialect.SQLiteDialect;

public class GuiceGeneralTestModule extends AbstractModule {

  private static final Logger logger = LoggerFactory.getLogger(GuiceGeneralTestModule.class);

  private final String jdbcUrl;

  public GuiceGeneralTestModule() {
    this.jdbcUrl = "jdbc:sqlite:test.db";
  }

  @Override
  public void configure() {}

  @Provides
  @Singleton
  @Named("jdbcUrl")
  public @NotNull String provideJdbcUrl() {
    return jdbcUrl;
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
    try {
      // The SessionFactory must be built only once for application lifecycle
      Configuration configuration =
          new Configuration()
              .setProperty(AvailableSettings.URL, jdbcUrl)
              .setProperty(AvailableSettings.DRIVER, JDBC.class.getName())
              .setProperty(AvailableSettings.DATASOURCE, SQLiteDataSource.class.getName())
              .setProperty(AvailableSettings.DIALECT, SQLiteDialect.class.getName())
              .setProperty(AvailableSettings.SHOW_SQL, "false")
              .setProperty(AvailableSettings.FORMAT_SQL, "false")
              .setProperty(AvailableSettings.HBM2DDL_AUTO, "update")
              .setProperty(AvailableSettings.HBM2DDL_CHARSET_NAME, "UTF-8")
              .addAnnotatedClass(CitizenVisibility.class)
              .configure();

      logger.info("Database connexion established (SQLite).");
      return configuration.buildSessionFactory();
    } catch (HibernateException e) {
      throw new CitizensVisibilityRuntimeException(
          String.format("Database connection failed (SQLite): %s", jdbcUrl), e);
    }
  }
}
