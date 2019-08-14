import java.awt.*;

public class Body
{
    private static final Color BODY_COLOR = AlgoVisHelper.Orange;
    private static final Color GRAVITY_COLOR = AlgoVisHelper.DarkGrey;
    private static final Color GRAVITY_BOUND_COLOR = AlgoVisHelper.Teal;

    private double x,y,m,bodyR, gravityR;
    private Color bodyColor, gravityColor, boundColor;

    public double getX() {return x;}
    public double getY() {return y;}
    public double getM() {return m;}
    public double getBodyR() {return bodyR;}
    public double getGravityR() {return gravityR;}
    public Color getBodyColor() {return bodyColor;}
    public Color getGravityColor() {return gravityColor;}
    public Color getBoundColor() {return boundColor;}

    public Body (double x, double y, double m, double bodyR, double gravityR)
    {
        this.x = x;
        this.y = y;
        this.m = m;
        this.bodyR = bodyR;
        this.gravityR = gravityR;
        this.bodyColor = BODY_COLOR;
        this.gravityColor = GRAVITY_COLOR;
        this.boundColor = GRAVITY_BOUND_COLOR;
    }
}