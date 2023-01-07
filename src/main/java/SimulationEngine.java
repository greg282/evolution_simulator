import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationEngine implements IEngine, Runnable {

    private Map map;
    private Simulation simulation;
    private List<Animal> animals = new ArrayList<>();
    protected java.util.Map<Vector2d, Plant> plants;

    private  List<Animal> dead_fields;

    private int plantEnergy;
    private int growingPlants;
    private int breedReady;
    private int breedEnergy;
    private int mutationMinimum;
    private int mutationMaximum;

    private int maxEnergy;
    private int genomeLength;

    private int startingEnergy;
    private boolean globeVariant;
    private boolean equatorsVariant;
    private boolean randomnessVariant;
    private boolean predestinationVariant;

    private boolean saveToCsv;

    private int delay = 0;
    private boolean paused = false;

    SimulationEngine(
            Simulation simulation,
            Map map,
            java.util.Map plants_map,
            int startingPlants,
            int providedEnergy,
            int growingPlants,
            int startingAnimals,
            int startingEnergy,
            int maxEnergy,
            int breedReady,
            int breedEnergy,
            int mutationMinimum,
            int mutationMaximum,
            int genomeLength,
            boolean globeVariant,
            boolean equatorsVariant,
            boolean randomnessVariant,
            boolean predestinationVariant,
            boolean saveToCsv
    ) {
        this.simulation = simulation;
        this.map = map;
        this.plants = plants_map;
        this.plantEnergy = providedEnergy;
        this.growingPlants = growingPlants;
        this.breedReady = breedReady;
        this.breedEnergy = breedEnergy;
        this.mutationMinimum = mutationMinimum;
        this.mutationMaximum = mutationMaximum;
        this.genomeLength = genomeLength;
        this.startingEnergy=startingEnergy;
        this.globeVariant = globeVariant;
        this.equatorsVariant = equatorsVariant;
        this.randomnessVariant = randomnessVariant;
        this.predestinationVariant = predestinationVariant;
        this.maxEnergy=maxEnergy;
        this.saveToCsv=saveToCsv;

        //spawn początkowych zwierząt
        spawnStartingAnimals(startingAnimals, startingEnergy);

        //spawn roslin na mapie z zasadą równika
        startSpawnPlants(startingPlants);
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void pause() {
        this.paused = true;
    }

    public void unpause() {
        this.paused = false;
    }

    public void run() {
        int day=0;
        int total_dead_animals=0;
        int total_dead_animals_age = 0;
        Date now = new Date();

        File file = new File("sim_stats"+now.getYear()+now.getMonth()+now.getDay()+now.getTime()+".csv");


        try {
            if(saveToCsv){
                writeHeader(file);
            }
        } catch (IOException exception) {
            System.out.println("Simulation stopped");
        }


        while (true) {
            day++;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException exception) {
                System.out.println("Simulation stopped");
            }

            if (!paused) {
                dead_fields=new ArrayList<>();
                //usunięcie martwych zwierząt zwierząt z mapy
                for (int i = 0; i < animals.size(); i++) {
                    if ( animals.get(i).energy == 0 ) {
                        //generowanie statystyk
                        total_dead_animals_age=animals.get(i).getAge();
                        total_dead_animals++;
                        /////////////////////
                        dead_fields.add(animals.get(i));
                        map.remove(animals.get(i));
                        animals.remove(animals.get(i));
                        i--;
                    }
                }

                ////////////////////////////////////////////
                //skręt i przemieszczenie każdego zwierzęcia
                for (Animal animal: animals) {
                    animal.move(predestinationVariant, globeVariant, breedEnergy);
                    animal.updateAge();
                }

                //konsumpcja roślin na których pola weszły zwierzęta
                animalsEating();

                //rozmnażanie się najedzonych zwierząt znajdujących się na tym samym polu
                animalsMultiplication();
                //wzrastanie nowych roślin na wybranych polach mapy
                addPlants(growingPlants);
                ///////////////////////////Statystyki symulacji
                DayStat dayStat=null;//zawiera statystyki dotyczące danego dnia
                if(total_dead_animals==0){
                    dayStat=generateDayStatistic(day,0);
                }
                else {
                   dayStat=generateDayStatistic(day,(double)total_dead_animals_age/total_dead_animals);
                }

                try {
                    if(saveToCsv){
                        dayStat.writeToFile(file);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //refresh mapy
                this.simulation.mapRefresh();
            }
        }
    }

    private void startSpawnPlants(int n_of_plants){
        List<Vector2d> fields_xy = new ArrayList<>();

        for (int x = map.getLowerLeftVector().x; x <= map.getUpperRightVector().x; x++) {
            for (int y = map.getLowerLeftVector().x; y <= map.getUpperRightVector().y; y++) {
                if(!plants.containsKey(new Vector2d(x,y))){
                    fields_xy.add(new Vector2d(x,y));
                }
            }
        }

        int curr_counter=0;
        while (curr_counter!=n_of_plants){
            int new_place_xy = (int) ((Math.random() * fields_xy.size() ));
            Vector2d xy=fields_xy.get(new_place_xy);
            if(!plants.containsKey(xy)){
                plants.put(xy,new Plant(xy));
                curr_counter++;
            }
        }
    }
    private void addPlants(int n_of_plants) { //mode 1 rownik mode 0 pola trupó
        List<Integer> preferable_fields_y = new ArrayList<>();
        int n_of_preferable_fields = (int) Math.round(map.getUpperRightVector().y * 0.2);
        int first_preferablefield = (int) Math.round((map.getUpperRightVector().y / 2) - (n_of_preferable_fields / 2));

        for (int i = first_preferablefield; i < first_preferablefield + n_of_preferable_fields; i++) {
            preferable_fields_y.add(i);
        }

        List<Integer> preferable_fields_x = new ArrayList<>();

        for (int i = map.getLowerLeftVector().x; i <= map.getUpperRightVector().x; i++) {
            preferable_fields_x.add(i);
        }

        List<Vector2d> preferable_fields_xy = new ArrayList<>();

        if( this.equatorsVariant) {//równik
            for(Integer x : preferable_fields_x){
                for (Integer y : preferable_fields_y){
                    if(!plants.containsKey(new Vector2d(x,y))){
                        preferable_fields_xy.add(new Vector2d(x,y));
                    }
                }
            }
        }else {
            if( dead_fields != null) {
                for(Animal animal : dead_fields){
                    if(!plants.containsKey(animal.getPosition())&&!preferable_fields_xy.contains(animal.getPosition())) {
                        preferable_fields_xy.add(animal.getPosition());
                    }
                }
            }
        }

        List<Integer> not_preferable_fields_y = new ArrayList<>();
        List<Vector2d> not_preferable_fields_xy = new ArrayList<>();

        for (int i = map.getLowerLeftVector().y ; i <= map.getUpperRightVector().y ; i++){
            if(this.equatorsVariant){
                if(!preferable_fields_y.contains(i)){
                    not_preferable_fields_y.add(i);
                }
            }else{
                not_preferable_fields_y.add(i);
            }
        }

        for(Integer x : preferable_fields_x){
            for (Integer y : not_preferable_fields_y){
                if(this.equatorsVariant){
                    if(!plants.containsKey(new Vector2d(x,y))){
                        not_preferable_fields_xy.add(new Vector2d(x,y));
                    }
                }else {
                    if(!plants.containsKey(new Vector2d(x,y)) && !preferable_fields_xy.contains(new Vector2d(x,y))){
                        not_preferable_fields_xy.add(new Vector2d(x,y));
                    }
                }


            }
         }

        int onNotPreferable = (int) Math.ceil(0.2 * n_of_plants);
        int onPreferable = n_of_plants - onNotPreferable;
        int curr_spawn_counter = 0;
        if(not_preferable_fields_xy.size()<onNotPreferable){
            onNotPreferable=not_preferable_fields_xy.size();
        }
        while(curr_spawn_counter != onNotPreferable  && not_preferable_fields_xy.size()!=0 && not_preferable_fields_xy.size()>=onNotPreferable){
            int new_place_xy = (int) ((Math.random() * not_preferable_fields_xy.size() ));
            Vector2d xy = not_preferable_fields_xy.get(new_place_xy);
            if (!this.plants.containsKey(xy)) {
                this.plants.put(xy, new Plant(xy));
                curr_spawn_counter++;
            }
        }

        curr_spawn_counter = 0;

        if(preferable_fields_xy.size()<onPreferable){
            onPreferable=preferable_fields_xy.size();
        }

        while(curr_spawn_counter != onPreferable && preferable_fields_xy.size()!=0 && preferable_fields_xy.size()>=onPreferable) {
            int new_place_xy = (int) ((Math.random() * preferable_fields_xy.size() ));
            Vector2d xy = preferable_fields_xy.get(new_place_xy);
                if (!this.plants.containsKey(xy)) {
                    this.plants.put(xy, new Plant(xy));
                    curr_spawn_counter++;
                }
        }


    }
    private void animalsEating() {
        List<Animal> animals_at_position;
        for (Animal animal:animals) {
            if (plants.containsKey(animal.getPosition())) {
                animals_at_position = new ArrayList<>();
                animals_at_position.add(animal);
                for(Animal other_animal:animals) {
                    if (animal == other_animal) {
                        continue;
                    }
                    if (animal.getPosition().equals(other_animal.getPosition())) {
                        animals_at_position.add(other_animal);
                    }
                }
                Collections.sort(animals_at_position, new AnimalComparator());
                Animal best_animal = animals_at_position.get(animals_at_position.size() - 1);
                best_animal.energy = best_animal.energy + plantEnergy;
                if (best_animal.energy>this.maxEnergy) best_animal.energy=this.maxEnergy;
                plants.remove(animal.getPosition());
            }
        }
    }

    class AnimalComparator implements Comparator<Animal> {
        public int compare(Animal a1, Animal a2)
        {
            if (a1.energy == a2.energy) {
                if (a1.getAge() == a2.getAge()) {
                    return Integer.compare(a1.n_of_children, a2.n_of_children);
                }
                return Integer.compare(a1.getAge(), a2.getAge());
            }
            else if (a1.energy > a2.energy)
                return 1;
            else
                return -1;
        }
    }

    private void animalsMultiplication() {
        List<Animal> animals_at_position;
        List<Animal> child_animal_list = new ArrayList<>();
        List<Animal> used_animals_list = new ArrayList<>();

        for (Animal animal:animals) {
            animals_at_position = new ArrayList<>();
            animals_at_position.add(animal);
            for(Animal other_animal:animals) {
                if (animal == other_animal || used_animals_list.contains(animal) || used_animals_list.contains(other_animal)){
                    continue;
                }
                if (animal.getPosition().equals(other_animal.getPosition())) {
                    animals_at_position.add(other_animal);
                }
            }
            if (animals_at_position.size() >= 2){
                Collections.sort(animals_at_position, new AnimalComparator());
                Animal best_animal1 = animals_at_position.get(animals_at_position.size()-1);
                Animal best_animal2 = animals_at_position.get(animals_at_position.size()-2);

                if (best_animal1.energy >= breedReady && best_animal2.energy >= breedReady) {
                    best_animal1.n_of_children++;
                    best_animal2.n_of_children++;
                    best_animal2.energy -= breedEnergy;
                    best_animal1.energy -= breedEnergy;
                    int new_energy=2*breedEnergy;

                    if(new_energy>this.startingEnergy){
                        new_energy=this.startingEnergy;
                    }

                    Animal child = new Animal(map, best_animal1.getPosition(), child_genome(best_animal1, best_animal2),new_energy);
                    if (map.place(child)) {
                        child_animal_list.add(child);
                        used_animals_list.addAll(animals_at_position);
                    }
                }

            }
        }
        animals.addAll(child_animal_list);
    }

    private int[] child_genome(Animal parent1, Animal parent2) {
        double percent_of_parent1 = ((double) parent1.energy / (double) (parent1.energy + parent2.energy));
        double percent_of_parent2 = 1 - percent_of_parent1;

        if (percent_of_parent2 > percent_of_parent1) { //ustawienie parent1 jako silniejszego
            double tmp = percent_of_parent1;
            percent_of_parent1 = percent_of_parent2;
            percent_of_parent2 = tmp;
            Animal tmp2 = parent2;
            parent2 = parent1;
            parent1 = tmp2;
        }

        ArrayList<Integer> new_genome = new ArrayList<>();

        int genome_side = (int) ((Math.random() * 2)); //0-left 1-right

        if (genome_side == 1) { //geny silniejszego bierzemy z prawej
            double tmp = percent_of_parent1;
            percent_of_parent1 = percent_of_parent2;
            percent_of_parent2 = tmp;
            Animal tmp2 = parent2;
            parent2 = parent1;
            parent1 = tmp2;
        }

        int range = (int) Math.round(parent1.getGenome().length * percent_of_parent1);
        for (int i=0;i<range;i++) {
            new_genome.add(parent1.getGenome()[i]);
        }

        range = parent2.getGenome().length - 1 - (int) Math.round(parent2.getGenome().length * percent_of_parent2);
        if(range<0){
            range=0;
        }
        for (int i=parent2.getGenome().length-1;i>=range;i--) {
            new_genome.add(parent2.getGenome()[i]);
        }

        return mutate(convertIntegers(new_genome));
    }

    private int[] mutate(int[] genome) {
        int max = genome.length;
        //int n_of_mutations = (int) ((Math.random() * max));
        int n_of_mutations = (int) ((Math.random() * this.mutationMaximum-this.mutationMinimum)+this.mutationMinimum);
        int curr_counter=0;
        while (curr_counter != n_of_mutations) {
            if (randomnessVariant) {
                genome[(int) ((Math.random() * max))] = (int) ((Math.random() * 8));
                curr_counter++;
            }
            else {
                int mode=(int)(Math.random() * 2);
                if (mode == 1) {
                    int index = (int) ((Math.random() * max));
                    genome[index]++;
                    if (genome[index]>7){
                        genome[index]=0;
                    }

                }
                else {
                    int index=(int) ((Math.random() * max));
                    genome[index]--;
                    if (genome[index]<0) {
                        genome[index]=7;
                    }
                }
                curr_counter++;
            }
        }
        return genome;
    }

    private int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++) {
            ret[i] = integers.get(i);
        }
        return ret;
    }

    private void spawnStartingAnimals(int startingAnimals, int startingEnergy) {

        for (int i=0; i < startingAnimals; i++) {
            Animal animal = new Animal(map, map.getRandomPosition(), generateStartingGenome(), startingEnergy);
            animals.add(animal);
            map.place(animal);
        }
    }

    private int[] generateStartingGenome() {

        Random random = new Random();
        int[] array = new int[genomeLength];
        for (int i = 0; i < genomeLength; i++) {
            array[i] = random.nextInt(8);
        }
        return array;
    }

    private DayStat generateDayStatistic(int day,double dead_animal_avg_lifespan){
        DayStat dayStat = new DayStat();
        dayStat.setDay(day);
        dayStat.setTotal_animals(animals.size());
        double avg_energy=0;
        for(Animal animal:animals){
            avg_energy+=animal.energy;
        }
        avg_energy=avg_energy/animals.size();
        dayStat.setAvg_energy(avg_energy);
        dayStat.setTotal_plants(plants.size());
        dayStat.setFree_fields((map.getUpperRightVector().x*map.getUpperRightVector().y)-animals.size()-plants.size());
        dayStat.setAvg_lifespan_of_dead(dead_animal_avg_lifespan);

        int[] max_genom=null;
        int max_counter=0;
        List<int[]> genoms = new ArrayList<>();
        for(Animal animal:animals){
            int [] curr_genom=animal.getGenome();
            int curr_counter=0;
            for(Animal other_animal:animals){
                if(animal.getGenome()==other_animal.getGenome()){
                    curr_counter++;
                }
            }
            if(curr_counter>max_counter){
                max_counter=curr_counter;
                max_genom=curr_genom;
            }
        }
        dayStat.genom=GenomToCSVString(max_genom);

        return dayStat;
    }
    private void writeHeader(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.append("Day,Total_animals,Total_plants,Free_fields,Avg_energy,Avg_lifespan_of_dead,MostPopularGenom");
        writer.close();
    }

    private String GenomToCSVString(int[] genom){
        StringBuilder result= new StringBuilder();

        for(int i=0;i<genom.length;i++){
            result.append(genom[i]);
        }

        return result.toString();
    }

}
