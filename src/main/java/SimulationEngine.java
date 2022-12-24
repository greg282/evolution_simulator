import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements IEngine, Runnable {

    private Map map;

    private List<Animal> animals = new ArrayList<>();

    private int delay = 0;

    SimulationEngine(Map map) {
        this.map = map;

        // tutaj spawn początkowych zwierząt

        Animal animal1 = new Animal(map, new Vector2d(1,1), new int[] {0}, 4 );
        animals.add(animal1);
        map.place(animal1);

        Animal animal2 = new Animal(map, new Vector2d(2,2), new int[] {1, 0, 5, 0, 4}, 10 );
        animals.add(animal2);
        map.place(animal2);
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

            for (Animal animal: animals) animal.move();

            //tutaj konsumpcja roślin na których pola weszły zwierzęta

            //tutaj rozmnażanie się najedzonych zwierząt znajdujących się na tym samym polu

            //tutaj wzrastanie nowych roślin na wybranych polach mapy
        }
    }

    public void addAnimalsObserver(IPositionChangeObserver observer) {
        for (Animal animal : animals) {
            animal.addObserver(observer);
        }
    }
}
