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

    public String getStandardForm()
    {
        if(isVertical)
            return "x = " + this.xForVerticalLine;

        return "y = " + this.a + "x + " + this.b;
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

    public void draw(Graphics2D g)
    {
        if(isInfinite)
        {
            if (isVertical)
                g.drawLine(xForVerticalLine, 0, xForVerticalLine, 600); //height constant
            else
            {
                //rysowanie linii niepionowej
                int x1 = 0;
                int y1 = (int) (a * x1 + b); // y = ax + b dla x = 0
                int x2 = 600;   //width constant
                int y2 = (int) (a * x2 + b); // y = ax + b dla x = szerokość panelu

                g.drawLine(x1, y1, x2, y2);
            }
        }
        else
            g.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }

    //Getters
    double getA() { return this.a; }
    double getB() { return this.b; }
    int getXForVerticalLine() { return this.xForVerticalLine; }
    boolean isVertical() { return this.isVertical; }
    boolean isInfinite() { return this.isInfinite; }
    Point getP1() { return this.p1; }
    Point getP2() { return this.p2; }
}
