import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

public class App extends Application {

    private SimulationEngine engine;
    private Map map;

    protected java.util.Map<Vector2d, Plant> plants = new HashMap<>();
    private VBox vBox;

    private int cellWidth = 40;
    private int cellHeight = 40;

    public void init() {
        try {
            map = new Map(10,10);

            engine = new SimulationEngine(this, map, plants);
            engine.setDelay(1000);

            vBox = new VBox(10, new Label(""));

        } catch(IllegalArgumentException exception) {
            System.out.println("EXCEPTION: " + exception.getMessage() + " TERMINATING PROGRAM");
        }
    }

    public void start(Stage primaryStage) {

        renderGridPane();

        Scene scene = new Scene(vBox, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

        Thread engineThread = new Thread(engine);
        engineThread.start();

        primaryStage.setOnCloseRequest(event -> {
            engineThread.interrupt();
            Platform.exit();
        });
    }

    private void renderGridPane() {

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        Label newLabel = new Label("y/x");
        gridPane.add(newLabel, 0, 0, 1, 1);
        gridPane.getColumnConstraints().add(new ColumnConstraints( cellWidth ));
        gridPane.getRowConstraints().add(new RowConstraints( cellHeight ));
        GridPane.setHalignment(newLabel, HPos.CENTER);

        for (int i=0; i <= map.getUpperRightVector().x - map.getLowerLeftVector().x; i++) {

            newLabel = new Label( String.valueOf(map.getLowerLeftVector().x + i));
            gridPane.add( newLabel , i+1, 0, 1, 1);
            gridPane.getColumnConstraints().add(new ColumnConstraints( cellWidth ));
            GridPane.setHalignment(newLabel, HPos.CENTER);
        }
        for (int i=0; i <= map.getUpperRightVector().y - map.getLowerLeftVector().y; i++) {

            newLabel = new Label( String.valueOf(map.getUpperRightVector().y - i));
            gridPane.add(newLabel, 0, i+1, 1, 1);
            gridPane.getRowConstraints().add(new RowConstraints( cellHeight ));
            GridPane.setHalignment(newLabel, HPos.CENTER);
        }

        for (int i = map.getLowerLeftVector().x; i <= map.getUpperRightVector().x; i++) {

            for (int j = map.getLowerLeftVector().y; j <= map.getUpperRightVector().y; j++) {

                if (map.isOccupied(new Vector2d(i,j))) {

                    VBox elementBox = new GuiElementBox( (IMapElement) map.objectAt( new Vector2d(i,j) ) ).getElementBox();

                    gridPane.add( elementBox , i - map.getLowerLeftVector().x + 1, map.getUpperRightVector().y - j + 1, 1, 1);
                }
                if (plants.containsKey(new Vector2d(i,j))) {
                    gridPane.add( new Label("Plant") , i - map.getLowerLeftVector().x + 1, map.getUpperRightVector().y - j + 1, 1, 1);

                }
            }
        }



        vBox.getChildren().remove(0);
        vBox.getChildren().add(0, gridPane);
    }

    public void mapRefresh() {
        Platform.runLater(this::renderGridPane);
    }
}
