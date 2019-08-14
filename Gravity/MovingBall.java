import java.awt.*;

public class MovingBall{
    public static int scoreIncrement = 10;
    public static double co = 0.9;
    private double x, y, vx, vy, r;
    private Color color;
    private double oriX, oriY, oriVx, oriVy;
    private int score;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() { return vx; }

    public double getVy() {
        return vy;
    }

    public double getSpeed() {return Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));}

    public double getR() {
        return r;
    }

    public Color getColor() {
        return color;
    }

    public double getOriX() {
        return oriX;
    }

    public double getOriY() {
        return oriY;
    }

    public double getOriVx() {
        return oriVx;
    }

    public double getOriVy() {
        return oriVy;
    }

    public int getScore() {
        return score;
    }

    public void increaseVx(double amount) {
        vx += amount;
    }

    public void increaseVy(double amount) {
        vy += amount;
    }

    public MovingBall(double x, double y, double vx, double vy, double r, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.r = r;
        this.color = color;
        oriX = x;
        oriY = y;
        oriVx = vx;
        oriVy = vy;
        score = 0;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double checkVelocity(double v) {
        if (Math.abs(v) >= r / 20) return v;
        else return 0;
    }

    public void reset(double x, double y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    public void move(int minX, int minY, int maxX, int maxY, Body[] bodies, Circle[] circles) {
        x += vx;
        y += vy;
        collideWithWalls(minX, minY, maxX, maxY);
        collideWithCircles(circles);
        interactWithBodies(bodies);
    }
    public void move_predicted(MapDimension mapDimension, Body[] bodies){
        x += vx;
        y += vy;
        collideWithWalls(-mapDimension.getWidth() / 2, -mapDimension.getHeight() / 2
                , mapDimension.getWidth() / 2, mapDimension.getHeight() / 2);
        interactWithBodies(bodies);
    }
    

    private void collideWithWalls(int minX, int minY, int maxX, int maxY) {
        if (x - r < minX && vx < 0) {
            x = minX + r;
            vx = -vx;
        }
        if (x + r >= maxX && vx > 0) {
            x = maxX - r;
            vx = -vx;
        }
        if (y - r < minY && vy < 0) {
            vy = -vy;
            y = minY + r;
        }
        if (y + r >= maxY && vy > 0) {
            vy = -vy;
            y = maxY - r;
        }
    }

    private void collideWithCircles(Circle[] circles) {
        for (Circle circle : circles) {
            if (circle.getStillExists() & touchesTheCircle(circle)) {
                circle.destroy();
                score += scoreIncrement;
            }
        }
    }

    private boolean touchesTheCircle(Circle circle) {
        double sumRSquares = Math.pow(r + circle.getR(), 2);
        double sumDistance = Math.pow(circle.getX() - x, 2) + Math.pow(circle.getY() - y, 2);
        return sumDistance < sumRSquares;
    }

    private void interactWithBodies(Body[] bodies) {
        for (Body body : bodies) {
            if (withinGravityField(body))
                attraction(body);
            if (onSurfaceOrBeneath(body))
                stop();
        }
    }

    private boolean withinGravityField(Body body) {
        double centerDistanceSquared = Math.pow(x - body.getX(), 2) + Math.pow(y - body.getY(), 2);
        double gravityDistanceSquared = Math.pow(body.getGravityR(), 2);
        double bodyRSquared = Math.pow(body.getBodyR(), 2);
        if ((centerDistanceSquared <= gravityDistanceSquared) & (centerDistanceSquared > bodyRSquared)) return true;
        else return false;
    }

    private void attraction(Body body) {
        if (!onSurfaceOrBeneath(body)) {
            double centerDistanceSquared = Math.pow(x - body.getX(), 2) + Math.pow(y - body.getY(), 2);
            double gravityForce = body.getM() / centerDistanceSquared;
            double centerDistance = Math.sqrt(centerDistanceSquared);
            vx -= gravityForce * ((x - body.getX()) / centerDistance);
            vy -= gravityForce * ((y - body.getY()) / centerDistance);
        } else {
            double centerDistanceSquared = Math.pow(x - body.getX(), 2) + Math.pow(y - body.getY(), 2);
            double centerDistance = Math.sqrt(centerDistanceSquared);
            double gravityForce = body.getM() * centerDistance / Math.pow(body.getBodyR(), 3);
            vx -= gravityForce * ((x - body.getX()) / centerDistance);
            vy -= gravityForce * ((y - body.getY()) / centerDistance);
        }
    }

    //以下是反重力恶搞
    private void repulsion(Body body) {
        double centerDistanceSquared = Math.pow(x - body.getX(), 2) + Math.pow(y - body.getY(), 2);
        double gravityForce = body.getM() / centerDistanceSquared;
        double centerDistance = Math.sqrt(centerDistanceSquared);
        vx += gravityForce * ((x - body.getX()) / centerDistance);
        vy += gravityForce * ((y - body.getY()) / centerDistance);
    }

    private boolean onSurfaceOrBeneath(Body body) {
        double closestDistance = Math.sqrt(Math.pow(x - body.getX(), 2) + Math.pow(y - body.getY(), 2)) - body.getBodyR() - r;
        return closestDistance < 0;
    }

    private void stop() {
        vx = 0;
        vy = 0;
    }

    //感觉像踩了棉花的恶搞
    private void decelerate() {
        vx *= co;
        vy *= co;
    }

    public void clickAccelerate(Point p, int sceneWidth, int sceneHeight)
    {
        vx += (p.x - sceneWidth / 2) * 0.01;
        vy += (p.y - sceneHeight / 2) * 0.01;
    }
}
