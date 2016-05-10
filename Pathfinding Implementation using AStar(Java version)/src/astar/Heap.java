package astar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Heap {

	int heapsize=0;
	public int parent(int i)
	{
		return (i+1)/2-1;
	}
	public int left(int i)
	{
		return (2*i)+1;
	}
	public int right(int i)
	{
		return (2*i)+2;
	}

	public void min_heapify(ArrayList<Point> A,int i)
	{
		int smallest;
		if(left(i)<=heapsize-1 && A.get(left(i)).getF()<A.get(i).getF())
		{
			smallest=left(i);
		}
		else
		{
			smallest=i;
		}
		if(right(i)<=heapsize-1 && A.get(right(i)).getF()<A.get(smallest).getF())
		{
			smallest=right(i);
		}
		if(smallest!=i)
		{
			Point temp;
			temp=A.get(i);
			A.set(i, A.get(smallest));
			A.set(smallest,temp);
			min_heapify(A, smallest);
		}
		
	}
	
	public Point extract_min(ArrayList<Point> A)
	{
		Point temp=A.get(0);
		A.set(0,A.get(heapsize-1));
		A.remove(heapsize-1);
		heapsize=heapsize-1;
		min_heapify(A, 0);
		return temp;
	}
	
	public void insert(ArrayList<Point> A,Point p)
	{
		heapsize=heapsize+1;
		A.add(p);
		
		for(int i=(heapsize)/2;i>=0;i--)
			min_heapify(A, i);
		
		
	}
	public static void main(String[] args) {
		
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			System.out.println("enter number of elements");
			int n=Integer.parseInt(br.readLine());
			ArrayList<Point> ap=new ArrayList<Point>();
			Heap h=new Heap();
			for(int i=0;i<n;i++)
			{
				Point tt=new Point();
				tt.setF(Integer.parseInt(br.readLine()));				
				h.insert(ap,tt);
				
			}
			
			for(Point fff: ap)
				System.out.println(fff.getF());
			
			while(h.heapsize!=0)
			{
				//System.out.println(h.extract_min(ap).getF());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
