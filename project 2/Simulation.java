import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulation
{
    //Attributes
    //Environment
    private boolean isRunning;
    private double simDurationTime;
    private boolean isPaused;
    private double simSpeedMultiplier;
    private double speedChange;

    //DeltaTime
    long lastTime;
    long now;
    double deltaTime;

    //Window
    private String bgImage;
    private int WIDTH, HEIGHT;
    private boolean bufferImage;
    private DisplayFrame window;

    //files
    String directory_path;
    String ship_file;
    String missile_file;

    //case data
    ArrayList<Point> ship_shape;
    ArrayList<Point> ship_verticies;
    Polygon ship_pol;
    double ship_pos_x, ship_pos_y;
    double ship_speed_x, ship_speed_y;
    ArrayList<Missile> missiles;
    ArrayList<Missile> hit_markers;

    //Initialization methods
    //Constructor / main method
    Simulation()
    {
        //environment
        initVars();
        initWindow();
        initListeners();
        initDeltaTime();

        //data
        try{initData();}
        catch (FileNotFoundException e) { System.out.println(e.getMessage()); }

        //sim case
        initShip();

        //rendering
        initShipRender();
        initMissilesRender();
    }

    //Initialization: Environment
    private void initVars()
    {
        isRunning = true;
        missiles = new ArrayList<>();
        hit_markers = new ArrayList<>();
        simDurationTime = 0;
        isPaused = true;
        simSpeedMultiplier = 1;
        speedChange = 0.6;
        directory_path = "input/starfleet/";
        ship_file = "space_craft1.txt";
        missile_file = "missiles4.txt";
    }

    private void initWindow()
    {
        WIDTH = 1200;
        HEIGHT = 1000;
        bufferImage = true;
        window = new DisplayFrame(bufferImage, WIDTH, HEIGHT);

        bgImage = directory_path + "background1.png";
        window.panel.setBackgroundImage(bgImage);
    }

    private void initListeners()
    {
        //space key listener / pause
        window.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                switch(e.getKeyCode())
                {
                    case KeyEvent.VK_SPACE:
                        //pause
                        isPaused = !isPaused;
                        break;

                    case KeyEvent.VK_Q:
                        //change the direction
                        simSpeedMultiplier = -simSpeedMultiplier;
                        break;

                    case KeyEvent.VK_UP:
                        //increase speed
                        simSpeedMultiplier /= speedChange;
                        break;
                    case KeyEvent.VK_DOWN:
                        //decrease speed
                        simSpeedMultiplier *= speedChange;
                        break;
                }
            }
        });
    }

    private void initDeltaTime()
    {
        lastTime = System.nanoTime();
    }

    //Initialization: sim case
    private void initData() throws FileNotFoundException
    {
        //get shape and vertices of the ship
        ship_shape = Point.loadFromFile(directory_path + "craft1_ksztalt.txt");
        if(ship_shape == null)
            throw new FileNotFoundException("Missing file: craft1_ksztalt.txt");

        //load ship speed and starting position
        File input = new File(directory_path + ship_file);
        if(input == null)
            throw new FileNotFoundException("Missing file: " + ship_file);

        Scanner scanner = new Scanner(input);

        //Ship starting ositions
        ship_pos_x = scanner.nextDouble();
        ship_pos_y = scanner.nextDouble();

        //Ship speed
        ship_speed_x = scanner.nextDouble();
        ship_speed_y = scanner.nextDouble();

        //laod missiles
        input = new File(directory_path + missile_file);
        if(input == null)
            throw new FileNotFoundException("Missing file: " + missile_file);

        scanner = new Scanner(input);

        while(scanner.hasNextLine())
        {
            //time of spotting
            double reveal = scanner.nextDouble();

            //starting pos
            double pos_x = scanner.nextDouble();
            double pos_y = scanner.nextDouble();

            //speed
            double speed_x = scanner.nextDouble();
            double speed_y = scanner.nextDouble();

            //advance the line
            if (scanner.hasNextLine())
                scanner.nextLine();

            //make missile instance
            missiles.add(new Missile(reveal, pos_x, pos_y, speed_x, speed_y));
        }
    }

    private void initShip()
    {
        //create verticies
        ship_verticies = Polygon.jarvis_march_2(ship_shape);

        //move ship to starting pos
        for(Point p : ship_shape)
            p.translate((int)ship_pos_x, (int)ship_pos_y);

        for(Point p : ship_verticies)
            p.translate((int)ship_pos_x, (int)ship_pos_y);

        //Make a polygon out of verticies
        Point[] verts = new Point[ship_verticies.size()];
        for(int i = 0; i < ship_verticies.size(); i++)
            verts[i] = ship_verticies.get(i);

        ship_pol = new Polygon(verts);
    }

    private void initMissilesRender()
    {
        for(Missile m : missiles)
            window.panel.AddDrawable(m, Color.red);
    }

    //Initialization: rendering
    private void initShipRender()
    {
        //Add interior
        for(Point p : ship_shape)
            window.panel.AddDrawable(p, new Color(0,30,255));

        //Add vertice lines
        //window.panel.AddDrawable(ship_pol, new Color(102, 255, 0));

        //Add verticies
        //for(Point p : ship_verticies)
            //window.panel.AddDrawable(p, new Color(204, 102, 0));
    }


    //Update methods
    //main method
    public void update()
    {
        //environment
        updateDeltaTime();

        //simulation
        if(!isPaused)
        {
            //Environment
            updateDurationTime();

            //Movement
            updateShip();
            updateMissileSpot();
            updateMissilePosition();

            //Collision
            updateCollision();
            updateHitmarkers();
        }
    }

    //Update: Environment
    private void updateDeltaTime()
    {
        //deltaTime
        now = System.nanoTime();
        deltaTime = (now - lastTime) / 1_000_000_000.0;
        lastTime = now;

        //System.out.println("FPS: " + 1 / deltaTime);
    }

    private void updateDurationTime()
    {
        simDurationTime += deltaTime * simSpeedMultiplier;
    }

    private void updateShip()
    {
        //calculate new positions
        double new_pos_x = (ship_speed_x * deltaTime * simSpeedMultiplier) + ship_pos_x;
        double new_pos_y = (ship_speed_y * deltaTime * simSpeedMultiplier) + ship_pos_y;

        //offsets between new and current integer positions
        int offset_x = (int)new_pos_x - (int)ship_pos_x;
        int offset_y = (int)new_pos_y - (int)ship_pos_y;

        //apply the offset (verticies included in ship_shape)
        for(Point p: ship_shape)
            p.translate(offset_x, offset_y);

        ship_pol.translate(offset_x, offset_y);

        //update the actual (double) position variables
        ship_pos_x = new_pos_x;
        ship_pos_y = new_pos_y;

        //debug
        //System.out.println("X: " + offset_x + " Y: " + offset_y);
    }

    private void updateMissileSpot()
    {
        for(Missile m : missiles)
        {
            //set if missile should be spotted
            m.setFired(m.Reveal_time() < simDurationTime);

            m.setEnabled(simDurationTime < m.HitTime());
        }
    }

    private void updateMissilePosition()
    {
        for(Missile m: missiles)
        {
            if(!m.IsFired())
                continue;

            double offset_X = m.Speed_x() * deltaTime * simSpeedMultiplier;
            double offset_Y = m.Speed_y() * deltaTime * simSpeedMultiplier;

            m.translate(offset_X, offset_Y);
        }
    }

    private void updateCollision()
    {
        for(Missile m : missiles)
        {
            if(!m.IsFired() || !m.Enabled())
                continue;

            //cast to a point, then check collision
            Point p = new Point((int) m.Pos_x(), (int) m.Pos_y());

            if(ship_pol.contains(p))
            {
                System.out.println("ALERT! HULL BREACH AT: " + String.format("%.2f", simDurationTime) + "!");
                m.setHit_time(simDurationTime);
                m.setEnabled(false);

                //create a hit marker but only once
                if(!m.Marked())
                {
                    m.setMarked(true);

                    Missile hit_marker = new Missile(0, m.Pos_x(), m.Pos_y(),0, 0);
                    hit_marker.setHit_time(simDurationTime);
                    hit_marker.setFired(true);
                    window.panel.AddDrawable(hit_marker, Color.green);
                    hit_markers.add(hit_marker);
                }
            }
        }
    }

    private void updateHitmarkers()
    {
        for(Missile m : hit_markers)
            m.setEnabled(simDurationTime > m.HitTime());
    }

    //Rendering
    void render()
    {
        window.panel.refreshBufferedImage();
    }

    //Getters
    public boolean isRunning() { return this.isRunning; }
}