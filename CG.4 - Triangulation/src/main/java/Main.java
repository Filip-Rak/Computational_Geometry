import java.util.LinkedList;

public class Main
{
    public static void main (String[] args)
    {
        //delaunay_example();
        //ear_clipping_example();
        comparison();
    }

    // Examples
    private static void delaunay_example()
    {
        LinkedList<Point> points = Point.loadFromFile("input/d_example1.txt");
        if(points != null)
        {
            //Point.transformList(points, 0.6, 0.6, -400, -200); // For d2
            DelaunayVis t = new DelaunayVis(points, 1.5, 100);
            displayResults(t.getTriangles(), "Delaunay", -1);
        }

    }

    private static void ear_clipping_example()
    {
        LinkedList<Point> points = Point.loadFromFile("input/ec_example.txt");

        if(points != null)
        {
            Point.transformList(points, 1, -1, -250, 200);
            EarClippingVis ec = new EarClippingVis(points, 1000); // delay = 1000
            displayResults(ec.getTriangles(), "Ear Clipping", -1);
        }
    }

    // Comparisons
    private static void comparison()
    {
        LinkedList<Point> points = Point.loadFromFile("input/comparison1.txt");

        if(points != null)
        {
            Point.transformList(points, 0.5, -0.5, -380, 250);

            // Time measurment
            Stoper stoper = new Stoper();

            // Delaunay
            stoper.start();
            Delaunay delaunay = new Delaunay(points, 1.5);
            long delaunay_duration = stoper.stop();

            // Ear clipping
            stoper.start();
            EarClipping earClipping = new EarClipping(points);
            long ear_clipping_duration = stoper.stop();

            displayResults(delaunay.getTriangles(), "Delaunay", delaunay_duration);
            displayResults(earClipping.getTriangles(), "Ear Clipping", ear_clipping_duration);
        }
    }

    // Helper methods
    static void displayResults(LinkedList<Triangle> triangles, String title, long time)
    {
        Statistics s = new Statistics(triangles, title, time);

        //Triangle.transformList(triangles, 0.8, 0.8, -200, 200);
        DisplayFrame window = new DisplayFrame(true, 800, 600);
        window.panel.AddDrawable(triangles);
        window.panel.refreshBufferedImage();
    }
}
