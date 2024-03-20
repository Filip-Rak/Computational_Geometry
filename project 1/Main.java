import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        //Tworzenie okienka
        DisplayFrame displayFrame = new DisplayFrame();

        //Punkty
        Point p1 = new Point(100, 250); //linia l1
        Point p2 = new Point(150, 450); //linia l1
        Point p3 = new Point(500, 150); //odcinek o1
        Point p4 = new Point(500, 550); //odcinek o2
        Point tgt1 = new Point(125, 350); //wolny
        Point tgt2 = new Point(160, 300); //wolny

        //Linie / odcinki
        Line l1 = new Line(p1, p2, true);
        Line o1 = new Line(p3, p4, false);
        Line o2 = new Line(p3, p4, false);

        //Rysowane linie / odcinki
        displayFrame.panel.AddDrawable(l1, Color.black);
        displayFrame.panel.AddDrawable(o1, Color.yellow);
        displayFrame.panel.AddDrawable(o2, Color.gray);

        //Rysowane punkty
        displayFrame.panel.AddDrawable(p1, Color.blue);
        displayFrame.panel.AddDrawable(p2, Color.blue);
        displayFrame.panel.AddDrawable(p3, Color.blue);
        displayFrame.panel.AddDrawable(p4, Color.blue);
        displayFrame.panel.AddDrawable(tgt1, Color.red);
        displayFrame.panel.AddDrawable(tgt2, Color.green);

        //Wyznaczenie rownanania prostej lini:
        System.out.println("Rownanie kierunkowe l1: " + l1.getStandardForm());
        System.out.println("Rownanie kierunkowe o1: " + o1.getStandardForm());

        //Sprawdzenie przynaleznosci punktu do prostej
        System.out.println("TGT1 aligned with l1: " + l1.isAligned(tgt1));

        //Sprawdzenie przynaleznosci punktu do odcinka
        System.out.println("TGT1 aligned with o1: " + o1.isAligned(tgt1));

        //Polozenie odcinka wzgledem prostej
        System.out.println("TGT2 in relevance to L1: " + tgt2.relationToLine(l1));

        //Translacja o2 (kopii o1) o wektor
        o2.translateLine(new Point(50, -40));

        //Odbicie tgt2 wzgledem l1
        Point tgt2_2 = tgt2.mirrorOverLine(l1);
        displayFrame.panel.AddDrawable(tgt2_2, Color.orange); //rysowanie tgt2_2
    }
}