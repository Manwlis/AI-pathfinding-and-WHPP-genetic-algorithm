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

    private void IsGoalState(){

    }

    private void IsValidAction(){

    }

    private void CreateChildren(){

    }

    public void SearchGoal(){

    }

    private void ExtractSolution(){

    }

}