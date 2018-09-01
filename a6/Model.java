import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.Iterator;
class SpriteComparator implements Comparator<Sprite>
{
	public int compare(Sprite a, Sprite b)
	{
		if(a.x < b.x)
			return -1;
		else if(a.x > b.x)
			return 1;
		else
			return 0;
	}
}

class Model
{
	Mario mario;
	ArrayList<Sprite> sprites;
	public enum Action { RUN, RUNJUMP, RUNSHOOT };
	Action controllerAction; //lets the controller use doAction to dictate movements
	int deadGoombas = 0;
	int numFireballs;
	int numJumps;
	Model()
	{
            //create redundant mario pointer, sprites ArrayList, and add mario to sprites list
            mario = new Mario(this);
            sprites = new ArrayList<Sprite>();
            sprites.add(mario);
		
	}
	//makes deep copy
	Model(Model copyme)
	{
        sprites = new ArrayList<Sprite>();
        deadGoombas = copyme.deadGoombas;
        numFireballs = copyme.numFireballs;
        numJumps = copyme.numJumps;
        for (int i = 0; i < copyme.sprites.size(); i++)
        {
            Sprite s = copyme.sprites.get(i).cloneme(); //makes a deep copy of the current sprite
            if (s.isMario()) { mario = (Mario) s; }
            s.setModel(this);
            sprites.add(s);
            
        }
	}
	public void update()
	{
            //loop though and update every sprite
            int i = 0;
            while(i < sprites.size())
            {
                Sprite s = sprites.get(i);
                s.update();
                i++;
            }
            //loop through and remove flagged sprites
            Iterator<Sprite> itr = sprites.iterator();
            while (itr.hasNext())
            {
                Sprite s = itr.next();
                if (s.toBeRemoved)
                {
                    itr.remove();
                }
            }
            
            //sort sprites
            Collections.sort(sprites, new SpriteComparator()); //I got this collection idea and support from https://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
            
	}
	
	public void addSprites(int x, int y, char type)
	{
        if (type != 'f') //x,y for fireballs mean nothing bc there is not a click
        {
            //checks if you clicked on a sprite (to delete it)
            for (int i = 0; i < sprites.size(); i++)
            {
                Sprite tmp = sprites.get(i);
                if (tmp.clickSprite(x,y) && !(tmp instanceof Mario)) //dont delete mario
                {
                    sprites.remove(i);
                    return;
                }
            }
        }
        //Stops any sprites from overlapping
        Sprite s;
        if (type == 't')
        {
            s = new Tube(x,y,this);
        }
        else if (type == 'g')
        {
            s = new Goomba(x,y,this);
        }
        else if (type == 'f')
        {
            s = new Fireball(this);
        }
        else { System.out.println("Unable to add Sprite"); return;}
        //check for overlap
        for (int i = 0; i < sprites.size(); i++)
        {
            Sprite tmp = sprites.get(i);
            if (s.spriteOverlap(tmp)) 
                return;
        }
        //if no overlap, then add sprite to ArrayList
        sprites.add(s);
        if (s.isFireball()) {numFireballs++;}
	}

	
	
	double evaluateAction(Action a, int depth)
	{
        int d = 35; //how many layers the AI tests
        int k = 6; //how many times the AI just simulates running before trying all actions
        
        //evaluate the state
        if (depth >= d)
        {
            return (mario.x - mario.startX) + (200 * deadGoombas) - numFireballs - numJumps;
        }
        
        //simulate the action
        Model copy = new Model(this); //makes a deep copy to do simulations
        copy.doAction(a); //does the action in the simulation
        copy.update(); //advance the simulation
        
        //Go "deeper"
        if (depth % k != 0) { return copy.evaluateAction(a.RUN, depth + 1); }
        else
        {
            //determine the most favorable action in each branch of the simulation
            double best = copy.evaluateAction(a.RUN, depth + 1);
            best = Math.max(best, copy.evaluateAction(a.RUNJUMP, depth + 1));
            best = Math.max(best, copy.evaluateAction(a.RUNSHOOT, depth + 1));
            return best;
        }
	}
	
	void doAction(Action a) //sets the action to be preformed when the model updates
	{
        
        switch (a)
        {
            case RUN:
                mario.moveRight = true;
                break;
            case RUNJUMP:
                mario.moveRight = true;
                mario.moveJump = true;
                break;
            case RUNSHOOT:
                mario.moveRight = true;
                this.addSprites(0, 0, 'f');
                break;
            default:
                System.out.println("Not a valid action");
                break;
        }
	}
	
	
	
	
	
	
	
	//Json stuff
	Json marshal()
	{
            Json ob = Json.newObject();
            Json tmpList = Json.newList();
            ob.add("sprites", tmpList);
            for (int i = 0; i < sprites.size(); i++)
            {
                if (sprites.get(i) instanceof Tube) //call Tube marshal if tube
                    tmpList.add( ((Tube)sprites.get(i)).marshal());
                else if(sprites.get(i) instanceof Mario) //mario marshal for mario
                    tmpList.add( ((Mario)sprites.get(i)).marshal());
                else if (sprites.get(i) instanceof Goomba)
                    tmpList.add( ((Goomba)sprites.get(i)).marshal());
            }
            return ob;
	}
	public void save(String filename) // called by controller
	{
		marshal().save(filename);
		System.out.println("The program has been saved.");
	}
	void unmarshal(Json ob)
	{
            sprites = new ArrayList<Sprite>();
            Json tmpList = ob.get("sprites");
            for (int i = 0; i < tmpList.size(); i++)
            {
                if (tmpList.get(i).getString("name").equals("Tube")) //if tube obj call tube constructor
                    sprites.add(new Tube(tmpList.get(i)));
                else if (tmpList.get(i).getString("name").equals("Mario")) //if mario obj call mario constructor
                    sprites.add(new Mario(tmpList.get(i)));
                else if (tmpList.get(i).getString("name").equals("Goomba"))
                    sprites.add(new Goomba(tmpList.get(i)));
                
            }
            for (int i = 0; i < sprites.size(); i++)
            {
                Sprite s = sprites.get(i);
                if (!(s instanceof Mario)) //if not Mario set model for drawing
                {
                    s.setModel(this);
                }
                else if (s instanceof Mario){
                    mario = ((Mario) s); //if mario update model's redundant pointer
                    s.setModel(this);
                }
            }
	}
	public void load(String filename) //called by controller
	{
		Json obj = Json.load(filename);
		unmarshal(obj);
		System.out.println("The program has been loaded.");
	}
	
	
	
	
	
}
