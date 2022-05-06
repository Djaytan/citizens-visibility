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

package fr.voltariuss.bukkit.citizens_visibility.model.dao;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract JPA DAO class to be inherited by entities' DAO classes.
 *
 * <p>The purpose of this class is to make generic the management of sessions and transactions
 * without the need to set up a utility class or duplicate the logic over each DAO class.
 *
 * @param <T> The entity type.
 * @param <I> The ID type of the entity.
 */
public abstract class JpaDao<T, I extends Serializable> implements Dao<T, I> {

  private final SessionFactory sessionFactory;
  private final Class<T> persistentClass;

  /**
   * Constructor.
   *
   * @param sessionFactory The session factory.
   */
  @SuppressWarnings({"MoveFieldAssignmentToInitializer", "unchecked"})
  protected JpaDao(@NotNull SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
    this.persistentClass =
        (Class<T>)
            ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
  }

  @Override
  public @NotNull Optional<T> findById(@NotNull I id) {
    Preconditions.checkNotNull(id);
    return executeQueryTransaction(
            session ->
                session
                    .createQuery(
                        String.format(
                            "SELECT %1$s FROM %1$s WHERE id = :id",
                            persistentClass.getSimpleName()),
                        persistentClass)
                    .setParameter("id", id))
        .uniqueResultOptional();
  }

  @Override
  public @NotNull List<T> findAll() {
    return executeQueryTransaction(
            session ->
                session.createQuery(
                    String.format("SELECT %1$s FROM %1$s", persistentClass.getSimpleName()),
                    persistentClass))
        .list();
  }

  @Override
  public void persist(@NotNull T entity) {
    Preconditions.checkNotNull(entity);
    executeTransaction(session -> session.persist(entity));
  }

  @Override
  public void update(@NotNull T entity) {
    Preconditions.checkNotNull(entity);
    executeTransaction(session -> session.merge(entity));
  }

  @Override
  public void delete(@NotNull T entity) {
    Preconditions.checkNotNull(entity);
    executeTransaction(session -> session.delete(entity));
  }

  protected void executeTransaction(@NotNull Consumer<Session> action) {
    Preconditions.checkNotNull(action);

    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      action.accept(session);
      session.getTransaction().commit();
    } catch (RuntimeException e) {
      throw new JpaDaoException("Something went wrong during session", e);
    }
  }

  protected @NotNull Query<T> executeQueryTransaction(@NotNull Function<Session, Query<T>> action) {
    Preconditions.checkNotNull(action);

    try (Session session = sessionFactory.openSession()) {
      session.beginTransaction();
      Query<T> query = action.apply(session);
      session.getTransaction().commit();
      return query;
    } catch (RuntimeException e) {
      throw new JpaDaoException("Something went wrong during session", e);
    }
  }
}
