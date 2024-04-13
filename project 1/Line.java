import java.awt.*;

public class Line implements Drawable
{
    //Attributes
    private final Point p1, p2;
    private final boolean isInfinite;
    private boolean isVertical = false;
    private int xForVerticalLine;
    private double a, b;

    //Constructor
    Line(Point p1, Point p2, boolean infinite)
    {
        this.p1 = new Point(p1.getX(), p1.getY());
        this.p2 = new Point(p2.getX(), p2.getY());
        this.isInfinite = infinite;
        this.calcStandardForm();
    }

    //Methods
    private void calcStandardForm()
    {
        if(p2.getX() == p1.getX()) //sprawdzenie czy linia jest pionowa
        {
            isVertical = true;
            xForVerticalLine = p1.getX();
        }
        else
        {
            isVertical = false;
            a = (double)(p2.getY() - p1.getY()) / (p2.getX() - p1.getX()); //obliczanie wspolczynika kierunkowego
            b = p1.getY() - a * p1.getX(); //obliczanie wyrazu wolnego
        }
    }

    public boolean isAligned(Point tgt)
    {
        //Ax + By + C = 0   //warunek
        //y = ax+b
        //A = a, B = -1, C=b. ax - y + b = 0  //warunek
        final double margin = 0.01; //Margines bledu

        double x = (double)tgt.getX();
        double y = (double)tgt.getY();
        double calc = (this.a * x) - y + this.b;

        if(Math.abs(calc) < margin) //sprawdz czy lezy na prostej
        {
            if(this.isInfinite) //czy chodzi nam o prosta?
                return true;

            else    //sprawdz czy punkt jest w x-ie i y-greku odcinka
            {
                boolean withinXBounds = Math.min(p1.getX(), p2.getX()) <= tgt.getX() && tgt.getX() <= Math.max(p1.getX(), p2.getX());
                boolean withinYBounds = Math.min(p1.getY(), p2.getY()) <= tgt.getY() && tgt.getY() <= Math.max(p1.getY(), p2.getY());

                return withinXBounds && withinYBounds;
            }
        }
        return false;

    }

    public void translateLine(Point vector)
    {
        p1.translate(vector.getX(), vector.getY());
        p2.translate(vector.getX(), vector.getY());
        this.calcStandardForm();
    }

    public static double[] lineEquation(Line l)
    {
        // Obliczanie współczynnika kierunkowego m
        double m = (double)(l.p2.getY() - l.p1.getY()) / (l.p2.getX() - l.p1.getX());

        // Obliczanie wyrazu wolnego b
        double b = l.p1.getY() - m * l.p1.getX();

        // Współczynniki dla równania ogólnego prostej
        double A = m;
        double B = -1;
        double C = b;

        // W przypadku linii pionowej
        if(l.p1.getX() == l.p2.getX()) {
            A = 1;
            B = 0;
            C = -l.p1.getX();
        }
        // W przypadku linii poziomej
        else if(l.p1.getY() == l.p2.getY()) {
            A = 0;
            B = 1;
            C = -l.p1.getY();
        }

        return new double[] {A, B, C};
    }

    public Point findCrossPoint(Line l2)    //using a,b
    {
        double new_x, new_y;

        if(this.isVertical && l2.isVertical || this.getA() == l2.getA())
            return null;

        if(this.isVertical())
        {
            new_x = this.getXForVerticalLine();
            new_y = l2.getA() * new_x + l2.getB();
        }
        else if(l2.isVertical())
        {
            new_x = l2.getXForVerticalLine();
            new_y = this.getA() * new_x + this.getB();;
        }
        else
        {
            new_x = (l2.getB() - this.getB()) / (this.getA() - l2.getA());
            new_y = (this.getA() * new_x) + this.getB();
        }

        return new Point((int)new_x, (int)new_y);
    }

