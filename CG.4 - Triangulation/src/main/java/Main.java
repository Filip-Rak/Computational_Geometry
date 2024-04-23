import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Main
{
    public static void main (String[] args)
    {
        M1();
        //pg2();
    }

    private static void pg2()
    {
        Point A = new Point(-50, -20);
        Point B = new Point(15,10);
        Point C = new Point(-15,50);
        Point TGT = new Point(-100,150);

        Triangle t = new Triangle(A, B, C);
        Circle circum = Circle.circumcircle(A, B, C);
        DisplayFrame window = new DisplayFrame(true, 800, 600);

        Point center = circum.getCenter();
        window.panel.AddDrawable(center, Color.red);
        window.panel.AddDrawable(circum, Color.red);
        window.panel.AddDrawable(A, Color.BLUE);
        window.panel.AddDrawable(B, Color.BLUE);
        window.panel.AddDrawable(C, Color.BLUE);
        window.panel.AddDrawable(TGT, Color.YELLOW);

        while(true)
        {
            try{Thread.sleep(20);}
            catch (Exception ignore){}

            A.translate(-2, -1);
            B.translate(6, 1);
            C.translate(-1, 2);
            circum.update(Circle.circumcircle(t));
            center.setX(circum.getCenter().getX());;
            center.setY(circum.getCenter().getY());
            System.out.println(inRange(circum, TGT));

            window.panel.refreshBufferedImage();
        }
    }

    static boolean inRange(Circle circle, Point tgt)
    {
        return Point.distance(tgt, circle.getCenter()) < circle.getRadius();
    }

    static void pg1()
    {
        LinkedList<Point> points = new LinkedList<>();
        points.add(new Point(-50, 15));
        points.add(new Point(55, 20));
        points.add(new Point(-55, -30));
        points.add(new Point(55, -30));

        DisplayFrame window = new DisplayFrame(true, 800, 600);
        for(Point p : points)
            window.panel.AddDrawable(p);

        Point p1 = new Point(0, 0);
        window.panel.AddDrawable(p1, Color.BLUE);
        sortAngles(points, p1);

        for(int i = 0; i < points.size()-1; i++)
            window.panel.AddDrawable(new Triangle(points.get(i), points.get(i+1), p1), new Color(i * 100, i * 100, i * 100));

        window.panel.AddDrawable(new Triangle(points.getLast(), points.getFirst(), p1), Color.green);

        window.panel.refreshBufferedImage();
    }

    static void  M1()
    {
        LinkedList<Point> points = prepareInput("input/kurwinox.txt");

        if(points != null)
        {
            Triangulation t = new Triangulation(points, 1.5, 1000);
            LinkedList<Triangle> mesh = t.getTriangles();

            DisplayFrame window = new DisplayFrame(true, 800, 600);
            for(Triangle triangle : mesh)
                window.panel.AddDrawable(triangle);

            window.panel.refreshBufferedImage();
        }
    }

    public static void sortAngles(List<Point> points, Point center)
    {
        points.sort((p1, p2) ->
        {
            double angle1 = Math.atan2(p1.y - center.y, p1.x - center.x);
            double angle2 = Math.atan2(p2.y - center.y, p2.x - center.x);
            return Double.compare(angle1, angle2);
        });
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
