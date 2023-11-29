package codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3d;

public class GraphGenerator {
	protected int upper_bound = 30;
	protected int coord_bound = 100;
	protected int vertex_number;
	protected int edge_number;
	
	public static int INF = 99999;
	
	protected int distance[][] = new int[vertex_number][vertex_number];
	protected int adjacency[][];
	AllPairShortestPath a = new AllPairShortestPath();
	
	protected Vertices n_vertices[];
	protected Edges n_edges[];
		
	protected List<Integer> burnt;
	protected List<Integer> centers;
	
	
	public GraphGenerator() {
		Random rand = new Random();
		
		vertex_number = rand.nextInt(upper_bound) + 20;
		
		edge_number = rand.nextInt(5) + vertex_number;
		
		adjacency = new int[vertex_number][vertex_number];
		for(int i = 0; i < vertex_number; i++)
		{
			for(int j = 0; j < vertex_number; j++)
			{
				adjacency[i][j] = INF;
			}
		}
				
		burnt = new ArrayList<>();
		centers = new ArrayList<>();
		
		System.out.println("Vertex number: " + vertex_number);
		System.out.println("Edge number: " + edge_number);
	}
	
	public TransformGroup generate_graph() {
		TransformGroup graphTG = new TransformGroup();
		
		graphTG.addChild(generate_vertices());
		graphTG.addChild(generate_edges());
		
		distance = adjacency.clone();
		a.floydWarshall(distance);
		
		return graphTG;
	}
	
	public TransformGroup generate_vertices() {
		TransformGroup vertexTG = new TransformGroup();
		n_vertices = new Vertices[vertex_number];
		Random rand = new Random();
		
		for(int i = 0; i < vertex_number; i++) {
			double x = rand.nextInt(coord_bound) / (double)coord_bound;
			double y = rand.nextInt(coord_bound) / (double)coord_bound;
			double z = rand.nextInt(coord_bound) / (double)coord_bound;
			n_vertices[i] = new Vertex(new Vector3d(x, y, z), Integer.toString(i));
			vertexTG.addChild(n_vertices[i].position_Object());
		}
		
		return vertexTG;
	}
	
	public TransformGroup generate_edges() {
		TransformGroup edgeTG = new TransformGroup();
		n_edges = new Edges[edge_number];
		Random rand = new Random();
		
		for(int i = 0; i < edge_number; i++) {
			int n1;
			if(i < vertex_number)
				n1 = i;
			else
				n1 = rand.nextInt(vertex_number);
			int n2;
			do {
				n2 = rand.nextInt(vertex_number);
			}while(n1 == n2);
			
			n_edges[i] = new Edge(n_vertices[n1].get_position(), n_vertices[n2].get_position());
			
			adjacency[n1][n2] = 1;
			adjacency[n2][n1] = 1;
			
			edgeTG.addChild(n_edges[i].position_Object());
		}
		
		return edgeTG;
	}
	
	public void change_color_of_vertex_at(int index)
	{
		try {
			n_vertices[index].change_color(Commons.Red);
		} catch(Exception e) {
			System.out.println("Exception : " + e.toString());
		}
	}
	
	public void burn_vertices_within_dist(int center, int dist)
	{
		for(int i = 0; i < vertex_number; i++)
		{
			if(distance[center][i] <= dist)
			{
				change_color_of_vertex_at(i);
			}
		}
	}
	
	public int get_vertex_number()
	{
		return vertex_number;
	}
	
	public int check_three_approximation(int g)
	{
		if(g == 0)
			return 0;
				
		for(int i = 0; i < vertex_number; i++)
		{
			if(centers.size() == 0)
			{
				centers.add(i);
				
				if(centers.size() >= g)
				{
					return 0;
				}
			}
			else
			{
				int min_dist = 99999;
				for(int j = 0; j < centers.size(); j++)
				{
					if(distance[i][centers.get(j)] < min_dist)
					{
						min_dist = distance[i][centers.get(j)];
					}
				}
				if(min_dist > 2 * g - 2)
				{
					centers.add(i);
					
					if(centers.size() >= g)
					{
						return 0;
					}
				}
			}
		}
		return centers.size();
	}
	
	public List<Integer> get_centers()
	{
		return centers;
	}
	
	public int get_farthest_first_result(int ind)
	{		
		centers = new ArrayList<>();
		burnt = new ArrayList<>();
				
		while(burnt.size() <= vertex_number)
		{
			if(centers.size() == 0)
			{
				centers.add(ind);
				burnt.add(ind);
			}
			else
			{
				int max_dist = -1;
				int furthest_vertex = ind;
				int min_dist = 99999;
				for(int i = 0; i < vertex_number; i++)
				{
					if(!burnt.contains(i))
					{
						min_dist = 99999;
						for(int j = 0; j < centers.size(); j++)
						{
							if(i != centers.get(j))
							{
								int dist = distance[i][centers.get(j)];
								if(dist < min_dist)
								{
									min_dist = dist;
								}
							}
						}
						if(min_dist > max_dist)
						{
							max_dist = min_dist;
							furthest_vertex = i;
						}
					}
				}
				
				if(!centers.contains(furthest_vertex))
				{
					centers.add(furthest_vertex);
					burnt.add(furthest_vertex);
					
					if(burnt.size() >= vertex_number)
					{
						return centers.size();
					}
					
				}
				
				for(int i = 0; i < centers.size(); i++)
				{
					for(int j = 0; j < vertex_number; j++)
					{
						if(distance[centers.get(i)][j] <= centers.size()-i-1)
						{
							if(!burnt.contains(j))
							{
								burnt.add(j);
								if(burnt.size() >= vertex_number)
								{
									return centers.size();
								}
							}
						}
					}
				}
				
			}
		}
		return centers.size();
	}
	
	public int get_random_algorithm(int limit)
	{
		centers = new ArrayList<>();
		burnt = new ArrayList<>();
		
		Random rand = new Random();
		
		for(int i = limit; i > 0; i--)
		{
			int trial;
			do
			{
				trial = rand.nextInt(vertex_number);
			}while(burnt.contains(trial));
			centers.add(trial);
			burnt.add(trial);
			
			if(burnt.size() >= vertex_number)
			{
				return centers.size();
			}
			
			for(int j = 0; j < vertex_number; j++)
			{
				if(distance[trial][j] <= i-1)
				{
					if(!burnt.contains(j))
					{
						burnt.add(j);
						if(burnt.size() >= vertex_number)
						{
							return centers.size();
						}
					}
				}
			}
		}
		return 0;
	}
	
	public int get_distance_between(int i, int j)
	{
		return distance[i][j];
	}
}
