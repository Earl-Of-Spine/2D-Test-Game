package ca.cloudspire.greenhills;

import ca.cloudspire.greenhills.graphics.Screen;
import ca.cloudspire.greenhills.graphics.SpriteSheet;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Copyright (c) 2014.
 * Created by Ryan Saunderson on 2014-09-20.
 */

public class GreenHills extends Canvas implements Runnable
{
    private static final long serialVersionID = 1L;

    public static final int WIDTH = 768;
    public static final int HEIGHT = WIDTH/12*9;
    public static final int SCALE = 3;
    public static final String GAME_NAME = "GreenHills";

    private JFrame frame;

    public boolean running = false;

    public int tickCount = 0;

    private Screen screen;

    private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    public GreenHills()
    {
        setMinimumSize(new Dimension(512, 256));
        setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        setPreferredSize(new Dimension(768, WIDTH/12*9));

        frame = new JFrame(GAME_NAME);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void tick()
    {
        tickCount++;

        for (int i=0;i<pixels.length;i++)
        {
            pixels[i] = i+tickCount;        }
    }

    public void render()
    {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null)
        {
            createBufferStrategy(3);
            return;
        }

        screen.render(pixels, 0, WIDTH);

        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0,0, getWidth(), getHeight());

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose();
        bs.show();
    }

    public void run()
    {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D/60D;

        int ticks = 0;
        int frames = 0;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        init();

        while (running)
        {
            long now = System.nanoTime();
            delta += (now - lastTime)/nsPerTick;
            lastTime = now;
            boolean shouldRender = true;

            while (delta >= 1)
            {
                ticks++;
                tick();
                delta -= 1;
                shouldRender = true;
            }

            try
            {
                Thread.sleep(2);

            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            if (shouldRender)
            {
                frames++;
                render();
            }

            if (System.currentTimeMillis()-lastTimer>1000)
            {
                lastTimer+=1000;
                System.out.println(ticks +" Ticks,"+ frames + " Frames");
                frames = 0;
                ticks = 0;
            }
        }
    }

    public void init() {
        screen = new Screen(WIDTH, HEIGHT, new SpriteSheet("/Sprite_Sheet_Basic.png"));
    }

    public synchronized void start()
    {
        running = true;
        new Thread(this).start();
    }

    public synchronized void stop()
    {
        running = false;
    }

    public static void main(String[] args)
    {
        new GreenHills().start();
    }
}
