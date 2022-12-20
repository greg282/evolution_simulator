public class Map {
    private int width;
    private int height;

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
}
