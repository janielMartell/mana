package org.mana.utils;

import javafx.stage.FileChooser;

public class FieldValidator {

    public static final FileChooser.ExtensionFilter imageFilter =  new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
    public static final String timeRegex =  "\\d{2}\\:([0-5])([0-9])\\:([0-5])([0-9])";

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
