import java.util.Random;

public class Chromosome
{
    private final static int NUM_EMPLOYEES = 30;
    private final static int SCHEDULE_LENGHT = 14; // in days
    private final static int NUM_GENES = NUM_EMPLOYEES * SCHEDULE_LENGHT;

    private final static int WEEK_DAYS = 7;
    private final static int SCHEDULE_WEEKS = SCHEDULE_LENGHT / WEEK_DAYS; // estw oti ta programmata einai panta se bdomades

    public int [][] genes = new int  [ NUM_EMPLOYEES ] [ SCHEDULE_LENGHT ];

    private boolean feasibility = true; // an den xreiazetai na to 8eimatai to katebazw sthn sunarthsh
    private int score;
    private double fitness;

    /* hard constraints */
    private final static int [][] COVERAGE_BOARD = {
        { 10 , 10 , 5 , 5 , 5 , 5 , 5 },
        { 10 , 10 , 10 , 5 , 10 , 5 , 5 },
        { 5 , 5 , 5 , 5 , 5 , 5 , 5 }
    };

    // shifts
    private final static int REPO = 0;
    private final static int MORNING = 1;
    private final static int AFTERNOON = 2;
    private final static int NIGHT = 3;


    public int getScore() { return score; }
    public boolean getFeasibility() { return feasibility; }
    public static int getNumGenes() { return NUM_GENES; }

    /*********************************************************/
    /******************** Initialization. ********************/
    /*********************************************************/

    /**
     * Arxikopoiei ta gonidia tou chromosomatos.
     * O ari8mos twn bardiwn kai oi 8eseis tou einai tuxaies. Den egguatai feasibility.
     */
    public void RandomInit()
    {
        final Random value = new Random();
        for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
                genes[employee][day] = value.nextInt(4); // 0, 1, 2, 3
    }

    /**
     * Arxikopoiei ta gonidia tou chromosomatos.
     * Determinstikos pinakas, egguatai feasibility.
     */
    public void DeterminsticInit()
    {
        for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
        {
            int morning_shifts = 0;
            int afternoon_shifts = 0;
            int night_shifts = 0;

            for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            {
                if ( morning_shifts != COVERAGE_BOARD[0][day % WEEK_DAYS] )
                {
                    genes[employee][day] = MORNING;
                    morning_shifts++;
                }
                else if ( afternoon_shifts != COVERAGE_BOARD[1][day % WEEK_DAYS] )
                {
                    genes[employee][day] = AFTERNOON;
                    afternoon_shifts++;
                }
                else if ( night_shifts != COVERAGE_BOARD[2][day % WEEK_DAYS] )
                {
                    genes[employee][day] = NIGHT;
                    night_shifts++;
                }  
            }
        }
    }

    /**
     * Arxikopoiei ta gonidia tou chromosomatos.
     * Tuxaiothta stis 8eseis pou mpainoun oi bardies, o ari8mos twn bardiwn einai sta8eros. 
     * Egguatai feasibility.
     */
    public void FeasibleRandomInit()
    {
        DeterminsticInit();

        for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
        {
            int index, temp;
            Random random = new Random();

            for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            {
                index = random.nextInt( NUM_EMPLOYEES );
                temp = genes[index][day];
                genes[index][day] = genes[employee][day];
                genes[employee][day] = temp;                
            }
        }
    }


    /*********************************************************/
    /********************** Constrains. **********************/
    /*********************************************************/

     /**
      * Elenxos hard constrains.
      * @return Feasibility tou chromosomatos
      */
    public boolean IsFeasible()
    {
        for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
        {
            int morning_shifts = 0;
            int afternoon_shifts = 0;
            int night_shifts = 0;

            for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            {
                if ( genes[employee][day] == MORNING )
                    morning_shifts++;
                else if ( genes[employee][day] == AFTERNOON )
                    afternoon_shifts++;
                else if ( genes[employee][day] == NIGHT )
                    night_shifts++;
            }
            // kapoia bardua den eksipiretitai swsta
            if ( morning_shifts != COVERAGE_BOARD[0][day % WEEK_DAYS] || afternoon_shifts != COVERAGE_BOARD[1][day % WEEK_DAYS]
                        || night_shifts != COVERAGE_BOARD[2][day % WEEK_DAYS] )
                return feasibility = false;
        }
        return feasibility = true;
    }

    /* shifts costs */
    private final static int MORNING_COST = 8;
    private final static int AFTERNOON_COST = 8;
    private final static int NIGHT_COST = 10;

