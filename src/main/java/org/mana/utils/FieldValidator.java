package org.mana.utils;

public class FieldValidator {

    public void FieldValidator() {}

    /**
     * Indicates whether a specified string is null, empty, or consists only of white-space characters.
     * @param value The string to test.
     * @return true if the value parameter is null or Empty, or if value consists exclusively of white-space characters.
     */
    public boolean isNullOrWhitespace(String value) {
        if(value == null)
            return true;

        final int len = value.length();

        if(len == 0)
            return true;

        for(int i = 0; i < len; i++)
            if (!Character.isWhitespace(value.charAt(i)))
                return false;

        return true;
    }
}
