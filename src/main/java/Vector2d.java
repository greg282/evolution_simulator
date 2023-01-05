import java.util.Objects;

public class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return ('(' + String.valueOf(this.x) + ',' + String.valueOf(this.y) + ')');
    }

    boolean precedes(Vector2d other) {
        return (this.x <= other.x && this.y <= other.y);
    }

    boolean follows(Vector2d other) {
        return (this.x >= other.x && this.y >= other.y);
    }

    Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    Vector2d upperRight(Vector2d other) {
        return new Vector2d(java.lang.Math.max(this.x, other.x), java.lang.Math.max(this.y, other.y));
    }

    Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(java.lang.Math.min(this.x, other.x), java.lang.Math.min(this.y, other.y));
    }

    Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;

        Vector2d that = (Vector2d) other;
        return  (this.x == that.x && this.y == that.y);
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
