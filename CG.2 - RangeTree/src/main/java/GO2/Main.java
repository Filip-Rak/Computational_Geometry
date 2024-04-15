package GO2;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main
{
    public static void main(String[] args)
    {
        init();
        oneDimensional();
        //twoDimensional();
    }

    static void twoDimensional()
    {
        // Point generation
        int size = 15;
        ArrayList<Point2D> points = PointGen.generate2D(-10, 25, size, false);

        // Tree creation & visualization
        RangeTree2DNaive tree2D = new RangeTree2DNaive(points);
        //Visualization2D v2d = new Visualization2D(tree2D.getHead());

        // Range search & printing
        LinkedList<TreeNode<Double>> rs = tree2D.rangeSearch2D(-10, 25, 0, 25);
        for(TreeNode<Double> d : rs)
            System.out.println(d.getValue() + " " + d);

    }

    static void oneDimensional()
    {
        // data generation
        final int size = 15000;
        ArrayList<Double> data = PointGen.generate1D(-1000, 300, size, false);

        // tree creation & visualization
        RangeTree1D tree1D = new RangeTree1D(data);
        //Visualization vis1d = new Visualization(size, tree1D.getHead(), "1D");

        // range search & print
        LinkedList<TreeNode<Double>> rs = tree1D.rangeSearch(-5, 150);
        for(TreeNode<Double> t : rs)
            System.out.println(t.getValue() + " " + t);
    }

    static void init()
    {
        LogManager.getLogManager().reset();
        Logger.getLogger("org.graphstream").setLevel(Level.OFF);
        Logger.getLogger("org.graphstream.ui").setLevel(Level.OFF);
    }

    //Złożoność czasowa wyszukiwania przedziału w drzewie zakresowym 2D to O(log^2 n + k),
    //gdzie n to liczba punktów w drzewie, a k to liczba punktów w wyniku zapytania.
    //Pierwszy czynnik wynika z przeszukiwania drzewa głównego i skojarzonych drzew (O(log n) dla każdego),
    //a k reprezentuje czas potrzebny na wylistowanie wyników.
}