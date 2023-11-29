package codes;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class three_approximation {
	protected static int steps;
	GraphGenerator graphGN;
	protected static List<Integer> burning_seq;
	protected static int g;
	protected static int centers_index;
	
	public three_approximation(GraphGenerator graphGN) {
		steps = 0;
		this.graphGN = graphGN;
		burning_seq = new ArrayList<>();
		
		Scanner scanObj = new Scanner(System.in);
		
		g = 0;
		centers_index = 0;
		System.out.println("Please guess a number: ");
		while(centers_index == 0)
		{
			if(g != 0)
				System.out.print("We can not burn this graph in " + g + " steps, please try some other value: ");
			do
			{
				g = scanObj.nextInt();
				if(g <= 0)
				{
					System.out.println("Guess value needs to be a positive integer");
				}
			}while(g <= 0);
			centers_index = graphGN.check_three_approximation(g);
		}
		
		burning_seq = graphGN.get_centers();
		
		System.out.println("Burning seq: ");
		for(int i = 0; i < centers_index; i++)
		{
			System.out.print("\t" + burning_seq.get(i));
		}
		
		visualization();
	}
	
	protected void visualization()
	{
		System.out.println("Current Step: " + ++steps);
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
		System.out.println("Letting it burn for extra 2g steps");
		for(int i = 0; i < 2*g; i++)
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
