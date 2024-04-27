import java.util.Collections;
import java.util.LinkedList;

public class Statistics
{
    // Attributes
    private final LinkedList<Triangle> triangles;
    private final String title;

    // Stats
    private LinkedList<Double> qualities;
    private double avg_quality;
    private double median_quality;
    private double min_quality;
    private double max_quality;

    private LinkedList<Double> sizes;
    private double avg_size;
    private double median_size;
    private double min_size;
    private double max_size;

    // Constructor
    Statistics(LinkedList<Triangle> t_input, String title)
    {
        // Initialization
        this.triangles = new LinkedList<>(t_input);
        this.title = title;

        // Stat gathering
        calcQuality();
        calcSize();

        // Print the results to console
        print();
    }


    // Init methods
    private void calcQuality()
    {
        avg_quality = 0;
        min_quality = triangleQuality(triangles.getFirst());
        max_quality = triangleQuality(triangles.getFirst());
        qualities = new LinkedList<>();

        for(Triangle triangle : triangles)
        {
            double quality = triangleQuality(triangle);
            avg_quality += quality;

            if(quality < min_quality) min_quality = quality;
            if(quality > max_quality) max_quality = quality;

            qualities.add(quality);
        }

        avg_quality /= triangles.size();
        median_quality = median(qualities);
    }

    private void calcSize()
    {
        avg_size = 0;
        min_size = triangleArea(triangles.getFirst());
        max_size = triangleArea(triangles.getFirst());
        sizes = new LinkedList<>();

        for(Triangle t : triangles)
        {
            double area = triangleArea(t);
            sizes.add(area);
            avg_size += area;

            if(area < min_size) min_size = area;
            if(area > max_size) max_size = area;
        }

        avg_size /= triangles.size();
        median_size = median(sizes);
    }

    private double triangleArea(Triangle triangle)
    {
        double A = triangle.getLengths(0);
        double B = triangle.getLengths(1);
        double C = triangle.getLengths(2);

        double S = (A + B + C) / 2;

        return Math.sqrt(S * (S - A) * (S - B) * (S - C));
    }

    private double median(LinkedList<Double> values)
    {
        Collections.sort(values);
        if(values.size() > 1)
        {
            if(values.size() % 2 == 0 )
                return values.get(values.size() / 2) + values.get((values.size() / 2) - 1) / 2;
            else
                return values.get((int)(values.size() / 2));
        }
        else if(values.size() == 1)
            return values.getFirst();
        else
            return -1;
    }

    private double triangleQuality(Triangle triangle)
    {
        double AB = Point.distance(triangle.getVertice(0), triangle.getVertice(1));
        double AC = Point.distance(triangle.getVertice(0), triangle.getVertice(2));
        double BC = Point.distance(triangle.getVertice(1), triangle.getVertice(2));
        double perimeter = AB + AC + BC;

        // Average length of the sides
        double averageLength = perimeter / 3.0;

        // Sum of absolute differences between each side and the average length
        double sumDifferences = Math.abs(AB - averageLength) + Math.abs(AC - averageLength) + Math.abs(BC - averageLength);

        // Normalize the result to range between 0 and 1
        double quality = sumDifferences / perimeter;

        // Invert the quality to get 1 for perfect equilateral triangle
        quality = 1 - quality;

        return quality;
    }

    private void print()
    {
        System.out.println(
                "-------------------------"
                + "\nMethod: " + this.title
                + "\nElements in total: " + this.triangles.size()
                + "\nAvergae quality: " + this.avg_quality
                + "\nMedian quality: " + this.median_quality
                + "\nWorst quality: " + this.min_quality
                + "\nBest quality: " + this.max_quality
                + "\nAverage area: " + this.avg_size
                + "\nMedian area: " + this.median_size
                + "\nSmallest area: " + this.min_size
                + "\nBiggest area: " + this.max_size
        );
    }
}
