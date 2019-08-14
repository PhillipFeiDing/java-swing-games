public class ArrowComponent {
    private double ox, oy, px, py;
    private double px1, py1, px2, py2;
    private double length;

    public ArrowComponent(double ox, double oy, double x, double y){
        this.ox = ox;
        this.oy = oy;
        this.px = x;
        this.py = y;
        calculate();
    }

    public int ox() {return (int) ox;}
    public int oy() {return (int) oy;}
    public int px() {return (int) px;}
    public int py() {return (int) py;}
    public int px1() {return (int) px1;}
    public int py1() {return (int) py1;}
    public int px2() {return (int) px2;}
    public int py2() {return (int) py2;}
    public int length() {return (int) length;}

    public void calculate() {
        length = Math.sqrt((Math.pow(px - ox, 2) + Math.pow(py - oy, 2)));
        double arrowLength = Math.max(length / 10, 5);
        double angle = Math.atan((px - ox) / (py - oy));
        if ((py - oy) < 0) {angle += Math.PI;}
        double angle1 = angle - Math.PI / 6  ;
        double angle2 = angle + Math.PI / 6  ;
        px1 = px - Math.sin(angle1) * arrowLength; py1 = py - Math.cos(angle1) * arrowLength;
        px2 = px - Math.sin(angle2) * arrowLength; py2 = py - Math.cos(angle2) * arrowLength;
    }
}
