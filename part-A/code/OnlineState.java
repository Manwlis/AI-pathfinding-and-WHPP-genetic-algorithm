import java.util.ArrayList;
import java.util.LinkedList;

public class OnlineState extends State
{
    private int [] position = new int [2];

    private static OnlineState [][] state_map;

    private int cell_cost;

    // kinhseis pou exoun ginei kai to plh8os tous
    private static LinkedList<Integer> positions_history = new LinkedList<Integer>();
    private static int num_moves;

    public int getNumMoves(){ return num_moves; }
    public LinkedList<Integer> GetPositionsHistory(){ return positions_history; }
    /******************************/
    /******** Constructors ********/
    /******************************/

    // constructor gia to state ths afethrias
    public OnlineState( Grid mygrid )
    {
        grid = mygrid;
        position_idx = grid.getStartidx();
        position = grid.getStart();

        state_map = new OnlineState [ grid.getNumOfRows() ] [ grid.getNumOfColumns() ];
        state_map[ position[0] ][ position[1] ] = this;

        expected_cost = ManhattanDistance( position , grid.getTerminal() );
        cell_cost = grid.getCell( position[0] , position[1] ).getCost();
        
        num_moves = 0;
    }

    // constructor gia tis upoloipes 8eseis
    public OnlineState(int [] new_position , int previous_cost )
    {
        position_idx = PosToIdx( new_position , grid.getNumOfColumns() );
        position = new_position;

        state_map[ new_position[0] ][ new_position[1] ] = this;


        // ftiaxnetai afou pame ekei kai mporoume na doume to kostos
        cell_cost = grid.getCell( position[0] , position[1] ).getCost();
        if (cell_cost > 100000) // band_aid gia integer overflow. To cell cost tou wall einai max_int kai an tou proste8ei kati paei se arnhtikh timh
            cell_cost = 100000;
        

        accumulated_cost = cell_cost + previous_cost;
        
        expected_cost = ManhattanDistance( position , grid.getTerminal() );
    }

    
    /******************************/
    /********** me8odoi ***********/
    /******************************/

    /* Dexetai ws eisodo mia kinhsh, epistrefei 1 einai valid, alliws 0.*/
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

        /*------------------ elenxos ekgurothtas neas 8eshs ------------------*/
        // ektos oriwn
        if ( new_position[0] < 0 || new_position[0] >= num_rows )
            return 0;
        if ( new_position[1] < 0 || new_position[1] >= num_columns )
            return 0;
        // gnwrizw oti einai teixos
        if ( state_map[ new_position[0] ][ new_position[1] ] != null)
        {
            if ( grid.getCell( new_position[0] , new_position[1] ).isWall() )
                return 0;
        }

