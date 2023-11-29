package codes;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.BoundingSphere;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.behaviors.vp.OrbitBehavior;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.java3d.utils.universe.ViewingPlatform;
import org.jogamp.vecmath.Point3d;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;


public class Main extends JPanel implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	private Canvas3D canvas;
	private static PickTool pickTool;
	private static GraphGenerator graphObj;
	
	private static int click;
	private static List<String> burnt;
	private static List<Integer> centers;
	private static int vertex_number;
	private static int inputOpt;
	
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
		
		sceneTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		graphObj = new GraphGenerator();
		
		sceneTG.addChild(Commons.add_light_at_position(Commons.White, new Point3f(-3.0f, 1.0f, -3.0f)));
		sceneTG.addChild(Commons.add_light_at_position(Commons.White, new Point3f(3.0f, -1.0f, 3.0f)));
		
		sceneTG.addChild(graphObj.generate_graph());
		
		vertex_number = graphObj.get_vertex_number();
		
		sceneBG.addChild(sceneTG);
		
		pickTool = new PickTool( sceneBG );                // allow picking of objects in 'sceneBG'
		pickTool.setMode(PickTool.GEOMETRY);                 // pick by bounding volume
		
		return sceneBG;
	}
	
	public Main()
	{
		frame = new JFrame("Research Project Demo");            // NOTE: change XY to student's initials		
		frame.getContentPane().add(new Main(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		click = 0;
		burnt = new ArrayList<>();
		centers = new ArrayList<>();
	}
		
	public Main(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		
		
		if(inputOpt == 4)
		{			
			canvas.addMouseListener(this);
		}
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
		Commons.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
		
		sceneBG.addChild(Commons.key_Navigation(su));    // allow key navigation
		sceneBG.compile();		                   // optimize the BranchGroup

		addMouseBehaviors(su, canvas);
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse
		
		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}
	
	private void addMouseBehaviors(SimpleUniverse universe, Canvas3D canvas) {
        ViewingPlatform viewingPlatform = universe.getViewingPlatform();        

        // Allow rotation with the mouse
        OrbitBehavior orbit = new OrbitBehavior(canvas, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere());
        viewingPlatform.setViewPlatformBehavior(orbit);
    }

	
	public static void main(String[] args)
	{
		Scanner scanObj = new Scanner(System.in);
		inputOpt = 0;
		System.out.println("------------------------------------------------------");
		System.out.println("      Welcome to the Graph Burning Visualization      ");
		System.out.println("------------------------------------------------------");
		System.out.println("We offer following options:");
		System.out.println("1. 3-Approximation");
		System.out.println("2. Furthest First");
		System.out.println("3. Randomized");
		System.out.println("4. Custom");
		System.out.println("Please select an option: ");
		inputOpt = scanObj.nextInt();
		while(inputOpt <= 0 || inputOpt > 4)
		{
			System.out.print("Invalid input, please try again: ");
			inputOpt = scanObj.nextInt();
		}
		
		frame = new JFrame("Research Project Demo");            // NOTE: change XY to student's initials
		frame.getContentPane().add(new Main(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
		click = 0;
		burnt = new ArrayList<>();
		centers = new ArrayList<>();
		
		if(inputOpt == 1)
		{
			new three_approximation(graphObj);
		}
		else if(inputOpt == 2)
		{
			new farthest_first(graphObj);
		}
		else if(inputOpt == 3)
		{
			new random_algorithm(graphObj);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		int x = event.getX(); int y = event.getY();        // mouse coordinates
		Point3d point3d = new Point3d(), center = new Point3d();
		canvas.getPixelLocationInImagePlate(x, y, point3d);// obtain AWT pixel in ImagePlate coordinates
		canvas.getCenterEyeInImagePlate(center);           // obtain eye's position in IP coordinates
		
		Transform3D transform3D = new Transform3D();       // matrix to relate ImagePlate coordinates~
		canvas.getImagePlateToVworld(transform3D);         // to Virtual World coordinates
		transform3D.transform(point3d);                    // transform 'point3d' with 'transform3D'
		transform3D.transform(center);                     // transform 'center' with 'transform3D'

		Vector3d mouseVec = new Vector3d();
		mouseVec.sub(point3d, center);
		mouseVec.normalize();
		pickTool.setShapeRay(point3d, mouseVec);           // send a PickRay for intersection

		if (pickTool.pickClosest() != null) {
			PickResult pickResult = pickTool.pickClosest();// obtain the closest hit
			Sphere vertexSphere = (Sphere) pickResult.getNode(PickResult.PRIMITIVE);
			if(vertexSphere != null)
			{
				if(!burnt.contains(vertexSphere.getName()))
				{
					graphObj.change_color_of_vertex_at(Integer.parseInt(vertexSphere.getName()));
					burnt.add(vertexSphere.getName());
					
					if(burnt.size() >= vertex_number)
					{
						System.out.println("You burnt the graph in " + click + " steps");
					}
					
					centers.add(Integer.parseInt(vertexSphere.getName()));
					click++;
				}
				for(int j = 0; j < centers.size(); j++)
				{
					for(int i = 0; i < vertex_number; i++)
					{
						if(graphObj.get_distance_between(i, centers.get(j)) <= click-j)
						{
							if(!burnt.contains(Integer.toString(i)))
							{
								burnt.add(Integer.toString(i));
								graphObj.change_color_of_vertex_at(i);
								
								if(burnt.size() >= vertex_number)
								{
									System.out.println("You burnt the graph in " + click + " steps");
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}