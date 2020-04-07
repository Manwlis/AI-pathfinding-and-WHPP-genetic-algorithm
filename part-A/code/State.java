import java.util.LinkedList;
import java.util.stream.IntStream;

// 
public class State 
{
    private State parent; // goneas
    private LinkedList<State> children = new LinkedList<>();

    private Grid grid; // to grid tou problhmatos. Blepoun to idio oles oi katastaseis alla den to allazoun
    private int  position_idx; // 8esh panw sto grid

    private int [] old_positions_idx; // 8umatai tis pallies 8eseis giati den 8elw na ksanapernaei apo ta idia tetragwna
    
    private int depth; // Deixnei poso ba8u exei ginei to dentro
    private int accumulated_cost; // kostos mexri stigmhs

    private static int min_cost_solution = Integer.MAX_VALUE;
    private static int num_states = 1; // ksekinaei apo to 1 gia na pernei ipopsin to root

    public static int blocked = 0;

    // create root
    public State( Grid grid )
    {
        this.parent = null; // root den exei gonea

        this.grid = grid;

        this.position_idx = grid.getStartidx();

        this.old_positions_idx = new int[ grid.getNumOfColumns() * grid.getNumOfRows() + 1 ]; //init with zeros by default

        this.depth = 0; // mipws na ksekinaei apo to 1. Alliws opios pinakas ftiaxnetai bash autou 8elei + 1
        this.accumulated_cost = 0;
    }

    // create non-root node
    public State( State parent , int move_idx , int move_cost )
    {
        this.grid = parent.grid;

        // sundesh paidiou kai patera
        this.parent = parent;
        parent.children.add( this );

        // ipologismos neas 8eshs
        int [] new_position = new int [2];
        int [] move = new int [2];

        new_position = IdxToPos( parent.position_idx , this.grid.getNumOfColumns() ).clone();
        move = IdxToPos( move_idx , 10 ).clone();

        new_position[0] = new_position[0] + move[0];
        new_position[1] = new_position[1] + move[1];

        this.position_idx = PosToIdx( new_position , this.grid.getNumOfColumns() );

        // pairnei apo ton patera ton pinaka me tis prohgoumenes 8eseis. Bazei kai auton mesa.
        this.old_positions_idx = parent.old_positions_idx.clone();
        this.old_positions_idx[ parent.depth ] = parent.position_idx;

        // ipologismos neou ba8ous kai kostous
        this.depth = parent.depth + 1;
        this.accumulated_cost = parent.accumulated_cost + move_cost;

        num_states++;
    }

    public boolean IsGoalState() { return ( this.grid.getTerminalidx() == this.position_idx ); }


    /* Elenxei an mia kinhsh einai egkurh. Epistrefei to kostos ths an einai, alliws 0.  */
    private int IsValidMove( int move_idx )
    {
        int num_columns = this.grid.getNumOfColumns();
        int num_rows = this.grid.getNumOfRows();

        /*---------------------- Upologismos neas 8eshs ----------------------*/
        // metatroph 8eshs kai kinhshs se [i][j] morfh
        int [] new_position = new int [2];
        int [] move = new int [2];
        new_position = IdxToPos( this.position_idx , num_columns ).clone();
        move = IdxToPos( move_idx , 10 ).clone();

        // ipologismos neas 8eshs
        new_position[0] = new_position[0] + move[0];
        new_position[1] = new_position[1] + move[1];

        // metatroph neas 8eshs se index morfh
        int new_position_idx = PosToIdx( new_position , num_columns );

        /*------------------ elenxos ekgurothtas neas 8eshs ------------------*/
        // ektos oriwn
        if ( new_position[0] < 0 || new_position[0] >= num_rows )
            return 0;
        if ( new_position[1] < 0 || new_position[1] >= num_columns )
            return 0;
        // lrta* mporei na to xrhsimopoieisei auto????
        // blepei an xtupaei toixo
        if ( this.grid.getCell( new_position[0] , new_position[1] ).isWall() )
            return 0;
        // exei ksanaepiskeu8ei authn th 8esh sto idio monopati
        if ( IntStream.of( this.old_positions_idx ).anyMatch(x -> x == new_position_idx ) )
            return 0;
        // exei bre8ei f8inoterh lush. Den xreiazetai na sunexistei auto to monopati.
        // +1 giati einai to mikrotero dunato kostos gia mia kinish. Dn bazw to pragmatiko kostos ths kinhshs gia na mporei na xrhsimopoiei8ei ston LRTA*
        if ( this.accumulated_cost + 1 >= min_cost_solution )
            return 0;

        return this.grid.getCell( new_position[0] , new_position[1] ).getCost();
    }

    /* elenxei an oles oi pi8anes einai egkures, kai dhmiourgei kombous gia autes pou einai *
     * pi8anes kiniseis: panw +01, deksia +10, katw -01, aristera -10                       */
    private void CreateChildren()
    {
        // kinish pros ta panw
        int cost = this.IsValidMove( 01 );
        if ( cost != 0 )
        {
            State child = new State( this , 01 , cost );
        }
        // deksia
        cost = this.IsValidMove( 10 );
        if ( cost != 0 )
        {
            State child = new State( this , 10 , cost );
        }
        // katw
        cost = this.IsValidMove( -01 );
        if ( cost != 0 )
        {
            State child = new State( this , -01 , cost );
        }
        // aristera
        cost = this.IsValidMove( -10 );
        if ( cost != 0 )
        {
            State child = new State( this , -10 , cost );
        }
    }


    // isws ta pia exoun episkeu8ei xreiazetai mono se auton ton algori8mo. Tote na metafer8ei to [] old_positions_idx edw. Den nomizw na isxuei.
    // xreiazetai allages gia na katalabainei ta kosth.
    public State BFS()
    {
        // root is goal state
        if ( this.IsGoalState() ){
            min_cost_solution = this.accumulated_cost;
            return this;
        }

        // kataskeuh FIFO ouras. Mpainei h riza.
        LinkedList<State> frontier = new LinkedList<>();
        frontier.add( this );

        State goal_state = null;
        while ( !frontier.isEmpty() )
        {
            // bgainei h prwth katastash apo to frontier kai psaxnw gia paidia ths
            State parent = frontier.removeFirst();
            parent.CreateChildren();

            // elenxw ola ta paidia an einai o stoxos. An einai to epistrefw, alliws mpainoun sto frontier
            for( int i = 0 ; i < parent.children.size() ; i++ )
            {
                if ( parent.children.get(i).IsGoalState() )
                {
                    min_cost_solution = parent.children.get(i).accumulated_cost;
                    // mpainei se mia metablhth kai ginetai return sto telos, wste na antikatasta8ei an bre8ei f8inoterh lhsh sto mellon
                    goal_state = parent.children.get(i);
                }
                // an ena monopati exei ftasei sto goal den 8elw na sunexisei. Gia auto den mpainei to state sto frontier
                else
                {
                    frontier.add( parent.children.get(i) );
                }
            }
        }
        return goal_state;
    }


    // epistrefei ena pinaka me indexes pou perigrafoun to monopati ths lhshs
    public int[] ExtractSolution( )
    {
        int [] steps = new int[this.depth];
        State node = this;

        for ( int i = 0 ; i < this.depth ; i++)
        {
            steps[ i ] = node.position_idx;
            node = node.parent;
        }
        return steps;
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

    public int getNum_states(){ return num_states; }
    public int getAccumulated_cost(){ return accumulated_cost; }

    // gia testarisma optimizations
    public int getBlocked(){ return blocked; }
}