import java.awt.*;

public class Triangle implements Drawable
{
    //Attributes
    private final Point[] vertices = new Point[3];
    private final double[] lengths = new double[3];
    private final double surfaceArea;

    //Constructors
    //Note: Mozna sprowadzic oba konstruktory do jednego uzywajac interfejsu
    Triangle(double[] coeff1, double[] coeff2, double[] coeff3)
    {
        //Obliczanie wierzcholkow
        this.vertices[0] = Line.findCrossPoint(coeff1, coeff2);
        this.vertices[1] = Line.findCrossPoint(coeff1, coeff3);
        this.vertices[2] = Line.findCrossPoint(coeff3, coeff2);

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distance(vertices[0], vertices[1]);
        this.lengths[1] = Point.distance(vertices[1], vertices[2]);
        this.lengths[2] = Point.distance(vertices[2], vertices[0]);

        //Obliczanie pochodnych wartosci
        this.surfaceArea = this.calcSurfaceArea();
    }

    Triangle(Line l1, Line l2, Line l3)
    {
        //Obliczanie wierzhcolkow
        this.vertices[0] = Line.findCrossPoint(l1, l2);
        this.vertices[1] = Line.findCrossPoint(l1, l3);
        this.vertices[2] = Line.findCrossPoint(l2, l3);

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distance(vertices[0], vertices[1]);
        this.lengths[1] = Point.distance(vertices[1], vertices[2]);
        this.lengths[2] = Point.distance(vertices[2], vertices[0]);

        //Obliczanie pochodnych wartosci
        this.surfaceArea = this.calcSurfaceArea();
    }

    Triangle (Point p1, Point p2, Point p3)
    {
        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distance(vertices[0], vertices[1]);
        this.lengths[1] = Point.distance(vertices[1], vertices[2]);
        this.lengths[2] = Point.distance(vertices[2], vertices[0]);

        //Obliczanie pochodnych wartosci
        this.surfaceArea = this.calcSurfaceArea();
    }

    //Methods
    private double calcSurfaceArea()
    {
        //p = (a + b + c) / 2
        double p =  (lengths[0] + lengths[1] + lengths[2]) / 2;

        //s = sqrt(p(p-a)(p-b)(p-c))
        return Math.sqrt(p * (p - lengths[0]) * (p - lengths[1]) * (p - lengths[2]));
    }

    public boolean contains(Point p)
    {
        //Zrobmy trzy mniejsze trojkaty, z wieszchalkami naszego trojkata i p
        Triangle t1 = new Triangle(p, vertices[0], vertices[1]);
        Triangle t2 = new Triangle(p, vertices[0], vertices[2]);
        Triangle t3 = new Triangle(p, vertices[1], vertices[2]);

        //Warunkiem jest to czy pole trzech trojkatow jest wieksze niz pole naszego trojkata
        double s = t1.getSurfaceArea() + t2.getSurfaceArea() + t3.getSurfaceArea();

        return s <= this.getSurfaceArea();
    }

    public boolean contains_ALT(Point p)
    {
        //Zrobimy trzy proste z poczatkiem p i koncami w wierzcholkach
        Line l1 = new Line(p, vertices[0], false);
        Line l2 = new Line(p, vertices[1], false);
        Line l3 = new Line(p, vertices[2], false);

        //liczenie katow
        double angle1 = Line.angle180(l1, l2);
        double angle2 = Line.angle180(l1, l3);
        double angle3 = Line.angle180(l2, l3);
        double angle_sum = angle1 + angle2 + angle3;

        //margines bledu
        double error_margin = 0.1;
        double lower_bound = 360 - error_margin;
        double upper_bound = 360 + error_margin;

        return (angle_sum > lower_bound && angle_sum < upper_bound) || Double.isNaN(angle_sum);
    }

    public void draw(Graphics2D g, int width, int height)
    {
        int centerX = width / 2;
        int centerY = height / 2;

        //Adjust each vertex position to make the screen center as (0, 0)
        int x0 = vertices[0].getX() + centerX;
        int y0 = centerY - vertices[0].getY(); //Inverting Y axis
        int x1 = vertices[1].getX() + centerX;
        int y1 = centerY - vertices[1].getY(); //Inverting Y axis
        int x2 = vertices[2].getX() + centerX;
        int y2 = centerY - vertices[2].getY(); //Inverting Y axis

        //Draw lines between vertices with adjusted coordinates
        g.drawLine(x0, y0, x1, y1);
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x0, y0);
    }


    //Getters
    Point[] getVertices() { return this.vertices; }
    double[] getLengths() { return this.lengths; }
    double getSurfaceArea() { return this.surfaceArea; }
}