import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

public class Population {

    private static int START_SIZE;
    private static int ITER_MAX;
    private ArrayList<Chromosome> chr_array = new ArrayList<Chromosome>(); // array, arraylist h list?

    private int num_generation;
    private int num_feasible = 0;

    private Chromosome best_chromosome;


    public int getSize() { return chr_array.size(); }
    public int getNumGeneration() { return num_generation; }
    public int getNumFeasible() { return num_feasible; }
    public Chromosome getBestChromosome() { return best_chromosome; }

    public static void setStartSize( int size ) { START_SIZE = size; }
    public static void setIterMax( int iter ) { ITER_MAX = iter; }

    /*********************************************************/
    /******************** Initialization. ********************/
    /*********************************************************/

    /**
     * Kataskeuh prwths genias.
     */
    public Population()
    {
        for ( int i = 0 ; i < START_SIZE ; i++ )
        {
            Chromosome chromosome = new Chromosome();
            chromosome.FeasibleRandomInit();
            AddChromosome(chromosome);
        }
        num_generation = 1;
    }

    /**
     * Kataskeuh neas genias.
     * @param parent_num_gen to plh8os twn genewn mexri twra.
     */
    public Population( int parent_num_gen )
    {
        num_generation = parent_num_gen + 1;
    }

    /**
     * Eisagei ena {@link Chromosome} sthn genia pou thn kalei.
     * @param chromosome pros eisagwgh
     */
    public void AddChromosome ( Chromosome chromosome )
    {
        chr_array.add( chromosome );

        // mpainoun mono feasible
        num_feasible++;

        int best_score = Integer.MAX_VALUE;
        // briskei pio einai to kalutero chromosoma
        int chromosome_score = chromosome.CalculateScore();
        if ( chromosome_score < best_score )
        {
            best_score = chromosome_score;
            best_chromosome = chromosome;
        }
    }




    /*********************************************************/
    /******************** Epilogh gonewn. ********************/
    /*********************************************************/
    // Leitourgoun kai me diaforetiko plh8os gonewn
    private final static int NUM_PARENTS = 2;

    /**
     * Epilogh chromosomatwn me ton algori8mo exponential ranking.
     * @param psel orizei thn klish ths ek8etikhs sunarthshs katanomhs.
     * Gia megalo psel, h klish ginetai pio omalh kai uparxoun perissoteres pi8anothtes na epilex8ei ena xromosoma me xamhlotero rank.
     * Gia mikro psel, ta xrwmosomata me kalutero rank, sugkentrwnoun tis perissoteres pi8anothtes epiloghs.
     * @return picked_chromosomes[], pinakas chromosomatwn me mege8os NUM_PARENTS
     */
    public Chromosome [] ExponentialRankSelection( double c )
    {
        Chromosome [] picked_chromosomes = new Chromosome[NUM_PARENTS];
        
        Random double_generator = new Random();

        LinkedList<Chromosome> sorted_chrs = new LinkedList<Chromosome> // topikh lista gia sort kai remove. Den xalaei o plu8hsmos
            ( chr_array.stream().sorted( Comparator.comparing( Chromosome::getScore ).reversed() ).collect(Collectors.toList()) );
        // reversed wste to xamhlotero (best) score na einai sto telos

        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
        {
            int size = sorted_chrs.size();

            //double c = 0.99; // oso mikrainei ginetai pio apotomh h klish
            double cumulative_p = 0;
            
            double normalizer = ( c - 1 ) / (Math.pow( c , size ) - 1 ); // kanonikopoiei tis pi8anotites sto pedio 0-1
            double random_value = double_generator.nextDouble();

            for ( int i = 0 ; i < size ; i++)
            {
                double p_i = Math.pow( c , size - (i + 1) ) * normalizer;
                cumulative_p += p_i;

                if ( ( cumulative_p - p_i <= random_value ) && ( random_value < cumulative_p ) )
                {
                    picked_chromosomes[parent] = sorted_chrs.remove(i); // gia na mhn mporei na epileksei ton idio patera polles fores
                    break;
                }
            }
            // Se akraies periptwseis, mporei to cumulative_p na einai mikrotero tou random value
            // ekseteias la8wn akribeias apo tis prakseis me double. Tote, epilegetai h teleutaia 8esh tou pinaka.
            if ( picked_chromosomes[parent] == null )
                picked_chromosomes[parent] = sorted_chrs.removeLast();
        }
        return picked_chromosomes;
    }

    /**
     * Epilogh chromosomatwn me ton algori8mo tournament. Mege8os tournament metablhto.
     * @param psel h pi8anothta na epilex8ei ena xrwmosoma gia na mpei sto tournament.
     * @return picked_chromosomes[], pinakas chromosomatwn me mege8os NUM_PARENTS
     */
    public Chromosome [] TournamentSelection( double psel )
    {
        //
        int tournament_size = (int) Math.round( (double) chr_array.size() * psel );
        
        Chromosome [] picked_chromosomes = new Chromosome[NUM_PARENTS];

        Random int_generator = new Random();

        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
        {
            // Dialegw mia tuxaia dekada. Ta chromosomata einai se tuxaia seira mesa ston plu8usmo.
            int random_index = int_generator.nextInt( chr_array.size() - tournament_size + 1 );

            // krataw to kalutero apo thn dekada
            int min_score = Integer.MAX_VALUE;
            for ( int i = 0 ; i < tournament_size ; i++ )
            {
                if ( chr_array.get( random_index + i).getScore() < min_score )
                {
                    picked_chromosomes[parent] = chr_array.get( random_index + i);
                    min_score = picked_chromosomes[parent].getScore();
                }
            }
            // bgazw proswrina auta pou dialextikan apo ton plu8ismo gia na mhn ksanadialextoun
            chr_array.remove(picked_chromosomes[parent]);
        }
        
        // ta ksanabazw ston plu8hsmo. Den exei shmasia pou, htan idh se tuxaia 8esh.
        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
            chr_array.add( int_generator.nextInt( chr_array.size() ) , picked_chromosomes[parent] );

        return picked_chromosomes;
    }


    private static int times = 0;
    /**
     * Deixnei an o plh8usmos eikanopoiei ta krhtiria termatismou
     * @return true/false
     */
    public boolean IsTerminationValid( int previous_best_score )
    {
        // bre8hke beltisth lush
        if ( best_chromosome.getScore() == 0 )
        {
            return true;
        }
        // ekane maximum epanalhpseis
        if ( num_generation == ITER_MAX )
            return true;

        if ( best_chromosome.getScore() - previous_best_score < 0.01 *previous_best_score )
            times++;
        else
            times = 0;

        if ( times == ITER_MAX/10 )
            return true;

        return false;
    }
}