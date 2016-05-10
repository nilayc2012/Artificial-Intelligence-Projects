#include <iostream>
#include <fstream>
#include <vector>
#include <map>
#include <set>

#define MAX_SIZE 101
#define HIGHER_G

using namespace std;

typedef struct node
{
	int x;
	int y;

	node(int a=0, int b=0):x(a), y(b){}
}Point;

bool operator<(const Point& l, const Point& r) {
     return (l.x<r.x || (l.x==r.x && l.y<r.y));
}

bool operator==(const Point& l, const Point& r) {
     return (l.x==r.x && l.y==r.y);
}

bool operator!=(const Point& l, const Point& r) {
     return (l.x!=r.x || l.y!=r.y);
}

vector<Point> hp;
map<Point, int> g;
map<Point, int> f;
map<Point, Point> came_from;
int exp_states;

void init_astar()
{
	g.clear();
	f.clear();
	came_from.clear();

	hp.clear();
	Point tmp(-1,-1);
	hp.push_back(tmp);
}

void insert_hp(Point p)
{
	hp.push_back(p);

	//find parent
	int cur = hp.size()-1;
	int par = cur/2;

	#ifdef HIGHER_G
	while(par>0 && ((f[hp[par]] > f[hp[cur]]) || ((f[hp[par]] == f[hp[cur]]) && (g[hp[par]] < g[hp[cur]]))) )
	#else
	while(par>0 && ((f[hp[par]] > f[hp[cur]]) || ((f[hp[par]] == f[hp[cur]]) && (g[hp[par]] > g[hp[cur]]))) )
	#endif
	{
		//swap
		Point tmp = hp[par];
		hp[par] = p;
		hp[cur] =  tmp;

		cur = par;
		par = par/2;
	}
}

Point extract_min()
{
	//if size 0 ?
	Point ex_min(hp[1].x,hp[1].y);
	hp[1] = hp[hp.size()-1];
	hp.pop_back();

	int cur = 1;

	while(cur < hp.size()-1)
	{
		int lchild = cur*2;
		int rchild = cur*2+1;

		if(lchild > hp.size()-1)break;
		if(rchild > hp.size()-1)
		{
			#ifdef HIGHER_G
			if((f[hp[lchild]] < f[hp[cur]]) || ((f[hp[lchild]] == f[hp[cur]]) && (g[hp[lchild]] > g[hp[cur]])))
			#else
			if((f[hp[lchild]] < f[hp[cur]]) || ((f[hp[lchild]] == f[hp[cur]]) && (g[hp[lchild]] < g[hp[cur]])))
			#endif
			{
				//swap
				Point tmp = hp[cur];
				hp[cur] = hp[lchild];
				hp[lchild] = tmp;
				cur = lchild;
				break;
			}
			else break;
		}

		#ifdef HIGHER_G
		if((f[hp[lchild]] < f[hp[rchild]]) || (((f[hp[lchild]] == f[hp[rchild]]) && (g[hp[rchild]] < g[hp[lchild]]))))
		#else
		if((f[hp[lchild]] < f[hp[rchild]]) || (((f[hp[lchild]] == f[hp[rchild]]) && (g[hp[rchild]] > g[hp[lchild]]))))
		#endif
		{	
			#ifdef HIGHER_G
			if(	(f[hp[lchild]] < f[hp[cur]]) || ((f[hp[lchild]] == f[hp[cur]]) && (g[hp[lchild]] > g[hp[cur]])) )
			#else
			if(	(f[hp[lchild]] < f[hp[cur]]) || ((f[hp[lchild]] == f[hp[cur]]) && (g[hp[lchild]] < g[hp[cur]])) )
			#endif
			{
				//swap
				Point tmp = hp[cur];
				hp[cur] = hp[lchild];
				hp[lchild] = tmp;
				cur = lchild;
			}
			else break;
		}
		else
		{
			#ifdef HIGHER_G
			if((f[hp[rchild]] < f[hp[cur]]) || ((f[hp[rchild]] == f[hp[cur]]) && (g[hp[rchild]] > g[hp[cur]])))
			#else
			if((f[hp[rchild]] < f[hp[cur]]) || ((f[hp[rchild]] == f[hp[cur]]) && (g[hp[rchild]] < g[hp[cur]])))
			#endif
			{
				//swap
				Point tmp = hp[cur];
				hp[cur] = hp[rchild];
				hp[rchild] = tmp;
				cur = rchild;
			}
			else break;
		}
	}
	return ex_min;
}

