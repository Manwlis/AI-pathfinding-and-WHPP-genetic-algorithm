/**
			INTELLIGENCE LAB
	course		: 	COMP 417 - Artificial Intelligence
	authors		:	A. Vogiatzis, N. Trigkas
	excercise	:	1st Programming
	term 		: 	Spring 2019-2020
	date 		:   March 2020
*/
import java.util.Random;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.awt.Canvas;

import java.util.Scanner;


class GridGenerator{

	public static void VisualizeGrid(String frame_name, int N, int M, int [] walls, int [] grass, int start_idx, int terminal_idx ){
		JFrame frame = new JFrame(frame_name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Canvas canvas = new Drawing(N,M,walls,grass,start_idx,terminal_idx);
		canvas.setSize(M*30,N*30);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public static void VisualizeGrid(String frame_name, int N, int M, int [] walls, int [] grass, int [] steps ,int start_idx, int terminal_idx ){
		JFrame frame = new JFrame(frame_name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Canvas canvas = new Drawing(N,M,walls,grass, steps, start_idx,terminal_idx);
		canvas.setSize(M*30,N*30);
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		String frame = "Random World";
		Grid mygrid;
		if (args.length<1)
			mygrid = new Grid();
		else if (args[0].equals("-i")){
			mygrid = new Grid(args[1]);
			frame = args[1].split("/")[1];
		}else if (args[0].equals("-d")){
			mygrid = new Grid(Integer.parseInt(args[1]),Integer.parseInt(args[2]));
		}else{
			mygrid = new Grid("world_examples/default.world");
			frame = "default.world";
		}

		int choice = ChooseAlgorithm();

		int N = mygrid.getNumOfRows();
		int M = mygrid.getNumOfColumns();
		
		VisualizeGrid(frame,N,M,mygrid.getWalls(),mygrid.getGrass(),mygrid.getStartidx(),mygrid.getTerminalidx());

		int [] steps;
		// epilish problhmatos
		if (choice == 4)
			steps = CalculateStepsOnline( choice , mygrid );
		else
			steps = CalculateStepsOffline( choice , mygrid );
		
		if( steps == null )
		{
			System.out.println("No solution found.");
			return;
		}

		// dinw xrono gia to visualize
		try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		
		VisualizeGrid(frame,N,M,mygrid.getWalls(),mygrid.getGrass(), steps ,mygrid.getStartidx(),mygrid.getTerminalidx());
	}	


	// diavasma apo console kai elenxos epiloghs algori8mou
	private static int ChooseAlgorithm()
	{
		Scanner scanner = new Scanner(System.in);

		String console_input = "";
		while(!console_input.matches("1|2|3|4"))
		{
			System.out.print("\nEpilogh algori8mou:\n1.BFS\n2.DFS\n3.A*\n4.LRTA*\n");
			console_input = scanner.nextLine();
		}
		scanner.close();

		return Integer.parseInt(console_input);
	}


	private static int[] CalculateStepsOffline( int algorithm , Grid myGrid )
	{
		OfflineState root = new OfflineState( myGrid );
		OfflineState goal_state = null;

		// an h arxh kai to telos briskontai sthn idia 8esh den exei nohma na treksoun oi algori8moi
		if ( root.IsGoalState() )
		{
			System.out.println("Start is goal!");
			return null;
		}

		switch ( algorithm )
		{
			case 1:
				System.out.println("BFS");
				goal_state = root.BFS();
				break;
			case 2:
				System.out.println("DFS");
				goal_state = root.DFS();
				break;
			case 3:
				System.out.println("A*");
				goal_state = root.Astar();
				break;
		}

		System.out.println( "sunolo katastasewn: " + root.getNum_states() );
		
		if( goal_state == null )
			return null;
			
		System.out.println( "kostos monopatiou : " + goal_state.getAccumulated_cost() );
		return goal_state.ExtractSolution();
	}

	private static int[] CalculateStepsOnline( int algorithm , Grid myGrid )
	{
		OnlineState root = new OnlineState( myGrid );
		OnlineState goal_state = null;

		// an h arxh kai to telos briskontai sthn idia 8esh den exei nohma na treksoun oi algori8moi
		if ( root.IsGoalState() )
		{
			System.out.println("Start is goal!");
			return null;
		}

		System.out.println("LRTA*");
		goal_state = root.LRTAstar();

		int cost = goal_state.ExtractPath();

		System.out.println( "kostos monopatiou : " + cost );
		System.out.println( "Ari8mos kinhsewn  : " + goal_state.getNumMoves() );
		return goal_state.GetPositionsHistory().stream().mapToInt( i -> i ).toArray();
	}
}