package codes;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.GeometryArray;
import org.jogamp.java3d.LineArray;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3d;

public abstract class Edges {
	protected TransformGroup objTG = new TransformGroup(); // use 'objTG' to position an object
	protected Vector3d position1;
	protected Vector3d position2;

	protected abstract Node create_Object();               // allow derived classes to create different objects
	
	public TransformGroup position_Object() {	       // retrieve 'objTG' to which 'obj_shape' is attached
		return objTG;   
	}
	
	protected Appearance app;                              // allow each object to define its own appearance
	public void add_Child(TransformGroup nextTG) {
		objTG.addChild(nextTG);                        // attach the next transformGroup to 'objTG'
	}
}

class Edge extends Edges {
	public Edge(Vector3d position1, Vector3d position2) {
		this.position1 = position1;
		this.position2 = position2;
		
		System.out.println("Position set: " + this.position1);
		System.out.println("Position set: " + this.position1);
		
		objTG = new TransformGroup();
		objTG.addChild(create_Object());
	}
	
	public Node create_Object() {
		int numVertices = 2;
		Point3f[] lineVertices = new Point3f[numVertices];
		
		lineVertices[0] = new Point3f((float) position1.x, (float) position1.y, (float) position1.z);
		lineVertices[1] = new Point3f((float) position2.x, (float) position2.y, (float) position2.z);
		
		// Create a LineArray with the vertices
		LineArray lineArray = new LineArray(numVertices, GeometryArray.COORDINATES);
		lineArray.setCoordinates(0, lineVertices);
		Appearance lineAppearance = new Appearance();
		// Create a material with the desired color
		Color3f lineColor = new Color3f(1.0f, 0.0f, 0.0f); // Red color
		Material lineMaterial = new Material(lineColor, new Color3f(0.0f, 0.0f, 0.0f), lineColor, new Color3f(1.0f, 1.0f, 1.0f), 64.0f);
		lineAppearance.setMaterial(lineMaterial);
		Shape3D lineShape = new Shape3D(lineArray, lineAppearance);
		return lineShape;
	}
}