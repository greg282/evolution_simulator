import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class GuiElementBox {

    private VBox elementBox;

    GuiElementBox(IMapElement mapElement) {

        double colorValue = (double) ((Animal) mapElement).energy/10;

        Circle circle = new Circle(15);
        circle.setFill(javafx.scene.paint.Color.color(1-colorValue, 1-colorValue, 1-colorValue));

        elementBox = new VBox(circle);
        elementBox.setAlignment(Pos.CENTER);
    }

    VBox getElementBox() {
        return elementBox;
    }
}
