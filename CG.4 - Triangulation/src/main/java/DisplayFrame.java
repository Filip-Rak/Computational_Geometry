import javax.swing.*;

public class DisplayFrame extends JFrame
{
    //Attributes
    DisplayPanel panel;

    //Constructor
    DisplayFrame(boolean bufferImage, int w, int h)
    {
        panel = new DisplayPanel(bufferImage, w , h);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }
}