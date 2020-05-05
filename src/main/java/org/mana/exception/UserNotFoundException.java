package org.mana.exception;

public class UserNotFoundException extends Exception {
    /**
     * Constructs a {@code UserNotFoundException} with no detail message.
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Constructs a {@code UserNotFoundException} with the specified
     * detail message.
     *
     * @param   s   the detail message.
     */
    public UserNotFoundException(String s) {
        super(s);
    }
}
