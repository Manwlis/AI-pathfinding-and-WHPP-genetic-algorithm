import java.util.stream.IntStream;

// 
public class State 
{
    private State parent; // goneas

    private Grid grid; // to grid tou problhmatos. Blepoun to idio oles oi katastaseis alla den to allazoun
    private int  position_idx; // 8esh panw sto grid

    private int [] old_positions_idx; // 8umatai tis pallies 8eseis giati den 8elw na ksanapernaei apo ta idia tetragwna
    
    private int depth; // Deixnei poso ba8u exei ginei to dentro
    private int accumulated_cost; // kostos mexri stigmhs

    // create root
    public State( Grid grid )
    {
        this.parent = null; // root den exei gonea

        this.grid = grid;

        this.position_idx = grid.getStartidx();

        this.old_positions_idx = new int[ grid.getNumOfColumns() * grid.getNumOfRows() ]; //init with zeros by default

        this.depth = 0; // mipws na ksekinaei apo to 1. Alliws opios pinakas ftiaxnetai bash autou 8elei + 1
        this.accumulated_cost = 0;
    }

    // create non-root node
    public State( State parent , int new_position_idx , int move_cost )
    {
        this.parent = parent;

        this.grid = parent.grid;

        this.position_idx = new_position_idx;

        this.old_positions_idx = parent.old_positions_idx.clone(); // krataei tis palies 8eseis apo to patera
        this.old_positions_idx[ parent.depth ] = parent.position_idx; // dhlwnei oti exei episkeu8ei ton patera

        this.depth = parent.depth + 1;
        this.accumulated_cost = parent.accumulated_cost + move_cost;
    }

    private boolean IsGoalState()
    {
        return ( this.grid.getTerminalidx() == this.position_idx );
    }

    // possible moves +01 / +10 / -01 / -10
    // koitaei an mia kinhsh einai egkurh
    private boolean IsValidMove( int move_idx )
    {
        int num_columns = this.grid.getNumOfColumns();
        int num_rows = this.grid.getNumOfRows();

        // metatroph 8eshs kai kinhshs se [i][j] morfh
        int [] new_position = new int [2];
        int [] move = new int [2];
        new_position = IdxToPos( this.position_idx , num_columns ).clone();
        move = IdxToPos( move_idx , 10 ).clone();

        // ipologismos neas 8eshs
        new_position[0] = new_position[0] + move[0];
        new_position[1] = new_position[1] + move[1];
        int new_position_idx = PosToIdx( new_position , num_columns );

        // ektos oriwn
        if ( new_position[0] < 0 || new_position[0] >= num_columns )
        {
            return false;
        }
        if ( new_position[1] < 0 || new_position[1] >= num_rows )
        {
            return false;
        }

        // lrta* mporei na to xrhsimopoieisei auto????
        // blepei an xtupaei toixo
        if ( this.grid.getCell( new_position[0] , new_position[1] ).isWall() )
        {
            return false;
        }
        // exei ksanaepiskeu8ei authn th 8esh
        if ( IntStream.of( this.old_positions_idx ).anyMatch(x -> x == new_position_idx ) )
        {
            return false;
        }
        


        return true;
    }

    private void CreateChildren(){

    }

    public void SearchGoal(){

    }

    private void ExtractSolution(){

    }

    // metatrepei mia 8esh apo morfh idx se morfh [i][j]
    private int[] IdxToPos( int idx , int num_columns )
    {
        int [] position = new int[2];

        position[0] = idx / num_columns;
        position[1] = idx % num_columns;

        return position;
    }

    // metatrepei mia 8esh apo morfh [i][j] se morfh idx
    private int PosToIdx( int [] position , int num_columns )
    {
        return position[0] * num_columns + position[1];
    }
}