#include<bits/stdc++.h>
using namespace std;

#define loop(i,a,b) for (int i = a; i < b; i++)
#define F first
#define S second


//=============================== Checking Puzzles Inversion===================================//

bool solvable(int grid,int inversion, int row)
{
    if(grid%2)
    {
        if(inversion%2)
            return false;
        else return true;
    }

    else
    {
        if(row%2==0 && inversion%2 || row%2 && inversion%2==0)
            return true;
    }

    return false;
}

int count_inversion(vector<int> &temp)
{

    int inversion = 0;

    loop(i,0,temp.size())
    {
        if(temp[i]!=0)
        {
            loop(j,i+1,temp.size())
            {
                if( temp[i]>temp[j] && temp[j]!=0)
                {
                    inversion++;
                }
            }
        }
    }

    return inversion;
}

//=============================== Checking Puzzles Inversion===================================//





//========================== BOARD SAME OR NOT ==================================//

bool sameTest(vector<vector<int>> &CurrentBoard,int grid, vector<Board*> closeList)
{
    int same;

    int clsz = closeList.size();

    loop(sz,0,clsz)
    {
        same = 1;
        loop(i,0,grid)
        {
            loop(j,0,grid)
            {
                if(closeList.at(sz)->CurrentBoard[i][j] != CurrentBoard[i][j])
                {
                    same = 0;
                    break;
                }
            }
            if(!same)
                break;
        }
        if(same)
            return true;
    }
    return false;
}

//========================== CURRENT BOARD & GOALBOARD SAME OR NOT ==================================//

bool Current_Goal(vector<vector<int>>&CurrentBoard,int grid, vector<vector<int>> &Goal)
{
    loop(i,0,grid)
    {
        loop(j,0,grid)
        {
            if (CurrentBoard[i][j] != Goal[i][j])
            {
                return false;
            }
        }
    }
    return true;
}
//===================================================================================================

bool testElement(vector<vector<int>>&Current,int grid)
{
    int sz = grid*grid -1;
    vector<int> oneD(sz+1,0);

    loop(i,0,grid)
    {
        loop(j,0,grid)
        {
            if(Current[i][j]<0 || Current[i][j]>sz)
                return false;
            else
            {
                oneD[Current[i][j]]++;
            }
        }
    }

    loop(i,0,sz)
    {
        if(oneD[i]>1)
            return false;
    }


    return true;
}
