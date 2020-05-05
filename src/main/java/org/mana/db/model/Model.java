package org.mana.db.model;

import org.mana.db.entity.Entity;
import org.mana.exception.EntityAlreadyExistsException;

import java.sql.SQLException;

public interface Model<T extends Entity> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely.
     * @param entity entity to save, must not be null.
     * @return the saved entity; or null if it fails.
     * @throws IllegalArgumentException in case the given entity is null.
     * @throws NullPointerException in case the entity properties are null.
     * @throws SQLException in case it fails to connect to the database
     * @throws EntityAlreadyExistsException in case the username is already taken.
     */
    public void save(T entity) throws IllegalArgumentException, SQLException, NullPointerException, EntityAlreadyExistsException;
}
