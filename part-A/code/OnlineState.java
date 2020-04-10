
    // Returns a list of actions allowed in this state 
    private ArrayList<Integer> Actions()
    {
        ArrayList<Integer> actions = new ArrayList<Integer>();

        if ( this.IsValidMove( 01 , true ) != 0 )
            actions.add(01);

        if ( this.IsValidMove( 10 , true ) != 0 )
            actions.add(10);

        if ( this.IsValidMove( -01 , true ) != 0 )
            actions.add(-01);

        if ( this.IsValidMove( -10 , true ) != 0 )
            actions.add(-10);

        return actions;
    }


    public State LRTAstar()
    {
        //int [] position = IdxToPos( this.position_idx , grid.getNumOfColumns() );
        this.expected_cost = ManhattanDistance( grid.getStart() , grid.getTerminal() );

        LinkedList<Integer> move_list = new LinkedList<Integer>();

        State parent = this;
        while ( !parent.IsGoalState() )
        {
            // ftiaxnw paidia
            parent.CreateChildren( true );

            // psaxnw to f8inotero
            int min_cost = Integer.MAX_VALUE;
            State min_cost_child = null;
            for ( int i = 0 ; i < parent.children.size() ; i++ ) 
            {
                // upologizo to expected kostos ka8e paidiou
                int [] child_pos = IdxToPos( this.position_idx , grid.getNumOfColumns() );
                parent.children.get(i).expected_cost = ManhattanDistance( child_pos , grid.getTerminal() ); // an den exei timh
                // krataw to f8inotero paidi
                if ( parent.children.get(i).expected_cost < min_cost )
                {
                    min_cost = parent.children.get(i).expected_cost;
                    min_cost_child = parent.children.get(i);
                }
            }
            // to f8hnotero paidi einai pio akribo, ara to pragmatiko kostos einai megalutero kata toulaxiston thn kinhsh gia to f8hnotero paidi
            if ( min_cost >= parent.expected_cost )
            {
                int [] next_position = IdxToPos( min_cost_child.position_idx , grid.getNumOfColumns() );
                parent.expected_cost = min_cost + grid.getCell( next_position[0] , next_position[1] ).getCost();
            }
            // paw sto epomeno state
            parent = min_cost_child;
        }
        return parent;
    }
