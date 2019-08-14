import java.awt.*;

public class Circle
{
    public final static Color CIRCLE_COLOR = AlgoVisHelper.Blue;
    private double x,y,r;
    private boolean stillExists;
    private Color color;

    public double getX() {return x;}
    public double getY() {return y;}
    public double getR() {return r;}
    public boolean getStillExists() {return stillExists;}
    public Color getColor() {return color;}

    public Circle (double x, double y, double r)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        this.stillExists = true;
        this.color = CIRCLE_COLOR;
    }

    public void destroy() {stillExists = false;}
}