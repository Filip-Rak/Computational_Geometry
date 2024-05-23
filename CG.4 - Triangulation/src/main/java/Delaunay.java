import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class Delaunay
{
    // Attibutes
    // Storage
    private LinkedList<Point> points;
    private LinkedList<Triangle> triangles;
    private Triangle superTriangle;

    // Modifiers
    private double expansion;

    // Constructors
    Delaunay(LinkedList<Point> pointsInput, double expansion)
    {
        // Initialization
        initModifiers(expansion);
        initStorage(pointsInput);
        initTriangulation();

        // Algortihm's loop
        Triangulate();
    }

    // Initialization methods
    private void initStorage(LinkedList<Point> pointsInput)
    {
        // Save points and prepare triangle storage
        this.points = new LinkedList<>(pointsInput);
        this.triangles = new LinkedList<>();

        // Calculate the super triangle
        this.superTriangle = calcSupTriangle();
    }

    private Triangle calcSupTriangle()
    {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Point p : points)
        {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        // Triangle's size modifier
        double expansionValue = Math.max(maxX - minX, maxY - minY) * this.expansion;

        // Verticies
        Point vertex1 = new Point(minX - expansionValue, minY - expansionValue);
        Point vertex2 = new Point(maxX + expansionValue, minY - expansionValue);
        Point vertex3 = new Point((minX + maxX) / 2, maxY + expansionValue);

        return new Triangle(vertex1, vertex2, vertex3);
    }

    private void initModifiers(double expansion)
    {
        this.expansion = expansion;
    }

    private void initTriangulation()
    {
        // Get superTriangle's verticies and make tringles with the first point
        LinkedList<Point> s_verts = superTriangle.getVerticesList();
        triangles.add(new Triangle(s_verts.get(0), s_verts.get(1), points.getFirst()));
        triangles.add(new Triangle(s_verts.get(2), s_verts.get(1), points.getFirst()));
        triangles.add(new Triangle(s_verts.get(2), s_verts.get(0), points.getFirst()));
    }

    // Triangulation methods
    private void Triangulate()
    {
        for(int i = 1; i < points.size(); i++)
        {
            Point currentPoint = points.get(i);

            // Find all broken traingles and delete them
            LinkedList<Triangle> brokenTriangles = findBrokenTriangles(currentPoint);
            removeTriangles(brokenTriangles);

            // Get all verticies of deleted triangles
            LinkedList<Point> verts = uniqueVerts(brokenTriangles);

            // Sort verts by angles between them and curent
            sortAngles(verts, currentPoint);

            // Create new triangles with current as their point
            addTriangles(verts, currentPoint);
        }

        // Dispose of the super triangle and triangles sharing it's verticies
        removeSuper();
    }

    private void removeSuper()
    {
        LinkedList<Triangle> toRemove = new LinkedList<>();
        Point A = superTriangle.getVertice(0);
        Point B = superTriangle.getVertice(1);
        Point C = superTriangle.getVertice(2);

        for(Triangle t : this.triangles)
        {
            for(Point p : t.getVertices())
            {
                if(A.getY() == p.getY() && A.getX() == p.getX())
                    toRemove.add(t);
                else if(B.getY() == p.getY() && B.getX() == p.getX())
                    toRemove.add(t);
                else if(C.getY() == p.getY() && C.getX() == p.getX())
                    toRemove.add(t);
            }
        }

        removeTriangles(toRemove);
    }

    private void addTriangles(LinkedList<Point> verts, Point currentPoint)
    {
        for(int i = 0; i < verts.size()-1; i++)
        {
            Triangle newTriangle = new Triangle(verts.get(i), verts.get(i+1), currentPoint);
            this.triangles.add(newTriangle);
        }


        if(verts.size() >= 2)
        {
            Triangle lastTriangle = new Triangle(verts.getLast(), verts.getFirst(), currentPoint);
            this.triangles.add(lastTriangle);
        }
    }

    private void removeTriangles(LinkedList<Triangle> brokenTriangles)
    {
        for(Triangle broken : brokenTriangles)
            triangles.remove(broken);
    }

    private LinkedList<Point> uniqueVerts(LinkedList<Triangle> triangles)
    {
        LinkedList<Point> points = new LinkedList<>();

        // Get all verticies
        for(Triangle t : triangles)
            points.addAll(t.getVerticesList());

        return Point.getUnique(points);
    }

    private LinkedList<Triangle> findBrokenTriangles(Point currentPoint)
    {
        LinkedList<Triangle> badOnes = new LinkedList<>();

        for(Triangle t : this.triangles)
        {
            Circle circle = Circle.circumcircle(t);

            if(circle == null)
                continue;

            if(inRange(circle, currentPoint))
                badOnes.add(t);
        }

        return badOnes;
    }

    private boolean inRange(Circle circle, Point tgt)
    {
        return Point.distance(tgt, circle.getCenter()) - 0.1 < circle.getRadius();
    }

    private void sortAngles(List<Point> points, Point center)
    {
        points.sort((p1, p2) ->
        {
            double angle1 = Math.atan2(p1.y - center.y, p1.x - center.x);
            double angle2 = Math.atan2(p2.y - center.y, p2.x - center.x);
            return Double.compare(angle1, angle2);
        });
    }

    // Getters
    LinkedList<Triangle> getTriangles() { return this.triangles; }
}