        return 1;
    }

    /* Returns a list of actions allowed in this state. */
    private ArrayList<Integer> Actions()
    {
        ArrayList<Integer> actions = new ArrayList<Integer>();

        if ( this.IsValidMove( 01 ) != 0 )
            actions.add(01);

        if ( this.IsValidMove( 10 ) != 0 )
            actions.add(10);

        if ( this.IsValidMove( -01 ) != 0 )
            actions.add(-01);

        if ( this.IsValidMove( -10 ) != 0 )
            actions.add(-10);

        return actions;
    }

    
    public OnlineState LRTAstar()
    {
        int current_pos[] = this.position.clone();

        OnlineState current_state = this;
        boolean first_time = true;
        // oso den exei ftasei sto terma
        while ( !current_state.IsGoalState() )
        {
            // koitaei poies kiniseis mporoun na ginoun
            ArrayList<Integer> valid_actions = current_state.Actions();

            int [] neighbours_expected_cost = new int [ valid_actions.size() ];
            int best_neighbour_move = 0;
            int best_neighbour_cost = Integer.MAX_VALUE;

            for ( int i = 0 ; i < valid_actions.size() ; i++ )
            {
                // upologismos kostous geitonwn
                neighbours_expected_cost[i] = current_state.LRTAstar_cost( valid_actions.get(i) );

                // krateitai h f8inoterh apo autes
                if ( neighbours_expected_cost[i] < best_neighbour_cost )
                {
                    best_neighbour_move = valid_actions.get(i);
                    best_neighbour_cost = neighbours_expected_cost[i];
                }
            }

            // update expected kostous 8eshs pou brisketai twra
            if ( best_neighbour_cost > current_state.expected_cost )
                current_state.expected_cost = best_neighbour_cost;
            
            // kane thn kinhsh
            int [] old_pos = current_pos.clone();
            int [] move = IdxToPos( best_neighbour_move , 10 );
            current_pos[0] = current_pos[0] + move[0];
            current_pos[1] = current_pos[1] + move[1];
            num_moves++;

            // uparxei state gia auton ton geitona?
            if ( state_map[ current_pos[0] ][ current_pos[1] ] != null)
            {
                first_time = false;
                current_state = state_map[ current_pos[0] ][ current_pos[1] ]; // an nai pigenei se auto
            }
            else
            {
                first_time = true;
                current_state = new OnlineState( current_pos , current_state.accumulated_cost ); // an oxi to ftiaxnei kai pigenei se auto
            }
            // an phge se toixo gurnaei pisw
            if ( grid.getCell( current_pos[0] , current_pos[1] ).isWall() )
            { 
                current_pos[0] = old_pos[0];
                current_pos[1] = old_pos[1];
                current_state = state_map[ current_pos[0] ][ current_pos[1] ];
                num_moves++;
                continue;
            }
            // an phge se xorta kai den exei ekserevnisei tous ipoloipous geitones paei pisw
            else if ( first_time == true && grid.getCell( current_pos[0] , current_pos[1] ).isGrass()
                    && !state_map[ old_pos[0] ][ old_pos[1] ].IsFullyExplored( old_pos , valid_actions ) )
            {
                current_pos[0] = old_pos[0];
                current_pos[1] = old_pos[1];
                current_state = state_map[ current_pos[0] ][ current_pos[1] ];
                num_moves++;
                continue;
            }
            // mpainei h kainourgia 8esh sto istoriko
            positions_history.add( PosToIdx( current_pos , grid.getNumOfColumns() ) );
        }
        return current_state;
    }


    private int LRTAstar_cost( int move_idx )
    {
        // upologismos 8eshs pou 8a me paei h kinhsh
        int [] move = IdxToPos( move_idx , 10 );
        int [] new_position = new int[2];
        new_position[0] = this.position[0] + move[0];
        new_position[1] = this.position[1] + move[1];

        // uparxei state gia authn th 8esh?
        // an oxi epestrepse manhattan ths
        if ( state_map[ new_position[0] ][ new_position[1] ] == null)
            return ManhattanDistance( new_position , grid.getTerminal() );

        // an nai epestrepse move cost + expected cost
        return state_map[ new_position[0] ][ new_position[1] ].expected_cost + state_map[ new_position[0] ][ new_position[1] ].cell_cost;
    }


    /* an kapios geitonas einai aneksereunhtos epistrefei false, alliws true */
    private boolean IsFullyExplored( int [] old_pos , ArrayList<Integer> valid_actions )
    {
        for ( int i = 0 ; i < valid_actions.size() ; i++ )
        {
            int [] move = IdxToPos( valid_actions.get(i) , 10 );
            int [] neighbour_pos = new int[2];
            neighbour_pos[0] = old_pos[0] + move[0];
            neighbour_pos[1] = old_pos[1] + move[1];

            if ( state_map[ neighbour_pos[0] ][ neighbour_pos[1] ] == null)
            {
                return false;
            }
        }
        return true;
    }

    /* Ka8arizei apo to monopati ta loops. Epishs Epistrefei to kostos tou telikou monopatiou */
    public int ExtractPath()
    {
        int i = 0 ;
        while (  i < positions_history.size() - 1)
        {
            boolean brhke = false;
            for ( int j = i + 1 ; j < positions_history.size() - 1 ; j++ )
            {
                if ( positions_history.get(i).equals( positions_history.get(j) ) )
                {
                    brhke = true;
                    positions_history.subList(i,j).clear();
                }
            }
            // afereitai to prwto loop pou ksekinaei apo auto to shmeio. Ksanaelenxetai giati mporei na exei kai alla
            if ( !brhke )
                i++;
        }

        // ipologismos kostous
        int cost = 0;
        for ( i = 0 ; i < positions_history.size() ; i++)
        {
            int [] pos = IdxToPos( positions_history.get(i) , grid.getNumOfColumns() ); 
            cost = cost + grid.getCell( pos[0] , pos[1] ).getCost();
        }
        return cost;
    }
}

