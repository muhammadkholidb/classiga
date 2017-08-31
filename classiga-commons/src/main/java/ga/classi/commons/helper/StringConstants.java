package ga.classi.commons.helper;

import java.io.File;

/**
 *
 * @author eatonmunoz
 */
public interface StringConstants {

    // Read https://stackoverflow.com/questions/1417190/should-a-static-final-logger-be-declared-in-upper-case#answer-12069687

    static final String fileSeparator = File.separator;
    static final String lineBreak = System.lineSeparator();
    
    static final String EMPTY = "";
    static final String SPACE = " ";
    static final String TAB = "\t";
    static final String SINGLE_QUOTE = "'";
    static final String DOUBLE_QUOTE = "\"";
    static final String DOT = ".";
    static final String COMMA = ",";
    static final String COLON = ":";
    static final String SEMICOLON = ";";
    static final String PLUS = "+";
    static final String MINUS = "-";
    static final String UNDERSCORE = "_";

}
