import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;
import java.util.ArrayList;

abstract class Sprite
{
    //current location
    int x;
    int y;
    //size of hitbox
    int w;
    int h;
    //previous location 
    int prevX;
    int prevY;
    //speed of sprite
    double speed;
    double vert_velocity;
    
    boolean direction; //true = to the right
    boolean toBeRemoved = false;
    Model model;
    int burnTime = 0;
    
    public abstract void draw(Graphics g);
    public abstract void update();
    
    //load images from file
    static BufferedImage loadImage(String filename)
    {
        BufferedImage m = null;
        try
        {
            m = ImageIO.read(new File(filename));
        } 
        catch(Exception e)
        {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        return m;
    }
    public void setModel(Model m)
    {
        model = m;
    }
    
    public boolean isTube()
    {
        return false;
    }
    public boolean isMario()
    {
        return false;
    }
    public boolean isGoomba()
    {
        return false;
    }
    public boolean isFireball()
    {
        return false;
    }
    
    abstract Sprite cloneme();
    
    //checks if a click is within a sprite
    public boolean clickSprite (int click_x, int click_y)
    {
        if (click_x > this.x + w) return false;
        if (click_x < this.x) return false;
        if (click_y > this.y + h) return false;
        if (click_y < this.y) return false;
        return true;
    }
    //function to run collison stuff
    public void collisonCorrection(ArrayList<Sprite> sprites)
    {
        int i = sprites.indexOf(this);
        for (int j = 0; j < sprites.size(); j++)
        {
            Sprite tmp = sprites.get(j);
            if (spriteOverlap(tmp) && i != j) //check if s overlaps with any other sprite
            {
                if (isMario()) //if the current sprite is a mario
                {
                    ((Mario)this).moveFromSprite(tmp); //move mario from collison
                }
                else if (isGoomba())
                {
                    ((Goomba)this).moveFromSprite(tmp); //otherwise move goomba from collison
                }
                else if (isFireball())
                {
                    ((Fireball)this).toBeRemoved = true; //if fireball hits another sprite (not a tube)
                    if (tmp.isGoomba())
                    {
                        ((Goomba)tmp).onFire = true; //set the Goomba on fire
                    }
                    else
                    {
                        ((Fireball)this).toBeRemoved = false;
                        ((Fireball)this).moveFromSprite(tmp); //bounce off tubez
                    }
                }
            }
        }
    }
    //what sprite should do if collison occurs
    public void moveFromSprite(Sprite a) 
    {
        if (prevX + w <= a.x)
        {
            x = a.x - w;
            direction = !direction;
        }
        else if ((prevX) >= (a.x + a.w))
        {
            x = a.x + a.w;
            direction = !direction;
        }
        else if (prevY + h <= a.y)
        {
            vert_velocity = 0.0;
            y = a.y - h;
        }
        else if (prevY >= a.y + a.h)
        {
            vert_velocity = 0.0;
            y = a.y + a.h;
        }
        else
        {
            //System.out.println("How did you get there?");
        }
        
    }
    //checks if collison occurs
    public boolean spriteOverlap(Sprite b) //compares sprites' hitboxes
    {
        if ((x + w) <= b.x)
            return false;
        if (x >= (b.x + b.w))
            return false;
        if ((y + h) <= b.y)
            return false;
        if (y >= (b.y + b.h))
            return false;

        return true;
        
    }
   
}
