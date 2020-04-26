import java.util.Arrays;

class WHPP
{
    final static int pop = 3000;
    final static int iter_max = 1000;
    final static double psel = 0.005;
    final static double pcross = 0.55;
    final static double pmut = 0.015;

    public static void main(String[] args)
    {
        Population.setStartSize(pop);
        Population.setIterMax(iter_max);
    
        Chromosome best_chr = GeneticAlgorithm();

        System.out.println("\n" + best_chr.getScore() );
    }


    /* Skeletos genetikou algori8mou */
    public static Chromosome GeneticAlgorithm()
    {
        Population pop = new Population();
        System.out.println( pop.getBestChromosome().getScore() );
        boolean completed = false;

        while ( !completed )
        {
            Population next_pop =  new Population( pop.getNumGeneration() );

            while ( next_pop.getSize() < pop.getSize() )
            {
                Chromosome [] parents = pop.TournamentSelection( psel );                    // epilogh xromosomatwn
                Chromosome child = parents[0].RandomColumnCrossing( parents[1] , pcross );  // diastaurwsh kai dhmiourgia neou xrwmosomatos
                //Chromosome child = parents[0].MeritCollumnCrosover( parents[1] );         // diastaurwsh kai dhmiourgia neou xrwmosomatos

                child.ColumnInversionMutation( pmut );                                      // metalaksh tou
                //child.SwapMutation( pmut );                                               // metalaksh tou
                
                if ( child.IsFeasible() )                                                   // elenxos sunepeias
                    next_pop.AddChromosome( child );                                        // eisagwgh sth nea genia 
            }

            if ( next_pop.IsTerminationValid( pop.getBestChromosome().getScore() ) )
                completed = true;
            
            pop = next_pop;

            System.out.println(pop.getNumGeneration() + "   " + pop.getBestChromosome().getScore() );
        }
        System.out.println("\n" + pop.getBestChromosome().IsFeasible() + "   " + pop.getBestChromosome().getScore() );
        System.out.println( " " + Arrays.deepToString( pop.getBestChromosome().genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );
            
        return pop.getBestChromosome();
    } 
}