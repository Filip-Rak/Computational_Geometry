import java.awt.*;

public class Missile implements Drawable
{
    //Attributes
    private final double reveal_time;
    private double hit_time;
    private double pos_x, pos_y;
    private double speed_x, speed_y;
    private boolean isFired;
    private boolean enabled;
    private boolean marked;

    //Constructor
    Missile(double reveal_time, double pos_x, double pos_y, double speed_x, double speed_y)
    {
        this.reveal_time = reveal_time;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.speed_x = speed_x;
        this.speed_y = speed_y;
        this.isFired = false;
        this.enabled = true;
        this.hit_time = Double.MAX_VALUE;
        marked = false;
    }

    //Methods
    public void translate(double x, double y)
    {
        pos_x += x;
        pos_y += y;
    }

    public void move()
    {
        pos_x += speed_x;
        pos_y += speed_y;
    }

    public void draw(Graphics2D g, int width, int height)
    {
        if(!isFired || !enabled)
            return;

        //copy coordinates
        double X = pos_x;
        double Y = pos_y;

        //make window center the focal point
        X += (double)(width) / 2;
        Y += (double)(height) / 2;

        //reverse Y
        Y = height - Y;

        //draw
        double diameter = 12;   //20 is default
        double radius = diameter / 2;
        g.fillOval((int) (X - radius), (int) (Y - radius), (int) diameter, (int) diameter);
    }

    //Getters
    public double Reveal_time() { return reveal_time; }
    public double Pos_x() { return pos_x; }
    public double Pos_y() { return pos_y; }
    public double Speed_x() { return speed_x; }
    public double Speed_y() { return speed_y; }
    public boolean IsFired() { return isFired; }
    public boolean Enabled() { return enabled; }
    public boolean Marked() { return marked; }
    public double HitTime() { return hit_time; }

    //Setters
    public void setPos_x(double x) { this.pos_x = x; }
    public void setPos_y(double y) { this.pos_y = y; }
    public void setSpeed_x(double x) { this.speed_x = x; }
    public void setSpeed_y(double y) { this.speed_y = y; }
    public void setFired(boolean value) { this.isFired = value; }
    public void setEnabled(boolean value) { this.enabled = value; }
    public void setMarked(boolean value) { this.marked = value; }
    public void setHit_time(double value) { this.hit_time = value; }
}