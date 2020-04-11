import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class OfflineState extends State
{
    private OfflineState parent;
    private LinkedList<OfflineState> children = new LinkedList<>();

    // 8umatai tis pallies 8eseis giati den 8elw na ksanapernaei apo ta idia tetragwna sto idio monopati
    private ArrayList<Integer> old_positions_idx; 
    
    private int depth; // Deixnei poso ba8u exei ginei to dentro

    private static int min_cost_solution = Integer.MAX_VALUE;
    private static int num_states = 1; // ksekinaei apo to 1 gia na pernei ipopsin to root

    // krataei to f8hnotero perasma apo ka8e 8esh
    private static int[] pos_min_cost;


    /******************************/
    /******** Constructors ********/
    /******************************/

    // create root
    public OfflineState( Grid my_grid )
    {
        this.parent = null; // root den exei gonea

        grid = my_grid;

        this.position_idx = grid.getStartidx();

        this.old_positions_idx = new ArrayList<Integer>(); //init with zeros by default
        this.old_positions_idx.add( this.position_idx ); // mpainei h riza

        this.depth = 0; // mipws na ksekinaei apo to 1. Alliws opios pinakas ftiaxnetai bash autou 8elei + 1
        this.accumulated_cost = 0;

        pos_min_cost = new int [ grid.getNumOfColumns() * grid.getNumOfRows() ];
        Arrays.fill( pos_min_cost , Integer.MAX_VALUE );
        pos_min_cost[ this.position_idx ] = 0;
    }

    // create non-root node
    public OfflineState( OfflineState parent , int move_idx , int move_cost )
    {
        // sundesh paidiou kai patera
        this.parent = parent;
        parent.children.add( this );

        // ipologismos neas 8eshs
        int [] new_position = new int [2];
        int [] move = new int [2];

        new_position = IdxToPos( parent.position_idx , grid.getNumOfColumns() ).clone();
        move = IdxToPos( move_idx , 10 ).clone();

        new_position[0] = new_position[0] + move[0];
        new_position[1] = new_position[1] + move[1];

        this.position_idx = PosToIdx( new_position , grid.getNumOfColumns() );

        // pairnei apo ton patera ton pinaka me tis prohgoumenes 8eseis. Bazei kai auton mesa.
        this.old_positions_idx = new ArrayList<Integer>( parent.old_positions_idx );
        this.old_positions_idx.add( this.position_idx );

        // ipologismos neou ba8ous kai kostous
        this.depth = parent.depth + 1;
        this.accumulated_cost = parent.accumulated_cost + move_cost;

        // to kostos tou monopatiou mexri na ftasei se authn th 8esh
        pos_min_cost[ this.position_idx ] = parent.accumulated_cost;

        num_states++;
    }

    // idia 8esh
    @Override
    public boolean equals( Object obj )
    {
        if ( !(obj instanceof OfflineState) )
            return false; 
        
        OfflineState other = (OfflineState) obj;
        if ( this.position_idx != other.position_idx )
            return false;

        return true;
    }


    /******************************/
    /******* Koines me8odoi *******/
    /******************************/


    /* Elenxei an mia kinhsh einai egkurh. Epistrefei to kostos ths an einai, alliws 0. * 
     * Dexetai ws eisodo thn kinhsh kai ton tupo ths anazhthshs.                        */
    protected int IsValidMove( int move_idx )
    {
        int num_columns = grid.getNumOfColumns();
        int num_rows = grid.getNumOfRows();

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
        // blepei an 8a xtuphsei teixo.
        if ( grid.getCell( new_position[0] , new_position[1] ).isWall() )
            return 0;
        // exei ksanaepiskeu8ei authn th 8esh sto idio monopati
        if ( this.old_positions_idx.contains( new_position_idx ) )
            return 0;
        // exei bre8ei f8inoterh lush. Den xreiazetai na sunexistei auto to monopati.
        // +1 giati einai to mikrotero dunato kostos gia mia kinish. Dn bazw to pragmatiko kostos ths kinhshs gia na mporei na xrhsimopoiei8ei ston LRTA*
        if ( this.accumulated_cost + CHEAPEST_MOVE >= min_cost_solution )
            return 0;
        // ena allo monopati exei ftasei se authn th 8esh me f8inotero h iso kostos.
        if ( this.accumulated_cost >= pos_min_cost[ new_position_idx ] )
            return 0;

        return grid.getCell( new_position[0] , new_position[1] ).getCost();
    }

    /* elenxei an oles oi pi8anes einai egkures, kai dhmiourgei kombous gia autes pou einai *
     * pi8anes kiniseis: panw +01, deksia +10, katw -01, aristera -10                       */
    private void CreateChildren()
    {
        // kinish pros ta panw
        int cost = this.IsValidMove( 01 );
        if ( cost != 0 )
        {
            OfflineState child = new OfflineState( this , 01 , cost );
        }
        // deksia
        cost = this.IsValidMove( 10 );
        if ( cost != 0 )
        {
            OfflineState child = new OfflineState( this , 10 , cost );
        }
        // katw
        cost = this.IsValidMove( -01 );
        if ( cost != 0 )
        {
            OfflineState child = new OfflineState( this , -01 , cost );
        }
        // aristera
        cost = this.IsValidMove( -10 );
        if ( cost != 0 )
        {
            OfflineState child = new OfflineState( this , -10 , cost );
        }
    }

    
    /******************************/
    /********* Algori8moi *********/
    /******************************/
    
    public OfflineState BFS()
    {
        // kataskeuh FIFO ouras. Mpainei h riza.
        LinkedList<OfflineState> frontier = new LinkedList<>();
        frontier.add( this );

        OfflineState goal_state = null;
        while ( !frontier.isEmpty() )
        {
            // bgainei h prwth katastash apo to frontier kai psaxnw gia paidia ths
            OfflineState parent = frontier.removeFirst();
            parent.CreateChildren();

            int num_children =  parent.children.size();
            // elenxw ola ta paidia an einai o stoxos. An einai to epistrefw, alliws mpainoun sto frontier
            for( int i = 0 ; i < num_children ; i++ )
            {
                if ( parent.children.getFirst().IsGoalState() )
                {
                    min_cost_solution = parent.children.getFirst().accumulated_cost;
                    // mpainei se mia metablhth kai ginetai return sto telos, wste na antikatasta8ei an bre8ei f8inoterh lhsh sto mellon
                    goal_state = parent.children.removeFirst();
                }
                // an ena monopati exei ftasei sto goal den 8elw na sunexisei. Gia auto den mpainei to state sto frontier
                else
                {
                    // mpainei sthn oura
                    frontier.add( parent.children.removeFirst() );
                }
            }
        }
        return goal_state;
    }

    public OfflineState DFS()
    {
        // kataskeuh stack. Mpainei h riza.
        LinkedList<OfflineState> frontier = new LinkedList<>();
        frontier.addFirst( this );

        OfflineState goal_state = null;
        while ( !frontier.isEmpty() )
        {
            // bgainei h prwth katastash apo to frontier kai psaxnw gia paidia ths
            OfflineState parent = frontier.removeFirst();

            parent.CreateChildren();

            int num_children =  parent.children.size();
            for( int i = 0 ; i < num_children ; i++ )
            {
                if ( parent.children.getFirst().IsGoalState() )
                {
                    min_cost_solution = parent.children.getFirst().accumulated_cost;
                    // mpainei se mia metablhth kai ginetai return sto telos, wste na antikatasta8ei an bre8ei f8inoterh lhsh sto mellon
                    goal_state = parent.children.removeFirst();                   
                }
                else
                {
                    // mpainei sto stack
                    frontier.addFirst( parent.children.removeFirst() );
                }
            }
        }
        return goal_state;
    }

    public OfflineState Astar()
    {
        LinkedList<OfflineState> open_list = new LinkedList<OfflineState>();
        LinkedList<OfflineState> closed_list = new LinkedList<OfflineState>();

        // bazei to root sthn anoixth lista
        open_list.add( this );

        while ( !open_list.isEmpty() )
        {
            OfflineState parent = open_list.remove();
            closed_list.add( parent );

            // eftase ton stoxo
            if ( parent.IsGoalState() )
                return parent;

            // ftiaxnei paidia
            parent.CreateChildren();

            int num_children =  parent.children.size();
            for ( int i = 0 ; i < num_children ; i++ )
            {
                OfflineState child = parent.children.removeFirst();

                // h 8esh einai sthn klisth lista
                if ( closed_list.contains( child ) )
                    continue;

                // upologismos expected kostous
                int heuristic_cost = ManhattanDistance( IdxToPos( child.position_idx , grid.getNumOfColumns() ) , grid.getTerminal() );
                child.expected_cost = child.accumulated_cost + heuristic_cost;

                int index = open_list.indexOf( child );
                // h 8esh einai sthn anoixth lista
                if ( index != -1 )
                {
                    // sth lista exei f8inotero monopati ara thn agnow
                    if ( child.expected_cost > open_list.get( index ).expected_cost )
                        continue;
                    else // sth lista exei akribotero monopati kai thn antika8istw
                        open_list.remove(index);
                }
                // mpainei to paidi sthn lista
                open_list.add( child );
            }
        }
        return null;
    }


    /***************************/
    /***** Utility methods *****/
    /***************************/

    public int getNum_states(){ return num_states; }

    // epistrefei ena pinaka me indexes pou perigrafoun to monopati ths lhshs
    public int[] ExtractSolution( )
    {
        int [] steps = new int[this.depth];
        OfflineState node = this;

        for ( int i = 0 ; i < this.depth ; i++)
        {
            steps[ i ] = node.position_idx;
            node = node.parent;
        }
        return steps;
    }
}