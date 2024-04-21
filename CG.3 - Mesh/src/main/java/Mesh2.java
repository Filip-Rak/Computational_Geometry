import java.util.ArrayList;
import java.util.LinkedList;

public class Mesh2
{
    // Attributes
    double range;
    LinkedList<Point> original_points;
    CircularList points;
    LinkedList<Triangle> triangles;
    int iter = 0;
    int idle = 0;
    double h_multi = 1;

    // Constructor
    Mesh2(ArrayList<Point> input, double range)
    {
        initVars(input, range);
        buildMesh();
    }

    // Init Methods
    void initVars(ArrayList<Point> input, double range)
    {
        original_points = new LinkedList<>(input);
        points = new CircularList(input);
        triangles = new LinkedList<>();
        this.range = range;
    }

    void buildMesh()
    {
        while (points.size() > 2 && iter <= 5000)
        {
            iter++;

            // Save points to buffer
            // DA and DB are potential third points

            System.out.println("index: " + points.getCurrentIndex() + " Size: " + points.size());

            Point DA = points.getNext();
            Point A = points.getNext();
            Point B = points.getNext();
            Point DB = points.getNext();

            // Calculate C point for an equilateral triangle
            Point C = calculateC(A, B);

            // Check if DA or DB is withing range of C
            if(domesticPointUsed(A, B, C, DA, DB))
                points.offset(-3);  // Repeat current interation for new A and B. One got deleted
            else if(calculatedPointUsed(A, B, C))    // neither in range
                points.offset(-3);  // Addvance 1 if nothing added, advance 2 if C added
            else
            {
                points.offset(-3);
                idle++;

                if(idle >= 40)
                {
                    range *= 1.0;
                    idle = 0;
                    h_multi *= 0.9;
                    System.out.println("BuildMesh: NOTHING VALID, INCREASING RANGE");
                }
            }
        }

    }

    public boolean calculatedPointUsed(Point A, Point B, Point C)
    {
        //if(!inRange(points.copy(), C))
        if(true)
        {
            System.out.println("CalcalculatedPointUsed: NOTHING IN RANGE OF C");
            Polygon p = new Polygon(original_points);
            if(!p.intersects(A, C) && !p.intersects(B, C))
            {
                System.out.println("CalcalculatedPointUsed: DOES NOT INTERSECT. INSERTED");
                points.add(C, B);
                triangles.add(new Triangle(A , C, B));
                return true;
            }
            System.out.println("CalcalculatedPointUsed: INTERSECTS");
        }
        else
            System.out.println("CalcalculatedPointUsed:  IN RANGE");

        return false;
    }

    private boolean domesticPointUsed(Point A, Point B, Point C, Point DA, Point DB)
    {
        // both in range, get the better one
        if(inRange(DA, C) && inRange(DB, C))
        {
            Point D = betterTriangle(A, B, DA, DB);
            Triangle ABD = new Triangle(A, B, D);

            if(D == DA) // D = A's neighbour
                points.remove(A);
            else    //D = B's neighbour
                points.remove(B);

            triangles.add(ABD);

            System.out.println("domesticPointUsed: BOTH WERE GOOD");
        }
        // A's neighbour in range only
        else if(inRange(DA, C))
        {
            triangles.add(new Triangle(A, B, DA));
            points.remove(A);
            System.out.println("domesticPointUsed: DA WAS GOOD");
        }
        // B's neighbour in range only
        else if(inRange(DB, C))
        {
            triangles.add(new Triangle(A, B, DA));
            points.remove(B);
            System.out.println("domesticPointUsed: DB WAS GOOD");
        }
        else
        {
            System.out.println("domesticPointUsed: NO VALID POINTS");
            return false;
        }

        return true;
    }

    private boolean inRange(Point D, Point origin)
    {
        return Math.abs(Point.distance(D, origin)) < range;
    }

    private boolean inRange(LinkedList<Point> list, Point origin)
    {
        for(Point p : list)
        {
            if(inRange(p, origin))
                return true;
        }

        return false;
    }

    private Point betterTriangle(Point A, Point B, Point DA, Point DB)
    {
        double differenceDA = calculateTriangleQuality(A, B, DA);
        double differenceDB = calculateTriangleQuality(A, B, DB);

        return differenceDA < differenceDB ? DA : DB;
    }

    private double calculateTriangleQuality(Point A, Point B, Point C)
    {
        double AB = Point.distance(A, B);
        double AC = Point.distance(A, C);
        double BC = Point.distance(B, C);
        double average = (AB + AC + BC) / 3.0;

        return Math.abs(AB - average) + Math.abs(AC - average) + Math.abs(BC - average);
    }


    private LinkedList<Point> within_range(Point origin, LinkedList<Edge> list, double range, Point ignore1, Point ignore2)
    {
        LinkedList<Point> result = new LinkedList<>();

        for(Edge e : list)
        {
            if(e.getP1() != ignore1 && e.getP1() != ignore2 && Point.distance(e.getP1(), origin) <= range)
                result.add(e.getP1());

            if(e.getP2() != ignore1 && e.getP2() != ignore2 && Point.distance(e.getP2(), origin) <= range)
                result.add(e.getP2());
        }

        return result;
    }

    private Point calculateC(Point A, Point B)
    {
        double a = Point.distance(A, B);
        double height = a * 0.86602540378; //a *  (Math.sqrt(3)) / 2);
        height *= h_multi;

        Point normal = new Point(B.y - A.y, -(B.x - A.x));

        // Normalize the normal vector
        double length = Math.sqrt(normal.x * normal.x + normal.y * normal.y);
        normal.x /= length;
        normal.y /= length;

        // Get the midpoint of AB
        Point midpoint = new Point((A.x + B.x) / 2, (A.y + B.y) / 2);

        // Calculate C
        return new Point(midpoint.x + normal.x * height, midpoint.y + normal.y * height);
    }

    // Getters
    LinkedList<Triangle> getTriangles() { return this.triangles; }
    LinkedList<Point> getPoints() { return this.points.copy(); }
}