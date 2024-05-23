import java.util.*;

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
    private Map<Double, Integer> qualityFrequency;
    private double modeQuality;

    private LinkedList<Double> sizes;
    private double avg_size;
    private double median_size;
    private double min_size;
    private double max_size;

    private final long time;

    // Constructor
    Statistics(LinkedList<Triangle> t_input, String title, long time)
    {
        // Initialization
        this.triangles = new LinkedList<>(t_input);
        this.title = title;
        this.time = time;

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
        calculateModeQuality();
    }

    private void calculateModeQuality()
    {
        // Number of intervals
        final int BINS = 10;
        qualityFrequency = new HashMap<>();
        for(double q : qualities)
        {
            // Calculate bin index, ensuring it falls within 0 to BINS - 1
            int binIndex = (int) Math.floor(q * BINS);
            if (q == 1.0)
                binIndex = BINS - 1;
            // Use binIndex as the key for the histogram
            double binKey = binIndex / (double) BINS;
            qualityFrequency.put(binKey, qualityFrequency.getOrDefault(binKey, 0) + 1);
        }

        // Find highest frequency
        int maxCount = -1;
        double modeQualityValue = -1.0;
        for(Map.Entry<Double, Integer> entry : qualityFrequency.entrySet())
        {
            if(entry.getValue() > maxCount)
            {
                maxCount = entry.getValue();
                modeQualityValue = entry.getKey();
            }
        }

        // Convert the bin key back to the quality score that it represents
        modeQuality = modeQualityValue + 1.0 / BINS / 2; // Add half the bin width to get the middle of the bin
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
                return (values.get(values.size() / 2) + values.get((values.size() / 2) - 1)) / 2;
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

    private void printHistogram()
    {
        System.out.println(title + " - Quality Histogram:");
        final int MAX_BAR_LENGTH = 50; // Maximum length of histogram bar for scaling

        // Convert to TreeMap to sort by keys
        TreeMap<Double, Integer> sortedHistogram = new TreeMap<>(qualityFrequency);
        int maxFrequency = Collections.max(sortedHistogram.values());


        for (Map.Entry<Double, Integer> entry : sortedHistogram.entrySet())
        {
            double binStart = entry.getKey();

            // End is exclusive; last bin includes 1.0
            double binEnd = binStart + 1.0 / 10;
            if (binEnd > 1.0) binEnd = 1.0;

            int frequency = entry.getValue();

            // Scale the bar length to the max frequency for better visualization
            int barLength = (int)(((double)frequency / maxFrequency) * MAX_BAR_LENGTH);

            // Create the bar using a string of asterisks
            String bar = String.join("", Collections.nCopies(barLength, "*"));

            // Correctly formatted string for the bin range
            String binRange = String.format("[%.2f - %.2f)", binStart, binEnd);
            if (binEnd == 1.0)
                binRange = String.format("[%.2f - %.2f]", binStart, binEnd); // Inclusive of 1.0 for the last bin

            System.out.printf("%s : %s (%d)\n", binRange, bar, frequency);
        }
    }

    private void print()
    {
        System.out.println(
                "-------------------------"
                + "\nMethod: " + this.title
                + "\nElements in total: " + this.triangles.size()
                + "\nAverage quality: " + this.avg_quality
                + "\nMedian quality: " + this.median_quality
                + "\nWorst quality: " + this.min_quality
                + "\nBest quality: " + this.max_quality
                + "\nMode quality: " + this.modeQuality
                + "\nAverage area: " + this.avg_size
                + "\nMedian area: " + this.median_size
                + "\nSmallest area: " + this.min_size
                + "\nBiggest area: " + this.max_size
                + "\nExecution time (ms): " + this.time / 1000000
        );

        printHistogram();
    }
}
