/*
 * 
 * Licensed under the MIT License. See LICENSE file in the project root for full license information.
 * 
 */
package ga.classi.commons.constant;

import java.io.File;

/**
 *
 * @author muhammad
 */
public final class StringConstants {

    public static final String FILE_SEPARATOR = File.separator;
    public static final String LINE_BREAK     = System.lineSeparator();
    public static final String EMPTY          = "";
    public static final String EMPTY_JSON     = "{}";
    public static final String SPACE          = " ";
    public static final String TAB            = "\t";
    public static final String SINGLE_QUOTE   = "'";
    public static final String DOUBLE_QUOTE   = "\"";
    public static final String DOT            = ".";
    public static final String COMMA          = ",";
    public static final String COLON          = ":";
    public static final String SEMICOLON      = ";";
    public static final String PLUS           = "+";
    public static final String MINUS          = "-";
    public static final String UNDERSCORE     = "_";
    public static final String ROUND_BRACKET_OPEN       = "(";
    public static final String ROUND_BRACKET_CLOSE      = ")";
    public static final String SQUARE_BRACKET_OPEN      = "[";
    public static final String SQUARE_BRACKET_CLOSE     = "]";
    public static final String CURLY_BRACKET_OPEN       = "\\{";
    public static final String CURLY_BRACKET_CLOSE      = "\\}";
    public static final String ANGLE_BRACKET_OPEN       = "<";
    public static final String ANGLE_BRACKET_CLOSE      = ">";

    private StringConstants() {
        // Restrict instantiation
    }
}
