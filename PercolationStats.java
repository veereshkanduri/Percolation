import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int size;
    private int size2;
    private double arr[];
    private double counter;
    public PercolationStats(int n, int trials){     // perform trials independent experiments on an n-by-n grid
        if(n>0 && trials>0){
            arr = new double[trials];
            size = n;
            size2 = size*size;
            for (int x = 0;x<trials;x++){
                //System.out.println("entered for loop");
                counter = 0;
                Percolation percs = new Percolation(size);
                while(!percs.percolates()){
                    int row = StdRandom.uniform(size);   //gives a random number from 0 to N-1
                    int col = StdRandom.uniform(size);
                    if(!percs.isOpen(row+1,col+1)){
                        counter++;
                        percs.open(row+1,col+1);
                        if (percs.percolates()){
                            arr[x] = counter/size2;
                        }
                    }

                }
            }
        }
        else {
            throw new IllegalArgumentException("Size and Trials should be greater than 0");
        }

    }

    public double mean(){                           // sample mean of percolation threshold
        return StdStats.mean(arr);
    }
    public double stddev(){                         // sample standard deviation of percolation threshold
        return StdStats.stddevp(arr);
    }
    public double confidenceLo(){                    // low  endpoint of 95% confidence interval
        return mean() - (1.96*stddev())/Math.sqrt(arr.length);
    }
    public double confidenceHi(){                   // high endpoint of 95% confidence interval
        return mean() + (1.96*stddev())/Math.sqrt(arr.length);
    }

    public static void main(String[] args){         // test client (described below)
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),Integer.parseInt(args[1]));

        System.out.println("mean                        = "+ ps.mean());
        System.out.println("stddev                      = "+ps.stddev());
        System.out.println("95% confidence interval     = ["+ps.confidenceLo()+", "+ps.confidenceHi()+"]");
    }
}
