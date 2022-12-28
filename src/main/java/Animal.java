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

    void move(boolean predestinationVariant) {

        orientation = orientation.rotate(genome[current]);

        if (predestinationVariant) {
            current++;
            if (current == genome.length) current = 0;
        }
        else {
            Random rand = new Random();
            double probability = rand.nextDouble();

            if (probability < 0.8) {
                current++;
                if (current == genome.length) current = 0;
            } else {
                Random rand2 = new Random();
                current = rand2.nextInt(genome.length);
            }
        }

        Vector2d nextPosition = position.add(orientation.toUnitVector());

        if (map.canMoveTo(nextPosition)) {
            map.remove(this);
            this.position = nextPosition;
            map.place(this);
        }

        energy--;
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
