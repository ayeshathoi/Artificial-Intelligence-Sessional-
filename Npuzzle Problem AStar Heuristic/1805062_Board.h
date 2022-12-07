#include<bits/stdc++.h>
using namespace std;

#define loop(i,a,b) for (int i = a; i < b; i++)
#define F first
#define S second

//===================== HEURISTIC FUNCTION CALCULATION STARTS =======================================//

int Hamming(vector<vector<int>> &board, vector<vector<int>> &goal, int grid)
{
    int Ham = 0;

    loop(i,0,grid)
    {
        loop(j,0,grid)
        {
            if(board[i][j] != goal[i][j] && board[i][j] !=0)
                Ham++;
        }
    }
    return Ham;
}

int Manhattan (vector<vector<int>> &board,int grid)
{
    int manhattan = 0, k = 0;
    int sz = grid*grid;
    pair<int,int> finalPosition[sz];

    loop(i,0,grid)
    {
        loop(j,0,grid)
        {
            finalPosition[k].F = i;
            finalPosition[k].S = j;
            k++;
        }
    }

    loop(i,0,grid)
    {
        loop(j,0,grid)
        {
            if(board[i][j]!=0)
            {
                manhattan+= abs(i - finalPosition[board[i][j]-1].F) + abs(j - finalPosition[board[i][j]-1].S);
            }
        }
    }

    return manhattan;
}

//===================== HEURISTIC FUNCTION CALCULATION ENDS =======================================//


struct Board
{
    Board* Parent;
    int grid,index,cost,row0,column0; //row0 and column0 are the position of 0

    vector<vector<int>> CurrentBoard;
};



Board* BoardAttribute(Board* parent,vector<vector<int>> &Current,
                      int grid,int row0,int column0,int index)
{
    Board *newBoard = new Board;
    newBoard->Parent = parent;

    newBoard->grid = grid;
    newBoard->row0 = row0;
    newBoard->column0 = column0;
    newBoard->index = index;

    vector<int> oneD(grid,1);
    newBoard->CurrentBoard.resize(grid,oneD);

    newBoard->CurrentBoard = Current;

    if(newBoard->Parent != NULL)
    {
        int parent0RowPosition = parent->row0;
        int parent0ColumnPosition = parent->column0;

        swap(newBoard->CurrentBoard[parent0RowPosition][parent0ColumnPosition ],newBoard->CurrentBoard[row0][column0]);
    }

    return newBoard;
}



//========================= PRINT PART ===============================

void PrintCurrentBoard(Board* Current)
{
    int row = Current->grid;

    loop(i,0,row)
    {
        loop(j,0,row)
        {
            if(Current->CurrentBoard[i][j]==0)
                cout<< "* ";
            else
            cout<< Current->CurrentBoard[i][j] << " ";
        }
        cout<<endl;
    }
    cout<<endl;
}

void PrintAllBoard(Board* Final)
{
    if(Final==NULL)
        return;

    PrintAllBoard(Final->Parent);

    PrintCurrentBoard(Final);
}

void PrintFinal(Board* cur,int open_sz,int close_sz,int heuristic)
{
    cout << "GOT GOALBOARD!"<<endl;

    cout << "OPTIMAL COST: " << cur->index << endl;

    if(heuristic == 0)
        cout << "HEURISTIC: HAMMING_DISTANCE"<<endl;
    else cout << "HEURISTIC: MANHATTAN_DISTANCE"<<endl;

    PrintAllBoard(cur);

    cout << "EXPANDED NODE: " << close_sz - 1<<endl;
    cout << "EXPLORED NODE: " << close_sz + open_sz << endl;
    cout<<endl;

    return;
}