    /* Soft constraints rules. */
    private final static int MAX_WORK_HOURS = 70; // weekly               // 0
    private final static int MAX_SEQUENTIAL_WORK_DAYS = 7;                // 1
    private final static int MAX_SEQUENTIAL_NIGHT_SHIFTS = 4;             // 2
    // nuxta kai epomenh mera prwi                                        // 3
    // apogeuma kai epomenh mera prwi                                     // 4
    // nuxta kai epomenh mera apogeuma                                    // 5
    private final static int [] LEAVES_PER_NIGHT_SHIFTS = { 2 , 4 };      // 6
    private final static int [] LEAVES_PER_WORK_DAYS = { 2 , 7 };         // 7
    // ergasia - adeia - ergasia                                          // 8
    // adeia - ergasia - adeia                                            // 9
    private final static int MAX_WORKING_WEEKENDS = 1;                    // 10

    /* Soft constraints costs. */
    private final static int [] SOFT_CONSTRAINTS_COST = { 1000 , 1000 , 1000 , 1000 , 800 , 800 , 100 , 100 , 1 , 1 , 1 };
    
    // Ypologizei to ka8e constraint 1 fora gia ka8e ergazomeno.
    // Gia 1 fora ana programma ta continue ginontai break. Gia polles fores ana ergazomeno feugoun entelws.
    /**
     * Elenxos soft constrains.
     * @return score xromosomatos.
     */
    @SuppressWarnings("unused")
    public int CalculateScore()
    {
        score = 0;
        // max wres ergasias
    SC0:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            int weekly_workhours = 0;
            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
            {
                if ( genes[employee][day] == MORNING )
                    weekly_workhours += MORNING_COST;
                else if ( genes[employee][day] == AFTERNOON )
                    weekly_workhours += AFTERNOON_COST;
                else if ( genes[employee][day] == NIGHT )
                    weekly_workhours += NIGHT_COST;
            }
            if ( weekly_workhours > MAX_WORK_HOURS )
            {
                score += SOFT_CONSTRAINTS_COST[0];
                continue;
            }      
        }
        // max sunexomenes hmeres ergasias
    SC1:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            int seq_work_days = 0;

            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
            {
                // douleuei
                if ( genes[employee][day] != REPO )
                    seq_work_days++;
                // den douleuei
                else
                    seq_work_days = 0;

                if ( seq_work_days > MAX_SEQUENTIAL_WORK_DAYS )
                {
                    score += SOFT_CONSTRAINTS_COST[1];
                    continue SC1;
                }
            }
        }
        // max sunexomenes nuxterines bardies
    SC2:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            int seq_night_shifts = 0;

            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
            {
                // douleuei nuxta
                if ( genes[employee][day] == NIGHT )
                    seq_night_shifts++;
                // den douleuei nuxta
                else
                    seq_night_shifts = 0;

                if ( seq_night_shifts > MAX_SEQUENTIAL_NIGHT_SHIFTS )
                {
                    score += SOFT_CONSTRAINTS_COST[2];
                    continue SC2;
                }
            }
        }
        // nuxta kai meta prwi
    SC3:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT - 1 ; day++ )
                if ( genes[employee][day] == NIGHT && genes[employee][ day + 1 ] == MORNING )
                {
                    score += SOFT_CONSTRAINTS_COST[3];
                    continue SC3;
                }
        // apogeuma kai meta meshmeri
    SC4:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT - 1 ; day++ )
                if ( genes[employee][day] == AFTERNOON && genes[employee][ day + 1 ] == MORNING )
                {
                    score += SOFT_CONSTRAINTS_COST[4];
                    continue SC4;
                }
        // nuxta kai meta apogeuma
    SC5:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT - 1 ; day++ )
                if ( genes[employee][day] == NIGHT && genes[employee][ day + 1 ] == AFTERNOON )
                {
                    score += SOFT_CONSTRAINTS_COST[5];
                    continue SC5;
                }
        // meres adeias meta apo nuxterines bardies
    SC6:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            int seq_night_shifts = 0;
            int seq_repo = 0;

            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
            {
                if ( genes[employee][day] == NIGHT )
                    seq_night_shifts++;
                else if ( genes[employee][day] == REPO && seq_night_shifts != 0 ) // na xoun arxisei na metriountai oi nuxtes
                    seq_repo++;

                // Exei ta anagkaia repo. Sunexizei o upologismos gia tis upoloipes 8eseis
                if ( seq_repo == LEAVES_PER_NIGHT_SHIFTS[0] )
                    seq_night_shifts = seq_repo = 0;
                // Kseperase to orio
                else if ( seq_night_shifts > LEAVES_PER_NIGHT_SHIFTS[1] )
                {
                    score += SOFT_CONSTRAINTS_COST[6];
                    continue SC6;
                }
            }
            // teleiwnei to programma me 4 nuxterines xwris 2 adeies ( 8a exei 4 mono an den exei mhdenistei )
            if ( seq_night_shifts == LEAVES_PER_NIGHT_SHIFTS[1] )
            {
                score += SOFT_CONSTRAINTS_COST[6];
                continue SC6;            
            }
        }
        // meres adeias meta apo meres ergasias
    SC7:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            int seq_work_days = 0;
            int seq_repo = 0;

            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
            {
                if ( genes[employee][day] != REPO )
                    seq_work_days++;
                else if ( genes[employee][day] == REPO && seq_work_days != 0 ) // na xoun arxisei na metriountai oi bardies
                    seq_repo++;

                // Exei ta anagkaia repo. Sunexizei o upologismos gia tis upoloipes 8eseis
                if ( seq_repo == LEAVES_PER_WORK_DAYS[0] )
                    seq_work_days = seq_repo = 0;
                // Den ta exei.
                else if ( seq_work_days > LEAVES_PER_WORK_DAYS[1] )
                {
                    score += SOFT_CONSTRAINTS_COST[7];
                    continue SC7;
                }
            }
            if ( seq_work_days == LEAVES_PER_WORK_DAYS[1] )
            {
                score += SOFT_CONSTRAINTS_COST[7];
                continue SC7;               
            }
        }
        // ergasia-adeia-ergasia
    SC8:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT - 2 ; day++ )
                if ( genes[employee][day] != REPO && genes[employee][ day + 1 ] == REPO && genes[employee][ day + 2 ] != REPO )
                {
                    score += SOFT_CONSTRAINTS_COST[8];
                    continue SC8;
                }
        // adeia-ergasia-adeia
    SC9:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT - 2 ; day++ )
                if ( genes[employee][day] == REPO && genes[employee][ day + 1 ] != REPO && genes[employee][ day + 2 ] == REPO )
                {
                    score += SOFT_CONSTRAINTS_COST[9];
                    continue SC9;
                }
        // ergasia sabatokuriaka
   SC10:for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            int working_weekends = 0;
            for ( int week = 0 ; week < SCHEDULE_WEEKS ; week++ )
            {
                if ( genes[employee][ week * WEEK_DAYS + 5] != 0 || genes[employee][ week * WEEK_DAYS + 6] != 0 )
                    working_weekends++;

                if ( working_weekends > MAX_WORKING_WEEKENDS )
                {
                    score += SOFT_CONSTRAINTS_COST[10];
                    continue SC10;
                }
            }
        }
        fitness = score!=0 ? ( 1 / (double) score ) : 1;
        return score;
    }


    /*********************************************************/
    /********************** Zeugarwmata. *********************/
    /*********************************************************/

    /**
     * Kataskeuazei apogono me tis stules gonidiwn twn gonewn. Egguatai feasibility. Biased ws pros ton gonea me to kalutero feasibility.
     * @param other ws suntrofos
     * @return {@link Chromosome} paidi
     */
    public Chromosome MeritCollumnCrosover( final Chromosome other )
    {
        Chromosome child = new Chromosome();

        // upologizw thn sxetikh diafora twn score twn gonewn. 
        double relative_fitness = ( this.fitness - other.fitness ) / this.fitness; // range 0 - 1

        // biased crosover point
        int crossover_point = SCHEDULE_LENGHT/2 + (int) (SCHEDULE_LENGHT/2 * relative_fitness );

        for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
        {
            for ( int day = 0 ; day < crossover_point ; day++ )
                child.genes[employee][day] = this.genes[employee][day];

            for ( int day = crossover_point ; day < SCHEDULE_LENGHT ; day++ )
                child.genes[employee][day] = other.genes[employee][day];
        }
        return child;
    }

    /**
     * Kataskeuazei apogono me ta gonidia twn gonewn. Polu spania bgazei kati feasible. Biased analoga to pcross.
     * @param other ws suntrofos
     * @param pcross h pi8anothta na perasei gonidio apo ton kalutero gonea.
     * @return chromosoma paidi
     */
    public Chromosome UniformCrossing( final Chromosome other , double pcross )
    {
        // briskw pio einai to kalutero kai tou dinw to bias. To bias einai to orio gia thn epilogh gonidiou gonea.
        // Anebazontas to, pernaei pio eukola h mana, enw meionontas to pernaei pio eukola o pateras.
        double bias;
        if ( this.fitness > other.fitness )
            bias = pcross;
        else
            bias = 1 - pcross;
        
        Chromosome child = new Chromosome();
        Random double_generator = new Random();

        // tuxaia epilogh gia ka8e gonidio
        for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
            for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
                child.genes[employee][day] = double_generator.nextDouble() < bias ? this.genes[employee][day] : other.genes[employee][day] ;

        return child;
    }

    /**
     * Kataskeuazei apogono me tis stules gonidiwn twn gonewn. Egguatai feasibility. Biased analoga to pcross.
     * @param other ws suntrofos.
     * @param pcross h pi8anothta na perasei sthlh apo ton kalutero gonea.
     * @return chromosoma paidi
     */
    public Chromosome RandomColumnCrossing( final Chromosome other , double pcross )
    {
        // briskw pio einai to kalutero kai tou dinw to bias. To bias einai to orio gia thn epilogh gonidiou gonea.
        // Anebazontas to, pernaei pio eukola h mana, enw meionontas to pernaei pio eukola o pateras.
        double bias;
        if ( this.fitness > other.fitness )
            bias = pcross;
        else
            bias = 1 - pcross;

        Chromosome child = new Chromosome();
        Random double_generator = new Random();

        // tuxaia epilogh gia ka8e stulh gonidiwn
        for ( int day = 0 ; day < SCHEDULE_LENGHT ; day++ )
        {
            double parent = double_generator.nextDouble();
            for ( int employee = 0 ; employee < NUM_EMPLOYEES ; employee++ )
                child.genes[employee][day] = parent < bias ? this.genes[employee][day] : other.genes[employee][day] ;
        }
        return child;
    }


    /*********************************************************/
    /********************** Metalakseis. *********************/
    /*********************************************************/

    /**
     * Antistrefei tuxaio kommati mia tuxaias sthlhs (meras). Egguatai feasibility.
     * @param pmut h ph8anothta na ginei invert 1 gonidio. num_inverted_genes = pmut * num_genes
     */
    public void ColumnInversionMutation( double pmut )
    {
        // posa gonidia 8a ginoun invert.
        int remaining_mutations = (int) Math.round( pmut * (double) NUM_GENES );

        Random int_generator = new Random();

        // invert stules mexri na ginoun osa prepei
        while ( remaining_mutations > 0 )
        {
            // generate random 8eseis se random meres
            int day = int_generator.nextInt( SCHEDULE_LENGHT ); 
            int inversion_start_point = int_generator.nextInt( NUM_EMPLOYEES );
            int inversion_end_point = int_generator.nextInt( NUM_EMPLOYEES - inversion_start_point ) + inversion_start_point;
            
            // Invert apo to meso ths sthlhs pros ta eksw. An ginei break 8a exei ginei invert se mia mikroterh uposthlh ths
            for ( int employee = (inversion_end_point - inversion_start_point + 1)/2 - 1 ; employee >= 0  ; employee-- )
            {
                int temp = genes[ inversion_start_point + employee ][day];
                genes[ inversion_start_point + employee ][day] = genes[ inversion_end_point - employee ][day];
                genes[ inversion_end_point - employee ][day] = temp;

                remaining_mutations -= 2; // Ginan 2 gonidia invert.
                if ( remaining_mutations <= 0 )
                    break; // den xreiazetai alla
            }
        }
    }

    /**
     * Swaps 2 tuxaia gonidia sthn idia sthlh. Egguatai feasiblity
     * @param pmut h ph8anothta na ginei swap 1 gonidio. num_swapped_genes = pmut * num_genes
     */
    public void SwapMutation( double pmut )
    {
        // posa gonidia 8a ginoun swap.
        int remaining_mutations = (int) Math.round( pmut * (double) NUM_GENES );
        //remaining_mutations = 2;
        Random int_generator = new Random();

        while ( remaining_mutations > 0 )
        {
            // epilogh tuxaiwn 8esewn
            int pos1 = int_generator.nextInt(NUM_EMPLOYEES);
            int pos2 = int_generator.nextInt(NUM_EMPLOYEES);
            int day = int_generator.nextInt(SCHEDULE_LENGHT);

            // swap
            int temp = genes[ pos1 ][ day ];
            genes[ pos1 ][ day ] = genes[ pos2 ][ day ];
            genes[ pos2 ][ day ] = temp;

            remaining_mutations -= 2; // Ginan 2 gonidia swap.
            if ( remaining_mutations <= 0 )
                break; // den xreiazetai alla         
        }
    }
}