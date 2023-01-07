public class AnimalStat {
    private String genom;
    private int energy;
    private int plants_eated=0;
    private int n_of_children;
    private int lifespan;

    @Override
    public String toString() {
        return "AnimalStat{" +
                "genom='" + genom + '\'' +
                ", energy=" + energy +
                ", plants_eated=" + plants_eated +
                ", n_of_children=" + n_of_children +
                ", lifespan=" + lifespan +
                ", day_of_die=" + day_of_die +
                '}';
    }

    private int day_of_die=-1;

    public String getGenom() {
        return genom;
    }

    public void setGenom(String genom) {
        this.genom = genom;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getPlants_eated() {
        return plants_eated;
    }

    public void setPlants_eated(int plants_eated) {
        this.plants_eated = plants_eated;
    }

    public int getN_of_children() {
        return n_of_children;
    }

    public void setN_of_children(int n_of_children) {
        this.n_of_children = n_of_children;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public int getDay_of_die() {
        return day_of_die;
    }

    public void setDay_of_die(int day_of_die) {
        this.day_of_die = day_of_die;
    }


}
