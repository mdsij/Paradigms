import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;

class View extends JPanel
{
    JButton b1;
    Model model;
    ArrayList<Backgrounds> tiles;

    View(Controller c, Model m)
    {
        tiles = new ArrayList<Backgrounds>();
        model = m;
        
        int dist = 100;
        for (int i = 0; i < 3; i++)
        {
            Cloud cl = new Cloud(dist);
            tiles.add(cl);
            dist += 200;
        }
    }

    public void setModel(Model m)
    {
        model = m;
    }

    public void paintComponent(Graphics g)
    {
        //draw sky
        g.setColor(new Color(128, 255, 255));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        

        //draw background tiles
        for (int i = 0; i < tiles.size(); i++)
        {
            tiles.get(i).draw(g);
        }


        //draw sprites
        for (int i = findFirstSpriteOnScreen(); i < model.sprites.size(); i++)
        {
            Sprite s = model.sprites.get(i);
            s.draw(g);
        }
        
        //draw Ground
        g.setColor(Color.green);
        g.fillRect(0, 400, 2000, 400);

        //fix frames
        Toolkit.getDefaultToolkit().sync();
    }
	
    int findFirstSpriteOnScreen()
    {
        int start = 0;
        int end = model.sprites.size();
        while(true)
        {
            int mid = (start + end) / 2;
            if(mid == start)
                return start;
            Sprite s = model.sprites.get(mid);
            if(s.x - model.mario.x + model.mario.startX < -100)
                start = mid;
            else
                end = mid;
        }
    }
	
	
	
}
