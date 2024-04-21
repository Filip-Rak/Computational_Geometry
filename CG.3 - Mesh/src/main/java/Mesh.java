import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Mesh
{
    // Attributes
    // Storage
    private ArrayList<Polygon> blank_spots;
    private CircularList points;
    private LinkedList<Triangle> triangles;

    // Building
    private double snap_range;
    private double snap_range_multiplier;
    private double height_multiplier;
    private double angle_Threshold;
    private double determination;
    private int ceil;

    // Trackers
    private int current_range_failures;
    private int failed_insertions;
    private int iteration;

    // Visualization
    private int delay;
    private DisplayFrame dp;

    // Constructors
    Mesh(ArrayList<Point> p_arr, int stride, double snap_range, double snap_range_multiplier, double height_multiplier, double determination, int ceil, int delay)
    {
        initVars(p_arr, stride, snap_range, snap_range_multiplier, height_multiplier, determination, ceil, delay,null);
        buildMesh();
    }

    Mesh(ArrayList<Point> p_arr, int stride, double snap_range, double snap_range_multiplier, double height_multiplier, double determination, int ceil, int delay, ArrayList<Polygon> blind_spots)
    {
        initVars(p_arr, stride, snap_range, snap_range_multiplier, height_multiplier, determination, ceil, delay, blind_spots);
        buildMesh();
    }

    // Initialization
    private void initVars(ArrayList<Point> p_arr, int stride, double snapRange, double multi, double height_multiplier, double determination, int ceil, int delay, ArrayList<Polygon> blind_spots)
    {
        // Lists
        points = new CircularList();
        for(int i = 0; i < p_arr.size(); i += stride)
            points.add(p_arr.get(i));

        this.triangles = new LinkedList<>();

        // Hit box
        if(blind_spots != null) // Has custom hit box
            this.blank_spots = new ArrayList<>(blind_spots);
        else
        {
            // No hitbox, generate one
            this.blank_spots = new ArrayList<>();
            this.blank_spots.add(new Polygon(points.copy()));
        }

        // Modifiers
        this.snap_range = snapRange;
        this.snap_range_multiplier = multi;
        this.determination = determination;
        this.height_multiplier = height_multiplier;

        // Visualization
        this.delay = delay;
        dp = new DisplayFrame(true, 800, 600);

        for(Polygon p : blank_spots)
            dp.panel.AddDrawable(p, Color.black);

        for(Point p : points.copy())
            dp.panel.AddDrawable(p, Color.BLUE);


        // Trackers
        this.current_range_failures = 0;
        this.failed_insertions = 0;
        this.iteration = 0;

        // Constants
        this.angle_Threshold = 0;
        this.ceil = ceil;
    }

    // Building methods
    private void buildMesh()
    {
        while(points.size() >= 3 && failed_insertions < (points.size() * determination) && iteration < ceil)
        {
            iteration++;

            System.out.println(
                    "----------------------------------------------------\n" +
                            "Iteration: " + iteration + ", Size: " +  points.size() +
                            ", Index: " + points.getCurrentIndex() +
                            ", Currrent range failures: " + current_range_failures +
                            ", Failed insertions: " + failed_insertions
            );

            // Get A, B and their neighbours
            Point DA = points.getNext();
            Point A = points.getNext();
            Point B = points.getNext();
            Point DB = points.getNext();

            // Calculate C
            Point C = calculateC(A, B);
            //dp.panel.AddDrawable(C, Color.yellow);

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
                current_range_failures = 0;
                failed_insertions = 0;
                dp.panel.AddDrawable(triangles.getLast(), Color.green);
            }
            else if(tryCalculated(A, B, C)) // added new point
            {
                // We want to make new A = previous B. We added C so we go back by 2
                points.offset(-2);
                current_range_failures = 0;
                failed_insertions = 0;
                dp.panel.AddDrawable(triangles.getLast(), Color.green);
            }
            else    //couldnt add anything
            {
                points.offset(-2);  // We want to advance by 1, since we dint add C with -2 A will become B

                if(current_range_failures > points.size())
                {
                    current_range_failures = 0;
                    snap_range *= snap_range_multiplier;
                }

                current_range_failures++;
                failed_insertions++;
            }

            dp.panel.refreshBufferedImage();

            try{Thread.sleep(delay);}
            catch (Exception ignore){}
        }
    }

    private boolean tryCalculated(Point A, Point B, Point C)
    {
        if(!intersection(A, B, C))
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
        boolean DA_valid = inRange(DA, C) && !intersection(A, B, DA) && Triangle.validateAngles(A, B, DA, angle_Threshold);
        boolean DB_valid = inRange(DB, C) && !intersection(A, B, DB) && Triangle.validateAngles(A, B, DB, angle_Threshold);

        boolean skip_DA = false;

        if(DA_valid && DB_valid)
        {
            System.out.println("tryDomestic: BOTH VALID");

            if(DB_is_better(A, B, DA, DB))
                skip_DA = true;
        }

        if(DA_valid && !skip_DA)
        {
            System.out.println("TRYDOMESTIC: DA IS VALID, DA INSERTED");
            // DA becomes the tip
            triangles.add(new Triangle(A, B, DA));
            points.remove(A);

            return true;
        }
        else if(DB_valid)
        {
            System.out.println("TRYDOMESTIC: DB IS VALID, DB INSERTED");
            // DB becomes the tip
            triangles.add(new Triangle(A, B, DB));
            points.remove(B);

            return true;
        }
        else
            System.out.println("TRYDOMESTIC: BOTH INVALID");

        return false;
    }

    private boolean DB_is_better(Point A, Point B, Point DA, Point DB)
    {
        double differenceDA = calculateTriangleQuality(A, B, DA);
        double differenceDB = calculateTriangleQuality(A, B, DB);

        return differenceDB < differenceDA;
    }

    private double calculateTriangleQuality(Point A, Point B, Point C)
    {
        double AB = Point.distance(A, B);
        double AC = Point.distance(A, C);
        double BC = Point.distance(B, C);
        double average = (AB + AC + BC) / 3.0;

        return Math.abs(AB - average) + Math.abs(AC - average) + Math.abs(BC - average);
    }

    private boolean inRange(Point D, Point origin)
    {
        return Math.abs(Point.distance(D, origin)) < snap_range;
    }

    public boolean intersection(Point A, Point B, Point C)
    {
        Line AB = new Line(A, B, false);
        Line AC = new Line(A, C, false);
        Line CB = new Line(C, B, false);

        // Checks for line intersections
        for(Triangle t : triangles)
        {
            LinkedList<Line> lines = t.getLines();
            if(lineIntersection(lines, AB, AC, CB))
                return true;
        }

        for(Polygon pol : blank_spots)
        {
            LinkedList<Line> lines = pol.getLines();
            if(lineIntersection(lines, AB, AC, CB))
                return true;
        }

        return false;
    }

    private boolean lineIntersection(LinkedList<Line> lines, Line AB, Line AC, Line CB)
    {
        for(Line l: lines)
        {
            Point p1 = Line.findCrossPoint(AB, l);
            if(p1 != null && !Line.commonPoints(AB, l)) return true;

            Point p2 = Line.findCrossPoint(AC, l);
            if(p2 != null && !Line.commonPoints(AC, l)) return true;

            Point p3 = Line.findCrossPoint(CB, l);
            if(p3 != null&& !Line.commonPoints(CB, l)) return true;
        }

        return false;
    }

    private Point calculateC(Point A, Point B)
    {
        double a = Point.distance(A, B);
        double height = a * 0.86602540378 * height_multiplier; //a *  (Math.sqrt(3)) / 2);

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
    public LinkedList<Triangle> getTriangles()
    {
        return this.triangles;
    }
}