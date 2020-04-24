import java.util.Arrays;

class WHPP
{
    public static void main(String[] args)
    {
        // GeneticAlgorithm();

        // Population initial_population = new Population();

        // System.out.println("Best score:   " + initial_population.getBestScore() );
        // System.out.println("Num feasible: " + initial_population.getNumFeasible() );
        // System.out.println("Num gen:      " + initial_population.getNumGeneration() );
        // System.out.println("Size:         " + initial_population.getSize() );

        // initial_population.ExponentialRankSelection();
        // initial_population.TournamentSelection(0.2);

        Population.setStartSize(100);
        Population pop = new Population();
        Chromosome [] parents = pop.TournamentSelection(0.2);

        Chromosome father = parents[0];
        father.CalculateScore();
        Chromosome mother = parents[1];
        mother.CalculateScore();

        Chromosome child = mother.RandomColumnCrossing( father , 0.5 );
        child.SwapMutation(0.01);

        System.out.println("child:    " + child.IsFeasible() + "   " + child.CalculateScore());
        System.out.println( " " + Arrays.deepToString( child.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );

        // child.ColumnInversionMutation( 0.1d );

        // System.out.println("father:   " + father.IsFeasible() + "   " + father.CalculateScore());
        // System.out.println( " " + Arrays.deepToString( father.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );
        
        // System.out.println("mother:   " + mother.IsFeasible() + "   " + mother.CalculateScore());
        // System.out.println( " " + Arrays.deepToString( mother.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );
    }


    /* Skeletos genetikou algori8mou */
    public static void GeneticAlgorithm()
    {
        Population pop = new Population();

        boolean completed = false;

        pop.CheckFeasibility();
        pop.CalculateScore();

        while ( !completed )
        {
            Population next_pop =  new Population( pop.getNumGeneration() );

            for ( int i = 0 ; i < pop.getSize() / 2 ; i++ )
            {
                Chromosome [] parents = pop.ExponentialRankSelection( 0.99 );         // epilogh xromosomatwn
                Chromosome child = parents[0].MeritCollumnCrosover( parents[1] );    // diastaurwsh kai dhmiourgia neou xrwmosomatos

                child.ColumnInversionMutation( 0.01d );                                        // metalaksh tou
                
                child.IsFeasible();                                             // elenxos sunepeias
                next_pop.AddChromosome( child );                                // eisagwgh sth nea genia
            }

            if ( next_pop.IsTerminationValid() )
                completed = true;
        }
    } 
}