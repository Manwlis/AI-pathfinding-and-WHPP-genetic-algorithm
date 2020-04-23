import java.util.Arrays;

class WHPP
{
    public static void main(String[] args)
    {
        // GeneticAlgorithm();

        // Population.setStartSize(100); // pop
        // Population initial_population = new Population();

        // System.out.println("Best score:   " + initial_population.getBestScore() );
        // System.out.println("Num feasible: " + initial_population.getNumFeasible() );
        // System.out.println("Num gen:      " + initial_population.getNumGeneration() );
        // System.out.println("Size:         " + initial_population.getSize() );

        // initial_population.ExponentialRankSelection();
        //initial_population.TournamentSelection();


        Chromosome father = new Chromosome();
        father.FeasibleRandomInit();
        father.CalculateScore();
        Chromosome mother = new Chromosome();
        mother.FeasibleRandomInit();
        mother.CalculateScore();


        Chromosome child = mother.RandomColumnCrossing(father);

        System.out.println("father:   " + father.IsFeasible() + "   " + father.CalculateScore());
        System.out.println( " " + Arrays.deepToString( father.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );
        
        System.out.println("mother:   " + mother.IsFeasible() + "   " + mother.CalculateScore());
        System.out.println( " " + Arrays.deepToString( mother.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );

        System.out.println("child:    " + child.IsFeasible() + "   " + child.CalculateScore());
        System.out.println( " " + Arrays.deepToString( child.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );

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
                Chromosome [] parents = pop.ExponentialRankSelection();         // epilogh xromosomatwn
                Chromosome child = parents[0].BiasedCollumnCrosover( parents[1] );    // diastaurwsh kai dhmiourgia neou xrwmosomatos

                child.MutationMethod1();                                        // metalaksh tou
                
                child.IsFeasible();                                             // elenxos sunepeias
                next_pop.AddChromosome( child );                                // eisagwgh sth nea genia
            }

            if ( next_pop.IsTerminationValid() )
                completed = true;
        }
    } 
}