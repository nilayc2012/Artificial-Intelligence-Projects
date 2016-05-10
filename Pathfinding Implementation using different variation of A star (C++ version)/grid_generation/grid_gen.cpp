#include<iostream>
#include<vector>
#include<stdlib.h>
#include<time.h>
#include<fstream>
#include<string>

#define MAZE_MIN 0
#define MAZE_MAX 100

using namespace std;

int set_block_status()
{
	int num = rand() % 10 + 1;

	//With 30% probability the cell is blocked
	if(num >=1 && num <=3)return 1;
	else return 0;
}

void dfs(vector<vector <int> > &maze, vector<vector <int> > &visit, int i, int j)
{
	visit[i][j] = 1;
	maze[i][j] = set_block_status();

	//go up
	if(i>MAZE_MIN)
	{
		if(!visit[i-1][j])
		{
			dfs(maze, visit, i-1, j);
			return;
		}
	}

	//go down
	if(i<MAZE_MAX)
	{
		if(!visit[i+1][j])
		{
			dfs(maze, visit, i+1, j);
			return;
		}
	}

	//go left
	if(j>MAZE_MIN)
	{
		if(!visit[i][j-1])
			{
				dfs(maze, visit, i, j-1);
				return;
			}
	}

	//go right
	if(j<MAZE_MAX)
	{
		if(!visit[i][j+1])
			{
				dfs(maze, visit, i, j+1);
				return;
			}
	}

}

//to print the output to a file
void printmaze(vector<vector <int> > maze, string filname)
{
	ofstream output_file;
	output_file.open(filname);

	for(int i=0;i<MAZE_MAX+1;i++)
	{
		for(int j=0;j<MAZE_MAX+1;j++)
		{
			output_file<<maze[i][j]<<" ";
		}
		output_file<<endl;
	}
	output_file.close();
}

int main()
{
	//generate 50 input cases
	for(int i=0;i<50;i++)
	{
		string filname = "input"+(to_string(i))+".txt";
		//Initializing the maze to 0
		vector<vector <int> > maze(MAZE_MAX+1,vector<int>(MAZE_MAX+1,0));
		vector<vector <int> > visit(MAZE_MAX+1,vector<int>(MAZE_MAX+1,0));

		//random seed
		srand (i);

		dfs(maze, visit, 0, 0);
		printmaze(maze, filname);

	}

	return 0;
}