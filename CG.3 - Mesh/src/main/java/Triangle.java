import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Triangle implements Drawable
{
    // Attributes
    private final Point[] vertices = new Point[3];
    private final double[] lengths = new double[3];
    private List<Triangle> neighbours;

    // Constructors
    Triangle (Point p1, Point p2, Point p3)
    {
        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distance(vertices[0], vertices[1]);
        this.lengths[1] = Point.distance(vertices[1], vertices[2]);
        this.lengths[2] = Point.distance(vertices[2], vertices[0]);
    }

    // Methods
    public void draw(Graphics2D g, int width, int height)
    {
        int centerX = width / 2;
        int centerY = height / 2;

        //Adjust each vertex position to make the screen center as (0, 0)
        double x0 = vertices[0].getX() + centerX;
        double y0 = centerY - vertices[0].getY(); //Inverting Y axis
        double x1 = vertices[1].getX() + centerX;
        double y1 = centerY - vertices[1].getY(); //Inverting Y axis
        double x2 = vertices[2].getX() + centerX;
        double y2 = centerY - vertices[2].getY(); //Inverting Y axis

        //Draw lines between vertices with adjusted coordinates
        g.drawLine((int) x0, (int) y0, (int) x1, (int) y1);
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        g.drawLine((int) x2, (int) y2, (int) x0, (int) y0);
    }

    public static boolean isValid(Point A, Point B, Point C)
    {
        double a = Point.distance(A, B);
        double b = Point.distance(B, C);
        double c = Point.distance(C, A);

        if(a + b > c )
        {
            if(a + c > b)
            {
                if(b + c > a)
                    return true;
            }
        }

        return false;
    }

    public void addNeighbour(Triangle t)
    {
        neighbours.add(t);
    }

    public static boolean validateAngles(Point a, Point b, Point c, double threshold)
    {
        double angleAB_C = Point.angle360(a, b, c); // Angle at A formed by sides AB and AC
        double angleBC_A = Point.angle360(b, c, a); // Angle at B formed by sides BC and BA
        double angleCA_B = Point.angle360(c, a, b); // Angle at C formed by sides CA and CB

        if
        (
                (angleAB_C < threshold && angleBC_A < threshold) ||
                (angleAB_C < threshold && angleCA_B < threshold) ||
                (angleBC_A < threshold && angleCA_B < threshold)
        )
            return false; // This means two or more angles are less than the threshold


        double sumOfAngles = angleAB_C + angleBC_A + angleCA_B;

        // Check if the sum of angles is approximately 180 degrees
        return Math.abs(sumOfAngles - 180) < 0.001; // Allowing a small margin for floating point arithmetic
    }

    // Getters
    public Point getVertice(int index)
    {
        if(index > 3 || index < 0)
            return null;

        return this.vertices[index];
    }

    public LinkedList<Line> getLines()
    {
        LinkedList<Line> lines = new LinkedList<>();
        lines.add(new Line(vertices[0], vertices[1], false));
        lines.add(new Line(vertices[1], vertices[2], false));
        lines.add(new Line(vertices[0], vertices[2], false));

        return lines;
    }

    public Point[] getVerticies() { return this.vertices; }
    public double[] getLengths() { return this.lengths; }
    public List<Triangle> getNeighbours() { return this.neighbours; }
}