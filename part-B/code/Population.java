import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

public class Population {

    private static int START_SIZE;
    private ArrayList<Chromosome> chr_array = new ArrayList<Chromosome>(); // array, arraylist h list?

    private int num_generation;
    private int num_feasible = 0;
    private int best_score = Integer.MAX_VALUE;


    public int getSize() { return chr_array.size(); }
    public int getNumGeneration() { return num_generation; }
    public int getNumFeasible() { return num_feasible; }
    public int getBestScore() { return best_score; }

    public static void setStartSize( int size ) { START_SIZE = size; }

    /*********************************************************/
    /******************** Initialization. ********************/
    /*********************************************************/

    /**
     * Kataskeuh prwths genias
     */
    public Population()
    {
        for ( int i = 0 ; i < START_SIZE ; i++ )
        {
            Chromosome chromosome = new Chromosome();
            chr_array.add( chromosome ); // estw oti ftiaxnontai ola feasible
        }
        num_generation = 1;
        num_feasible = START_SIZE;

        CalculateScore();
    }

    /**
     * Kataskeuh neas genias.
     * @param parent_num_gen to plh8os twn genewn mexri twra
     */
    public Population( int parent_num_gen )
    {
        num_generation = parent_num_gen++;
    }

    /**
     * Eisagei ena {@link Chromosome} sthn genia pou thn kalei.
     * @param chromosome pros eisagwgh
     */
    public void AddChromosome ( Chromosome chromosome )
    {
        chr_array.add( chromosome );
    }


    /*********************************************************/
    /********************** Constrains. **********************/
    /*********************************************************/

    /**
     * Elenxei ola ta chromosomata an einai feasible. Isws den xreiazetai an ola einai feasible, na dw meta to mutation
     */
    public void CheckFeasibility()
    {
        for ( Chromosome chromosome : chr_array )
            if ( chromosome.IsFeasible() )
                num_feasible++;
    }

    /**
     * upologizei to kostos olwn twn chromosomatwn ths genias. Mallon den xreiazetai. Na dw meta to mutation.
     */
    public void CalculateScore()
    {
        for ( Chromosome chromosome : chr_array )
        {
            int chr_cost = chromosome.CalculateScore();
            if ( chr_cost < best_score )
                best_score = chr_cost;
        }
    }


    /*********************************************************/
    /******************** Epilogh gonewn. ********************/
    /*********************************************************/
    // Leitourgoun kai me diaforetiko plh8os gonewn
    private final static int NUM_PARENTS = 2;

    /**
     * Epilogh chromosomatwn me ton algori8mo exponential ranking.
     * https://www.researchgate.net/publication/259461147_Selection_Methods_for_Genetic_Algorithms p.337
     * @return picked_chromosomes[], pinakas chromosomatwn me mege8os NUM_PARENTS
     */
    public Chromosome [] ExponentialRankSelection()
    {
        Chromosome [] picked_chromosomes = new Chromosome[NUM_PARENTS];
        
        Random double_generator = new Random();

        LinkedList<Chromosome> sorted_chrs = new LinkedList<Chromosome> // topikh lista gia sort kai remove. Den xalaei o plu8hsmos
            ( chr_array.stream().sorted( Comparator.comparing( Chromosome::getScore ).reversed() ).collect(Collectors.toList()) );

        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
        {
            int size = sorted_chrs.size();

            double c = ( size * 2 * ( size - 1 ) ) / ( 6 * ( size - 1 ) + size ); // interpolation gia na apofugei dunameis me dekadikous??

            double random_value = double_generator.nextDouble();

            for ( int i = 0 ; i < size ; i++)
            {
                double p_i = Math.exp( - i / c );

                if ( p_i <= random_value )
                {
                    picked_chromosomes[parent] = sorted_chrs.get(i); 
                    sorted_chrs.remove(i); // gia na mhn mporei na epileksei ton idio patera polles fores
                    break;
                }
            }
        }
        return picked_chromosomes;
    }

    final static int TOURNAMENT_SIZE = 5; // size / psel
    /**
     * Epilogh chromosomatwn me ton algori8mo tournament. Mege8os tournament metablhto.
     * @return picked_chromosomes[], pinakas chromosomatwn me mege8os NUM_PARENTS
     */
    public Chromosome [] TournamentSelection()
    {
        Chromosome [] picked_chromosomes = new Chromosome[NUM_PARENTS];
        int [] picked_index = new int[NUM_PARENTS]; // gia na ta ksanabgalw pisw


        Random int_generator = new Random();

        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
        {
            // Dialegw mia tuxaia dekada. Ta chromosomata einai se tuxaia seira mesa ston plu8usmo.
            int random_index = int_generator.nextInt( chr_array.size() - TOURNAMENT_SIZE + 1 );

            // krataw to kalutero apo thn dekada
            int min_score = Integer.MAX_VALUE;
            for ( int i = 0 ; i < TOURNAMENT_SIZE ; i++ )
            {
                if ( chr_array.get( random_index + i).getScore() < min_score )
                {
                    picked_chromosomes[parent] = chr_array.get( random_index + i);
                    picked_index[parent] = random_index + i;
                    min_score = picked_chromosomes[parent].getScore();
                }
            }
            // bgazw proswrina auta pou dialextikan apo ton plu8ismo gia na mhn ksanadialextoun
            chr_array.remove(picked_chromosomes[parent]);
        }
        
        // ta ksanabazw ston plu8hsmo. Den exei shmasia pou, htan idh se tuxaia 8esh.
        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
            chr_array.add( picked_index[parent] , picked_chromosomes[parent] );

        return picked_chromosomes;
    }



    /**
     * Deixnei an o plh8usmos eikanopoiei ta krhtiria termatismou
     * @return true/false
     */
    public boolean IsTerminationValid()
    {
        return false;
    }
}