package astar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class AstarMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File file=new File("G:/Astar/ProjectAStar/src/astar/output.txt");
		File file1=new File("G:/Astar/ProjectAStar/src/astar/backwardoutput.txt");
		File file2=new File("G:/Astar/ProjectAStar/src/astar/AdaptiveOutput.txt");
		
		ArrayList<Point> arp=new ArrayList<Point>();
		Set<Point> expandedNodes=new HashSet<Point>();
		try{
			FileWriter fw=new FileWriter(file);
		PrintWriter pw=new PrintWriter(fw);
		pw.println("The Sample grid is ");
		
		long startTime = System.nanoTime();
		//Forward A star
		//Grid creation
		GridGenerator gg=new GridGenerator();
		
		System.out.println("Enter Source i: ");
		BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
		int starti=Integer.parseInt(br.readLine());
		
		System.out.println("Enter Source j: ");
		int startj=Integer.parseInt(br.readLine());
		
		System.out.println("Enter Dest i: ");
		int endi=Integer.parseInt(br.readLine());
		System.out.println("Enter Dest j: ");
		int endj=Integer.parseInt(br.readLine());


		
		for(int i=0;i<gg.grid.length;i++){
			for(int j=0;j<gg.grid[i].length;j++)
			{
				if(starti==i && startj==j)
				{
					gg.grid[i][j].setVal("S");
					pw.print(("S"+" "));
				}
				else if(endi==i && endj==j){
					gg.grid[i][j].setVal("D");
					pw.print(("D"+" "));
				}
				else{
				pw.print(gg.grid[i][j].getVal()+" ");
				//System.out.print((gg.grid[i][j].getVal()+" "));
				}
			}
			pw.println();
			//System.out.println();
		}
		
		//A star
		//Astar a=new Astar();
		Pos current=new Pos();
		current.x=starti;
		current.y=startj;
		
		Pos dest=new Pos();
		dest.x=endi;
		dest.y=endj;
		

		
		Point[][] graph=new Point[101][101];
		
		for(int l=0;l<graph.length;l++)
			for(int m=0;m<graph[l].length;m++)
			{
				graph[l][m]=new Point();
				graph[l][m].x=l;
				graph[l][m].y=m;
		
			}
		while(current.x!=dest.x || current.y!=dest.y)
		{
			
			for(int l=0;l<graph.length;l++)
				for(int m=0;m<graph[l].length;m++)
				{
					graph[l][m].setG(Integer.MAX_VALUE);
					graph[l][m].setF(Integer.MAX_VALUE);
					graph[l][m].setVisited(false);
					
					if(graph[l][m].getVal()!="1")
					graph[l][m].setVal("0");
				}
			
			graph[current.x][current.y].setG(0);
			graph[current.x][current.y].setF(0);
			
			if(current.x>0)
			{
				graph[current.x-1][current.y].setVal(gg.grid[current.x-1][current.y].getVal());
			}
			if(current.x<100)
			{
				graph[current.x+1][current.y].setVal(gg.grid[current.x+1][current.y].getVal());
			}
			if(current.y>0)
			{
				graph[current.x][current.y-1].setVal(gg.grid[current.x][current.y-1].getVal());
			}
			if(current.y<100)
			{
				graph[current.x][current.y+1].setVal(gg.grid[current.x][current.y+1].getVal());
			}
			Astar a=new Astar();
			ArrayList<Point> path =a.computePath(graph, current, dest,expandedNodes);
			
			if(path.size()>1)
			arp.add(path.get(path.size()-2));
			
			//System.out.println("begin "+path.get(0).x+","+path.get(0).y+" end "+path.get(path.size()-1).x+","+path.get(path.size()-1).y);
			
			if(path.size()==0)
			{
				pw.println("Destination not reachable");
				break;
			}
			//System.out.println("path size" +path.size());
			
			for(Point p: path)
			{
				graph[p.x][p.y].setVal("*");
			}
			
			pw.println("The computed path");
			for(int i=0;i<graph.length;i++){
				for(int j=0;j<graph[i].length;j++)
				{
					pw.print(graph[i][j].getVal()+" ");
				}
				pw.println();
				}
			current.x=path.get(path.size()-2).x;
			current.y=path.get(path.size()-2).y;
			//System.out.println(current.x+" "+current.y);
			path.clear();
		}
		
		Point[][] outputGrid =gg.grid.clone();
		pw.println("Final path trace");
		for(Point dd: arp)
		{
			outputGrid[dd.x][dd.y].setVal("*");
			
		}
		
		for(int u=0;u<outputGrid.length;u++){
			for(int z=0;z<outputGrid[u].length;z++)
			{
				pw.print(outputGrid[u][z].getVal()+" ");
				
			}
		pw.println();
		}
		
		long endTime = System.nanoTime();
		System.out.println("Forward A star Took "+(endTime - startTime) + " ns"); 
		
		System.out.println("The number of total expanded cells is "+expandedNodes.size());
		
		//Backward A star
		startTime= System.nanoTime();
		expandedNodes=new HashSet<Point>();
		FileWriter fw1=new FileWriter(file1);
		PrintWriter pw1=new PrintWriter(fw1);
		pw1.println("The Sample grid is ");
		arp=new ArrayList<Point>();
		
		
		for(int i=0;i<gg.grid.length;i++){
			for(int j=0;j<gg.grid[i].length;j++)
			{
				if(starti==i && startj==j)
				{
					gg.grid[i][j].setVal("S");
					pw1.print(("S"+" "));
				}
				else if(endi==i && endj==j){
					gg.grid[i][j].setVal("D");
					pw1.print(("D"+" "));
				}
				else{
				pw1.print(gg.grid[i][j].getVal()+" ");
				//System.out.print((gg.grid[i][j].getVal()+" "));
				}
			}
			pw1.println();
			//System.out.println();
		}
		//a=new Astar();
		current=new Pos();
		current.x=starti;
		current.y=startj;
		
		dest=new Pos();
		dest.x=endi;
		dest.y=endj;
		
		
		graph=new Point[101][101];
		
		for(int l=0;l<graph.length;l++)
			for(int m=0;m<graph[l].length;m++)
			{
				graph[l][m]=new Point();
				graph[l][m].x=l;
				graph[l][m].y=m;
		
			}
		while(current.x!=dest.x || current.y!=dest.y)
		{
			
			for(int l=0;l<graph.length;l++)
				for(int m=0;m<graph[l].length;m++)
				{
					graph[l][m].setG(Integer.MAX_VALUE);
					graph[l][m].setF(Integer.MAX_VALUE);
					graph[l][m].setVisited(false);
					
					if(graph[l][m].getVal()!="1")
					graph[l][m].setVal("0");
				}
			
			graph[dest.x][dest.y].setG(0);
			graph[dest.x][dest.y].setF(0);
			
			if(current.x>0)
			{
				graph[current.x-1][current.y].setVal(gg.grid[current.x-1][current.y].getVal());
			}
			if(current.x<100)
			{
				graph[current.x+1][current.y].setVal(gg.grid[current.x+1][current.y].getVal());
			}
			if(current.y>0)
			{
				graph[current.x][current.y-1].setVal(gg.grid[current.x][current.y-1].getVal());
			}
			if(current.y<100)
			{
				graph[current.x][current.y+1].setVal(gg.grid[current.x][current.y+1].getVal());
			}
			Astar a=new Astar();
			ArrayList<Point> path =a.computePath(graph, dest, current,expandedNodes);
			if(path.size()>0)
			arp.add(path.get(1));
			
			//System.out.println("begin "+path.get(0).x+","+path.get(0).y+" end "+path.get(path.size()-1).x+","+path.get(path.size()-1).y);
			
			if(path.size()==0)
			{
				pw1.println("No path found");
				break;
			}
			//System.out.println("path size" +path.size());
			
			for(Point p: path)
			{
				graph[p.x][p.y].setVal("*");
			}
			
			pw1.println("The computed path");
			for(int i=0;i<graph.length;i++){
				for(int j=0;j<graph[i].length;j++)
				{
					pw1.print(graph[i][j].getVal()+" ");
				}
				pw1.println();
				}
			current.x=path.get(1).x;
			current.y=path.get(1).y;
			//System.out.println(current.x+" "+current.y);
			path.clear();
		}
		
		outputGrid =gg.grid.clone();
		pw1.println("Final path trace");
		for(Point dd: arp)
		{
			outputGrid[dd.x][dd.y].setVal("*");
			
		}
		
		for(int u=0;u<outputGrid.length;u++){
			for(int z=0;z<outputGrid[u].length;z++)
			{
				pw1.print(outputGrid[u][z].getVal()+" ");
				
			}
		pw1.println();
		}
		
		endTime = System.nanoTime();
		System.out.println("Backward A star Took "+(endTime - startTime) + " secs"); 
		System.out.println("The number of total expanded cells is "+expandedNodes.size());

		
		//Adaptive A star
		
		startTime= System.nanoTime();
		expandedNodes=new HashSet<Point>();
		FileWriter fw2=new FileWriter(file2);
		PrintWriter pw2=new PrintWriter(fw2);
		pw2.println("The Sample grid is ");
		arp=new ArrayList<Point>();
		
		for(int i=0;i<gg.grid.length;i++){
			for(int j=0;j<gg.grid[i].length;j++)
			{
				if(starti==i && startj==j)
				{
					gg.grid[i][j].setVal("S");
					pw2.print(("S"+" "));
				}
				else if(endi==i && endj==j){
					gg.grid[i][j].setVal("D");
					pw2.print(("D"+" "));
				}
				else{
				pw2.print(gg.grid[i][j].getVal()+" ");
				//System.out.print((gg.grid[i][j].getVal()+" "));
				}
			}
			pw2.println();
			//System.out.println();
		}
		
		//a=new Astar();
		current=new Pos();
		current.x=starti;
		current.y=startj;
		
		dest=new Pos();
		dest.x=endi;
		dest.y=endj;
		

		graph=new Point[101][101];
		
		for(int l=0;l<graph.length;l++)
			for(int m=0;m<graph[l].length;m++)
			{
				graph[l][m]=new Point();
				graph[l][m].x=l;
				graph[l][m].y=m;
		
			}
		
		while(current.x!=dest.x || current.y!=dest.y)
		{
			
			for(int l=0;l<graph.length;l++)
				for(int m=0;m<graph[l].length;m++)
				{
					graph[l][m].setG(Integer.MAX_VALUE);
					graph[l][m].setF(Integer.MAX_VALUE);
					graph[l][m].setVisited(false);
					
					if(graph[l][m].getVal()!="1")
					graph[l][m].setVal("0");
				}
			
			graph[current.x][current.y].setG(0);
			graph[current.x][current.y].setF(0);
			
			if(current.x>0)
			{
				graph[current.x-1][current.y].setVal(gg.grid[current.x-1][current.y].getVal());
			}
			if(current.x<100)
			{
				graph[current.x+1][current.y].setVal(gg.grid[current.x+1][current.y].getVal());
			}
			if(current.y>0)
			{
				graph[current.x][current.y-1].setVal(gg.grid[current.x][current.y-1].getVal());
			}
			if(current.y<100)
			{
				graph[current.x][current.y+1].setVal(gg.grid[current.x][current.y+1].getVal());
			}
			Astar a=new Astar();
			ArrayList<Point> path =a.computePath(graph, current, dest,expandedNodes);
			
			if(path.size()>1)
			arp.add(path.get(path.size()-2));
			
			a.count++;
			//System.out.println("begin "+path.get(0).x+","+path.get(0).y+" end "+path.get(path.size()-1).x+","+path.get(path.size()-1).y);
			
			if(path.size()==0)
			{
				pw2.println("Destination not reachable");
				break;
			}
			//System.out.println("path size" +path.size());
			
			for(Point p: path)
			{
				graph[p.x][p.y].setVal("*");
			}
			
			pw2.println("The computed path");
			for(int i=0;i<graph.length;i++){
				for(int j=0;j<graph[i].length;j++)
				{
					pw2.print(graph[i][j].getVal()+" ");
				}
				pw2.println();
				}
			current.x=path.get(path.size()-2).x;
			current.y=path.get(path.size()-2).y;
			//System.out.println(current.x+" "+current.y);
			path.clear();
		}
		
		outputGrid =gg.grid.clone();
		pw2.println("Final path trace");
		for(Point dd: arp)
		{
			outputGrid[dd.x][dd.y].setVal("*");
			
		}
		
		for(int u=0;u<outputGrid.length;u++){
			for(int z=0;z<outputGrid[u].length;z++)
			{
				pw2.print(outputGrid[u][z].getVal()+" ");
				
			}
		pw2.println();
		}
		endTime = System.nanoTime();
		System.out.println("Adaptive A star Took "+(endTime - startTime) + " secs"); 
		System.out.println("The number of total expanded cells is "+expandedNodes.size());

		
		pw.close();
		fw.close();
		pw1.close();
		fw1.close();
		pw2.close();
		fw2.close();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
