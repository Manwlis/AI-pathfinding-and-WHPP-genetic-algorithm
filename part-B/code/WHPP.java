class WHPP
{
    public static void main(String[] args)
    {
        GeneticAlgorithm();
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
            Population next_pop =  new Population( pop.num_generation );

            for ( int i = 0 ; i < pop.getSize() / 2 ; i++ )
            {
                Chromosome [] parents = pop.PickChromosomes();                  // epilogh xromosomatwn
                Chromosome child = parents[0].CrossingMethod1( parents[1] );    // diastaurwsh kai dhmiourgia neou xrwmosomatos

                child.MutationMethod1();                                        // metalaksh tou
                
                child.IsFeasible();                                             // elenxos sunepeias
                next_pop.AddChromosome( child );                                // eisagwgh sth nea genia
            }

            if ( next_pop.IsTerminationValid() )
                completed = true;
        }
    } 
}