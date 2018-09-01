import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;

class Goomba extends Sprite
{
    static BufferedImage[] goomba_image = null;
    int imageCount = 0;
    int burnTime = 0;
    boolean onFire = false;
    
    
    Goomba(int xx, int yy, Model m)
    {
        direction = false; //true = to the right
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
            
            if (burnTime > 40) toBeRemoved = true;
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
    public void moveFromSprite(Sprite a) //what to do if Goomba collides with another sprite
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
            //System.out.println("How did Goomba get there?");
        }
        
    }
    
    Goomba(Json ob)
    {
        w = 99;
        h = 118;
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        vert_velocity = -12.0;
        speed = 7.0;
        if (goomba_image == null)
        {
            goomba_image = new BufferedImage[2];
            goomba_image[0] = loadImage("goomba.png");
            goomba_image[1] = loadImage("goomba_fire.png");
        }
    }
    Json marshal()
    {
        
        Json ob = Json.newObject();
        ob.add("name", "Goomba");
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }
}