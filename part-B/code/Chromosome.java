
public class Chromosome
{
    private static int SCHEDULE_LENGHT = 14; // in days
    private static int NUM_EMPLOYEES = 30;

    private int [][] genes = new int [ SCHEDULE_LENGHT ] [ NUM_EMPLOYEES ];

    private boolean feasibility = true; // an den xreiazetai na to 8eimatai to katebazw sthn sunarthsh
    private int score;

    /* hard constraints */
    private static int [][] COVERAGE_BOARD = {
        { 10 , 10 , 5 , 5 , 5 , 5 , 5 },
        { 10 , 10 , 10 , 5 , 10 , 5 , 5 },
        { 5 , 5 , 5 , 5 , 5 , 5 , 5 }
    };

    /* soft constraints */
    static int MAX_WORK_HOURS = 70; // weekly               // 0
    static int MAX_SEQUENTIAL_WORK_DAYS = 7;                // 1
    static int MAX_SEQUENTIAL_NIGHT_SHIFTS = 4;             // 2
    // nuxta kai epomenh mera prwi                          // 3
    // apogeuma kai epomenh mera prwi                       // 4
    // nuxta kai epomenh mera apogeuma                      // 5
    static int [] LEAVES_PER_NIGHT_SHIFTS = { 2 , 4 };      // 6
    static int [] LEAVES_PER_WORK_DAYS = { 2 , 7 };         // 7
    // ergasia - adeia - ergasia                            // 8
    // adeia - ergasia - adeia                              // 9
    static int MAX_WORKING_WEEKENDS = 1;                    // 10

    static int [] SOFT_CONSTRAINTS_COST = { 1000 , 1000 , 1000 , 1000 , 800 , 800 , 100 , 100 , 1 , 1 , 1 };

    // shifts costs
    private static int MORNING_COST = 8;
    private static int AFTERNOON_COST = 8;
    private static int NIGHT_COST = 10;

    public Chromosome()
    {

    }

    public void RandomInitialization()
    {

    }

    
    /* Elenxos hard constrains  *
     * Epistrefei nai/oxi       */
    public boolean IsFeasible()
    {
        return feasibility;
    }

    /* Elenxos soft constrains              *
     * Epistefei to score tou chromosomatos */
    public int CalculateScore()
    {
        return 0;
    }


    /* Zeugarwma 1                          * 
     * Dexetai suntrofo, epistrefei apogono */
    public Chromosome CrossingMethod1( Chromosome other )
    {
        return null;
    }

    /* Zeugarwma 2                          * 
     * Dexetai suntrofo, epistrefei apogono */
    public Chromosome CrossingMethod2( Chromosome other )
    {
        return null;
    }


    /* Metalaksh 1       *
     * Metalazei to idio */
    public void MutationMethod1()
    {

    }

    /* Metalaksh 2       *
     * Metalazei to idio */
    public void MutationMethod2()
    {

    }
}