package astar;

import java.util.*;

public class Astar {

	public static int count=0;
	
	public Astar()
	{
		
	}
	public int estimateHeuristic(Pos start,Pos end)
	{
		return (Math.abs(start.x - end.x) + Math.abs(start.y - end.y));
	}
	
	public ArrayList<Point> computePath(Point[][] grid,Pos src,Pos dest,Set<Point> expandedNodes)
	{
		ArrayList<Point> path=new ArrayList<Point>();
		HashMap<Point,Point> cameFrom=new HashMap<Point,Point>();
		cameFrom.put(grid[src.x][src.y], null);
		//ArrayList<Point> openSet=new ArrayList<Point>();
		PriorityQueue<Point> openSet=new PriorityQueue<Point>();
		
		/*grid[src.x][src.y].x=src.x;
		grid[src.x][src.y].y=src.y;*/

		openSet.offer(grid[src.x][src.y]);
		//Heap h=new Heap();
		Point current;
		while(!openSet.isEmpty())
		{
		
			//Point current=h.extract_min(openSet);
			current=openSet.poll();
			//System.out.println("hello"+current.getF()+ " "+current.getG()+" "+current.x+" "+current.y);
			current.setVisited(true);
			
			if(current.x==dest.x && current.y==dest.y)
			{
				path.add(current);
				while(cameFrom.get(current)!=null)
				{	
					current=cameFrom.get(current);
					path.add(current);
				}
				return path;
			}
			
			
			if(current.x>0)
			{//System.out.println((current.x-1)+" "+current.y);
				if(!grid[current.x-1][current.y].getVal().equals("1")&&(!grid[current.x-1][current.y].isVisited()))
				{//System.out.println("hello1");
					if(grid[current.x-1][current.y].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x-1][current.y].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x-1;
						p1.y=current.y;
						grid[current.x-1][current.y].setF(grid[current.x-1][current.y].getG()+estimateHeuristic(p1, dest));
						cameFrom.put(grid[current.x-1][current.y],current);
						
						if(!openSet.contains(grid[current.x-1][current.y])){
							expandedNodes.add(grid[current.x-1][current.y]);
						openSet.offer(grid[current.x-1][current.y]);
						}
						//System.out.println("size is "+openSet.size());

					}
				}
			}
			if(current.x<100)
			{
				if(!grid[current.x+1][current.y].getVal().equals("1")&&(!grid[current.x+1][current.y].isVisited()))
				{//System.out.println("hello2");
					if(grid[current.x+1][current.y].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x+1][current.y].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x+1;
						p1.y=current.y;
						grid[current.x+1][current.y].setF(grid[current.x+1][current.y].getG()+estimateHeuristic(p1, dest));
						cameFrom.put(grid[current.x+1][current.y],current);
						
						if(!openSet.contains(grid[current.x+1][current.y])){
							expandedNodes.add(grid[current.x+1][current.y]);
							openSet.offer(grid[current.x+1][current.y]);
						
						}
						//System.out.println("size is "+openSet.size());
						//System.out.println((current.x+1)+" "+current.y+" parent is "+ grid[current.x+1][current.y].parent.x+" "+grid[current.x+1][current.y].parent.y);
					}
				}
			}
			if(current.y>0)
			{
				if(!grid[current.x][current.y-1].getVal().equals("1")&&(!grid[current.x][current.y-1].isVisited()))
				{//System.out.println("hello3");
					if(grid[current.x][current.y-1].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x][current.y-1].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x;
						p1.y=current.y-1;
						grid[current.x][current.y-1].setF(grid[current.x][current.y-1].getG()+estimateHeuristic(p1, dest));
						cameFrom.put(grid[current.x][current.y-1],current);
						
						if(!openSet.contains(grid[current.x][current.y-1])){
							expandedNodes.add(grid[current.x][current.y-1]);
						openSet.offer(grid[current.x][current.y-1]);
						}
						//System.out.println("size is "+openSet.size());
					}
				}
			}
			if(current.y<100)
			{
				if(!grid[current.x][current.y+1].getVal().equals("1")&&(!grid[current.x][current.y+1].isVisited()))
				{//System.out.println("hello4");
					if(grid[current.x][current.y+1].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x][current.y+1].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x;
						p1.y=current.y+1;
						grid[current.x][current.y+1].setF(grid[current.x][current.y+1].getG()+estimateHeuristic(p1, dest));
						cameFrom.put(grid[current.x][current.y+1],current);
						
						if(!openSet.contains(grid[current.x][current.y+1])){
							expandedNodes.add(grid[current.x][current.y+1]);
							openSet.offer(grid[current.x][current.y+1]);
						}
						//System.out.println("size is "+openSet.size());
					}
				}
			}
			
			
		}
		
		return path;
	}
	
	public ArrayList<Point> computeAdaptivePath(Point[][] grid,Pos src,Pos dest,Set<Point> expandedNodes)
	{
		ArrayList<Point> path=new ArrayList<Point>();
		HashMap<Point,Point> cameFrom=new HashMap<Point,Point>();
		cameFrom.put(grid[src.x][src.y], null);
		//ArrayList<Point> openSet=new ArrayList<Point>();
		PriorityQueue<Point> openSet=new PriorityQueue<Point>();
		
		/*grid[src.x][src.y].x=src.x;
		grid[src.x][src.y].y=src.y;*/

		openSet.offer(grid[src.x][src.y]);
		//Heap h=new Heap();
		Point current;
		while(!openSet.isEmpty())
		{
		
			//Point current=h.extract_min(openSet);
			current=openSet.poll();
			//System.out.println("hello"+current.getF()+ " "+current.getG()+" "+current.x+" "+current.y);
			current.setVisited(true);
			
			if(current.x==dest.x && current.y==dest.y)
			{
				path.add(current);
				while(cameFrom.get(current)!=null)
				{	
					current=cameFrom.get(current);
					path.add(current);
				}
				
				for(int t=0;t<grid.length;t++)
					for(int y=0;y<grid[t].length;y++)
					{
						if(grid[t][y].getG()!=Integer.MAX_VALUE)
							grid[t][y].h=grid[current.x][current.y].getG()-grid[t][y].getG();
					}
				
				return path;
			}
			
			
			if(current.x>0)
			{//System.out.println((current.x-1)+" "+current.y);
				if(!grid[current.x-1][current.y].getVal().equals("1")&&(!grid[current.x-1][current.y].isVisited()))
				{//System.out.println("hello1");
					if(grid[current.x-1][current.y].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x-1][current.y].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x-1;
						p1.y=current.y;
						
						if(count==0)
						grid[current.x-1][current.y].setF(grid[current.x-1][current.y].getG()+estimateHeuristic(p1, dest));
						else
							grid[current.x-1][current.y].setF(grid[current.x-1][current.y].getG()+(grid[current.x-1][current.y].h));	
						
						cameFrom.put(grid[current.x-1][current.y],current);
						
						if(!openSet.contains(grid[current.x-1][current.y])){
							expandedNodes.add(grid[current.x-1][current.y]);
						openSet.offer(grid[current.x-1][current.y]);}
						//System.out.println("size is "+openSet.size());

					}
				}
			}
			if(current.x<100)
			{
				if(!grid[current.x+1][current.y].getVal().equals("1")&&(!grid[current.x+1][current.y].isVisited()))
				{//System.out.println("hello2");
					if(grid[current.x+1][current.y].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x+1][current.y].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x+1;
						p1.y=current.y;
						
						if(count==0)
						grid[current.x+1][current.y].setF(grid[current.x+1][current.y].getG()+estimateHeuristic(p1, dest));
						else
							grid[current.x+1][current.y].setF(grid[current.x+1][current.y].getG()+grid[current.x+1][current.y].h);
						
						cameFrom.put(grid[current.x+1][current.y],current);
						
						if(!openSet.contains(grid[current.x+1][current.y])){
							expandedNodes.add(grid[current.x+1][current.y]);
						openSet.offer(grid[current.x+1][current.y]);}
						
						//System.out.println("size is "+openSet.size());
						//System.out.println((current.x+1)+" "+current.y+" parent is "+ grid[current.x+1][current.y].parent.x+" "+grid[current.x+1][current.y].parent.y);
					}
				}
			}
			if(current.y>0)
			{
				if(!grid[current.x][current.y-1].getVal().equals("1")&&(!grid[current.x][current.y-1].isVisited()))
				{//System.out.println("hello3");
					if(grid[current.x][current.y-1].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x][current.y-1].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x;
						p1.y=current.y-1;
						
						if(count==0)
						grid[current.x][current.y-1].setF(grid[current.x][current.y-1].getG()+estimateHeuristic(p1, dest));
						else
							grid[current.x][current.y-1].setF(grid[current.x][current.y-1].getG()+grid[current.x][current.y-1].h);	
						
						cameFrom.put(grid[current.x][current.y-1],current);
						
						if(!openSet.contains(grid[current.x][current.y-1])){
							expandedNodes.add(grid[current.x][current.y-1]);
						openSet.offer(grid[current.x][current.y-1]);}
						
						//System.out.println("size is "+openSet.size());
						//System.out.println((current.x)+" "+(current.y-1)+" parent is "+ grid[current.x][current.y-1].parent.x+" "+grid[current.x][current.y-1].parent.y);
					}
				}
			}
			if(current.y<100)
			{
				if(!grid[current.x][current.y+1].getVal().equals("1")&&(!grid[current.x][current.y+1].isVisited()))
				{//System.out.println("hello4");
					if(grid[current.x][current.y+1].getG()>(grid[current.x][current.y].getG()+1))
					{
						grid[current.x][current.y+1].setG(grid[current.x][current.y].getG()+1);
						Pos p1=new Pos();
						p1.x=current.x;
						p1.y=current.y+1;
						
						if(count==0)
						grid[current.x][current.y+1].setF(grid[current.x][current.y+1].getG()+estimateHeuristic(p1, dest));
						else
							grid[current.x][current.y+1].setF(grid[current.x][current.y+1].getG()+grid[current.x][current.y+1].h);
							
						cameFrom.put(grid[current.x][current.y+1],current);
						
						if(!openSet.contains(grid[current.x][current.y+1])){
							expandedNodes.add(grid[current.x][current.y+1]);
						openSet.offer(grid[current.x][current.y+1]);}
						
						//System.out.println("size is "+openSet.size());
						//System.out.println((current.x)+" "+(current.y+1)+" parent is "+ grid[current.x][current.y+1].parent.x+" "+grid[current.x][current.y+1].parent.y);
					}
				}
			}
			
			
		}
		
		return path;
	}
}