    public static Point findCrossPoint(double[] coeff1, double[] coeff2)
    {
        double a1 = coeff1[0], b1 = coeff1[1], c1 = coeff1[2];
        double a2 = coeff2[0], b2 = coeff2[1], c2 = coeff2[2];

        //wspólny mianownik
        double denominator = (a1 * b2) - (a2 * b1);

        //czy linie nie są równoległe
        if (denominator == 0)
            return null;

        //punkt przecięcia
        double x0 = (((b2 * c1) - (b1 * c2)) / denominator) * -1;
        double y0 = ((c1 * a2) - (c2 * a1)) / denominator;

        return new Point((int)x0, (int)y0);
    }

    public static Point findCrossPoint(Line l1, Line l2)
    {
        //liczenie współczynników
        double[] coeff1 = Line.lineEquation(l1);
        double[] coeff2 = Line.lineEquation(l2);

        double a1 = coeff1[0], b1 = coeff1[1], c1 = coeff1[2];
        double a2 = coeff2[0], b2 = coeff2[1], c2 = coeff2[2];

        //wspólny mianownik
        double denominator = (a1 * b2) - (a2 * b1);

        //czy linie nie są równoległe
        if (denominator == 0)
            return null;

        //punkt przecięcia
        double x0 = (((b2 * c1) - (b1 * c2)) / denominator) * -1;
        double y0 = ((c1 * a2) - (c2 * a1)) / denominator;
        Point intersection = new Point((int)Math.round(x0), (int)Math.round(y0));

        //Sprawdzanie dla odcinka
        Line[] to_check = {l1, l2};
        for(Line check : to_check)
        {
            if(!check.isInfinite())
            {
                double max_x = Math.max(check.getP1().getX(), check.getP2().getX());
                double min_x = Math.min(check.getP1().getX(), check.getP2().getX());
                double max_y = Math.max(check.getP1().getY(), check.getP2().getY());
                double min_y = Math.min(check.getP1().getY(), check.getP2().getY());

                if(intersection.getX() > max_x || intersection.getX() < min_x || intersection.getY() > max_y || intersection.getY() < min_y)
                    return null;
            }
        }

        return intersection;
    }

    public Point findCrossPoint_ALT(Line l2)
    {
        //przepisanie x i y dla this
        int x1 = this.getP1().getX();
        int y1 = this.getP1().getY();
        int x2 = this.getP2().getX();
        int y2 = this.getP2().getY();

        //przepisanie x i y dla l2
        int x3 = l2.getP1().getX();
        int y3 = l2.getP1().getY();
        int x4 = l2.getP2().getX();
        int y4 = l2.getP2().getY();

        //mianownik px i py
        double denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);

        if(denominator == 0)
            return null; //linie nie przecinają się

