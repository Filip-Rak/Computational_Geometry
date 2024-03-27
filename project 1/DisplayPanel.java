import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DisplayPanel extends JPanel
{
    private final List<Drawable> drawableList = new ArrayList<>();
    private final List<Color> colorList = new ArrayList<>();
    private BufferedImage bufferedImage;
    private final boolean useBufferedImage;
    public int HEIGHT = 600;
    public int WIDTH = 1200;

    //Constructor
    public DisplayPanel(boolean useBufferedImage, int width, int height)
    {
        this.useBufferedImage = useBufferedImage;
        HEIGHT = height;
        WIDTH = width;
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        if (useBufferedImage)
        {
            bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            fillBufferedImage();
        }
    }

    //Methods
    private void fillBufferedImage()
    {
        Graphics2D g2D = bufferedImage.createGraphics();
        g2D.setStroke(new BasicStroke(5));

        //proba poprawy jakosci
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < drawableList.size(); i++)
        {
            g2D.setColor(colorList.get(i));
            drawableList.get(i).draw(g2D, WIDTH, HEIGHT);
        }

        g2D.dispose();
    }

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

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (useBufferedImage)
            g.drawImage(bufferedImage, 0, 0, this);
        else
        {
            Graphics2D g2D = (Graphics2D) g;
            g2D.setStroke(new BasicStroke(5));
            for (int i = 0; i < drawableList.size(); i++)
            {
                g2D.setColor(colorList.get(i));
                drawableList.get(i).draw(g2D, WIDTH, HEIGHT);
            }

        }
    }

    public void refreshBufferedImage()
    {
        if (useBufferedImage)
        {
            fillBufferedImage();
            repaint();
            drawableList.clear();
            colorList.clear();
            bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            fillBufferedImage();
        }
    }
}