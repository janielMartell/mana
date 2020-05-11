package org.mana.exception;

public class EntityAlreadyExistsException extends EntityException {
    /**
     * Constructs a {@code EntityAlreadyExistsException} with no detail message.
     */
    public EntityAlreadyExistsException() {
        super();
    }

    /**
     * Constructs a {@code EntityAlreadyExistsException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public EntityAlreadyExistsException(String s) {
        super(s);
    }
}
