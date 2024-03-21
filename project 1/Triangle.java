import java.awt.*;

public class Triangle implements Drawable
{
    //Attributes
    private final Point[] vertices = new Point[3];
    private final double[] lengths = new double[3];
    private final double surfaceArea;

    //Constructors
    //Note: Mozna sprowadzic oba konstruktory do jednego uzywajac interfejs
    Triangle(double[] coeff1, double[] coeff2, double[] coeff3) throws Exception
    {
        //Obliczanie wierzcholkow
        this.vertices[0] = Line.findCrossPoint(coeff1, coeff2);
        this.vertices[1] = Line.findCrossPoint(coeff1, coeff3);
        this.vertices[2] = Line.findCrossPoint(coeff3, coeff2);

        //Sprawdzanie czy trojkat istnieje
        for(Point p : vertices)
        {
            if(p == null)
                throw new Exception("Invalid Traingle");
        }

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distToPoint(vertices[0], vertices[1]);
        this.lengths[1] = Point.distToPoint(vertices[1], vertices[2]);
        this.lengths[2] = Point.distToPoint(vertices[2], vertices[0]);

        //Obliczanie pochodnych wartosci
        this.surfaceArea = this.calcSurfaceArea();
    }

    Triangle(Line l1, Line l2, Line l3) throws Exception
    {
        //Obliczanie wierzhcolkow
        this.vertices[0] = Line.findCrossPoint(l1, l2);
        this.vertices[1] = Line.findCrossPoint(l1, l3);
        this.vertices[2] = Line.findCrossPoint(l2, l3);

        //Sprawdzanie czy trojkat istnieje
        for(Point p : vertices)
        {
            if(p == null)
                throw new Exception("Invalid Traingle");
        }

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distToPoint(vertices[0], vertices[1]);
        this.lengths[1] = Point.distToPoint(vertices[1], vertices[2]);
        this.lengths[2] = Point.distToPoint(vertices[2], vertices[0]);

        //Obliczanie pochodnych wartosci
        this.surfaceArea = this.calcSurfaceArea();
    }

    Triangle (Point p1, Point p2, Point p3)
    {
        vertices[0] = p1;
        vertices[1] = p2;
        vertices[2] = p3;

        //Obliczanie dlugosci bokow
        this.lengths[0] = Point.distToPoint(vertices[0], vertices[1]);
        this.lengths[1] = Point.distToPoint(vertices[1], vertices[2]);
        this.lengths[2] = Point.distToPoint(vertices[2], vertices[0]);

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
        double angle1 = Line.angleBetweenLines(l1, l2);
        double angle2 = Line.angleBetweenLines(l1, l3);
        double angle3 = Line.angleBetweenLines(l2, l3);
        double angle_sum = angle1 + angle2 + angle3;

        //margines bledu
        double error_margin = 0.1;
        double lower_bound = 360 - error_margin;
        double upper_bound = 360 + error_margin;

        return (angle_sum > lower_bound && angle_sum < upper_bound) || Double.isNaN(angle_sum);
    }

    public void draw(Graphics2D g)
    {
        int[] x = new int[] { vertices[0].getX(), vertices[1].getX(), vertices[2].getX() };
        int[] y = new int[] { vertices[0].getY(), vertices[1].getY(), vertices[2].getY() };

        //alternatywnie narysuj trzy linie
        g.drawPolygon(x, y, 3);

        //System.out.println("x = " + x[0] + ", " + x[1] + ", " + x[2]);
        //System.out.println("y = " + y[0] + ", " + y[1] + ", " + y[2]);
    }

    //Getters
    Point[] getVertices() { return this.vertices; }
    double[] getLengths() { return this.lengths; }
    double getSurfaceArea() { return this.surfaceArea; }
}
