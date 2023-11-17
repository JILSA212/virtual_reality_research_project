package codes;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.Canvas3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.universe.SimpleUniverse;
import org.jogamp.vecmath.Point3d;

public class Main extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	
	public static BranchGroup create_Scene() {
		BranchGroup sceneBG = new BranchGroup();           // create the scene' BranchGroup
		TransformGroup sceneTG = new TransformGroup();     // create the scene's TransformGroup
		
		GraphGenerator graphObj = new GraphGenerator();
		
		sceneTG.addChild(graphObj.generate_graph());
		sceneBG.addChild(sceneTG);
		
		sceneBG.addChild(Commons.add_Lights(Commons.White, 1));	
		return sceneBG;
	}
	
	public Main(BranchGroup sceneBG) {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		
		SimpleUniverse su = new SimpleUniverse(canvas);    // create a SimpleUniverse
//		Commons.define_Viewer(su, new Point3d(4.0d, 0.0d, 1.0d));
		Commons.define_Viewer(su, new Point3d(54.0d, 0.0d, 10.0d));
		
		sceneBG.addChild(Commons.key_Navigation(su));    // allow key navigation
		sceneBG.compile();		                   // optimize the BranchGroup
		su.addBranchGraph(sceneBG);                        // attach the scene to SimpleUniverse

		setLayout(new BorderLayout());
		add("Center", canvas);
		frame.setSize(800, 800);                           // set the size of the JFrame
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		frame = new JFrame("Research Project Demo");            // NOTE: change XY to student's initials
		frame.getContentPane().add(new Main(create_Scene()));  // create an instance of the class
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}