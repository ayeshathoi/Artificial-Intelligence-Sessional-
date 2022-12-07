#include<bits/stdc++.h>
using namespace std;

#include "1805062_Board.h"
#include "1805062_Test.h"



#define loop(i,a,b) for (int i = a; i < b; i++)
#define F first
#define S second


void fast()
{
    std::ios_base::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
}


//COMPARATOR FOR PRIORITY QUEUE
struct CompareHeuristicCost
{
    bool operator()(Board* const& a, Board* const& b)
    {
        return (b->cost) < (a->cost);
    }
};


Board* ChildAssign(Board* cur,int heuristic, int column,int row,vector<vector<int>> &Goal)
{
    Board *leaf = BoardAttribute(cur,cur->CurrentBoard,cur->grid,row,
                                 column, cur->index + 1);

    if(heuristic==0)
        leaf->cost = leaf->index + Hamming(leaf->CurrentBoard,Goal,cur->grid);
    else leaf->cost = leaf->index + Manhattan(leaf->CurrentBoard,cur->grid);

    return leaf;


}


void PuzzleMove(int heuristic,int row0,int column0,int grid, vector<vector<int>>&Root,
                vector<vector<int>>&Goal)
{

    if(heuristic!=1 && heuristic !=0)
    {
        cout<<"You have entered wrong number.\nWe have only two Heuristic. Sorry!"<<endl;
        return;
    }

    Board* root = BoardAttribute(NULL,Root,grid,row0,column0,0);

    if(heuristic==0)
        root->cost =  Hamming(Root,Goal,grid);
    else root->cost = Manhattan(Root,grid);

    vector<Board*> CloseQueueMain;

    priority_queue <Board *, vector<Board *>, CompareHeuristicCost > OpenQueueTesting;
    OpenQueueTesting.push(root);

    int MoveRow[4]    = {-1, 1, 0, 0};
    int MoveColumn[4] = {0, 0, -1, 1};

    while(!OpenQueueTesting.empty())
    {
        Board* cur = OpenQueueTesting.top();
        OpenQueueTesting.pop();

        bool checkSame;
        checkSame = sameTest(cur->CurrentBoard,grid,CloseQueueMain);

        if (!checkSame)
        {
            CloseQueueMain.push_back(cur);

            bool checkGoal = Current_Goal(cur->CurrentBoard,grid,Goal);

            if(checkGoal)
            {
                PrintFinal(cur,OpenQueueTesting.size(),CloseQueueMain.size(),heuristic);
                return;
            }

            loop(i,0,4)
            {
                int column = cur->column0 + MoveColumn[i];
                int row    = cur->row0 + MoveRow[i];
                if((column>=0 && column<grid) && (row>=0 && row<grid))
                {
                    Board *child = ChildAssign(cur,heuristic,column,row,Goal);

                    bool checkSame = sameTest(child->CurrentBoard,grid,CloseQueueMain);
                    if (!checkSame)
                        OpenQueueTesting.push(child);
                }
            }
        }
    }
}



//=========================PUZZLE INITIALIZATION============================================//
void nPuzzle()
{
    int grid,row,column,cnt=0;
    cin>>grid;

    int temp_sz = grid*grid;
    vector<int> oneD (grid,1), temp (temp_sz);
    vector<vector<int>> Rootboard(grid, oneD), GoalBoard(grid, oneD);

    loop(i,0,grid)
    {
        loop(j,0,grid)
        {
            while(isspace(cin.peek()))
                cin.ignore();

            if(cin.peek() == '*')
            {
                cin.ignore();
                Rootboard[i][j]=0;
            }
            else
            {
                cin>>Rootboard[i][j];
            }

            temp[cnt] = Rootboard[i][j];

            GoalBoard[i][j] = ++cnt;


            if(Rootboard[i][j]==0)
            {
                row    = i; // i = row0
                column = j; // j = column0
            }
        }
    }

    GoalBoard[grid-1][grid-1] = 0;

    cout << endl;

    bool ok = testElement(Rootboard,grid);

    if(!ok)
    {
        cout<<"DUPLICATE ELEMENT OR OUT OF BOUND"<<endl;
        cout<<"NOT POSSIBLE TO SOLVE"<<endl;
        return;
    }


    if(solvable(grid,count_inversion(temp),row))
        cout<<"Solvable"<<endl;

    else
    {
        cout<<"Not Solvable"<<endl;
        return;
    }

    int heuristic;

    loop(i,0,3)
    {
        cout<<"You Got "<<3-i<<" Chance.\nENTER 0 for Hamming and 1 for Manhattan:" <<endl;
        cin>>heuristic;
        PuzzleMove(heuristic,row,column,grid,Rootboard,GoalBoard);
    }


}

//=========================PUZZLE INITIALIZATION ENDS============================================//


int main()
{
    fast();
    nPuzzle();
    return 0;
}
