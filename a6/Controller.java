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
    Model copy;
    boolean keyLeft;
    boolean keyRight;
    boolean keyS;
    boolean keyL;
    boolean keySpace;
    boolean keyCtrl;
    boolean keyG;


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
            model.addSprites(e.getX() + model.mario.x - model.mario.startX, e.getY(), 't');
        }
        if (e.getButton() == MouseEvent.BUTTON3)
        {
            model.addSprites(e.getX() + model.mario.x - model.mario.startX, e.getY(), 'g'); 
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
            //run and shoot fireball
            case KeyEvent.VK_CONTROL: keyCtrl = true; break;
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
                model.mario.countJump = 5;
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
        if (keyRight)
        {
        	copy = new Model(model);
        	System.out.println("break");
        }
        double score_run = model.evaluateAction(model.controllerAction.RUN, 0);
        double score_runjump = model.evaluateAction(model.controllerAction.RUNJUMP, 0);
        double score_runshoot = model.evaluateAction(model.controllerAction.RUNSHOOT, 0);

        if (score_runshoot > score_runjump && score_runshoot > score_run)
            model.doAction(model.controllerAction.RUNSHOOT);
        else if (score_runjump > score_run)
            model.doAction(model.controllerAction.RUNJUMP);
        else
            model.doAction(model.controllerAction.RUN);
    }
}
