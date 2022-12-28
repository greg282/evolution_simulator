import java.util.*;

public class SimulationEngine implements IEngine, Runnable {

    private Map map;
    private Simulation simulation;
    private List<Animal> animals = new ArrayList<>();
    protected java.util.Map<Vector2d, Plant> plants;
    private int plantEnergy;
    private int growingPlants;
    private int breedReady;
    private int breedEnergy;
    private int mutationMinimum;
    private int mutationMaximum;
    private int genomeLength;

    private boolean globeVariant;
    private boolean equatorsVariant;
    private boolean randomnessVariant;
    private boolean predestinationVariant;

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
            int breedReady,
            int breedEnergy,
            int mutationMinimum,
            int mutationMaximum,
            int genomeLength,
            boolean globeVariant,
            boolean equatorsVariant,
            boolean randomnessVariant,
            boolean predestinationVariant) {

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

        this.globeVariant = globeVariant;
        this.equatorsVariant = equatorsVariant;
        this.randomnessVariant = randomnessVariant;
        this.predestinationVariant = predestinationVariant;

        //spawn początkowych zwierząt
        spawnStartingAnimals(startingAnimals, startingEnergy);

        //spawn roslin na mapie z zasadą równika
        addPlants(startingPlants);
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

        while (true) {

            try {
                Thread.sleep(delay);
            } catch (InterruptedException exception) {
                System.out.println("Simulation stopped");
            }

            if (!paused) {

                //usunięcie martwych zwierząt zwierząt z mapy
                for (int i = 0; i < animals.size(); i++) {
                    if ( animals.get(i).energy == 0 ) {
                        map.remove(animals.get(i));
                        animals.remove(animals.get(i));
                        i--;
                    }
                }

                //skręt i przemieszczenie każdego zwierzęcia
                for (Animal animal: animals) {
                    animal.move(predestinationVariant);
                    animal.updateAge();
                }

                //konsumpcja roślin na których pola weszły zwierzęta
                animalsEating();

                //rozmnażanie się najedzonych zwierząt znajdujących się na tym samym polu
                animalsMultiplication();

                //wzrastanie nowych roślin na wybranych polach mapy
                addPlants(growingPlants);

                //refresh mapy
                this.simulation.mapRefresh();
            }
        }
    }

    private void addPlants(int n_of_plants) { //narazie tylko wariant rownik bo nie mamy jeszcze martwych zwierząt
        List<Integer> preferable_fields_y = new ArrayList<>();
        int n_of_preferable_fields = (int) Math.round(map.getUpperRightVector().y * 0.2);
        int first_preferablefield = (int) Math.round((map.getUpperRightVector().y / 2) - (n_of_preferable_fields / 2));

        for (int i = first_preferablefield; i <+ first_preferablefield + n_of_preferable_fields; i++) {
            preferable_fields_y.add(i);
        }

        int onNotPreferable = (int) Math.ceil(0.2 * n_of_plants);
        int onPreferable = n_of_plants - onNotPreferable;

        int curr_spawn_counter = 0;

        int max_x = map.getUpperRightVector().x + 1;
        int min_x = map.getLowerLeftVector().x;

        int max_y_not_preferable = map.getUpperRightVector().y+1;
        int min_y_not_preferable = map.getLowerLeftVector().y;

        int max_y_preferable = preferable_fields_y.size();
        int min_y_preferable = 0;

        while(curr_spawn_counter != onNotPreferable){
            int new_place_y = (int) ((Math.random() * (max_y_not_preferable - min_y_not_preferable)) + min_y_not_preferable);
            if (!preferable_fields_y.contains(new_place_y)) {
                int new_place_x = (int) ((Math.random() * (max_x - min_x)) + min_x);
                if (!this.plants.containsKey(new Vector2d(new_place_x, new_place_y))) {
                    this.plants.put(new Vector2d(new_place_x,new_place_y), new Plant(new Vector2d(new_place_x, new_place_y)));
                    curr_spawn_counter++;
                }
            }
        }

        curr_spawn_counter = 0;

        while(curr_spawn_counter != onPreferable) {
            int new_place_y = (int) ((Math.random() * (max_y_preferable - min_y_preferable)) + min_y_preferable);
            new_place_y = preferable_fields_y.get(new_place_y);
                int new_place_x = (int) ((Math.random() * (max_x - min_x)) + min_x);
                if (!this.plants.containsKey(new Vector2d(new_place_x, new_place_y))) {
                    this.plants.put(new Vector2d(new_place_x, new_place_y), new Plant(new Vector2d(new_place_x, new_place_y)));
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
                if (best_animal.energy>10) best_animal.energy=10; //zakładam że max energia to 10 tak jak teraz jest
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

                    Animal child = new Animal(map, best_animal1.getPosition(), child_genome(best_animal1, best_animal2),2 * breedEnergy);
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
        for (int i=parent2.getGenome().length-1;i>=range;i--) {
            new_genome.add(parent2.getGenome()[i]);
        }

        return mutate(convertIntegers(new_genome));
    }

    private int[] mutate(int[] genome) {
        int max = genome.length;
        int n_of_mutations = (int) ((Math.random() * max));
        int curr_counter=0;
        while (curr_counter != n_of_mutations) {
            if (randomnessVariant) {
                genome[(int) ((Math.random() * max))] = (int) ((Math.random() * 8));
                curr_counter++;
            }
            else {
                if ((int)(Math.random() * 2) == 1) {
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
            Animal animal = new Animal(map, getRandomPosition(), generateStartingGenome(), startingEnergy);
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

    private Vector2d getRandomPosition() {
        Random random = new Random();
        int randomX = random.nextInt(map.getUpperRightVector().x + 1);
        int randomY = random.nextInt(map.getUpperRightVector().y + 1);
        return new Vector2d(randomX, randomY);
    }
}
