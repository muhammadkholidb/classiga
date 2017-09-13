package ga.classi.commons.helper;

import java.util.Random;

public class DefaultUser {

    public static final Long USER_GROUP_ID = -1L;
    public static final String USER_GROUP_NAME = "Default Group";
    public static final Long USER_ID = -1L;

    private static final String[] NAMES = {
        "Johnny Depp", 
        "Kevin Spacey", 
        "Denzel Washington", 
        "Russell Crowe", 
        "Tom Cruise", 
        "John Travolta", 
        "Sylvester Stallone", 
        "Christian Bale", 
        "Dian Sastrowardoyo",
        "Morgan Freeman", 
        "Keanu Reeves", 
        "Katherine Heigl", 
        "Cameron Diaz", 
        "Nicole Kidman", 
        "Megan Fox", 
        "Meg Ryan", 
        "Sandra Bullock", 
        "Scarlett Johansson", 
        "Keira Knightley", 
        "Charlize Theron", 
        "Kate Winslet" };
    
    public static String chooseName() {
        return NAMES[new Random().nextInt(NAMES.length)];
    }

}