int heuristic_estimate(Point start, Point end)
{
	//manhattan distance
	return (abs(end.x - start.x) + abs(end.y - start.y));
}

int inOpenSet(Point p)
{
	for(int i=1;i<hp.size();i++)
	{
		if(hp[i].x == p.x && hp[i].y == p.y)return i;
	}
	return 0;
}

vector<Point> gen_path(Point goal)
{
	vector<Point> res;
	res.push_back(goal);
	Point cur = goal;

	while(came_from.find(cur) != came_from.end())
	{
		cur = came_from[cur];
		res.push_back(cur);
	}
	return res;
}

int adaptive_astar(Point start, Point end, vector<vector <char> > &graph, vector<vector <int> > &astar_gcount, int astar_count, vector<Point> &res, vector<vector <int> > &h)
{
	init_astar();
	res.clear();
	insert_hp(start);

	g[start] = 0;
	astar_gcount[start.x][start.y] = astar_count;

	if(h[start.x][start.y] != -1)
		f[start] = h[start.x][start.y];
	else
		f[start] = heuristic_estimate(start, end);

	while(hp.size() > 1)
	{
		//get min from p.q. and remove from open list
		Point cur = extract_min();

		if(cur == end)
		{	
			res = gen_path(end);
			return 1;
		}

		//add to closed set
		graph[cur.x][cur.y] = '1';
		

		//left neighbour
		if(cur.x > 0)
		{
			//If not in closed set
			if(graph[cur.x-1][cur.y] == '0')
			{
				Point p(cur.x-1,cur.y);
				if(astar_gcount[p.x][p.y] < astar_count){
					exp_states++;
					astar_gcount[p.x][p.y] = astar_count;
					g[p] = g[cur] + 1;

					if(h[p.x][p.y] != -1)
						f[p] = g[p] + h[p.x][p.y];
					else
						f[p] = g[p] + heuristic_estimate(p, end);

					insert_hp(p);
					came_from[p] = cur;
				}
			}
		}

		//down neighbour
		if(cur.y > 0)
		{
			//If not in closed set
			if(graph[cur.x][cur.y-1] == '0')
			{
				Point p(cur.x,cur.y-1);
				if(astar_gcount[p.x][p.y] < astar_count){
					exp_states++;
					astar_gcount[p.x][p.y] = astar_count;
					g[p] = g[cur] + 1;

					if(h[p.x][p.y] != -1)
						f[p] = g[p] + h[p.x][p.y];
					else
						f[p] = g[p] + heuristic_estimate(p, end);

					insert_hp(p);
					came_from[p] = cur;
					}
			}
		}

		//right neighbour
		if(cur.x < graph.size()-1)
		{
			//If not in closed set
			if(graph[cur.x+1][cur.y] == '0')
			{
				Point p(cur.x+1,cur.y);
				if(astar_gcount[p.x][p.y] < astar_count){
					exp_states++;
					astar_gcount[p.x][p.y] = astar_count;
					g[p] = g[cur] + 1;

					if(h[p.x][p.y] != -1)
						f[p] = g[p] + h[p.x][p.y];
					else
						f[p] = g[p] + heuristic_estimate(p, end);

					insert_hp(p);
					came_from[p] = cur;
				}
			}
		}

		//up neighbour
		if(cur.y < graph.size()-1)
		{
			//If not in closed set
			if(graph[cur.x][cur.y+1] == '0')
			{
				Point p(cur.x,cur.y+1);
				if(astar_gcount[p.x][p.y] < astar_count){
					exp_states++;
					astar_gcount[p.x][p.y] = astar_count;
					g[p] = g[cur] + 1;

					if(h[p.x][p.y] != -1)
						f[p] = g[p] + h[p.x][p.y];
					else
						f[p] = g[p] + heuristic_estimate(p, end);

					insert_hp(p);
					came_from[p] = cur;
					}
			}
		}
	}//do while until open list is not empty

	//returning dummy incase of failure
	//cout<<"Error ! No path found !!"<<endl;
	Point p(0,0);
	res.push_back(p);
	return 0;
}


//there is a space-time tradeoff here. Either we store a set/list of blocked points and update the current state as we explore, in which
//case time complexity is of the order of O(number of blocked nodes) or we store the current state in a graph and only update the new
//additions(extra space of order of the graph).

