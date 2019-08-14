import java.awt.*;

public class MovingBall
{
    public static final int scoreIncrement = 1;
    private double x,y,vx,vy,r;
    private Color color;
    private int score;

    public double getX() {return x;}
    public double getY() {return y;}
    public double getVx() {return vx;}
    public double getVy() {return vy;}
    public double getR() {return r;}
    public Color getColor() {return color;}
    public int getScore() {return score;}

    public void increaseVx(double amount) {vx += amount; vx = checkVelocity(vx);}
    public void increaseVy(double amount) {vy += amount; vy = checkVelocity(vy);}

    public double checkVelocity(double v)
    {
        double maxV = r * 0.5;
        double minV = -maxV;
        double finalV = Math.min(maxV, v);
        finalV = Math.max(minV, finalV);
        return finalV;
    }

    public MovingBall (double x, double y, double vx, double vy, double r, Color color)
    {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.color = color;
        this.score = 0;
    }

    public void move(int minX, int minY, int maxX, int maxY, Circle[] circles)
    {
        x += vx;
        y += vy;
        collideWithCircles(circles);
        collideWithWalls(minX, minY, maxX, maxY);
    }

    private void collideWithWalls(int minX, int minY, int maxX, int maxY)
    {
        if (x - r < minX && vx < 0)  { x = r;         vx = -vx;}
        if (x + r >= maxX && vx > 0) { x = maxX - r;  vx = -vx;}
        if (y - r < minY && vy < 0)  { vy = -vy; y = r;}
        if (y + r >= maxY && vy > 0) { vy = -vy; y = maxY - r;}
    }

    private void collideWithCircles(Circle[] circles)
    {
        for (Circle circle: circles)
        {
            if (circle.getStillExists() & touchesTheCircle(circle))
            {
                circle.destroy();
                score += scoreIncrement;
            }
        }
    }

    private boolean touchesTheCircle(Circle circle)
    {
        double sumRSquares = Math.pow(r + circle.getR(), 2);
        double sumDistance = Math.pow(circle.getX() - x, 2) + Math.pow(circle.getY() - y, 2);
        return sumDistance < sumRSquares;
    }
}
