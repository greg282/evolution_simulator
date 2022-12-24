public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    private MapDirection next() {
        return switch(this) {
            case NORTH -> NORTH_EAST;
            case NORTH_EAST -> EAST;
            case EAST -> SOUTH_EAST;
            case SOUTH_EAST -> SOUTH;
            case SOUTH -> SOUTH_WEST;
            case SOUTH_WEST -> WEST;
            case WEST -> NORTH_WEST;
            case NORTH_WEST -> NORTH;
        };
    }

    MapDirection rotate(int times) {
        MapDirection direction = this;
        for (int i = 0; i < times; i++) {
            direction = direction.next();
        }
        return direction;
    }

    Vector2d toUnitVector() {
        return switch(this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTH_EAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTH_EAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTH_WEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTH_WEST -> new Vector2d(-1, 1);
        };
    }
}
