import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;

class Goomba extends Sprite
{
    static BufferedImage[] goomba_image = null;
    int imageCount = 0;
    boolean onFire = false;
    
    Goomba(int xx, int yy, Model m)
    {
        model = m;
        x = xx;
        y = yy;
        w = 99;
        h = 118;
        vert_velocity = -12.0;
        speed = 7.0;
        if (goomba_image == null)
        {
            goomba_image = new BufferedImage[2];
            goomba_image[0] = loadImage("goomba.png");
            goomba_image[1] = loadImage("goomba_fire.png");
        }
    }
    Goomba(Goomba copyme)
    {
        w = copyme.w;
        h = copyme.h;
        x = copyme.x;
        y = copyme.y;
        speed = copyme.speed;
        imageCount = copyme.imageCount;
        onFire = copyme.onFire;
        burnTime = copyme.burnTime;
        vert_velocity = copyme.vert_velocity;
        if (goomba_image == null)
        {
            goomba_image = new BufferedImage[2];
            goomba_image[0] = loadImage("goomba.png");
            goomba_image[1] = loadImage("goomba_fire.png");
        }
        
    }
    Sprite cloneme()
    {
        return new Goomba(this);
    }
    public void update()
    {
        prevX = x;
        prevY = y;

        vert_velocity += 7.0;
        y += vert_velocity;

        if (y > 400 - h)
        {
            vert_velocity = 0.0;
            y = 400 - h;
        }
        
        if (onFire)
        {
            burnTime++;
            if (direction) x += ((1.5) * speed);
            else x -= ((1.5) * speed);
            imageCount = 1;
            
            if (burnTime > 5)
            {
            	model.deadGoombas++;
                toBeRemoved = true;
            }
        }
        else
        {
            if (direction) x += speed;
            else x -= speed;
        }
        
        this.collisonCorrection(model.sprites);
    }
    
    public void draw(Graphics g)
    {
        g.drawImage(goomba_image[imageCount], x - model.mario.x + model.mario.startX, y, null);
    }
    
    public boolean isGoomba()
    {
        return true;
    }
    //Json
    Goomba(Json ob)
    {
        //goomba stats
        w = 99;
        h = 118;
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        vert_velocity = -12.0;
        speed = 7.0;
        if (goomba_image == null) //lazy loading
        {
            goomba_image = new BufferedImage[2];
            goomba_image[0] = loadImage("goomba.png");
            goomba_image[1] = loadImage("goomba_fire.png");
        }
    }
    Json marshal()
    {
        
        Json ob = Json.newObject();
        ob.add("name", "Goomba"); //stores name of sprite
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }
}
