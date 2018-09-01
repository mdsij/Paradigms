import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Graphics;
class Mario extends Sprite
{
    int countJump = 0;
    int startX = 200;
    int imageCount;
    //booleans that controller flags, so that only update() changes mario
    boolean moveLeft;
    boolean moveRight;
    boolean moveJump;
    static BufferedImage[] mario_image = null;
    int numJumps;


    Mario(Model m)
    {
        model = m;
        direction = true;
        w = 61;
        h = 95;
        x = startX;
        y = 200;
        vert_velocity = -12.0;
        speed = 10.0;
        if (mario_image == null)
        {
            mario_image = new BufferedImage[5];
            mario_image[0] = loadImage("mario1.png");
            mario_image[1] = loadImage("mario2.png");
            mario_image[2] = loadImage("mario3.png");
            mario_image[3] = loadImage("mario4.png");
            mario_image[4] = loadImage("mario5.png");
        }
    }
    Mario(Mario copyme)
    {
        w = copyme.w;
        h = copyme.h;
        x = copyme.x;
        y = copyme.y;
        numJumps = copyme.numJumps;
        vert_velocity = copyme.vert_velocity;
        speed = copyme.speed;
        if (mario_image == null)
        {
            mario_image = new BufferedImage[5];
            mario_image[0] = loadImage("mario1.png");
            mario_image[1] = loadImage("mario2.png");
            mario_image[2] = loadImage("mario3.png");
            mario_image[3] = loadImage("mario4.png");
            mario_image[4] = loadImage("mario5.png");
        }
    }
    Sprite cloneme()
    {
        return new Mario(this);
    }
    public void draw(Graphics g)
    {
        g.drawImage(mario_image[imageCount], startX, y, null);
    }
    public void update()
    {
        //set previous positon before changing it
        prevX = x;
        prevY = y;
        
        //controller updates directions moving
        if (moveLeft)
            x -= speed;
        if (moveRight)
            x += speed;
        if (moveJump) jump();
        
        if (prevX < x) direction = true;
        else if (prevX > x) direction = false;
        
        //basic gravity
        vert_velocity += 7.0;
        y += vert_velocity;
        
        //dont let mario fall through the ground
        if (y > 400 - h)
        {
            vert_velocity = 0.0;
            y = 400 - h;
            countJump = 0;
        }   
        
        
        //makes mario stand still if not moving
        if (moveLeft || moveRight)
        {
            imageCount++;
            if (imageCount > 4) imageCount = 0;
        }
        else
        {
            imageCount = 3;
        }
        
        //resets move commands
        moveLeft = false;
        moveRight = false;
        moveJump = false;
        
        this.collisonCorrection(model.sprites);
    }

    public boolean isMario()
    {
        return true;
    }
    public void moveFromSprite(Sprite a) //what to do if Mario collides with another sprite
    {
        if (prevX + w <= a.x)
        {
            x = a.x - w;
        }
        else if ((prevX) >= (a.x + a.w))
        {
            x = a.x + a.w;
        }
        else if (prevY + h <= a.y)
        {
            vert_velocity = 0.0;
            y = a.y - h;
            countJump = 0;
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

    public void jump()
    {
        
        if (countJump < 5)
        {
            vert_velocity -= 65.0;
            
        }
        model.numJumps++;
        countJump = 5;
    }

    
    
    Json marshal()
    {
        Json ob = Json.newObject();
        ob.add("name", "Mario");
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }
    Mario(Json ob)
    {
        direction = true;
        w = 61;
        h = 95;
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        vert_velocity = -12.0;
        speed = 10;
        if (mario_image == null)
        {
            mario_image = new BufferedImage[5];
            mario_image[0] = loadImage("mario1.png");
            mario_image[1] = loadImage("mario2.png");
            mario_image[2] = loadImage("mario3.png");
            mario_image[3] = loadImage("mario4.png");
            mario_image[4] = loadImage("mario5.png");
        }
    }
    
}
