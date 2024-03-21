import java.awt.*;

public class Point implements Drawable
{
    //Attributes
    private int x, y;

    //Constructor
    Point(int x, int y)
    {
        this.x = x;
        this.y = y;

        //translateToScreenCenter();
    }

    //Methods
    void translateToScreenCenter()
    {
            this.x += DisplayPanel.HEIGHT / 2;
            this.y += DisplayPanel.WIDTH / 2;
    }

    public String relationToLine(Line l)
    {
        //Ax + By + C < || > 0   //warunek
        //y = ax+b
        //A = a, B = -1, C=b. ax - y + b = 0  //warunek

        double calc = (l.getA() * x) - y + l.getB();

        if(calc < 0)
            return "Right";
        if (calc > 0)
            return "Left";
        else
            return "Aligned";
    }

    public void translate(int dx, int dy)
    {
        this.x += dx;
        this.y += dy;
    }

    public Point mirrorOverLine(Line ln)
    {
        if(ln.isVertical())
        {
            //dla linii pionowej prosta odbicia przebiega rownolegle do osi Y
            //x' = 2d - x, y = y
            int xp = 2 * ln.getXForVerticalLine() - this.x;
            return new Point(xp, this.y);
        }
        else if(ln.getA() == 0)
        {
            //dla linii poziomej prosta odbicia przebiega rownolegle do osi X
            //x = x, y' = 2b - y
            int yp = 2 * (int)ln.getB() - this.y;
            return new Point(this.x, yp);
        }
        else
        {
            //dla linii o nachyleniu innym niż 0 i niewertykalnych
            //prosta  prostopadlaj przechodzaca przez dany punkt
            //a' = -1/a
            double ap = -1 / ln.getA();
            //c' = y + x/a
            double cp = this.y - ap * this.x;

            //obliczanie punktu przeciecia z daną linią
            //x2 = (b - c') / (a' - a)
            double x2 = (ln.getB() - cp) / (ap - ln.getA());
            //y2 = ax2 + b
            double y2 = ln.getA() * x2 + ln.getB();

            //pbliczanie odbitego punktu
            //x_odbity = 2x2 - x
            int x_odbity = (int)(2 * x2 - this.x);
            //y_odbity = 2y2 - y
            int y_odbity = (int)(2 * y2 - this.y);

            return new Point(x_odbity, y_odbity);
        }
    }

    public void draw(Graphics2D g)
    {
        int diameter = 20;
        int radius = diameter / 2; //polowa srednicy
        g.fillOval(this.x - radius, this.y - radius, diameter, diameter);
    }

    public double distanceToLine(Line ln)
    {
        //przepisanie wartosci dla ultawnienia wizualizacji
        double x0 = this.getX(), x1 = ln.getP1().getX(), x2 = ln.getP2().getX();
        double y0 = this.getY(), y1 = ln.getP1().getY(), y2 = ln.getP2().getY();

        double numerator = Math.abs( (x2 - x1) * (y1 - y0) - (x1 - x0) * (y2 - y1) );
        double denominator = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        return numerator / denominator;
    }

    public static double distToPoint(Point p1, Point p2)
    {
        //d = sqrt((x2 - x1) ^ 2 + (y2 - y1) ^ 2)

        double x_pow = Math.pow( (double)p2.getX() -  (double)p1.getX(), 2);
        double y_pow = Math.pow( (double)p2.getY() -  (double)p1.getY(), 2);

        return Math.sqrt(x_pow + y_pow);
    }

    public static Point directionVector(Line line)
    {
        //Przepisanie wartosci
        double x1 = line.getP1().getX();
        double y1 = line.getP1().getY();
        double x2 = line.getP2().getX();
        double y2 = line.getP2().getY();

        //obliczenie wektora kierunkowego
        double vx = x2 - x1;
        double vy = y2 - y1;

        return new Point((int)vx, (int)vy);
    }

    //Getters
    int getX() { return this.x; }
    int getY() { return this.y; }

    //Setters
    void setX(int x) { this.x = x; }
    void setY(int y) { this.y = y; }
}
