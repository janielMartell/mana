package org.mana.db.model;

import org.mana.db.entity.Entity;
import org.mana.exception.EntityException;

import java.sql.SQLException;
import java.util.Collection;

public interface Model<T extends Entity> {
    /**
     * Saves a given entity.
     * @param entity entity to save, must not be null.
     * @throws IllegalArgumentException in case the given entity is null.
     * @throws NullPointerException in case the entity properties are null.
     * @throws SQLException in case it fails to connect to the database
     * @throws EntityException in case the database signals 45000 error.
     */
    public void save(T entity) throws IllegalArgumentException, SQLException, NullPointerException, EntityException;

    /**
     * Get all records from database of the given entity.
     * @return a list of the specified entity.
     * @throws SQLException
     */
    public Collection<T> findAll();
}
