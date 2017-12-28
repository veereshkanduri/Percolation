import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private Boolean[][] grid;
    private int N;
    private final int top;
    private final int bottom;
    private int size ;
    private WeightedQuickUnionUF wqu = null;

    public Percolation(int n) {                   // create n-by-n grid, with all sites blocked
        if (n>0){
            N = n;
            size = N*N+2;
            wqu = new WeightedQuickUnionUF(size);

            this.top = 0;
            this.bottom = (N*N)+1;

            grid = new Boolean[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    grid[i][j] = false;                      // grid=0 indicates closed
                }
            }
        } else{
            throw new IllegalArgumentException("The size of percolation system should be greater than 0");
        }

    }

    public boolean isOpen(int row, int col){    // is site (row, col) open?
        if (row>0 && row<N+1 && col>0 && col<N+1){
            return grid[row-1][col-1]==true;
        }
        else {
            throw new IllegalArgumentException("row and col indices should be within 1 and "+N);
        }
    }

    public boolean isFull(int row, int col){    // is site (row, col) open?
        if (row>0 && row<N+1 && col>0 && col<N+1){
            return wqu.connected(top,xyto1D(row,col));
        }
        else {
            throw new IllegalArgumentException("row and col indices should be within 1 and "+N);
        }
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i=0;i<N;i++){
            for (int j = 0; j < N; j++) {
                if (grid[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }


    public void open(int x, int y) {         // open site (row, col) if it is not open already
        if (x>0 && x<N+1 && y>0 && y<N+1){
            int row = x-1;
            int col = y-1;
            grid[row][col] = true;
            if (x == 1) {
                //connect to top element
                wqu.union(top, xyto1D(x, y));
                if (isOpen(x+1,y)){
                    wqu.union(xyto1D(x,y),xyto1D(x+1,y));
                }
            }
            if (x == N) {
                //comnnect to bottom element
                wqu.union(xyto1D(x, y), bottom);
                if (isOpen(x-1,y)){
                    wqu.union(xyto1D(x,y),xyto1D(x-1,y));
                }
            }
            if (x>1 && x<N){
                //check (row-1,col) is open, if open connect the wqu object
                //Current indices are: row+1, col
                // UP direction
                if (isOpen(x-1,y)) {
                    wqu.union(xyto1D(x, y), xyto1D(x-1,y));
                }
                //check (row+1,col) is open, if open connect the wqu object
                //DOWN direction
                if (isOpen(x+1,y)) {
                    wqu.union(xyto1D(x,y), xyto1D(x+1,y));
                }
            }

            if (y > 1) {
                //check (row,col-1) is open, if open connect the wqu object
                //Left side
                if(isOpen(x,y-1)){
                    wqu.union(xyto1D(x,y),xyto1D(x,y-1));
                }
            }
            if (y < N ) {
                //check (row,col+1) is open, if open connect the wqu object
                //Right side
                if (isOpen(x,y+1)){
                    wqu.union(xyto1D(x,y),xyto1D(x,y+1));
                }
            }

        }
        else {
            throw new IllegalArgumentException("row and col indices should be within 1 and "+N);
        }

    }

    public boolean percolates(){
        return wqu.connected(top,bottom);
    }

    private int xyto1D(int x, int y){
        int row = x-1;
        int col = y-1;
        return (row*N)+(col+1);
    }

    private int[] getRandom(){
        int row = StdRandom.uniform(N);   //gives a random number from 0 to size-1
        int col = StdRandom.uniform(N);
        int[] gridIndices = new int[2];
        while(isOpen(row+1,col+1)){
            row = StdRandom.uniform(N);
            col = StdRandom.uniform(N);
        }
        gridIndices[0] = row;
        gridIndices[1] = col;
        return gridIndices;
    }
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        double counter = 0;
        Percolation percs = new Percolation(size);
        while(!percs.percolates()){
            int[] gridIndices = new int[2];
            gridIndices = percs.getRandom();
            int row = gridIndices[0];
            int col = gridIndices[1];
            counter++;
            percs.open(row+1,col+1);

        }
        System.out.println("System percolates after " + counter+" attempts.");
    }
}
