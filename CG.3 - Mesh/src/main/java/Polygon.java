import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Polygon implements Drawable
{
    //Attributes
    private final Point[] verticies;
    private Line[] lines;

    //Constructors
    Polygon(List<Point> verts)
    {
        verticies = verts.toArray(new Point[0]);
        this.lines = calcLines();
    }

    //Methods
    private Line[] calcLines()
    {
        Line[] arr = new Line[verticies.length];
        int i;
        for(i = 0; i < verticies.length -1; i++)
            arr[i] = new Line(verticies[i], verticies[i + 1], false);

        arr[i] = new Line(verticies[0], verticies[i], false);

        return arr;
    }

    public boolean contains(Point p)
    {
        // Obecnie metoda daje falsz dla wartosci zmienno przeciwnkowych
        // tj. quick fix

        p.setX((int) p.getX());
        p.setY((int) p.getY());

        //Szukanie punktu najbardziej po lewej
        Point destination = verticies[0];
        for(int i = 1; i < verticies.length; i++)
        {
            if(verticies[i].getX() < destination.getX())
                destination = verticies[i];
        }

        //upewnienie sie, ze jest poza figura
        destination = new Point(destination.getX(), destination.getY());
        destination.translate(-1, 0);
        destination.setY(p.getY());
        Line ray = new Line(p, destination, false);

        //zliczanie punktow przeciecia
        int count = 0;
        for(Line l : this.lines)
        {
            Point check = Line.findCrossPoint(l, ray);
            if(check != null)
            {
                //sprawdzenie czy nie liczymy podwojnie
                //jeden z pkt jest ponizej
                if(l.getP1().getY() < destination.getY() || l.getP2().getY() < destination.getY())
                {
                    //drugi pkt jest wyzej lub na rowni
                    if (l.getP1().getY() >= destination.getY() || l.getP2().getY() >= destination.getY())
                        count++;
                }
            }
        }
        return count % 2 != 0;
    }

    boolean areEqual(double a, double b)
    {
        return Math.abs(a - b) < 0.000001;
    }


    public static ArrayList<Point> jarvis_march_1(ArrayList<Point> input)
    {
        //kopia wejscia
        ArrayList<Point> p_arr = new ArrayList<>();
        for(Point p : input)
            p_arr.add(new Point(p));

        //znajdz punkt z najmniejszym y i x
        Point p1 = p_arr.getFirst();
        for(int i = 1; i < p_arr.size(); i++)
        {
            if(p_arr.get(i).getY() < p1.getY() || (p_arr.get(i).getY() == p1.getY() && p_arr.get(i).getX() < p1.getX()))
                p1 = p_arr.get(i);
        }

        //punkt pomocniczy
        Point p0 = new Point(-Double.MAX_VALUE, p1.getY());

        //wynik
        ArrayList<Point> boundry = new ArrayList<>();
        boundry.add(p1);

        //tworzenie otoczki
        Point start = p1;
        while(!p_arr.isEmpty()) //popraw warunek polegajacy na znalezieniu pkt poczatkowego
        {
            Line l1 = new Line(p0, p1, false);
            Line l2 = new Line(p1, p_arr.getFirst(), false);
            Point max_p = p_arr.getFirst();
            double max_angle = Line.angle360(l1, l2);
            double max_distance = Point.distance(max_p, p1);

            for(int i = 1; i < p_arr.size(); i++)
            {
                l2 = new Line(p1, p_arr.get(i), false);
                double new_angle = Line.angle360(l1, l2);
                double new_distance = Point.distance(p_arr.get(i), p1);

                if(new_angle > max_angle || (equal(new_angle, max_angle, 0.1) && new_distance > max_distance))
                {
                    max_distance = Point.distance(p_arr.get(i), p1);
                    max_angle = new_angle;
                    max_p = p_arr.get(i);
                }
            }

            boundry.add(max_p);
            p_arr.remove(max_p);
            p0 = p1;
            p1 = max_p;

            if(max_p == start)
                break;
        }

        return boundry;
    }

    private static boolean equal(double d1, double d2, double margin)
    {
        return (Math.abs(d1 -d2) < margin);
    }

    public static ArrayList<Point> jarvis_march_2(ArrayList<Point> input)
    {
        //kopia wejscia
        ArrayList<Point> p_arr = new ArrayList<>();
        for(Point p : input)
            p_arr.add(new Point(p));

        //szukanie najblizszego i najdalszego punktu od poczatku ukladu
        Point point_min = p_arr.getFirst();
        Point point_max = p_arr.getFirst();
        for(int i = 1; i < p_arr.size(); i++)
        {
            //mapowanie
            Point p = p_arr.get(i);

            if(p.getY() < point_min.getY() || p.getY() == point_min.getY() && p.getX() < point_min.getX())
                point_min = p;
            else if(p.getY() > point_max.getY() || p.getY() == point_max.getY() && p.getX() > point_max.getX())
                point_max = p;
        }

        //robienie otoczki
        ArrayList<Point> result = new ArrayList<>();
        result.add(point_min);

        chain(result, p_arr, point_min, point_max, 1);
        chain(result, p_arr, point_max, point_min, -1);

        return result;
    }

    private static void chain(ArrayList<Point> result, ArrayList<Point> p_arr, Point startPoint, Point endPoint, double direction)
    {
        //wyznaczanie jednej strony lancucha
        Point tgt = startPoint;
        while(!((tgt.getX() == endPoint.getX()) && tgt.getY() == endPoint.getY()))
        {
            //stworz punkt na prawo/lewo od sprawdzanego punktu i narysuj linie
            Point extension = new Point(tgt);
            extension.translate(direction, 0);
            Line l1 = new Line(tgt, extension, false);

            //inicjalizacja sprawdzania
            Point best = new Point(0, 0);
            double min_angle = Double.MAX_VALUE;

            for (Point point : p_arr)
            {
                Point p3 = new Point(point);

                //utworz linie od p1 do p3 i porownaj z linia p1 extension
                Line l2 = new Line(tgt, p3, false);

                double new_angle = Line.angle360(l1, l2);
                if (new_angle < min_angle)
                {
                    min_angle = new_angle;
                    best = p3;
                }
            }

            result.add(best);
            tgt = best;
            p_arr.remove(tgt);
        }
    }

    public void translate(double dx, double dy)
    {
        for (Point p : verticies)
            p.translate(dx, dy);

        this.lines = calcLines();
    }

    public void draw(Graphics2D g, int width, int height)
    {
        for(Line l : this.lines)
        {
            //convert the coordinates of the first point of the line
            double x1 = l.getP1().getX() + width / 2.0;
            double y1 = (height / 2.0) - l.getP1().getY(); // Shift and invert Y

            //convert the coordinates of the second point of the line
            double x2 = l.getP2().getX() + width / 2.0;
            double y2 = (height / 2.0) - l.getP2().getY(); // Shift and invert Y

            //draw the line with adjusted coordinates
            g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        }
    }

    public boolean intersects(Point p1, Point p2)
    {
        Line testLine = new Line(p1, p2, false);
        for (Line l : lines)
        {
            Point intersection = Line.findCrossPoint(l, testLine);
            if (intersection != null) {
                return true;  // Return true immediately upon finding the first intersection
            }
        }
        return false;  // Return false if no intersections are found
    }

    //Getters
    Point[] getVerticies() { return this.verticies; }
    LinkedList<Line> getLines() { return new LinkedList<Line>(List.of(lines)); }
    int getVertsNum() { return this.verticies.length; }
}