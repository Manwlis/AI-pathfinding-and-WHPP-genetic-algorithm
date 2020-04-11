
public abstract class State
{
    protected static Grid grid; // Koino gia oles tis katastaseis
    protected int position_idx; // 8esh panw sto grid

    protected int accumulated_cost; // kostos mexri stigmhs. g(n) gia A* kai LRTA*
    protected int expected_cost; // f(n) = g(n) + h(n) gia A* , h(s) gia LRTA*
    
    // 8ewrw oti h f8hnoterh kinhsh 8a einai se gh me timh 1.
    protected static int CHEAPEST_MOVE = 1;


    protected abstract int IsValidMove( int move_idx );

    
    /*                    heurestic function                    *
     * Briskei thn manhattan apostash metaksu duo shmeiwn.      *
     * 8ewrei oti oles oi kiniseis exoun to elaxisto kostos.    *
     * Ara, den kanei pote overstimate kai einai admissible.    */
    protected static int ManhattanDistance( int [] position , int [] target )
    {
        int dx = Math.abs(position[0] - target[0]);
        int dy = Math.abs(position[1] - target[1]);

        return CHEAPEST_MOVE * (dx + dy);
    }

    public boolean IsGoalState() { return ( grid.getTerminalidx() == this.position_idx ); }

    /***************************/
    /***** Utility methods *****/
    /***************************/

    // metatrepei mia 8esh apo morfh idx se morfh [i][j]
    protected static int[] IdxToPos( int idx , int num_columns )
    {
        int [] position = new int[2];
        position[0] = idx / num_columns;
        position[1] = idx % num_columns;
        return position;
    }
    // metatrepei mia 8esh apo morfh [i][j] se morfh idx
    protected static int PosToIdx( int [] position , int num_columns )
    {
        return position[0] * num_columns + position[1];
    }

    public int getAccumulated_cost(){ return accumulated_cost; }
}