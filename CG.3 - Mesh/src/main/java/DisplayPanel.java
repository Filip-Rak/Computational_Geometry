import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayPanel extends JPanel
{
    private final List<Drawable> drawableList = new ArrayList<>();
    private final List<Color> colorList = new ArrayList<>();
    private BufferedImage bufferedImage;
    private final boolean useBufferedImage;
    private BufferedImage bgImage = null;
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
            bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
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
            Graphics2D g2d = bufferedImage.createGraphics();

            // Check if bgImage is not null and draw it; otherwise, fill with white
            if (bgImage != null)
                g2d.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, null); // Draw the image to fill the panel
             else
             {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(0, 0, WIDTH, HEIGHT); // Fill with white as fallback
            }

            g2d.dispose();

            // Now fill the buffered image with current drawable objects
            fillBufferedImage();

            // Request the panel to repaint itself
            repaint();
        }
    }

    //getters
    public void setBackgroundImage(String path)
    {
        File f = new File(path);
        try { bgImage = ImageIO.read(f); }
        catch (IOException e) {
            bgImage = null; // Image failed to load, so set to null
        }
    }

    int getDrawableSize() { return this.drawableList.size(); }

}