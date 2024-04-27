import java.awt.*;
import java.util.LinkedList;

public class Main
{
    public static void main (String[] args)
    {
        //M1();
        M2();
        //ec1();
        //pg1();
        //pg2();
    }

    private static void pg2()
    {
        Triangle t = new Triangle(new Point(0, 0), new Point(20, 0), new Point(50, 86.5));

        DisplayFrame window = new DisplayFrame(true, 800, 600);

        System.out.println(triangleQuality(t));

        window.panel.AddDrawable(t, Color.green);
        window.panel.refreshBufferedImage();
    }

    private static double triangleQuality(Triangle triangle)
    {
        double AB = Point.distance(triangle.getVertice(0), triangle.getVertice(1));
        double AC = Point.distance(triangle.getVertice(0), triangle.getVertice(2));
        double BC = Point.distance(triangle.getVertice(1), triangle.getVertice(2));
        double perimeter = AB + AC + BC;

        // Calculate the average length of the sides
        double averageLength = perimeter / 3.0;

        // Calculate the sum of absolute differences between each side and the average length
        double sumDifferences = Math.abs(AB - averageLength) + Math.abs(AC - averageLength) + Math.abs(BC - averageLength);

        // Normalize the result to range between 0 and 1
        double quality = sumDifferences / perimeter;

        // Invert the quality to get 1 for perfect equilateral triangle
        quality = 1 - quality;

        return quality;
    }


    private static void pg1()
    {
        Point A = new Point(0, 0);
        Point B = new Point(100, 0);
        Point C = new Point(-50, 100);

        Point Q = new Point(0, -1);

        Triangle T = new Triangle(A, B, C);
        DisplayFrame window = new DisplayFrame(true, 800, 600);

        System.out.println(T.contains(Q));

        window.panel.AddDrawable(T);
        window.panel.AddDrawable(Q, Color.RED);
        window.panel.refreshAndClear();
    }

    private static void ec1()
    {
        LinkedList<Point> points = Point.loadFromFile("input/ec1.txt");

        if(points != null)
        {
            Point.transformList(points, 1, -1, -250, 200);
            EarClipping ec = new EarClipping(points, 0); // delay = 1000

            DisplayFrame window = new DisplayFrame(true, 800, 600);

            LinkedList<Triangle> triangles = ec.getTriangles();
            window.panel.AddDrawable(triangles);
            window.panel.refreshBufferedImage();

            Statistics s = new Statistics(triangles, "Ear Clipping");
        }
    }

    private static void M2()
    {
        LinkedList<Point> points = Point.loadFromFile("input/d1.txt");

        if(points != null)
        {

            Point.transformList(points, 0.8, -0.8, -400, 280);
            Delaunay t = new Delaunay(points, 1.5, 10);

            LinkedList<Triangle> triangles = t.getTriangles();
            DisplayFrame window = new DisplayFrame(true, 800, 600);
            for(Triangle triangle : triangles)
                window.panel.AddDrawable(triangle);

            window.panel.refreshBufferedImage();

            Statistics s = new Statistics(triangles, "Delaunay");
        }
    }

    private static void M1()
    {
        LinkedList<Point> points = new LinkedList<>();
        points.add(new Point(0, -200));
        points.add(new Point(100, -100));
        points.add(new Point(200, 0));
        points.add(new Point(100, 100));
        points.add(new Point(0, 200));
        points.add(new Point(-100, 100));
        points.add(new Point(-200, 0));
        points.add(new Point(-100, -100));

        Delaunay t = new Delaunay(points, 1.5, 100);
        LinkedList<Triangle> mesh = t.getTriangles();

        DisplayFrame window = new DisplayFrame(true, 800, 600);
        for(Triangle triangle : mesh)
            window.panel.AddDrawable(triangle);

        window.panel.refreshBufferedImage();

    }

    static LinkedList<Point> prepareInput(String filename)
    {
        LinkedList<Triangle> triangles = Triangle.loadFromFile(filename);
        if(triangles != null)
        {
            LinkedList<Point> points = Point.extractPoints(triangles);

            return Point.getUnique(points);
        }

        return null;
    }
}
