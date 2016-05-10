package astar;

import java.util.Random;

public class GridGenerator {
	
	public Point[][] grid=new Point[101][101];
	Random randomGenarator=new Random();
	int randomInt;
	public GridGenerator() {
		// TODO Auto-generated constructor stub
		dfs();
	}
	
	String getRandom()
	{
		
		if(randomGenarator.nextInt(100) <=30)
			return "1";
		else
			return "0";	
	}
	
	void dfs()
	{
		for(int i=0;i<grid.length;i++)
			for(int j=0;j<grid[i].length;j++)
			{
				grid[i][j]=new Point();
				grid[i][j].setVisited(false);
			}
		
		
		Random randomGenarator=new Random();
		int i= randomGenarator.nextInt(101);
		int j=randomGenarator.nextInt(101);
		
		grid[i][j].setVal("0");
		grid[i][j].setVisited(true);
		dfsVisit(i, j);
		
	}
	
	void dfsVisit(int i,int j)
	{
		if((i-1)>=0){
			if(!grid[i-1][j].isVisited()){
		grid[i-1][j].setVisited(true);
		grid[i-1][j].setVal(getRandom());
		dfsVisit(i-1, j);
			}
		}
		
		if((i+1)<=100){
			if(!grid[i+1][j].isVisited()){
		grid[i+1][j].setVisited(true);
		grid[i+1][j].setVal(getRandom());
		dfsVisit(i+1, j);
			}
		}
		
		if((j-1)>=0){
			if(!grid[i][j-1].isVisited()){
		grid[i][j-1].setVisited(true);
		grid[i][j-1].setVal(getRandom());
		dfsVisit(i, j-1);
			}
		}
		
		if((j+1)<=100){
			if(!grid[i][j+1].isVisited()){
		grid[i][j+1].setVisited(true);
		grid[i][j+1].setVal(getRandom());
		dfsVisit(i, j+1);
			}
		}
	}
	
}
