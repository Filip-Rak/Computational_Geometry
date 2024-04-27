import java.awt.*;
import java.util.LinkedList;

public class EarClippingVis
{
    // Attributes
    // Important storage
    private CircularList points;
    private LinkedList<Triangle> triangles;

    // Visualization
    private DisplayFrame window;
    private int delay;

    // Constructor
    EarClippingVis(LinkedList<Point> pointsInput, int delay)
    {
        initStorage(pointsInput);
        initVisualization(delay);

        Triangulate();
    }

    // Initialization methods
    private void initStorage(LinkedList<Point> pointsInput)
    {
        this.points = new CircularList(pointsInput);
        this.triangles = new LinkedList<>();
    }

    private void initVisualization(int delay)
    {
        this.delay = delay;
        window = new DisplayFrame(true, 800, 600);
    }

    // Building method
    private void Triangulate()
    {
        while(points.size() > 3)
        {
            // Saving points to buffer
            Point previousPoint = points.getPrevious();
            Point currentPoint = points.getNext();
            Point nextPoint = points.getNext();

            // Visual outline
            window.panel.AddDrawable(new Polygon(points.copy()));

            if(isConvex(currentPoint, previousPoint, nextPoint))
            {
                Triangle potentialTriangle = new Triangle(previousPoint, currentPoint, nextPoint);
                if(validateTriangle(potentialTriangle, previousPoint, currentPoint, nextPoint))
                {
                    triangles.add(potentialTriangle);
                    points.remove(currentPoint);
                    points.setCurrentIndex(0);
                }
                else
                {
                    // Adding invalid triangle to screen
                    window.panel.AddDrawable(potentialTriangle, Color.red);
                }
            }


            // Visuals
            window.panel.AddDrawable(triangles, Color.green);
            window.panel.AddDrawable(points.copy(), Color.BLUE);
            window.panel.AddDrawable(currentPoint, Color.YELLOW);
            refreshScreen();
        }

        // Three last verticies make the last triangle
        triangles.add(new Triangle(points.getNext(), points.getNext(), points.getNext()));

        // Visuals
        window.panel.AddDrawable(triangles, Color.green);
        window.panel.AddDrawable(points.copy(), Color.BLUE);
        refreshScreen();
    }

    private boolean validateTriangle(Triangle potentialTriangle, Point previousPoint, Point currentPoint, Point nextPoint)
    {
        for(Point p : points.copy())
        {
            if(p != previousPoint && p != currentPoint && p != nextPoint)
            {
                if(potentialTriangle.contains(p))
                    return false;
            }
        }

        return true;
    }

    private boolean isConvex(Point tgt, Point left, Point right)
    {
        Line l1 = new Line(tgt, left, false);
        Line l2 = new Line(tgt, right, false);

        return Line.angle360(l1, l2) < 180;
    }

    private void refreshScreen()
    {
        window.panel.refreshAndClear();
        try{Thread.sleep(this.delay);}
        catch (Exception ignore){}
    }

    // Getters
    public LinkedList<Triangle> getTriangles() { return this.triangles; }
}