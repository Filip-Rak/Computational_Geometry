package GO2;

import java.util.ArrayList;
import java.util.Random;

public class PointGen
{
    //Constructor
    PointGen() {}

    //Methods
    static ArrayList<Double> generate1D(double min, double max, int number, boolean fractions)
    {
        ArrayList<Double> results = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < number; i++)
        {
            double rand;
            if(fractions)
                rand = random.nextDouble(max - min + 1) + min;
            else
                rand = random.nextInt((int)max - (int)min + 1) + (int)min;

            results.add(rand);
        }

        return results;
    }

    static ArrayList<Point2D> generate2D(double min, double max, int number, boolean fractions)
    {
        ArrayList<Point2D> results = new ArrayList<>();
        Random random = new Random();

        for(int i = 0; i < number; i++)
        {
            double x, y;
            if(fractions)
            {
                x = random.nextDouble(max - min + 1) + min;
                y = random.nextDouble(max - min + 1) + min;
            }
            else
            {
                x = random.nextInt((int)max - (int)min + 1) + (int)min;
                y = random.nextInt((int)max - (int)min + 1) + (int)min;
            }

            results.add(new Point2D(x, y));
        }

        return results;
    }
}
