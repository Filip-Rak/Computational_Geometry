import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Point implements Drawable
{
    //Attributes
    private int x, y;

    //Constructor
    Point(int x, int y)
    {
        this.x = x;
        this.y = y;

        //translateToScreenCenter();
    }

    Point(Point p)
    {
        this.x = p.getX();
        this.y = p.getY();
    }

    //Methods
    public String relationToLine(Line l)
    {
        //Ax + By + C < || > 0   //warunek
        //y = ax+b
        //A = a, B = -1, C=b. ax - y + b = 0  //warunek

        double calc = (l.getA() * x) - y + l.getB();

        if(calc < 0)
            return "Right";
        if (calc > 0)
            return "Left";
        else
            return "Aligned";
    }

    public void translate(int dx, int dy)
    {
        this.x += dx;
        this.y += dy;
    }

    public Point mirrorOverLine(Line ln)
    {
        if(ln.isVertical())
        {
            //dla linii pionowej prosta odbicia przebiega rownolegle do osi Y
            //x' = 2d - x, y = y
            int xp = 2 * ln.getXForVerticalLine() - this.x;
            return new Point(xp, this.y);
        }
        else if(ln.getA() == 0)
        {
            //dla linii poziomej prosta odbicia przebiega rownolegle do osi X
            //x = x, y' = 2b - y
            int yp = 2 * (int)ln.getB() - this.y;
            return new Point(this.x, yp);
        }
        else
        {
            //dla linii o nachyleniu innym niż 0 i niewertykalnych
            //prosta  prostopadlaj przechodzaca przez dany punkt
            //a' = -1/a
            double ap = -1 / ln.getA();
            //c' = y + x/a
            double cp = this.y - ap * this.x;

            //obliczanie punktu przeciecia z daną linią
            //x2 = (b - c') / (a' - a)
            double x2 = (ln.getB() - cp) / (ap - ln.getA());
            //y2 = ax2 + b
            double y2 = ln.getA() * x2 + ln.getB();

            //pbliczanie odbitego punktu
            //x_odbity = 2x2 - x
            int x_odbity = (int)(2 * x2 - this.x);
            //y_odbity = 2y2 - y
            int y_odbity = (int)(2 * y2 - this.y);

            return new Point(x_odbity, y_odbity);
        }
    }

    public void draw(Graphics2D g, int width, int height)
    {
        int X = this.x, Y = this.y;

        //make middle of the widnow (0, 0)
        X += width / 2;
        Y += height / 2;

        //reverse Y
        Y = height - Y;

        int diameter = 12;   //20 is default
        int radius = diameter / 2; //polowa srednicy
        g.fillOval(X- radius, Y - radius, diameter, diameter);
    }

    public double distance(Line ln)
    {
        //przepisanie wartosci dla ultawnienia wizualizacji
        double x0 = this.getX(), x1 = ln.getP1().getX(), x2 = ln.getP2().getX();
        double y0 = this.getY(), y1 = ln.getP1().getY(), y2 = ln.getP2().getY();

        double numerator = Math.abs( (x2 - x1) * (y1 - y0) - (x1 - x0) * (y2 - y1) );
        double denominator = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        return numerator / denominator;
    }

    public static double distance(Point p1, Point p2)
    {
        //d = sqrt((x2 - x1) ^ 2 + (y2 - y1) ^ 2)

        double x_pow = Math.pow( (double)p2.getX() -  (double)p1.getX(), 2);
        double y_pow = Math.pow( (double)p2.getY() -  (double)p1.getY(), 2);

        return Math.sqrt(x_pow + y_pow);
    }

    public static Point directionVector(Line line)
    {
        //Przepisanie wartosci
        double x1 = line.getP1().getX();
        double y1 = line.getP1().getY();
        double x2 = line.getP2().getX();
        double y2 = line.getP2().getY();

        //obliczenie wektora kierunkowego
        double vx = x2 - x1;
        double vy = y2 - y1;

        return new Point((int)vx, (int)vy);
    }

    public static ArrayList<Point> loadFromFile(String filename)
    {
        try
        {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);
            int size = scanner.nextInt();

            //utworz tablice i odczytaj plik
            ArrayList<Point> arr = new ArrayList<Point>();

            for(int i = 0; i < size; i++)
            {
                int x = scanner.nextInt();
                int y = scanner.nextInt();

                arr.add(new Point(x, y));
            }

            return arr;
        }
        catch(FileNotFoundException f)
        {
            System.out.println("File " + filename +  "  not found!");
            return null;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;

        // Check if obj is an instance of Point, if so cast it
        if (!(obj instanceof Point other))
            return false;

        // Compare the data members and return accordingly
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    //Getters
    int getX() { return this.x; }
    int getY() { return this.y; }

    //Setters
    void setX(int x) { this.x = x; }
    void setY(int y) { this.y = y; }
    void setCords(int x, int y) { this.x = x; this.y = y; }
}