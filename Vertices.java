package codes;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;
import org.jogamp.vecmath.Vector3d;

public abstract class Vertices {
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object
	protected Vector3d position;

	protected abstract Node create_Object();               // allow derived classes to create different objects
	
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
}

class Vertex extends Vertices {
	public Vertex(Vector3d position)
	{
		Transform3D transform = new Transform3D();
		transform.setTranslation(position);
		objTG = new TransformGroup(transform);
		objTG.addChild(create_Object());
	}
	
	protected Node create_Object() {
		float radius = 0.3f;
		int x_resolution = 30;
		Appearance app = Commons.obj_Appearance(Commons.White);
		return new Sphere(radius,Primitive.GENERATE_NORMALS, x_resolution, app);
	}
}