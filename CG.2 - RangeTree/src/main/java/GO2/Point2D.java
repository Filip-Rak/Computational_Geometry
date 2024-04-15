package GO2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Point2D
{
    //Attributes
    private double x, y;

    //Constructor
    Point2D(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    //Methods
    public static void sortByX(ArrayList<Point2D> arr)
    {
        arr.sort(new Comparator<Point2D>()
        {
            public int compare(Point2D p1, Point2D p2)
            {
                return Double.compare(p1.getX(), p2.getX());
            }
        });
    }

    public static void sortByY(ArrayList<Point2D> arr)
    {
        arr.sort(new Comparator<Point2D>()
        {
            public int compare(Point2D p1, Point2D p2)
            {
                return Double.compare(p1.getY(), p2.getY());
            }
        });
    }

    //Getters
    public double getX() { return this.x; }
    public double getY() { return this.y; }

    //Setter
    public void setX(double x) {this.x = x; };
    public void setY(double y) {this.y = y; };
}
