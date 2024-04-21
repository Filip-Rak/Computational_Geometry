import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Point implements Drawable
{
    // Attributes
    public double x, y;

    // Constructors
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    Point(Point p)
    {
        this.x = p.getX();
        this.y = p.getY();
    }

    // Methods
    public void draw(Graphics2D g, int width, int height)
    {
        double X = this.x, Y = this.y;

        //make middle of the widnow (0, 0)
        X += width / 2.0;
        Y += height / 2.0;

        //reverse Y
        Y = height - Y;

        int diameter = 12;   //20 is default
        int radius = diameter / 2; //polowa srednicy
        g.fillOval((int) (X- radius), (int) (Y - radius), diameter, diameter);
    }

    public void translate(double dx, double dy)
    {
        this.x += dx;
        this.y += dy;
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

        return new Point(vx, vy);
    }

    public static ArrayList<Point> loadFromFile(String filename)
    {
        try
        {
            File input = new File(filename);
            Scanner scanner = new Scanner(input);

            // Get file size
            int size = scanner.nextInt();

            // Create an array, load contents
            ArrayList<Point> arr = new ArrayList<>();
            for(int i = 0; i < size; i++)
            {
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();

                arr.add(new Point(x, y));
            }

            return arr;
        }
        catch(FileNotFoundException f)
        {
            System.out.println("File " + filename +  " not found!");
            return null;
        }
    }

    public static void transformArray(ArrayList<Point> arr, double scale_X, double scale_Y, double offset_X, double offset_Y)
    {
        for(Point p : arr)
        {
            double new_x = p.getX();
            double new_y = p.getY();

            new_x *= scale_X;
            new_y *= scale_Y;

            new_x += offset_X;
            new_y += offset_Y;

            p.setX(new_x);
            p.setY(new_y);
        }
    }

    public static double distance(Point p1, Point p2)
    {
        //d = sqrt((x2 - x1) ^ 2 + (y2 - y1) ^ 2)

        double x_pow = Math.pow( p2.getX() -  p1.getX(), 2);
        double y_pow = Math.pow( p2.getY() -  p1.getY(), 2);

        return Math.sqrt(x_pow + y_pow);
    }

    public static double angle360(Point a, Point b, Point c) {
        // Vector AB
        double abX = b.x - a.x;
        double abY = b.y - a.y;

        // Vector AC
        double acX = c.x - a.x;
        double acY = c.y - a.y;

        // Dot product and magnitude calculations
        double dotProduct = abX * acX + abY * acY;
        double magnitudeAB = Math.sqrt(abX * abX + abY * abY);
        double magnitudeAC = Math.sqrt(acX * acX + acY * acY);

        // Calculating angle in degrees
        double angleRadians = Math.acos(dotProduct / (magnitudeAB * magnitudeAC));

        // Adjust based on directionality if needed
        return Math.toDegrees(angleRadians);
    }

    public static boolean pointsEqual(Point p1, Point p2)
    {
        return p1.getX() == p2.getX() && p1.getY() == p2.getY();
    }

    // Getters
    public double getX() { return this.x; }
    public double getY() { return this.y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}