import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DayStat {
    private int day;
    private int total_animals;

    public String genom;

    private int total_plants;
    private int free_fields;
    private double avg_energy;
    private double avg_lifespan_of_dead;

    public void setDay(int day) {
        this.day = day;
    }
    public int getDay() {
        return day;
    }

    public void setTotal_animals(int total_animals) {
        this.total_animals = total_animals;
    }
    public int getTotal_animals() {
        return total_animals;
    }

    public void setTotal_plants(int total_plants) {
        this.total_plants = total_plants;
    }
    public int getTotal_plants() {
        return total_plants;
    }

    public void setFree_fields(int free_fields) {
        this.free_fields = free_fields;
    }
    public int getFree_fields() {
        return free_fields;
    }

    public void setAvg_energy(double avg_energy) {
        this.avg_energy = avg_energy;
    }
    public double getAvg_energy() {
        return (double) Math.round(avg_energy * 100) / 100;
    }

    public void setAvg_lifespan_of_dead(double avg_lifespan_of_dead) {
        this.avg_lifespan_of_dead = avg_lifespan_of_dead;
    }
    public double getAvg_lifespan_of_dead() {
        return (double) Math.round(avg_lifespan_of_dead * 100) / 100;
    }

    public String getGenome() {
        return genom;
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
                "," + getAvg_energy() +
                "," + getAvg_lifespan_of_dead() +
                "," + genom;
    }
}
