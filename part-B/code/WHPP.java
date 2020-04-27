import java.util.Arrays;

class WHPP
{
    final static int pop = 2000;
    final static int iter_max = 500;
    final static double psel = 0.04;
    final static double pcross = 0.6;
    final static double pmut = 0.05;

    public static void main(String[] args)
    {
        Population.setStartSize(pop);
        Population.setIterMax(iter_max);
    
        Chromosome best_chr = GeneticAlgorithm();

        System.out.println("\n" + best_chr.IsFeasible() + "   " + best_chr.getScore() );
        System.out.println( " " + Arrays.deepToString( best_chr.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );
    }


    /* Skeletos genetikou algori8mou */
    public static Chromosome GeneticAlgorithm()
    {
        Population pop = new Population();
        
        boolean completed = false;

        while ( !completed )
        {
            long mo_genias = pop.getSumScore() / pop.getSize();
            System.out.println(pop.getNumGeneration() + "   " + pop.getBestChromosome().getScore() + "   " + mo_genias );
       
            Population next_pop =  new Population( pop.getNumGeneration() );

            while ( next_pop.getSize() < pop.getSize() )
            {
                Chromosome [] parents = pop.TournamentSelection( psel );                    // epilogh xromosomatwn
                // Chromosome child = parents[0].RandomColumnCrossing( parents[1] , pcross );  // diastaurwsh kai dhmiourgia neou xrwmosomatos
                Chromosome child = parents[0].MeritCollumnCrosover( parents[1] );         // diastaurwsh kai dhmiourgia neou xrwmosomatos

                // child.ColumnInversionMutation( pmut );                                      // metalaksh tou
                child.SwapMutation( pmut );                                               // metalaksh tou
                
                if ( child.IsFeasible() )                                                   // elenxos sunepeias
                    next_pop.AddChromosome( child );                                        // eisagwgh sth nea genia 
            }

            if ( next_pop.IsTerminationValid( pop.getBestChromosome().getScore() ) )
                completed = true;
            
            pop = next_pop;
        }     
        // print teleutaia genia
        long mo_genias = pop.getSumScore() / pop.getSize();
        System.out.println(pop.getNumGeneration() + "   " + pop.getBestChromosome().getScore() + "   " + mo_genias );
   
        return pop.getBestChromosome();
    } 
}