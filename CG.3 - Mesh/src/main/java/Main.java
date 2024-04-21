import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        //M1();
        //M2();
        //M3();
        M4();
        //M5();
    }

    private static void M5()
    {
        ArrayList<Point> p_arr = Point.loadFromFile("input/pacman_2.txt");
        Point.transformArray(p_arr, 0.5, 0.5, -300, -200);
        ArrayList<Point> eye_left = Point.loadFromFile("input/pacman_left_eye.txt");
        Point.transformArray(eye_left, 0.5, 0.5, -300, -200);
        ArrayList<Point> eye_right = Point.loadFromFile("input/pacman_right_eye.txt");
        Point.transformArray(eye_right, 0.53, 0.5, -300, -200);
        ArrayList<Point> main = Point.loadFromFile("input/pacman_contour.txt");
        Point.transformArray(main, 0.5, 0.5, -300, -200);

        ArrayList<Polygon> blank_spots = new ArrayList<>();
        blank_spots.add(new Polygon(eye_right));
        blank_spots.add(new Polygon(eye_left));
        blank_spots.add(new Polygon(main));
        
        Mesh4 mesh = new Mesh4(p_arr, 3 , 16, 2, 2, 600, 1000);

        DisplayFrame window = new DisplayFrame(true, 800, 600);
        LinkedList<Triangle> p = mesh.triangles;

        for(Triangle t : p)
            window.panel.AddDrawable(t, Color.black);

        window.panel.refreshBufferedImage();
    }

    private static void M4()
    {
        ArrayList<Point> p_arr = Point.loadFromFile("input/rectangle.txt");
        Point.transformArray(p_arr, 0.5, 0.5, -300, -200);

        //Mesh4 mesh = new Mesh4(p_arr, 8, 24, 3, 4, 400, 100); //kurwinox
        Mesh4 mesh = new Mesh4(p_arr, 8, 12, 1.5, 4, 600, 10); //rectangle

        DisplayFrame window = new DisplayFrame(true, 800, 600);
        LinkedList<Triangle> p = mesh.triangles;

        for(Triangle t : p)
            window.panel.AddDrawable(t, Color.black);

        //LinkedList<Point> unused = mesh.points.copy();
        //for(Point w : unused)
            //window.panel.AddDrawable(w, Color.BLUE);

        window.panel.refreshBufferedImage();
    }


    private static void M3()
    {
        //DisplayFrame window = new DisplayFrame(true, 800, 600);
        //ArrayList<Point> p_arr = Point.loadFromFile("C:/Users/Filip/IdeaProjects/CG.3 - Mesh/src/main/resources/trudny_wariant.txt");
        ArrayList<Point> p_arr = new ArrayList<>();
        p_arr.add(new Point(-200, 0));
        p_arr.add(new Point(-100, 100));
        p_arr.add(new Point(0, 200));
        p_arr.add(new Point(100, 100));
        p_arr.add(new Point(200, 0));
        p_arr.add(new Point(100, -100));
        p_arr.add(new Point(0, -200));
        p_arr.add(new Point(-100, -100));


        //Point.transformArray(p_arr, 0.5, 0.5, -300, -200);
        Mesh3 mesh = new Mesh3(p_arr, 60, 3, 1000);

        LinkedList<Triangle> triangles = mesh.triangles;

        //for(Triangle t : triangles)
           // window.panel.AddDrawable(t);

       // window.panel.AddDrawable(triangles.get(triangles.size() - 1));
        //window.panel.AddDrawable(triangles.get(triangles.size() - 2));
        //window.panel.AddDrawable(triangles.get(1));

        //LinkedList<Point> unused = mesh.points.copy();
        //for(Point p : unused)
           // window.panel.AddDrawable(p, Color.red);

        //for(Point p : p_arr)
          //  window.panel.AddDrawable(p, Color.BLUE);

       // window.panel.refreshBufferedImage();
    }


    public static void M2()
    {
        ArrayList<Point> p_arr = new ArrayList<>();
        p_arr.add(new Point(-200, 0));
        p_arr.add(new Point(-100, 100));
        p_arr.add(new Point(0, 200));
        p_arr.add(new Point(100, 100));
        p_arr.add(new Point(200, 0));
        p_arr.add(new Point(100, -100));
        p_arr.add(new Point(0, -200));
        p_arr.add(new Point(-100, -100));



       DisplayFrame window = new DisplayFrame(true, 800, 600);
       Mesh2 mesh = new Mesh2(p_arr, 80);
       LinkedList<Triangle> t_arr = mesh.getTriangles();

        for(Triangle p : t_arr)
          window.panel.AddDrawable(p);
          //window.panel.AddDrawable(t_arr.getFirst());
          //window.panel.AddDrawable(t_arr.get(1));

        LinkedList<Point> unused = mesh.getPoints();
        for(Point p : unused)
            window.panel.AddDrawable(p, Color. blue);

        for(Point p : p_arr)
            window.panel.AddDrawable(p, Color. RED);

       window.panel.refreshBufferedImage();
    }

    public static void M1()
    {
        DisplayFrame window = new DisplayFrame(true, 800, 600);
        ArrayList<Point> p_arr = Point.loadFromFile("C:/Users/Filip/IdeaProjects/CG.3 - Mesh/src/main/resources/trudny_wariant.txt");

        Point.transformArray(p_arr, 0.5, 0.5, -300, -200);
        ArrayList<Point> less = new ArrayList<>();
        for(int i = 0; i < p_arr.size(); i+= 16)
            less.add(p_arr.get(i));

        Mesh mesh = new Mesh(p_arr, 60);
        LinkedList<Triangle> ts = mesh.getTriangles();

        for(Triangle t : ts)
            window.panel.AddDrawable(t, Color.green);

        LinkedList<Point> unused = mesh.getPoints();
        for (Point p : unused)
            window.panel.AddDrawable(p, Color.BLUE);

        window.panel.refreshBufferedImage();
    }
}
