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

package fr.voltariuss.bukkit.citizens_visibility.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import fr.voltariuss.bukkit.citizens_visibility.CitizensVisibilityRuntimeException;
import fr.voltariuss.bukkit.citizens_visibility.JdbcUrl;
import fr.voltariuss.bukkit.citizens_visibility.model.entity.CitizenVisibility;
import javax.inject.Named;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class GuiceCitizensVisibilityModule extends AbstractModule {

  private final JdbcUrl jdbcUrl;
  private final Logger logger;

  public GuiceCitizensVisibilityModule(@NotNull Logger logger) {
    this.jdbcUrl = new JdbcUrl("", 0, ""); // TODO: JdbcUrl
    this.logger = logger;
  }

  @Provides
  @Singleton
  public @NotNull JdbcUrl provideJdbcUrl() {
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
      Configuration configuration = new Configuration();

      configuration.setProperty(AvailableSettings.URL, jdbcUrl.asStringUrl());
      configuration.setProperty(AvailableSettings.USER, ""); // TODO: database username
      configuration.setProperty(AvailableSettings.PASS, ""); // TODO: database password
      configuration.setProperty(
          AvailableSettings.CONNECTION_PROVIDER,
          "org.hibernate.hikaricp.internal.HikariCPConnectionProvider");
      configuration.setProperty(AvailableSettings.DRIVER, "org.mariadb.jdbc.Driver");
      configuration.setProperty(AvailableSettings.DATASOURCE, "org.mariadb.jdbc.MariaDbDataSource");
      configuration.setProperty(
          AvailableSettings.DIALECT, "org.hibernate.dialect.MariaDBDialect"); // TODO: SQLite
      configuration.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
      configuration.setProperty(AvailableSettings.SHOW_SQL, "false");
      configuration.setProperty(AvailableSettings.FORMAT_SQL, "false");
      configuration.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
      configuration.setProperty(AvailableSettings.HBM2DDL_CHARSET_NAME, "UTF-8");
      // TODO: cache properties definition

      configuration.setProperty("hibernate.hikari.maximumPoolSize", "10");
      configuration.setProperty("hibernate.hikari.minimumIdle", "5");
      // Because plugin is mono-thread only one SQL request is dispatched at the same time, so there
      // isn't any concurrency with the database. It's why serializable transaction isolation is
      // actually the preference to ensure the best isolation as possible.
      configuration.setProperty(
          "hibernate.hikari.transactionIsolation", "TRANSACTION_SERIALIZABLE");

      configuration.addAnnotatedClass(CitizenVisibility.class);

      logger.info("Database connexion established.");
      return configuration.buildSessionFactory();
    } catch (HibernateException e) {
      throw new CitizensVisibilityRuntimeException(
          String.format("Database connection failed: %s", jdbcUrl.asStringUrl()), e);
    }
  }
}
