import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main
{
    public static void main(String[] args)
    {
        M1();   // Alien
        //M2();   // Pacman blind
        //M3();   // Pacman with eyes
        //M4();   // 'Rectangle'

        //Load();   // Loading saved mesh
    }

    private static void Load()
    {
        LinkedList<Triangle> mesh = Mesh.loadFromFile("output/rectangle.txt");

        if(mesh != null)
        {
            DisplayFrame window = new DisplayFrame(true, 800, 600);

            for(Triangle t : mesh)
                window.panel.AddDrawable(t, Color.green);

            window.panel.refreshBufferedImage();
        }
    }

    private static void M4()
    {
        ArrayList<Point> p_arr = Point.loadFromFile("input/rectangle.txt");

        if(p_arr != null)
        {
            Point.transformArray(p_arr, 0.5, 0.5, -300, -200);

            Mesh mesh = new Mesh(p_arr, 8, 12, 1.5,1,  4, 600, 50);
            //Mesh mesh = new Mesh(p_arr, 8, -1, 1.5,1,  4, 1200, 5);

            DisplayFrame window = new DisplayFrame(true, 800, 600);
            LinkedList<Triangle> p = mesh.getTriangles();

            for(Triangle t : p)
                window.panel.AddDrawable(t, Color.black);

            window.panel.refreshBufferedImage();

            mesh.outputToFile("output/rectangle.txt", false);
        }
    }

    private static void M3()
    {
        ArrayList<Point> p_arr = Point.loadFromFile("input/pacman_3.txt");
        ArrayList<Point> eye_left = Point.loadFromFile("input/pacman_left_eye.txt");
        ArrayList<Point> eye_right = Point.loadFromFile("input/pacman_right_eye.txt");

        if(p_arr != null && eye_left != null && eye_right != null)
        {
            Point.transformArray(p_arr, 0.5, 0.5, -300, -200);
            Point.transformArray(eye_left, 0.5, 0.5, -300, -200);
            Point.transformArray(eye_right, 0.515, 0.5, -300, -200);

            ArrayList<Polygon> blank_spots = new ArrayList<>();
            blank_spots.add(new Polygon(eye_right));
            blank_spots.add(new Polygon(eye_left));

            Mesh mesh = new Mesh(p_arr, 6 , 12, 2, 0.7, 2, 800, 10, blank_spots);

            DisplayFrame window = new DisplayFrame(true, 800, 600);
            LinkedList<Triangle> p = mesh.getTriangles();

            for(Triangle t : p)
                window.panel.AddDrawable(t, Color.black);

            window.panel.refreshBufferedImage();

            mesh.outputToFile("output/pacman.txt", false);
        }


    }

    private static void M2()
    {
        ArrayList<Point> p_arr = Point.loadFromFile("input/pacman_eyeless.txt");

        if(p_arr != null)
        {
            Point.transformArray(p_arr, 0.5, 0.5, -300, -200);
            //Mesh mesh = new Mesh(p_arr, 6 , 16, 2, 1.2, 2, 600, 10);
            Mesh mesh = new Mesh(p_arr, 6 , -1, 1.5, 1, 2, 600, 10);

            DisplayFrame window = new DisplayFrame(true, 800, 600);
            LinkedList<Triangle> p = mesh.getTriangles();

            for(Triangle t : p)
                window.panel.AddDrawable(t, Color.black);

            window.panel.refreshBufferedImage();

            mesh.outputToFile("output/pacman_eyeless.txt", false);
        }
    }

    private static void M1()
    {
        ArrayList<Point> p_arr = Point.loadFromFile("input/kurwinox.txt");

        if(p_arr != null)
        {
            Point.transformArray(p_arr, 0.5, 0.5, -300, -200);

            Mesh mesh = new Mesh(p_arr, 8, 24, 3, 1.4, 4, 400, 100);
            //Mesh mesh = new Mesh(p_arr, 8, -1, 2, 1, 4, 400, 100);

            DisplayFrame window = new DisplayFrame(true, 800, 600);
            LinkedList<Triangle> p = mesh.getTriangles();

            for(Triangle t : p)
                window.panel.AddDrawable(t, Color.black);

            window.panel.refreshBufferedImage();

            mesh.outputToFile("output/kurwinox.txt", false);
        }
    }
}