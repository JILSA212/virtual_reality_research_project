package codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class random_algorithm {
	protected static int steps;
	GraphGenerator graphGN;
	protected static List<Integer> burning_seq;
	protected static int limit;
	protected static int centers_index;
	
	public random_algorithm(GraphGenerator graphGN)
	{
		steps = 0;
		this.graphGN = graphGN;
		burning_seq = new ArrayList<>();

		Scanner scanObj = new Scanner(System.in);
		System.out.println("Enter the maximum burning range: ");
		limit = scanObj.nextInt();
		
		centers_index = graphGN.get_random_algorithm(limit);
		while(centers_index == 0)
		{
			System.out.print("The randomized algorithm failed, let's try again:" );
			limit = scanObj.nextInt();
			centers_index = graphGN.get_random_algorithm(limit);
		}
		
		burning_seq = graphGN.get_centers();
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
		
		for(int i = 0; i < limit - centers_index + 1; i++)
		{
			for(int j = 0; j < centers_index; j++)
			{
				graphGN.burn_vertices_within_dist(burning_seq.get(j), i-j+centers_index);
			}
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Burning Complete");
	}
}
