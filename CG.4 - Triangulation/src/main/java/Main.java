import java.awt.*;
import java.util.LinkedList;

public class Main
{
    public static void main (String[] args)
    {
        //M1();
        //M2();
        //M3();
        ec1();
        //pg1();
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
            EarClipping ec = new EarClipping(points, 1000);

            DisplayFrame window = new DisplayFrame(true, 800, 600);

            LinkedList<Triangle> triangles = ec.getTriangles();
            window.panel.AddDrawable(triangles);

            window.panel.refreshBufferedImage();
        }
    }

    private static void M3()
    {
        LinkedList<Point> points = Point.loadFromFile("input/zestaw1.txt");

        if(points != null)
        {

            Point.transformList(points, 0.8, 0.8, -450, -280);
            Triangulation t = new Triangulation(points, 1.5, 10);
            LinkedList<Triangle> triangles = t.getTriangles();
            DisplayFrame window = new DisplayFrame(true, 1000, 800);
            for(Triangle triangle : triangles)
                window.panel.AddDrawable(triangle);

            window.panel.refreshBufferedImage();
        }
    }

    private static void M2()
    {
        LinkedList<Point> points = new LinkedList<>();
        points.add(new Point(0, -250));
        points.add(new Point(50, -200));
        points.add(new Point(100, -100));
        points.add(new Point(250, 0));
        points.add(new Point(100, 100));
        points.add(new Point(50, 200));
        points.add(new Point(0, 250));
        points.add(new Point(-100, 100));
        points.add(new Point(-250, 0));
        points.add(new Point(-100, -100));

        Triangulation t = new Triangulation(points, 1.5, 0);
        LinkedList<Triangle> mesh = t.getTriangles();

        DisplayFrame window = new DisplayFrame(true, 800, 600);
        for(Triangle triangle : mesh)
            window.panel.AddDrawable(triangle);

        window.panel.refreshBufferedImage();

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

        Triangulation t = new Triangulation(points, 1.5, 100);
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
