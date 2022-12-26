import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return new Vector2d(width, height);
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
        return position.follows(new Vector2d(0,0)) && position.precedes(new Vector2d(width,height));
    }

    public boolean isOccupied(Vector2d position) {
        return animals.containsKey(position);  //|| grass.containsKey(position);
    }

    public Object objectAt(Vector2d position) {
        Object returnObject = animals.get(position).get(0); //wybiera pierwszy element z listy do wyświetlenia
        //if (returnObject == null) returnObject = grass.get(position);
        return returnObject;
    }
}