import java.awt.*;

public class Circle implements Drawable
{
    // Attributes
    private Point center;
    private double radius;

    // Constructor
    Circle (Point center, double radius)
    {
        this.center = center;
        this.radius = radius;
    }

    // Getters
    public Point getCenter() { return center; }

    public double getRadius() { return radius; }

    public static Circle circumcircle(Point A, Point B, Point C)
    {
        double ax = A.x, ay = A.y;
        double bx = B.x, by = B.y;
        double cx = C.x, cy = C.y;

        double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));
        if (d == 0)
            return null; // Punkty są współliniowe, okrąg opisany nie istnieje

        double ux = ((ax*ax + ay*ay) * (by - cy) + (bx*bx + by*by) * (cy - ay) + (cx*cx + cy*cy) * (ay - by)) / d;
        double uy = ((ax*ax + ay*ay) * (cx - bx) + (bx*bx + by*by) * (ax - cx) + (cx*cx + cy*cy) * (bx - ax)) / d;

        double radius = Math.sqrt((ux - ax) * (ux - ax) + (uy - ay) * (uy - ay));
        return new Circle(new Point(ux, uy), radius);
    }

    public static Circle circumcircle(Triangle triangle)
    {
        Point A = triangle.getVertice(0);
        Point B = triangle.getVertice(1);
        Point C = triangle.getVertice(2);

        return circumcircle(A, B, C);
    }

    public void draw(Graphics2D g, int width, int height)
    {
        double X = this.center.getX();
        double Y = this.center.getY();

        // (height - Y) flips the Y coordinate to match the graphical context
        X += width / 2.0;
        Y = height / 2.0 - Y;  // Adjust Y to graphical context from a centered origin

        // Calculate the top-left corner for the oval drawing method
        int topLeftX = (int) (X - radius);
        int topLeftY = (int) (Y - radius);

        int diameter = (int) (radius * 2);

        g.drawOval(topLeftX, topLeftY, diameter, diameter);
    }

    public void update(Circle c)
    {
        this.center = c.getCenter();
        this.radius = c.getRadius();
    }
}