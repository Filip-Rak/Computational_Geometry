import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Mesh3
{
    // Attributes
    Polygon original_shape;
    CircularList points;
    LinkedList<Triangle> triangles;
    double snap_range;
    double snap_range_multiplier;
    double angle_Threshold;
    double angle_Threshold_multiplier;
    int no_insertions;
    int iter = 0;
    int delay;

    DisplayFrame dp = new DisplayFrame(true, 800, 600);

    // Constructor
    Mesh3(ArrayList<Point> p_arr, double snap_range, double multi, int delay)
    {
        initVars(p_arr, snap_range, multi, delay);
        buildMesh();
    }

    private void initVars(ArrayList<Point> p_arr, double snapRange, double multi, int delay)
    {
        points = new CircularList(p_arr);
        this.original_shape = new Polygon(points.copy());
        this.triangles = new LinkedList<>();
        this.snap_range = snapRange;
        this.snap_range_multiplier = multi;
        this.no_insertions = 0;
        this.angle_Threshold = 0;
        this.angle_Threshold_multiplier = 0.8;
        this.delay = delay;

        for(Point p : p_arr)
            dp.panel.AddDrawable(p, Color.BLUE);

        dp.panel.AddDrawable(original_shape, Color.black);
    }

    private void buildMesh()
    {
        while(points.size() >= 3 && iter < 300)
        {
            iter++;
            System.out.println("ITER: " + iter + "\nSize: " + points.size() + " Index: " + points.getCurrentIndex());

            // Get A, B and their neighbours
            Point DA = points.getNext();
            Point A = points.getNext();
            Point B = points.getNext();
            Point DB = points.getNext();

            // Calculate C
            Point C = calculateC(A, B);
            dp.panel.AddDrawable(C, Color.yellow);

            System.out.println("DA: " + DA.getX() + " " + DA.getY() +
                    "\nA: " + A.getX() + " " + A.getY() +
                    "\nB: " + B.getX() + " " + B.getY() +
                    "\nDB: " + DB.getX() + " " + DB.getY() +
                    "\nC: " + C.getX() + " " + C.getY()
            );


            // Deciding if to add / delete or skip
            if(tryDomestic(A, B, C, DA, DB))  // we deleted one point
            {
                // We want to repeat the iteration, but we deleted either A or B, so we go back 3 instead of 4
                points.offset(-3);
                no_insertions = 0;
                dp.panel.AddDrawable(triangles.getLast(), Color.green);
            }
            else if(tryCalculated(A, B, C)) // added new point
            {
                // We want to make new A = previous B. We added C so we go back by 2
                points.offset(-2);
                no_insertions = 0;
                dp.panel.AddDrawable(triangles.getLast(), Color.green);
            }
            else    //couldnt add anything
            {
                points.offset(-2);  // We want to advance by 1, since we ddint add C with -2 A will become B
                if(no_insertions > points.size())
                {
                    no_insertions = 0;
                    snap_range *= snap_range_multiplier;
                }

                no_insertions++;
            }

            dp.panel.refreshBufferedImage();

            try{Thread.sleep(delay);}
            catch (Exception ignore){}
        }
    }

    private boolean tryCalculated(Point A, Point B, Point C)
    {
        if(insideMesh(original_shape, C) && !triangleIntersection(triangles, A, B, C))
        //if(!triangleIntersection(triangles, A, B, C))
        //if(insideMesh(original_shape, C))
        {
            System.out.println("TRYCALCULATED: inside, no intersections. C INSERTED");
            triangles.add(new Triangle(A, B, C));
            dp.panel.AddDrawable(C, Color.RED);
            points.add(C, B);
            return true;
        }
        else
        {
            System.out.println("TRYCALCULATED: outside or intersects");
            return false;
        }

    }

    private boolean tryDomestic(Point A, Point B, Point C, Point DA, Point DB)
    {
        // In this trial I will not choose the 'better one', but instead will go with the left one if both are valid
        // much easier code and possibly no difference in the effectiveness

        //if(inRange(DA, C) && !triangleIntersection(triangles, A, B, DA) && Triangle.validateAngles(A, B, DA, angle_Threshold))
        if(inRange(DA, C) && !triangleIntersection(triangles, A, B, DA))
        {
            System.out.println("TRYDOMESTIC: DA IS VALID, DA INSERTED");
            // DA becomes the tip
            triangles.add(new Triangle(A, B, DA));
            points.remove(A);

            return true;
        }
        //else if(inRange(DB, C) && !triangleIntersection(triangles, A, B, DB) && Triangle.validateAngles(A, B, DB, angle_Threshold))
        else if(inRange(DB, C) && !triangleIntersection(triangles, A, B, DA))
        {
            System.out.println("TRYDOMESTIC: DB IS VALID, DB INSERTED");
            // DB becomes the tip
            triangles.add(new Triangle(A, B, DB));
            points.remove(B);

            return true;
        }
        else
        {
            System.out.println("TRYDOMESTIC: BOTH INVALID");
        }
            return false;
    }

    private boolean inRange(Point D, Point origin)
    {
        return Math.abs(Point.distance(D, origin)) < snap_range;
    }

    public static boolean insideMesh(Polygon contour, Point point)
    {
        return contour.contains(point);
    }

    public static boolean triangleIntersection(LinkedList<Triangle> triangles, Point A, Point B, Point C)
    {
        Line AB = new Line(A, B, false);
        Line AC = new Line(A, C, false);
        Line CB = new Line(C, B, false);

        for(Triangle t : triangles)
        {
            LinkedList<Line> tLines = t.getLines();
            for(Line l : tLines)
            {
                Point p1 = Line.findCrossPoint(AB, l);
                if(p1 != null && !Line.commonPoints(AB, l)) return true;

                Point p2 = Line.findCrossPoint(AC, l);
                if(p2 != null && !Line.commonPoints(AC, l)) return true;

                Point p3 = Line.findCrossPoint(CB, l);
                if(p3 != null&& !Line.commonPoints(CB, l)) return true;
            }
        }

        return false;
    }

    private Point calculateC(Point A, Point B)
    {
        double a = Point.distance(A, B);
        double height = a * 0.86602540378; //a *  (Math.sqrt(3)) / 2);

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

    public LinkedList<Triangle> getTriangles()
    {
        return this.triangles;
    }
}
