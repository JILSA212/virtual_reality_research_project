package codes;

import java.util.Random;

import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3d;

public class GraphGenerator {
	protected int upper_bound = 30;
	protected int coord_bound = 100;
	protected int vertex_number;
	protected int edge_number;
		
	protected Vertices n_vertices[];
	protected Edges n_edges[];
	
	public GraphGenerator() {
		Random rand = new Random();
		
		vertex_number = rand.nextInt(upper_bound);
		edge_number = rand.nextInt(vertex_number * 3 - 6);
		
		System.out.println("Vertex number: " + vertex_number);
		System.out.println("Edge number: " + edge_number);
	}
	
	public TransformGroup generate_graph() {
		TransformGroup graphTG = new TransformGroup();
		
		graphTG.addChild(generate_vertices());
//		graphTG.addChild(generate_edges());
		
		return graphTG;
	}
	
	public BranchGroup generate_vertices() {
		BranchGroup vertexBG = new BranchGroup();
		n_vertices = new Vertices[vertex_number];
		Random rand = new Random();
		
		for(int i = 0; i < vertex_number; i++) {
			int x = rand.nextInt(coord_bound);
			int y = rand.nextInt(coord_bound);
			int z = rand.nextInt(coord_bound);
			System.out.println("Adding Vertex number " + i + " with x: " + Integer.toString(x) + "\t y: " + Integer.toString(y) + "\t z: " + Integer.toString(z));
			n_vertices[i] = new Vertex(new Vector3d(x, y, z));
			vertexBG.addChild(n_vertices[i].position_Object());
		}
		
		return vertexBG;
	}
	
	public BranchGroup generate_edges() {
		BranchGroup edgeBG = new BranchGroup();
		n_edges = new Edges[edge_number];
		Random rand = new Random();
		
		for(int i = 0; i < edge_number; i++) {
			int n1 = rand.nextInt(vertex_number);
			int n2;
			do {
				n2 = rand.nextInt(vertex_number);
			}while(n1 == n2);
			
			System.out.println("Adding Edge number " + i + " between vertex: " + n1 + "\t and " + n2);
			n_edges[i] = new Edge(n_vertices[n1].get_position(), n_vertices[n2].get_position());
			edgeBG.addChild(n_edges[i].position_Object());
		}
		
		return edgeBG;
	}
}
