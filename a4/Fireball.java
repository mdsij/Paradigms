import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;

class Fireball extends Sprite
{
    static BufferedImage fireball_image = null;
    int time = 0;
    Fireball(Model m)
    {
        model = m;
        direction = model.mario.direction; //right == true
        w = 47;
        h = 47;
        setX();
        y = (model.mario.y);
        speed = 14.0;
        vert_velocity = 0;
        if (fireball_image == null)
            fireball_image = loadImage("fireball.png");
    }
    public void update()
    {
        if (toBeRemoved || (time > 75)) model.sprites.remove(this);
        
        prevX = x;
        prevY = y;
        vert_velocity += 7.0;
        y += vert_velocity;
        
        if (y > 400 - h)
        {
            vert_velocity = -40.0;
            y = 400 - h;
        }
        
        if (direction) x += speed;
        else x -= speed;
        
        time++;
        this.collisonCorrection(model.sprites);
    }
    public void draw(Graphics g)
    {
        g.drawImage(fireball_image, x - model.mario.x + model.mario.startX, y, null);
    }
    public void setX()
    {
        if (direction)
            x = model.mario.x + model.mario.w;
        else if (!direction)
            x = model.mario.x - w;
    }
    public void moveFromSprite(Sprite a) //what to do if Fireball collides with Tube
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
           // System.out.println("How did Goomba get there?");
        }
        
    }
}