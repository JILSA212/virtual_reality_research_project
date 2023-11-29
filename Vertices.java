package codes;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Vector3d;

public abstract class Vertices {
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object
	protected Vector3d position;
	protected Sphere node;
	protected int index;
	protected String name;
		
	public TransformGroup position_Object() {	       // retrieve 'objTG' to which 'obj_shape' is attached
		return objTG;   
	}
	
	protected Appearance app;                              // allow each object to define its own appearance
	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                        // attach the next transformGroup to 'objTG'
	}

	protected Vector3d get_position() {
		return position;
	}
	
	public void change_color(Color3f color)
	{
		node.setAppearance(Commons.obj_Appearance(color));
	}
}

class Vertex extends Vertices {
	public Vertex(Vector3d position, String index)
	{
		this.position = position;
		Transform3D transform = new Transform3D();
		transform.setTranslation(position);
		this.name = index;
		objTG = new TransformGroup(transform);

		float radius = 0.05f;
		int x_resolution = 30;
		Appearance app = Commons.obj_Appearance(Commons.White);
		node = new Sphere(radius,Primitive.GENERATE_NORMALS | Primitive.ENABLE_APPEARANCE_MODIFY, x_resolution, app);

		node.setName(name);
		objTG.addChild(node);
	}
}