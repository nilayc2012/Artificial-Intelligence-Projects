package astar;

public class Point implements Comparable<Point>{
	
	public Point parent;
	private int f;
	private int g;
	public int h;
	public int x;
	public int y;

	
	private String val;
	private boolean visited;
	
	public int getF() {
		return f;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}



	public void setF(int f) {
		this.f = f;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Point() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Point o) {
		// TODO Auto-generated method stub
		if(this.f<o.getF())
			return -1;
		else if(this.f>o.getF())
			return 1;
		else
		{
			if(this.g>o.getG())
				return -1;
			else if(this.g<o.getG())
				return 1;
			else
				return 0;
		}
		
	}
	
	
	
}
