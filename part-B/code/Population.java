import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.stream.Collectors;

public class Population {

    private static int START_SIZE;
    private static int ITER_MAX;
    private LinkedList<Chromosome> chr_array = new LinkedList<Chromosome>(); // array, arraylist h list?

    private int num_generation;
    private int num_feasible = 0;

    private Chromosome best_chromosome;
    private long sum_score = 0;

    public int getSize() { return chr_array.size(); }
    public int getNumGeneration() { return num_generation; }
    public int getNumFeasible() { return num_feasible; }
    public Chromosome getBestChromosome() { return best_chromosome; }
    public long getSumScore() { return sum_score; }

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
        num_generation = 0;
    }

    /**
     * Kataskeuh neas genias.
     * @param parent_num_gen to plh8os twn genewn mexri twra.
     */
    public Population( int parent_num_gen )
    {
        num_generation = parent_num_gen + 1;
    }

    private int best_score = Integer.MAX_VALUE;
    /**
     * Eisagei ena {@link Chromosome} sthn genia pou thn kalei.
     * @param chromosome pros eisagwgh
     */
    public void AddChromosome ( Chromosome chromosome )
    {
        chr_array.add( chromosome );

        // mpainoun mono feasible
        num_feasible++;

        // briskei pio einai to kalutero chromosoma
        int chromosome_score = chromosome.CalculateScore();
        if ( chromosome_score < best_score )
        {
            best_score = chromosome_score;
            best_chromosome = chromosome;
        }
        sum_score += chromosome_score;
    }




    /*********************************************************/
    /******************** Epilogh gonewn. ********************/
    /*********************************************************/
    // Leitourgoun kai me diaforetiko plh8os gonewn.
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
        // To psel orizei thn pi8anothta ena xrwmoswma na labei meros se ena tournament.
        int tournament_size = (int) Math.round( (double) chr_array.size() * psel );
        
        Chromosome [] picked_chromosomes = new Chromosome[NUM_PARENTS];

        LinkedList<Chromosome> competitors = new LinkedList<Chromosome>();

        Random int_generator = new Random();

        // Epanalambanetai gia osous goneis xreiazomai.
        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
        {
            // krataw to kalutero apo thn dekada
            int min_score = Integer.MAX_VALUE;

            for ( int i = 0 ; i < tournament_size ; i++ )
            {
                // Dialegw tuxaia chromosomata. Ta bgazw prosorina apo ton plu8usmo gia na mhn ta ksanadw sto idio tournament
                int random_index = int_generator.nextInt( chr_array.size() - tournament_size + 1 );
                competitors.add( chr_array.remove( random_index ) );

                // briskw to kalutero
                if ( competitors.get(i).getScore() < min_score )
                {
                    picked_chromosomes[parent] = competitors.get(i);
                    min_score = picked_chromosomes[parent].getScore();
                }
            }
            // Ksanabazw mesa tous competitors gia na mporoun na paroun meros se allo tournament.
            // Den exei shmasia pou, afou dialegontai tuxaia.
            while ( competitors.size() > 0 )
                chr_array.add( competitors.remove() );
            
            // menei eksw autos pou nikise gia na mhn ginei goneas panw apo 1 fora.
            chr_array.remove( picked_chromosomes[parent] );
        }      
        //Ksanabazw tous goneis ston plu8hsmo. Den exei shmasia pou.
        for ( int parent = 0 ; parent < NUM_PARENTS ; parent++ )
            chr_array.add( picked_chromosomes[parent] );

        return picked_chromosomes;
    }


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

        return false;
    }
}