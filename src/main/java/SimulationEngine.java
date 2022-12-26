import java.lang.reflect.Array;
import java.util.*;

public class SimulationEngine implements IEngine, Runnable {

    private int plantEnergy=4;

    private Map map;
    private App app;
    private List<Animal> animals = new ArrayList<>();
    protected java.util.Map<Vector2d, Plant> plants;
    private int delay = 0;

    SimulationEngine(Map map,java.util.Map plants_map,App app) {
        this.map = map;
        this.plants=plants_map;
        // tutaj spawn początkowych zwierząt

        Animal animal1 = new Animal(map, new Vector2d(1,1), new int[] {0}, 4 );
        animals.add(animal1);
        map.place(animal1);

        Animal animal2 = new Animal(map, new Vector2d(2,2), new int[] {1, 0, 5, 0, 4}, 10);
        animals.add(animal2);
        map.place(animal2);


        this.app=app;
        addPlants(10);//spawn roslin na mapie z zasada rownika

    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void run() {

        while (true) {

            try {
                Thread.sleep(delay);
            } catch (InterruptedException exception) {
                System.out.println("EXCEPTION: Simulation stopped TERMINATING PROGRAM");
                System.exit(0);
            }

            for (int i = 0; i < animals.size(); i++) {
                if ( animals.get(i).energy == 0 ) {
                    map.remove(animals.get(i));
                    animals.get(i).positionChanged();
                    animals.remove(animals.get(i));
                    i--;
                }
            }

            for (Animal animal: animals) {
                animal.move();
                animal.updateAge();
            }

            //tutaj konsumpcja roślin na których pola weszły zwierzęta
            animalsEating();

            //tutaj rozmnażanie się najedzonych zwierząt znajdujących się na tym samym polu
            animalsMultiplication();

            //tutaj wzrastanie nowych roślin na wybranych polach mapy
            addPlants(2);


            Animal animal2 = new Animal(map, new Vector2d(2,2), new int[] {1, 0, 5, 0, 4}, 10);
            if(map.place(animal2))  animals.add(animal2);

            this.app.positionChanged();//refresh mapy
        }
    }

    public void addAnimalsObserver(IPositionChangeObserver observer) {
        for (Animal animal : animals) {
            //animal.addObserver(observer); odpiałem observera bo sie gui zawieszało przez to
        }
    }

    private void addPlants(int n_of_plants){//narazie tylko wariant rownik bo nie mamy jeszcze martwych zwierząt
        List<Integer> preferable_fields_y = new ArrayList<>();
        int n_of_preferable_fields= (int) Math.round(map.getUpperRightVector().y*0.2);
        int first_prefrebalefield=(int) Math.round((map.getUpperRightVector().y/2)-(n_of_preferable_fields/2));

        for(int i=first_prefrebalefield;i<+first_prefrebalefield+n_of_preferable_fields;i++){
            preferable_fields_y.add(i);
        }

        int onNotPreferable= (int) Math.ceil(0.2*n_of_plants);
        int onPreferable=n_of_plants-onNotPreferable;

        int curr_spawn_counter=0;

        int max_x=map.getUpperRightVector().x+1;
        int min_x=map.getLowerLeftVector().x;

        int max_y_not_preferable=map.getUpperRightVector().y+1;
        int min_y_not_preferable=map.getLowerLeftVector().y;

        int max_y_prefrable=preferable_fields_y.size();
        int min_y_preferable=0;

        while(curr_spawn_counter!=onNotPreferable){
            int new_place_y=(int) ((Math.random() * (max_y_not_preferable - min_y_not_preferable)) + min_y_not_preferable);
            if(!preferable_fields_y.contains(new_place_y)){
                int new_place_x=(int) ((Math.random() * (max_x - min_x)) + min_x);
                if(!this.plants.containsKey(new Vector2d(new_place_x,new_place_y))){
                    this.plants.put(new Vector2d(new_place_x,new_place_y),new Plant(new Vector2d(new_place_x,new_place_y)));
                   curr_spawn_counter++;
                }
            }
        }

        curr_spawn_counter=0;

        while(curr_spawn_counter!=onPreferable){
            int new_place_y=(int) ((Math.random() * (max_y_prefrable - min_y_preferable)) + min_y_preferable);
            new_place_y=preferable_fields_y.get(new_place_y);
                int new_place_x=(int) ((Math.random() * (max_x - min_x)) + min_x);
                if(!this.plants.containsKey(new Vector2d(new_place_x,new_place_y))){
                    this.plants.put(new Vector2d(new_place_x,new_place_y),new Plant(new Vector2d(new_place_x,new_place_y)));
                    curr_spawn_counter++;
                }

        }

    }
    private void animalsEating(){
        List<Animal> animals_at_position;
        for(Animal animal:animals){
            if(plants.containsKey(animal.getPosition())){
                animals_at_position = new ArrayList<>();
                animals_at_position.add(animal);
                for(Animal other_animal:animals){
                    if (animal==other_animal){
                        continue;
                    }
                    if(animal.getPosition().equals(other_animal.getPosition())){
                        animals_at_position.add(other_animal);

                    }
                }
                Collections.sort(animals_at_position,new AnimalComparator());
                Animal best_animal=animals_at_position.get(animals_at_position.size()-1);
                best_animal.energy=best_animal.energy + plantEnergy;
                if(best_animal.energy>10) best_animal.energy=10;//zakładam że max energia to 10 tak jak teraz jest
                plants.remove(animal.getPosition());
            }
        }
    }

    class AnimalComparator implements Comparator<Animal> {
        public int compare(Animal a1, Animal a2)
        {
            if (a1.energy == a2.energy) {
                if (a1.getAge()==a2.getAge()){
                    return Integer.compare(a1.n_of_children,a2.n_of_children);
                }
                return Integer.compare(a1.getAge(), a2.getAge());

            }
            else if (a1.energy > a2.energy)
                return 1;
            else
                return -1;
        }
    }
    private void animalsMultiplication(){
        List<Animal> animals_at_position;
        List<Animal> child_animal_list= new ArrayList<>();
        List<Animal> used_animals_list= new ArrayList<>();

        for(Animal animal:animals){
            animals_at_position = new ArrayList<>();
            animals_at_position.add(animal);
            for(Animal other_animal:animals){
                if (animal==other_animal || used_animals_list.contains(animal) || used_animals_list.contains(other_animal)){
                    continue;
                }
                if(animal.getPosition().equals(other_animal.getPosition())){
                    animals_at_position.add(other_animal);
                }
            }
            if(animals_at_position.size()>=2){
                Collections.sort(animals_at_position,new AnimalComparator());
                Animal best_animal1=animals_at_position.get(animals_at_position.size()-1);
                Animal best_animal2=animals_at_position.get(animals_at_position.size()-2);

                int required_amount_of_energy=4;//energia konieczna, by uznać zwierzaka za najedzonego (i gotowego do rozmnażania)
                int energy_used=2;//energia rodziców zużywana by stworzyć potomka,

                if(best_animal1.energy>=required_amount_of_energy&&best_animal2.energy>=required_amount_of_energy){
                    best_animal1.n_of_children++;
                    best_animal2.n_of_children++;
                    best_animal2.energy-=energy_used;
                    best_animal1.energy-=energy_used;

                    Animal child=new Animal(map,best_animal1.getPosition(),child_genom(best_animal1,best_animal2),2*energy_used);//
                    if(map.place(child)) {
                        child_animal_list.add(child);
                        used_animals_list.addAll(animals_at_position);
                    }
                }

            }
        }
        animals.addAll(child_animal_list);
    }

    private int[] child_genom(Animal parent1,Animal parent2){
        double procent_of_parent1=((double)parent1.energy/(double) (parent1.energy+parent2.energy));
        double procent_of_parent2=1-procent_of_parent1;

        if (procent_of_parent2>procent_of_parent1){//ustaweinie parent1 jako silniejszego
            double tmp=procent_of_parent1;
            procent_of_parent1=procent_of_parent2;
            procent_of_parent2=tmp;
            Animal tmp2=parent2;
            parent2=parent1;
            parent1=tmp2;
        }

        ArrayList<Integer> new_genom=new ArrayList<>();

        int genom_side=(int) ((Math.random() * 2));//0-left 1-right

        if (genom_side==1){//geny silniejszego bierzemy z prawej
            double tmp=procent_of_parent1;
            procent_of_parent1=procent_of_parent2;
            procent_of_parent2=tmp;
            Animal tmp2=parent2;
            parent2=parent1;
            parent1=tmp2;
        }

        int range=(int) Math.round(parent1.getGenome().length*procent_of_parent1);
        for(int i=0;i<range;i++){
            new_genom.add(parent1.getGenome()[i]);
        }

        range=parent2.getGenome().length-1-(int) Math.round(parent2.getGenome().length*procent_of_parent2);
        for(int i=parent2.getGenome().length-1;i>=range;i--){
            new_genom.add(parent2.getGenome()[i]);
        }



        return mutate(convertIntegers(new_genom),1);//mode okresla wariant symulacji
    }

    private int[] mutate(int[] genom,int mode){//mode 1 - full random mode 2 - korekta
        int max=genom.length;
        int n_of_mutations=(int) ((Math.random() * max));
        int curr_counter=0;
        while (curr_counter!=n_of_mutations){
            if(mode==1){
                genom[(int) ((Math.random() * max))]=(int) ((Math.random() * 8));
                curr_counter++;
            }
            else {
                if((int)(Math.random() * 2)==1){
                    int index=(int) ((Math.random() * max));
                    genom[index]++;
                    if (genom[index]>7){
                        genom[index]=0;
                    }
                }
                else {
                    int index=(int) ((Math.random() * max));
                    genom[index]--;
                    if (genom[index]<0){
                        genom[index]=7;
                    }
                }
            }
        }
        return genom;
    }

    private int[] convertIntegers(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = integers.get(i);
        }
        return ret;
    }
}
