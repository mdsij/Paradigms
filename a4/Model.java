
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
	Model()
	{
            //create redundant mario pointer, sprites ArrayList, and add mario to sprites list
            mario = new Mario(this);
            sprites = new ArrayList<Sprite>();
            sprites.add(mario);
		
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
            Iterator<Sprite> itr = sprites.iterator();
            while (itr.hasNext())
            {
                Sprite s = itr.next();
                if (s.toBeRemoved) itr.remove();
            }
            
            //sort sprites
            Collections.sort(sprites, new SpriteComparator()); //I got this collection idea and support from https://stackoverflow.com/questions/2839137/how-to-use-comparator-in-java-to-sort
            
	}
	
	public void addSprites(int x, int y, boolean leftClick)
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
            //Stops any sprites from overlapping
            Sprite s;
            if (leftClick) //left click is for a tube
            {
                s = new Tube(x,y,this);
            }
            else
            {
                s = new Goomba(x,y,this);
            }
            for (int i = 0; i < sprites.size(); i++)
            {
                Sprite tmp = sprites.get(i);
                if (s.spriteOverlap(tmp)) 
                    return;
            }
            //if no overlap, then add sprite to ArrayList
            sprites.add(s);
            
	}
	public void addFireball()
	{
            Sprite s = new Fireball(this);
            for (int i = 0; i < sprites.size(); i++)
            {
                Sprite tmp = sprites.get(i);
                if (s.spriteOverlap(tmp))
                    return;
            }
            sprites.add(s);
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
