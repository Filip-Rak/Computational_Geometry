import java.awt.*;

public class Polygon implements Drawable
{
    //Attributes
    private final Point[] verticies;
    private final Line[] lines;

    //Constructors
    Polygon(Point[] verts)
    {
        verticies = verts;
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
        //Szukanie punktu najbardziej po lewej
        Point destination = verticies[0];
        for(int i = 1; i < verticies.length; i++)
        {
            if(verticies[i].getX() < destination.getX())
                destination = verticies[i];
        }

        //upewnienie sie, ze jest poza figura
        destination = new Point(destination.getX(), destination.getY());
        destination.translate(-50, 0);
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

    public void draw(Graphics2D g)
    {
        //alternatywnie, uzyj wbudowanej funkcji
        for(Line l : this.lines)
            g.drawLine(l.getP1().getX(), l.getP1().getY(), l.getP2().getX(), l.getP2().getY());
    }

    //Getters
    Point[] getVerticies() { return this.verticies; }
    Line[] getLines() { return this.lines; }
    int getVertsNum() { return this.verticies.length; }
}