        //liczniki
        double Px_numerator = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4);
        double Py_numerator = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4);

        //punkt przeciecia
        double Px = Px_numerator / denominator;
        double Py = Py_numerator / denominator;

        return new Point((int)Px, (int)Py);
    }

    public static double angle180(Line l1, Line l2)
    {
        //Obliczenie wektora kierunkowego
        Point dv_1 = Point.directionVector(l1);
        Point dv_2 = Point.directionVector(l2);

        //Przepisanie wartosci
        double a1 = dv_1.getX(), a2 = dv_1.getY();
        double b1 = dv_2.getX(), b2 = dv_2.getY();

        //Obliczenie modulow: sqrt(a1^2 + a2^2)
        double magnitude_1 = Math.sqrt(Math.pow(a1, 2) + Math.pow(a2, 2));
        double magnitude_2 = Math.sqrt(Math.pow(b1, 2) + Math.pow(b2, 2));

        //Iloczyn skalarny: a1 * b1 + a2 * b2
        double scalar = (a1 * b1) + (a2 * b2);

        //Kat:
        double radian_angle = Math.acos((scalar) / (magnitude_1 * magnitude_2));

        //Konwersja na stopnie
        return (radian_angle * 180) / Math.PI;
    }

    public static double angle360(Line l1, Line l2)
    {
        //Obliczenie wektora kierunkowego
        Point dv_1 = Point.directionVector(l1);
        Point dv_2 = Point.directionVector(l2);

        //Przepisanie wartości
        double a1 = dv_1.getX(), a2 = dv_1.getY();
        double b1 = dv_2.getX(), b2 = dv_2.getY();

        //Iloczyn skalarny: a1 * b1 + a2 * b2
        double dotProduct = (a1 * b1) + (a2 * b2);

        //Obliczenie modułów: sqrt(a1^2 + a2^2) oraz sqrt(b1^2 + b2^2)
        double magnitude_1 = Math.sqrt(Math.pow(a1, 2) + Math.pow(a2, 2));
        double magnitude_2 = Math.sqrt(Math.pow(b1, 2) + Math.pow(b2, 2));

        //Iloczyn wektorowy: a1*b2 - a2*b1 (zwraca 'z' w 3D, ale używamy do orientacji w 2D)
        double crossProduct = (a1 * b2) - (a2 * b1);

        //Kat (wielkość) na podstawie iloczynu skalarnego i magnitud
        double angle = Math.acos(dotProduct / (magnitude_1 * magnitude_2));

        //Konwersja radianów na stopnie
        angle = Math.toDegrees(angle);

        //Jeśli iloczyn wektorowy jest ujemny, obrót jest zgodny z ruchem wskazówek zegara,
        //więc przekształcamy kąt na kąt w zakresie 0-360 stopni
        if (crossProduct < 0)
            angle = 360 - angle;

        return angle;
    }

    public void draw(Graphics2D g, int width, int height)
    {
        //calc the center shifting vals
        int centerX = width / 2;
        int centerY = height / 2;

        if(isInfinite)
        {
            if (isVertical)
            {
                //for vertical lines, only X is adjusted
                int newX = centerX + xForVerticalLine;
                g.drawLine(newX, 0, newX, height); //height remains constant
            }

            else
            {
                //for non-vertical infinite lines
                int x1 = 0; //start from the left edge
                int x2 = width; //to the right edge of the panel
                //calculate Y positions using the line equation, adjusted for the center origin
                int y1 = (int) (a * (x1 - centerX) + b) + centerY; // y = ax + b, adjusted for x = 0
                int y2 = (int) (a * (x2 - centerX) + b) + centerY; // y = ax + b, adjusted for x = width

                // Draw the line with adjusted coordinates
                g.drawLine(x1, height - y1, x2, height - y2);
            }
        }

        else
        {
            //adjust points for regular lines
            int newX1 = p1.getX() + centerX;
            int newY1 = centerY - p1.getY(); //invert Y and adjust
            int newX2 = p2.getX() + centerX;
            int newY2 = centerY - p2.getY(); //invert Y and adjust

            //draw the line with new coordinates
            g.drawLine(newX1, newY1, newX2, newY2);
        }
    }



    //Getters
    double getA() { return this.a; }
    double getB() { return this.b; }
    int getXForVerticalLine() { return this.xForVerticalLine; }
    boolean isVertical() { return this.isVertical; }
    boolean isInfinite() { return this.isInfinite; }
    Point getP1() { return this.p1; }
    Point getP2() { return this.p2; }
    public String getStandardForm()
    {
        if(isVertical)
            return "x = " + this.xForVerticalLine;

        return "y = " + this.a + "x + " + this.b;
    }

    //Known problems:
    //Mozna utworzyc linie z tym samym punktem pcozatkowym i koncowym
    //Obecnie efekty tego bledu sa widoczne w Triangle.contains_ALT,
    //gdzie wyliczany kat moze wyniesc NULL (dzielenie przez 0) w Line.angleBetweenLines,
    //jezeli sprawdzany punkt jest wierzcholkiem
    //w returnie Triangle.contains_ALT jest to wziete pod uwage
}