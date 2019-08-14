import javax.swing.*;
import java.util.*;

public abstract class Initialization
{
    // public static final String[] ENEMY_FACES = {"resources/pig.png", "resources/pig_hit.png", "resources/pig_crushed.png"};
    // public static final String[] FISTS = {"resources/fist_from_bottom.png", "resources/fist_from_left.png",
    //                                        "resources/fist_from_top.png", "resources/fist_from_right.png"};

    public static final String[] ENEMY_FACES = {"resources/pig_crushed.png", "resources/pig_crushed_side.png",
                                                "resources/pig_crushed.png", "resources/pig_crushed_side.png",
                                                "resources/pig_hit.png", "resources/pig.png"};
    public static final String[] FISTS = {"resources/fist_from_bottom.png", "resources/fist_from_left.png",
                                            "resources/fist_from_top.png", "resources/fist_from_right.png"};
    public static final int[][] ENEMY_POSITION = {{126, 300},{217, 300},{126,397},{126,300},{126,300},{126,300}};
    public static final int[][] FIST_POSITION = {{140,331},{117,314},{140,297},{163,314}};

    public static Character initializeEnemy()
    {
        Scanner in = new Scanner(System.in);

        Character enemy = new Character("大白猪", 350, 10, 10, ENEMY_FACES, ENEMY_POSITION);
        return enemy;
    }

    public static Character initializeMe()
    {
        Character me = new Character("Me",350, 100, 100, FISTS, FIST_POSITION);
        return me;
    }
}