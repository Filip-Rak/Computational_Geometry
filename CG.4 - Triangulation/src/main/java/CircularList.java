import java.util.ArrayList;
import java.util.LinkedList;

public class CircularList
{
    // Attributes
    private final LinkedList<Point> list;
    private int currentIndex = 0;

    // Constructors
    CircularList(LinkedList<Point> input)
    {
        list = new LinkedList<>(input);
    }

    CircularList()
    {
        list = new LinkedList<>();
    }

    // Methods
    public void add(Point item)
    {
        list.add(item);
    }

    public void add(Point newItem, Point right)
    {
        int index = list.indexOf(right);
        list.add(index, newItem);
    }

    public Point getNext()
    {
        currentIndex = (currentIndex + 1) % list.size();  // Zapętlenie indeksu
        return list.get(currentIndex);
    }

    public Point getPrevious()
    {
        currentIndex = (currentIndex - 1 + list.size()) % list.size();  // Odejmowanie z zapętleniem
        return list.get(currentIndex);
    }

    public LinkedList<Point> copy()
    {
        return new LinkedList<>(this.list);
    }

    public void remove(Point p)
    {
        list.remove(p);
    }

    public void offset(int off)
    {
        currentIndex = (currentIndex + off) % list.size();
        if (currentIndex < 0) {
            currentIndex += list.size();
        }
    }

    // Getters
    public int size() { return this.list.size(); }
    public int getCurrentIndex() { return this.currentIndex; }

    // Setters
    public void setCurrentIndex(int index) { this.currentIndex = index; }
}
