import java.util.LinkedList;

public class EarClipping
{
    // Attributes
    private CircularList points;
    private LinkedList<Triangle> triangles;

    // Constructor
    EarClipping(LinkedList<Point> pointsInput)
    {
        initAttributes(pointsInput);
        Triangulate();
    }

    // Initialization methods
    private void initAttributes(LinkedList<Point> pointsInput)
    {
        this.points = new CircularList(pointsInput);
        this.triangles = new LinkedList<>();
    }

    // Building methods
    private void Triangulate()
    {
        while(points.size() > 3)
        {
            // Saving points to buffer
            Point previousPoint = points.getPrevious();
            Point currentPoint = points.getNext();
            Point nextPoint = points.getNext();


            if(isConvex(currentPoint, previousPoint, nextPoint))
            {
                Triangle potentialTriangle = new Triangle(previousPoint, currentPoint, nextPoint);
                if(validateTriangle(potentialTriangle, previousPoint, currentPoint, nextPoint))
                {
                    triangles.add(potentialTriangle);
                    points.remove(currentPoint);
                    points.setCurrentIndex(0);
                }

            }
        }

        // Three last verticies make the last triangle
        triangles.add(new Triangle(points.getNext(), points.getNext(), points.getNext()));
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

    // Getters
    public LinkedList<Triangle> getTriangles() { return this.triangles; }
}