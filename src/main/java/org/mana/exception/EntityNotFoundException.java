package org.mana.exception;

public class EntityNotFoundException extends EntityException {
    /**
     * Constructs a {@code EntityNotFoundException} with no detail message.
     */
    public EntityNotFoundException() {
        super();
    }

    /**
     * Constructs a {@code EntityNotFoundException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public EntityNotFoundException(String s) {
        super(s);
    }
}
