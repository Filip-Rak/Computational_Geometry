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
        Point p3 = new Point(1000, 150); //odcinek o1
        Point p4 = new Point(1000, 550); //odcinek o1
        Point p5 = new Point(20, 140);   //odcinek o3
        Point p6 = new Point(330, 110);   //odcinek o3
        Point p7 = new Point(280, 10);   //odcinek o4
        Point p8 = new Point(280, 170);   //odcinek o4
        Point p9 = new Point(850, 550);   //odcinek o5
        Point p10 = new Point(1000, 550);   //odcinek o5
        Point tgt1 = new Point(125, 350); //wolny
        Point tgt2 = new Point(160, 300); //wolny

        //Linie / odcinki
        Line l1 = new Line(p1, p2, true);   //pionowy, lewa strona - pierwsza linia czarna
        Line o1 = new Line(p3, p4, false);  //pionowy, prawa strona - odcinek po prawej z obiciem
        Line o2 = new Line(p3, p4, false);  //pionowy, prawa strona - kopia odcinka o1
        Line o3 = new Line(p5, p6, false);  //poziomy, lew gorna
        Line o4 = new Line(p7, p8, false);  //pionowy, lewa gorna
        Line o5 = new Line(p9, p10, false); //poziomy, prawy dolny

        //Rysowane linie / odcinki
        displayFrame.panel.AddDrawable(l1, Color.black);
        displayFrame.panel.AddDrawable(o1, Color.yellow);
        displayFrame.panel.AddDrawable(o2, Color.gray);
        displayFrame.panel.AddDrawable(o3, Color.orange);
        displayFrame.panel.AddDrawable(o4, new Color(51153255));
        displayFrame.panel.AddDrawable(o5, Color.cyan);

        //Rysowane punkty
        displayFrame.panel.AddDrawable(p1, Color.blue);
        displayFrame.panel.AddDrawable(p2, Color.blue);
        displayFrame.panel.AddDrawable(p3, Color.blue);
        displayFrame.panel.AddDrawable(p4, Color.blue);
        displayFrame.panel.AddDrawable(p5, Color.blue);
        displayFrame.panel.AddDrawable(p6, Color.blue);
        displayFrame.panel.AddDrawable(p7, Color.blue);
        displayFrame.panel.AddDrawable(p8, Color.blue);
        displayFrame.panel.AddDrawable(p9, Color.blue);
        displayFrame.panel.AddDrawable(p10, Color.blue);
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

        //Punkt na przecieciu l1 i o3
        Point cross_1 = l1.findCrossPoint(o3);

        if(cross_1 != null)
        {
            displayFrame.panel.AddDrawable(cross_1, new Color(1020102));
            System.out.println("Intersection point of l1 and o3 is at: x = " + cross_1.getX() + ", y = " + cross_1.getY());
        }
        else
            System.out.println("l1 and o3 do not intersect");

        //Punkt na przecieciu o4 i o3
        Point cross_2 = o4.findCrossPoint_ALT(o3);

        if(cross_2 != null)
        {
            displayFrame.panel.AddDrawable(cross_2, new Color(1020102));
            System.out.println("Intersection point of o4 and o3 is at: x = " + cross_2.getX() + ", y = " + cross_2.getY());
        }
        else
            System.out.println("o4 and o3 do not intersect");


        //Dystans punktu tgt_2 (zielony) do linii o3 (orange)
        System.out.println("Distance from point tgt_2 (green) to line o3 (orange): " + tgt2.distanceToLine(o3));

        //Trojkat
        try
        {
            //Tworzenie trojkata z wspolczynnikow 3 linii
            //Punkty
            Point tp1 = new Point(220, 250);    //tl1
            Point tp2 = new Point(420, 270);    //tl1
            Point tp3 = new Point(240, 200);    //tl2
            Point tp4 = new Point(290, 400);    //tl2
            Point tp5 = new Point(260, 390);    //tl3
            Point tp6 = new Point(380, 220);    //tl3
            Point tp7 = new Point(300, 300);    //wolny

            //Linie
            Line tl1 = new Line(tp1, tp2, false);
            Line tl2 = new Line(tp3, tp4, false);
            Line tl3 = new Line(tp5, tp6, false);

            Triangle t1 = new Triangle(tl1, tl2, tl3);

            //Zapisanie wierzcholkow trojkata dla wizualizacji
            Point[] t1v = t1.getVertices();

            //Rysowanie linii
            displayFrame.panel.AddDrawable(tl1, Color.PINK);
            displayFrame.panel.AddDrawable(tl2, Color.PINK);
            displayFrame.panel.AddDrawable(tl3, Color.PINK);

            //Rysowanie trojkata
            displayFrame.panel.AddDrawable(t1, new Color(255102255));

            //Rysowanie punktow
            displayFrame.panel.AddDrawable(tp1, Color.red);
            displayFrame.panel.AddDrawable(tp2, Color.red);
            displayFrame.panel.AddDrawable(tp3, Color.red);
            displayFrame.panel.AddDrawable(tp4, Color.red);
            displayFrame.panel.AddDrawable(tp5, Color.red);
            displayFrame.panel.AddDrawable(tp6, Color.red);
            displayFrame.panel.AddDrawable(t1v[0], Color.cyan); //wierzcholek t1_1
            displayFrame.panel.AddDrawable(t1v[1], Color.cyan); //wierzcholek t1_2
            displayFrame.panel.AddDrawable(t1v[2], Color.cyan); //wierzcholek t1_3
            displayFrame.panel.AddDrawable(tp7, Color.green);

            //Boki trojkata
            System.out.print("T1 Lengths: ");

            for(double d : t1.getLengths())
                System.out.print(d + " ");

            System.out.print("\n");

            //Pole trojkata
            System.out.println("Surface area of T1: " + t1.getSurfaceArea());

            //Czy punkt jest zawarty w trojkacie metoda Triangle.contains
            System.out.println("TP7 (green) is contained within t1: " + t1.contains(tp7));
            System.out.println("T1V[0] (bluish) is contained within t1: " + t1.contains(t1v[0]));
            System.out.println("TP1 (red) is contained within t1: " + t1.contains(tp1));

            //Czy punkt jest zawarty w trojkacie metoda Triangle.containsALT
            System.out.println("--------------------------------");
            System.out.println("TP7 (green) is contained within t1: " + t1.contains_ALT(tp7));
            System.out.println("T1V[0] (bluish) is contained within t1: " + t1.contains_ALT(t1v[0]));
            System.out.println("TP1 (red) is contained within t1: " + t1.contains_ALT(tp1));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        //Kat pomiedzy l1 i o3:
        System.out.println("Angle between l1 and o3: " + Line.angleBetweenLines(l1, o3));

        //Kat pomiedzy o1 i o5:
        System.out.println("Angle between o1 and o5: " + Line.angleBetweenLines(o1, o5));


        //Tworzenie wielokata
        //Wierzholki
        Point[] wpv = {
                new Point(480, 540),
                new Point(520, 460),
                new Point(700, 430),
                new Point(680, 580),
                new Point(580, 560),
                new Point(620, 480),
        };

        Polygon w1 = new Polygon(wpv);

        //Rysowanie wielokatu
        displayFrame.panel.AddDrawable(w1);

        //Rysowanie wierzcholkow
        for(Point vert : wpv)
            displayFrame.panel.AddDrawable(vert, Color.blue);

        Point tgt_3 = new Point(660, 490);   //punkt testowy    //y = 480, 460 dla wierzcholka
        displayFrame.panel.AddDrawable(tgt_3, Color.green);
        System.out.println("W1 contains tgt_3: " + w1.contains(tgt_3));
    }
}