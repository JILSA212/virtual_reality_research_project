package codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class farthest_first {
	GraphGenerator graphGN;
	protected static List<Integer> burning_seq;
	protected static int centers_index;
	
	public farthest_first(GraphGenerator graphGN)
	{
		this.graphGN = graphGN;
		burning_seq = new ArrayList<>();
		
		int total_vertex = graphGN.get_vertex_number();
		
		Scanner scanObj = new Scanner(System.in);
		
		System.out.println("Enter the index of starting vertex: ");
		int ind = scanObj.nextInt();
		while(ind < 0 || ind > total_vertex)
		{
			System.out.print("Invalid index, please try again: ");
			ind = scanObj.nextInt();
		}
		
		centers_index = graphGN.get_farthest_first_result(ind);
		while(centers_index == 0)
		{
			System.out.println("Something went wrong, please try again");
			System.out.println("Enter the index of starting vertex: ");
			ind = scanObj.nextInt();
			centers_index = graphGN.get_farthest_first_result(ind);
		}
		
		burning_seq = graphGN.get_centers();
		
		System.out.println("Centers Index: " + centers_index);
		System.out.println("Burning Sequence: ");
		for(int i = 0; i < centers_index; i++)
		{
			System.out.print("\t" + burning_seq.get(i));
		}
		
		scanObj.close();
		visualization();
	}
	
	protected void visualization()
	{		
		for(int i = 0; i < centers_index; i++)
		{
			System.out.println("Burning center: " + burning_seq.get(i));

			for(int j = 0; j <= i; j++)
			{
				graphGN.burn_vertices_within_dist(burning_seq.get(j), i-j);
			}
			
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for(int i = 0; i < centers_index; i++)
		{
			graphGN.burn_vertices_within_dist(burning_seq.get(i), centers_index-i);
		}
		System.out.println("Burning Complete");
	}
}
