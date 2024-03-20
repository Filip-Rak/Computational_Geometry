import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DisplayPanel extends JPanel
{
    //Attributes
    private final List<Drawable> drawableList = new ArrayList<>();  //holds objects to draw
    private final List<Color> colorList = new ArrayList<>();  //holds colors for objects

    //Constructor
    DisplayPanel()
    {
        this.setPreferredSize(new Dimension(600, 600));
    }

    //Methods
    public void AddDrawable(Drawable object, Color color)
    {
        this.drawableList.add(object);
        this.colorList.add(color);
    }

    public void AddDrawable(Drawable object)
    {
        this.drawableList.add(object);
        this.colorList.add(Color.BLACK);
    }

    public void paintComponent(Graphics g)  //draws all objects
    {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5));

        for(int i = 0; i < drawableList.size(); i++)
        {
            g2D.setColor(colorList.get(i));
            drawableList.get(i).draw(g2D);
        }


    }
}
