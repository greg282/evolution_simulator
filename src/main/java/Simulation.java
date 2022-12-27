import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.HashMap;

public class Simulation {

    private SimulationEngine engine;
    private Map map;

    protected java.util.Map<Vector2d, Plant> plants = new HashMap<>();
    private VBox vBox;

    private int cellWidth = 40;
    private int cellHeight = 40;

    public void init() {

        map = new Map(10,10);

        engine = new SimulationEngine(this, map, plants);
        engine.setDelay(200);

        Button startStopButton = new Button("Pause");
        startStopButton.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");

        startStopButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                if (startStopButton.getText() == "Pause") {
                    startStopButton.setText("Unpause");
                    engine.pause();
                }

                else {
                    startStopButton.setText("Pause");
                    engine.unpause();
                }

            }
        });

        vBox = new VBox(10, new Label(""), startStopButton);

    }

    public void start(Stage primaryStage) {

        renderGridPane();

        Scene scene = new Scene(vBox, 600, 600);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Evolution Simulator");
        primaryStage.show();

        Thread engineThread = new Thread(engine);
        engineThread.setDaemon(true);
        engineThread.start();

        primaryStage.setOnCloseRequest(event -> {
            engineThread.interrupt();
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

                if ( map.isOccupied(new Vector2d(i,j)) || plants.containsKey(new Vector2d(i,j)) ) { //pole jest zajęte przez przynajmniej jedno zwierzę lub na polu rośnie roślina

                    VBox elementBox = new VBox();

                    if ( map.isOccupied(new Vector2d(i,j)) ) {

                        double colorValue = (double) map.animalAt( new Vector2d(i,j) ).energy / 10;
                        Circle circle = new Circle(15);
                        circle.setFill(javafx.scene.paint.Color.color(1-colorValue, 1-colorValue, 1-colorValue));
                        elementBox = new VBox(circle);
                        elementBox.setAlignment(Pos.CENTER);

                    }

                    if ( plants.containsKey(new Vector2d(i,j)) ) {
                        elementBox.setStyle("-fx-background-color: green;");
                    }

                    gridPane.add( elementBox , i - map.getLowerLeftVector().x + 1, map.getUpperRightVector().y - j + 1, 1, 1);
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
