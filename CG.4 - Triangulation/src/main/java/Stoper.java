public class Stoper
{
    // Attributes
    private long start_time;

    // Constructor
    Stoper() {}

    // Methods
    public void start()
    {
        start_time = System.nanoTime();
    }

    public long stop()
    {
        return System.nanoTime() - start_time;
    }
}