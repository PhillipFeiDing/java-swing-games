import java.util.ArrayList;
public class Track {
    private ArrayList<Position> track;
    private MovingBall shuttle;
    private static final int trackLength = 1000;
    private MapDimension mapDimension;
    private Body[] bodies;
    public static final double err = 0.01;

    public Track(MovingBall shuttle, MapDimension mapDimension, Body[] bodies){
        track = new ArrayList<Position>();
        this.shuttle = new MovingBall(shuttle.getX(), shuttle.getY(), shuttle.getVx(), shuttle.getVy(), shuttle.getR(), shuttle.getColor());
        this.mapDimension = mapDimension;
        this.bodies = bodies;
        calculateTrack();
    }
    private void calculateTrack(){
        track.add(new Position(shuttle.getX(), shuttle.getY()));
        for (int i = 0; i < trackLength; i ++){
            shuttle.move_predicted(mapDimension, bodies);
            track.add(new Position(shuttle.getX(), shuttle.getY()));
        }
    }

    public void update(){
        shuttle.move_predicted(mapDimension, bodies);
        track.add(new Position(shuttle.getX(), shuttle.getY()));
        track.remove(0);
    }

    public void recalculate(MovingBall shuttle){
        track = new ArrayList<Position>();
        this.shuttle = new MovingBall(shuttle.getX(), shuttle.getY(), shuttle.getVx(), shuttle.getVy(), shuttle.getR(), shuttle.getColor());
        calculateTrack();
    }

    public Position getPoint(int i){
        return track.get(i);
    }
    public int length() {return track.size();}
}
