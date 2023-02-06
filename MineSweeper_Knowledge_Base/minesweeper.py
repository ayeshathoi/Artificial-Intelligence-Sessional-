import itertools
import random


class Minesweeper():
    """Minesweeper game representation"""

    def __init__(self, height=8, width=8, mines=8):

        # Set initial width, height, and number of mines
        self.height = height
        self.width = width
        self.mines = set()

        # Initialize an empty field with no mines
        self.board = []
        for i in range(self.height):
            row = []
            for j in range(self.width):
                row.append(False)
            self.board.append(row)

        # Add mines randomly
        while len(self.mines) != mines:
            i = random.randrange(height)
            j = random.randrange(width)
            if not self.board[i][j]:
                self.mines.add((i, j))
                self.board[i][j] = True

        # At first, player has found no mines
        self.mines_found = set()

    def print(self):
        """Prints a text-based representation of where mines are located."""

        for i in range(self.height):
            print("--" * self.width + "-")
            for j in range(self.width):
                if self.board[i][j]:
                    print("|X", end="")
                else:
                    print("| ", end="")
            print("|")
        print("--" * self.width + "-")

    def is_mine(self, cell):
        i, j = cell
        return self.board[i][j]

    def nearby_mines(self, cell):

        # Keep count of nearby mines
        count = 0

        # Loop over all cells within one row and column
        for i in range(cell[0] - 1, cell[0] + 2):
            for j in range(cell[1] - 1, cell[1] + 2):

                #ignore clicked cell
                if (i, j) == cell:
                    continue

                #----------------------------------------------------------
                #ignore clicked cell's diagonal cells
                elif (i+1, j+1) == cell:
                    continue
                elif (i+1, j-1) == cell:
                    continue
                elif (i-1, j-1) == cell:
                    continue
                elif (i-1, j+1) == cell:
                    continue
                #----------------------------------------------------------

                # Update count if cell in bounds and is mine
                if 0 <= i < self.height and 0 <= j < self.width:
                    if self.board[i][j]:
                        count += 1

        return count

    def won(self):
        return self.mines_found == self.mines


class Sentence():

    def __init__(self, cells, count):
        self.cells = set(cells)
        self.count = count

    def __eq__(self, other):
        return self.cells == other.cells and self.count == other.count

    def __str__(self):
        return f"{self.cells} = {self.count}"
#-------------------------------------------------------------------------------
    def known_mines(self):
        #cell e jara jara unexplored tara
        Unknown_state_neighbourCells = len(self.cells)
        number_of_neighbour_mines    = self.count

        if Unknown_state_neighbourCells == number_of_neighbour_mines:
            return self.cells

        return None


    def known_safes(self):

        number_of_neighbour_mines = self.count

        if number_of_neighbour_mines == 0:
            return self.cells

        return None
#-------------------------------------------------------------------------------

    def mark_mine(self, cell):

        discovered_mine = self.cells

        if cell in discovered_mine:
            self.cells.remove(cell)
            self.count -= 1


    def mark_safe(self, cell):

        already_safe = self.cells

        if cell in already_safe:
            self.cells.remove(cell)
            


class MinesweeperAI():
    """Minesweeper game player"""

    def __init__(self, height=8, width=8):

        self.height = height
        self.width = width

        self.moves_made = set()

        self.mines = set()
        self.safes = set()

        self.knowledge = []
#----------------------------------------------------------------------------------
    def mark_mine(self, cell):

        new_mine_cell = cell
        
        # new exploration er knowledge e add kore rakhlam
        for knowledge in self.knowledge:
            knowledge.mark_mine(new_mine_cell)

        #mine e add kore rakhlam jaate new kore explore na kora lage
        self.mines.add(cell)

    def mark_safe(self, cell):
        new_safe_cell = cell

        # new exploration er knowledge e add kore rakhlam
        for knowledge in self.knowledge:
            knowledge.mark_safe(new_safe_cell)
        
        #mine e add kore rakhlam jaate new kore explore na kora lage
        self.safes.add(cell)
#-----------------------------------------------------------------------------------
    def add_knowledge(self, cell, count):

        self.moves_made.add(cell)
        self.mark_safe(cell)
        cells = set()

        for i in range(cell[0] - 1, cell[0] + 2):
            for j in range(cell[1] - 1, cell[1] + 2):

                #ignore clicked cell
                if (i, j) == cell:
                    continue
                #ignore clicked cell's diagonal cells
                #--------------------------------------------
                elif (i+1, j+1) == cell:
                    continue
                elif (i+1, j-1) == cell:
                    continue
                elif (i-1, j-1) == cell:
                    continue
                elif (i-1, j+1) == cell:
                    continue
                #--------------------------------------------
                if 0 <= i < self.height and 0 <= j < self.width:
                    #unknown unexplored cells
                    if (i, j) not in self.moves_made and (i, j) not in self.mines:
                        cells.add((i, j))
                    
                    #as amra already mine er hishab kore felsi
                    elif (i, j) in self.mines:
                        count -= 1

        self.knowledge.append(Sentence(cells, count))

        for one in self.knowledge:
            safes = one.known_safes()
            if safes:
                for cell in safes.copy():
                    self.mark_safe(cell)
            mines = one.known_mines()
            if mines:
                for cell in mines.copy():
                    self.mark_mine(cell)

        for one in self.knowledge:
            for another in self.knowledge:

                #one colliding with ownself
                if one is another:
                    continue

                #when different knowledge but same
                if one == another:
                    self.knowledge.remove(another)

                #when one is subset of other
                elif one.cells.issubset(another.cells):

                    new_count = another.count - one.count
                    new_cells = another.cells - one.cells

                    new_knowledge = Sentence(new_cells,new_count)
                    #check if already here or not
                    if new_knowledge not in self.knowledge:
                        self.knowledge.append(new_knowledge)

#------------------------------------------------------------------------------
    def make_safe_move(self):

        total_done = self.moves_made
        safe_steps = self.safes
        left_steps = safe_steps - total_done

        if left_steps:
            (i,j) = random.choice(tuple(left_steps))
            return (i,j)
        return None

#-------------------------------------------------------------------------------
    def make_random_move(self):

        mines       = len(self.mines)
        safe        = len(self.moves_made)
        total_cells = self.height * self.width

        if safe + mines == total_cells:
            return None

        while True:
            i = random.randrange(self.height)
            j = random.randrange(self.width)

            possible_move = (i, j) not in self.moves_made and (i, j) not in self.mines
            if possible_move:
                return (i, j)
