import java.util.ArrayList;

public class Population {

    private static int START_SIZE = 10000;
    private ArrayList<Chromosome> chr_array = new ArrayList<Chromosome>(); // array, arraylist h list?

    int num_generation;
    int num_feasible = 0;
    int best_score = Integer.MAX_VALUE;

    // fisrt generation
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

    // new generation
    public Population( int parent_num_gen )
    {
        num_generation = parent_num_gen++;
    }


    public int getSize() { return chr_array.size();	}


    public void AddChromosome ( Chromosome chromosome )
    {
        chr_array.add( chromosome );
    }

    // elenxei ola ta chromosomata an einai feasible
    // isws den xreiazetai an mpainoun mono feasible
    public void CheckFeasibility()
    {
        for ( Chromosome chromosome : chr_array )
        {
            if ( chromosome.IsFeasible() )
                num_feasible++;
        }
    }

    // upologizei to kostos olwn twn chromo0somatwn
    // isws den xreiazetai kai na ginetai ston konstraktora tou arxikou
    // kai sthn dimiourgia twn xrwmosomatwn tou neou
    public void CalculateScore()
    {
        for ( Chromosome chromosome : chr_array )
        {
            int chr_cost = chromosome.CalculateScore();
            if ( chr_cost < best_score )
            best_score = chr_cost;
        }
    }

    public Chromosome [] PickChromosomes()
    {
        Chromosome [] picked_chromosomes = new Chromosome[2];

        return picked_chromosomes;
    }

    public boolean IsTerminationValid()
    {
        return false;
    }
}