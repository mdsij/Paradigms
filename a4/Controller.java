import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

class Controller implements MouseListener, KeyListener
{
    View view;
    Model model;
    boolean keyLeft;
    boolean keyRight;
    boolean keyS;
    boolean keyL;
    boolean keySpace;
    boolean keyCtrl;


    Controller(Model m)
    {
        model = m;
    }
    void setView(View v)
    {
        view = v;
    }
    void setModel(Model m)
    {
        model = m;
    }	



    public void mousePressed(MouseEvent e)
    {
        //Removed to prevent map editing
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            model.addSprites(e.getX() + model.mario.x - model.mario.startX, e.getY(), true);
        }
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            model.addSprites(e.getX() + model.mario.x - model.mario.startX, e.getY(), false); 
        }
    }
    public void mouseReleased(MouseEvent e) {    }
    public void mouseEntered(MouseEvent e) {    }
    public void mouseExited(MouseEvent e) {    }
    public void mouseClicked(MouseEvent e)
    {
        
    }

    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT: keyRight = true; break;
            case KeyEvent.VK_LEFT: keyLeft = true; break;
            case KeyEvent.VK_S: keyS = true; break;
            case KeyEvent.VK_L: keyL = true; break;
            case KeyEvent.VK_SPACE: keySpace = true; break;
            case KeyEvent.VK_CONTROL: keyCtrl = true; model.addFireball(); keyCtrl = false; break;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_RIGHT: keyRight = false; break;
            case KeyEvent.VK_LEFT: keyLeft = false; break;
            case KeyEvent.VK_S: keyS = false; break;
            case KeyEvent.VK_L: keyL = false; break;
            case KeyEvent.VK_SPACE:
                keySpace = false;
                model.mario.countJump = 4;
                break;
            case KeyEvent.VK_CONTROL: keyCtrl = false;
        }
    }
    public void keyTyped(KeyEvent e)
    {
    }



    void update()
    {
        if(keyS) model.save("map.json");
        if(keyL)
        {
            model.load("map.json");
        }
        if(keySpace) model.mario.moveJump = true;
        if(keyRight)
        {
            model.mario.moveRight = true;
        }
        if(keyLeft)
        {
            model.mario.moveLeft = true;
        }
    }
}