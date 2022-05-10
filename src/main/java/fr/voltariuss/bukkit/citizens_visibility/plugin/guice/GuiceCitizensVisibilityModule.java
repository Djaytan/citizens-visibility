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

package fr.voltariuss.bukkit.citizens_visibility.plugin.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.voltariuss.bukkit.citizens_visibility.CitizensVisibilityRuntimeException;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.DefaultCitizenVisibility;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.Player;
import java.nio.file.Path;
import javax.inject.Named;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.sqlite.JDBC;
import org.sqlite.hibernate.dialect.SQLiteDialect;

public class GuiceCitizensVisibilityModule extends AbstractModule {

  private final Logger logger;
  private final String jdbcUrl;

  public GuiceCitizensVisibilityModule(@NotNull Logger logger, @NotNull Path pluginDataSourcePath) {
    this.logger = logger;

    Path sqliteDatabaseFile = pluginDataSourcePath.resolve("data.db");

    this.jdbcUrl = "jdbc:sqlite:" + sqliteDatabaseFile;
  }

  @Provides
  @Singleton
  @Named("jdbcUrl")
  public @NotNull String provideJdbcUrl() {
    return jdbcUrl;
  }

  @Provides
  @Named("debugMode")
  public Boolean provideDebugMode() {
    boolean debugMode = false;
    logger.info("Debug mode: {}", debugMode);
    return debugMode;
  }

  @Provides
  @Singleton
  public @NotNull SessionFactory provideSessionFactory() {
    try {
      // The SessionFactory must be built only once for application lifecycle
      Configuration configuration =
          new Configuration()
              .setProperty(AvailableSettings.URL, jdbcUrl)
              .setProperty(AvailableSettings.AUTOCOMMIT, "false")
              .setProperty(AvailableSettings.CONNECTION_PROVIDER_DISABLES_AUTOCOMMIT, "true")
              .setProperty(
                  AvailableSettings.CONNECTION_PROVIDER,
                  "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
              .setProperty(AvailableSettings.DRIVER, JDBC.class.getName())
              .setProperty(AvailableSettings.DATASOURCE, "org.sqlite.SQLiteDataSource")
              .setProperty(AvailableSettings.DIALECT, SQLiteDialect.class.getName())
              .setProperty(AvailableSettings.SHOW_SQL, "false")
              .setProperty(AvailableSettings.FORMAT_SQL, "false")
              .setProperty(AvailableSettings.HBM2DDL_AUTO, "update")
              .setProperty(AvailableSettings.HBM2DDL_CHARSET_NAME, "UTF-8")
              .setProperty("hibernate.hikari.autoCommit", "false")
              .setProperty("hibernate.hikari.maximumPoolSize", "10")
              .setProperty("hibernate.hikari.minimumIdle", "5")
              .addAnnotatedClass(CitizenVisibility.class)
              .addAnnotatedClass(DefaultCitizenVisibility.class)
              .addAnnotatedClass(Player.class);

      logger.info("Database connexion established (SQLite).");
      return configuration.buildSessionFactory();
    } catch (HibernateException e) {
      throw new CitizensVisibilityRuntimeException(
          String.format("Database connection failed (SQLite): %s", jdbcUrl), e);
    }
  }
}
