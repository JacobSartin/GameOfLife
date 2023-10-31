// This class implements the logic of Conway's Game of Life.
// there are 4 rules to Conway's Game of Life
// 1. Each cell with 1 or fewer neighbors dies, as if by solitude.
// 2. Each cell with 4 or more neighbors dies, as if by overpopulation.
// 3. Each cell with 2 or 3 neighbors survives.
// 4. Each cell with 3 neighbors becomes populated.
public class GameOfLife {
    private boolean[][] grid;

    // randomly populate the grid
    public GameOfLife(int width, int height) {
        grid = new boolean[width][height];
        populate();
    }

    // use this constructor to create a custom starting grid 
    // from ints because that's easier to type
    public GameOfLife(int[][] grid) {
        this.grid = new boolean[grid.length][grid[0].length];
        // copy the values from grid to this.grid
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++)
                this.grid[x][y] = (grid[x][y] == 1);
        }
    }

    // use this constructor to create a custom starting grid
    // for GUI input
    public GameOfLife(boolean[][] grid) {
        this.grid = grid;
    }

    //accessor method for grid so that we can draw it in GamePanel
    public boolean[][] getGrid() {
        return grid;
    }

    private void populate() {
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                if (Math.random() < 0.1)
                    grid[x][y] = true;
            }
        }
    }

    public void tick() {
        boolean[][] next = new boolean[grid.length][grid[0].length];

        // loop through every cell and apply the rules
        // save the result in next[][]
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                int neighbors = countNeighbors(x, y);
                next[x][y] = findNextState(x, y, neighbors);
            }
        }
        // once we have found the next generation, we can update the grid
        grid = next;
    }

    // count the number of neighbors a cell at x, y has
    private int countNeighbors(int x, int y) {
        int sum = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                // ignore invalid indexes
                if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length)
                    if (grid[i][j])
                        sum++;
            }
        }
        if (grid[x][y])
            sum--;
        return sum;
    }

    // apply the rules to the cell at x, y
    private boolean findNextState(int x, int y, int neighbors) {
        if (grid[x][y]) {
            // if the cell has 1 or fewer neighbors, it dies per rule 1
            // or if the cell has 4 or more neighbors, it dies per rule 2
            if (neighbors < 2 || neighbors > 3)
                return false;
            // if the cell has 2 or 3 neighbors, it survives per rule 3
            else
                return true;
        }
        // if the cell is empty and has 3 neighbors, it becomes populated per rule 4
        else if (!grid[x][y] && neighbors == 3) {
            return true;
        }
        return false;
    }

    // print the grid line by line
    public String toString() {
        String s = "";
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++) {
                s += grid[x][y] + " ";
            }
            s += "\n";
        }
        return s;
    }
}