public class Edge
{
    // Attributes
    private final Point p1, p2;

    // Constructor
    Edge(Point p1, Point p2)
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    // Methods
    public boolean pointIncluded(Point p)
    {
        return p == this.p1 || p == this.p2;
    }

    public Point middlePoint()
    {
        double X = (p1.getX() + p2.getX()) / 2;
        double Y = (p1.getY() + p2.getY()) / 2;

        return new Point(X, Y);
    }

    public Point directionVector()
    {
        double X = p1.getX() - p2.getX();
        double Y = p1.getY() - p2.getY();

        return new Point(X, Y);
    }

    public Point getP1() { return this.p1; }
    public Point getP2() { return this.p2; }
}