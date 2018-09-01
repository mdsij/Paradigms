import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;

class Fireball extends Sprite
{
    static BufferedImage fireball_image = null;
    Fireball(Model m)
    {
    	toBeRemoved = false;
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
    Fireball(Fireball copyme)
    {
        direction = copyme.direction;
        w = copyme.w;
        h = copyme.h;
        x = copyme.x;
        y = copyme.y;
        speed = copyme.speed;
        burnTime = copyme.burnTime;
        vert_velocity = copyme.vert_velocity;
        if (fireball_image == null)
            fireball_image = loadImage("fireball.png");
    }
    Sprite cloneme()
    {
        return new Fireball(this);
    }
    public void update()
    {
        if (burnTime > 54) toBeRemoved = true;
        
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
        
        burnTime++;
        this.collisonCorrection(model.sprites);
    }
    public void draw(Graphics g)
    {
        g.drawImage(fireball_image, x - model.mario.x + model.mario.startX, y, null);
    }
    public boolean isFireball()
    {
        return true;
    }
    //determines direction of fireball based upon Mario's
    public void setX()
    {
        if (direction)
            x = model.mario.x + model.mario.w;
        else if (!direction)
            x = model.mario.x - w;
    }
    
}
