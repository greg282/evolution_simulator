import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal extends AbstractWorldMapElement {

    private Map map;

    private MapDirection orientation;

    int energy;

    int n_of_children = 0;

    private int age = 0;

    private int[] genome;
    private int current;

    Animal(Map map, Vector2d position, int[] genome, int energy) {

        Random random = new Random();

        this.map = map;
        this.position = position;
        this.genome = genome;
        this.energy = energy;
        this.current = random.nextInt(genome.length);

        this.orientation = MapDirection.values()[random.nextInt(MapDirection.values().length)];
    }

    void move() {

        orientation = orientation.rotate(genome[current]);
        current++;
        energy--;
        if (current == genome.length) current = 0;

        Vector2d nextPosition = position.add(orientation.toUnitVector());

        if (map.canMoveTo(nextPosition)) {
            map.remove(this);
            this.position = nextPosition;
            map.place(this);
        }

    }

    public int[] getGenome() {
        return genome;
    }

    public int getAge() {
        return age;
    }

    public void updateAge() {
        this.age++;
    }
}
