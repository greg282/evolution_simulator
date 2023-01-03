import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DayStat {
    private int day;
    private int total_animals;



    private int total_plants;
    private int free_fields;
    private double avg_energy;
    private double avg_lifespan_of_dead;

    public void setDay(int day) {
        this.day = day;
    }

    public void setTotal_animals(int total_animals) {
        this.total_animals = total_animals;
    }

    public void setTotal_plants(int total_plants) {
        this.total_plants = total_plants;
    }

    public void setFree_fields(int free_fields) {
        this.free_fields = free_fields;
    }

    public void setAvg_energy(double avg_energy) {
        this.avg_energy = avg_energy;
    }

    public void setAvg_lifespan_of_dead(double avg_lifespan_of_dead) {
        this.avg_lifespan_of_dead = avg_lifespan_of_dead;
    }

    public void writeToFile(File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.append('\n');
        writer.append(this.toString());
        writer.close();
    }


    @Override
    public String toString() {
        return day +
                "," + total_animals +
                "," + total_plants +
                "," + free_fields +
                "," + avg_energy +
                "," + avg_lifespan_of_dead;
    }
}
