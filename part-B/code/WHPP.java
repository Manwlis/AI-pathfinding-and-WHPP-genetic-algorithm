import java.util.Arrays;
import java.util.Scanner;

class WHPP
{
    static int pop;
    static int iter_max;
    static double psel;
    static double pcross;
    static double pmut;
    static double psearch;

    public static void main(String[] args)
    {
        if (args.length<5)
        {
            System.out.println("Insufficient arguments!");
            return;
        }
        pop = Integer.parseInt( args[0] );
        iter_max = Integer.parseInt( args[1] );
        psel = Double.parseDouble( args[2] );
        pcross = Double.parseDouble( args[3] );
        pmut = Double.parseDouble( args[4] );
        psearch = Double.parseDouble( args[5] );

        System.out.println(pop + "  " + iter_max + "  " + psel + "  " + pcross + "  " + pmut + "  " + psearch);

        Population.setStartSize(pop);
        Population.setIterMax(iter_max);

        // epilogh me8odwn
		Scanner scanner = new Scanner(System.in);
        int crossover = ChooseAlgorithm( scanner ,"\nEpilogh algori8mou crossover:\n1.Random\n2.Merit\n");
        int mutation = ChooseAlgorithm( scanner , "\nEpilogh algori8mou mutation:\n1.Inversion\n2.Swap\n");
		scanner.close();

        Chromosome best_chr = GeneticAlgorithm( crossover , mutation );

        System.out.println("\n" + best_chr.IsFeasible() + "   " + best_chr.getScore() );
        System.out.println( " " + Arrays.deepToString( best_chr.genes ).replace("],","\n").replace("[","").replace(",","").replace("]","") );
    }


    /* Skeletos genetikou algori8mou */
    public static Chromosome GeneticAlgorithm( final int crossover , final int mutation )
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
                Chromosome child;
                if ( crossover == 1 )
                    child = parents[0].RandomColumnCrossing( parents[1] , pcross );
                else
                    child = parents[0].MeritCollumnCrosover( parents[1] );

                // metalaksh tou
                if ( mutation == 1 )
                    child.ColumnInversionMutation( pmut );
                else
                    child.SwapMutation( pmut );

                // topikh anazhthsh
                child.LocalSearch( psearch );
                
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


    // diavasma apo console kai elenxos epiloghs algori8mou
	private static int ChooseAlgorithm( final Scanner scanner , final String message )
	{
		String console_input = "";
		while(!console_input.matches("1|2"))
		{
            System.out.print(message);
			console_input = scanner.nextLine();
		}
		return Integer.parseInt(console_input);
	}
}