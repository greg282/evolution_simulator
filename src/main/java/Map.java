import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Map {
    private int width;
    private int height;

    protected java.util.Map<Vector2d, List<Animal>> animals = new HashMap<>();

    Map(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector2d getLowerLeftVector() {
        return new Vector2d(0,0);
    }

    public Vector2d getUpperRightVector() {
        return new Vector2d(width - 1, height - 1);
    }

    public Vector2d getRandomPosition() {
        Random random = new Random();
        int randomX = random.nextInt(getUpperRightVector().x + 1);
        int randomY = random.nextInt(getUpperRightVector().y + 1);
        return new Vector2d(randomX, randomY);
    }

    public boolean place(Animal animal) {

        Vector2d position = animal.getPosition();

        if (canMoveTo(position)) {

            if (isOccupied(position)) {

                List<Animal> animalList = animals.get(position);
                animalList.add(animal);
            }
            else {
                List<Animal> animalList = new ArrayList<>();
                animalList.add(animal);
                animals.put(position, animalList);
            }

            return true;
        }
        return false;
    }

    public void remove(Animal animal) {

        List<Animal> animalsToUpdate = animals.get(animal.getPosition());
        animalsToUpdate.remove(animal);
        if ( animalsToUpdate.size() == 0) {
            animals.remove(animal.getPosition());
        }
    }

    public boolean canMoveTo(Vector2d position) {
        return position.follows(getLowerLeftVector()) && position.precedes(getUpperRightVector());
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);
    }

    public Animal animalAt(Vector2d position) {
        return animals.get(position).get(0); //wybiera pierwsze zwierzę z listy do wyświetlenia
    }
}
