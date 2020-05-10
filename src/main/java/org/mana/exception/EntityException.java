package org.mana.exception;

public class EntityException extends Exception {
    /**
     * Constructs a {@code EntityException} with no detail message.
     */
    public EntityException() {
        super();
    }

    /**
     * Constructs a {@code EntityException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public EntityException(String s) {
        super(s);
    }
}
