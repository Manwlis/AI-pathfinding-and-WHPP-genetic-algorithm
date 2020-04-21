import java.util.ArrayList;

public class Population {

    private final static int START_SIZE = 10000;
    private ArrayList<Chromosome> chr_array = new ArrayList<Chromosome>(); // array, arraylist h list?

    private int num_generation;
    private int num_feasible = 0;
    private int best_score = Integer.MAX_VALUE;


    public int getSize() { return chr_array.size(); }
    public int getNumGeneration() { return num_generation; }
    public int getNumFeasible() { return num_feasible; }
    public int getBestScore() { return best_score; }


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
    /***************** Leitourgies plu8usmou. ****************/
    /*********************************************************/
    
    /**
     * Epistrefei 2 {@link Chromosome} gia zeugarwma
     * @return picked_chromosomes[2]
     */
    public Chromosome [] PickChromosomes()
    {
        Chromosome [] picked_chromosomes = new Chromosome[2];

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