import java.awt.*;

public class MapDimension
{
    private int maxX,maxY;
    private int Width, Height;
    public static final Color boundaryColor = AlgoVisHelper.Yellow;

    public MapDimension(int maxX, int maxY)
    {
        this.maxX = Math.abs(maxX);
        this.maxY = Math.abs(maxY);
        Width = Math.abs(maxX * 2);
        Height = Math.abs(maxY * 2);
    }

    public int getWidth() {return Width;}
    public int getHeight() {return Height;}
}
