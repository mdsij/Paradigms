import javax.swing.JFrame;

public class Game extends JFrame
{
	Model model;
	Controller controller;
	View view;
	public Game()
	{
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		controller.setView(view);
		this.addKeyListener(controller); //controller controls key inputs
		view.addMouseListener(controller); //controller controls mouse inputs
		this.setTitle("Kool Tubez");
		this.setSize(500, 500);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void run()
	{
		while(true)
		{
			controller.update();
			model.update();
			view.repaint(); // Indirectly calls View.paintComponent

			// Go to sleep for 40 miliseconds
			try
			{
				Thread.sleep(40);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}
}