bool update_neighbours(Point cur, vector<vector <char> > input_graph, vector<vector <char> > &explored_graph)
{
	//update neighbours from the test case
	bool updated = 0;

	//left
	if(cur.x > 0)
	{
		if(input_graph[cur.x-1][cur.y] != explored_graph[cur.x-1][cur.y])
		{
			explored_graph[cur.x-1][cur.y] = input_graph[cur.x-1][cur.y];
			updated =1;
		}
	}

	//right
	if(cur.x < input_graph.size()-1)
	{
		if(input_graph[cur.x+1][cur.y] != explored_graph[cur.x+1][cur.y])
		{
			explored_graph[cur.x+1][cur.y] = input_graph[cur.x+1][cur.y];
			updated =1;
		}
	}

	//down
	if(cur.y > 0)
	{
		if(input_graph[cur.x][cur.y-1] != explored_graph[cur.x][cur.y-1])
		{
			explored_graph[cur.x][cur.y-1] = input_graph[cur.x][cur.y-1];
			updated =1;
		}
	}

	//up
	if(cur.y < input_graph.size()-1)
	{
		if(input_graph[cur.x][cur.y+1] != explored_graph[cur.x][cur.y+1])
		{
			explored_graph[cur.x][cur.y+1] = input_graph[cur.x][cur.y+1];
			updated =1;
		}
	}

	return updated;
}


int main(int argc, char *argv[])
{
	vector<vector <char> > input_graph(MAX_SIZE,vector<char>(MAX_SIZE,'0'));
	vector<vector <char> > explored_graph(MAX_SIZE,vector<char>(MAX_SIZE,'0'));
	vector<vector <int> > astar_gcount(MAX_SIZE,vector<int>(MAX_SIZE,0));
	vector<vector <int> > h(MAX_SIZE,vector<int>(MAX_SIZE,-1));

	vector<Point> final_path;
	exp_states=0;
	//start and end positions - need to be taken as input
	Point start(0,0);
	Point end(20,20);
	Point current_position(start.x, start.y);
	
	//take input from file
	ifstream input_file;
	input_file.open(argv[1]);
	for(int i = 0; i < MAX_SIZE; i++)
		for(int j = 0; j < MAX_SIZE; j++)
        	input_file >> input_graph[i][j];

	input_file.close();

	//The destination cannot be blocked
	input_graph[end.x][end.y] = '0';

	//Output File
	ofstream output_file;
	output_file.open(argv[2]);

    vector<Point> res;
    int pos = 0;
    bool no_path = 1;
    int num_steps = 0;
    int num_astar = 0;
    int ret = 0;

	while(current_position != end)
	{
		//cout<<current_position.x<<" "<<current_position.y<<endl;
		num_steps++;
        final_path.push_back(current_position);

		bool updated  = update_neighbours(current_position, input_graph, explored_graph);
		if(updated || no_path)
		{
			num_astar++;
			vector<vector <char> > current_graph(explored_graph);
			
			//For adaptive astar
			
			ret = adaptive_astar(current_position, end, current_graph, astar_gcount, num_astar, res, h);

			for(int i=0;i<res.size();i++)
			{
				h[res[i].x][res[i].y] = g[end] - g[res[i]];
			}
			

			//No solution returned from astar - Failure Condition
			if(ret == 0)
			{
					cout<<0<<endl;
					return -1;
			}

			//We got atleast one path
			no_path = 0;

			//Reset to the new result size
			//for forward
			pos = res.size()-1;
		}

		//for forward
		pos--;

		current_position.x = res[pos].x;
		current_position.y = res[pos].y;
	}

	//Output the path traversed
	for(int i =0;i<final_path.size();i++)
	{
		input_graph[final_path[i].x][final_path[i].y] = '*';
	}
	input_graph[start.x][start.y] = 'S';
	input_graph[end.x][end.y] = 'D';

	for(int i = 0; i < MAX_SIZE; i++)
		{for(int j = 0; j < MAX_SIZE; j++)
        	output_file<<input_graph[i][j]<<" ";output_file<<endl;}
		
	output_file.close();

	//Output some stats
	cout<<exp_states<<endl;
	//cout<<"Expanded States : "<<exp_states.size()<<endl;
	//cout<<"Number of steps : "<<num_steps<<endl<<"Number of times astar : "<<num_astar<<endl;
	return 0;
}