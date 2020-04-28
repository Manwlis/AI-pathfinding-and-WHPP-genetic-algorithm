import java.util.Arrays;

class WHPP
{
    final static int pop = 1500;
    final static int iter_max = 1000;
    final static double psel = 0.022;
    final static double pcross = 0.5;
    final static double pmut = 0.028;

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
                // epilogh xromosomatwn
                Chromosome [] parents = pop.TournamentSelection( psel );

                // diastaurwsh kai dhmiourgia neou xrwmosomatos
                Chromosome child = parents[0].RandomColumnCrossing( parents[1] , pcross );
                //Chromosome child = parents[0].MeritCollumnCrosover( parents[1] );         

                // metalaksh tou
                //child.ColumnInversionMutation( pmut );
                child.SwapMutation( pmut );

                // elenxos sunepeias kai eisagwgh sth nea genia 
                if ( child.IsFeasible() )                                                   
                    next_pop.AddChromosome( child );
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