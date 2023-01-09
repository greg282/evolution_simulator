import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Simulation {

    private SimulationEngine engine;
    private Map map;
    private int maxEnergy;
    protected java.util.Map<Vector2d, Plant> plants = new HashMap<>();

    private VBox vBox;
    private Group group;
    private GridPane gridPane;

    private int cellWidth = 40;
    private int cellHeight = 40;

    public void init(
            int mapHeight,
            int mapWidth,
            int startingPlants,
            int providedEnergy,
            int growingPlants,
            int startingAnimals,
            int startingEnergy,
            int maxEnergy,
            int breedReady,
            int breedEnergy,
            int mutationMinimum,
            int mutationMaximum,
            int genomeLength,
            boolean globeVariant,
            boolean equatorsVariant,
            boolean randomnessVariant,
            boolean predestinationVariant,
            boolean saveToCsv,
            int refreshTime
    ) {
        this.maxEnergy = maxEnergy;

        map = new Map(mapWidth, mapHeight);

        engine = new SimulationEngine(
                this,
                map,
                plants,
                startingPlants,
                providedEnergy,
                growingPlants,
                startingAnimals,
                startingEnergy,
                maxEnergy,
                breedReady,
                breedEnergy,
                mutationMinimum,
                mutationMaximum,
                genomeLength,
                globeVariant,
                equatorsVariant,
                randomnessVariant,
                predestinationVariant,
                saveToCsv
        );
        engine.setDelay(refreshTime);

        Button startStopButton = new Button("Pause");
        startStopButton.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        HBox startStop = new HBox(startStopButton);
        startStop.setAlignment(Pos.CENTER);

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

        vBox = new VBox(50, new Label(""), new Label(""), startStop);

    }

    public void start(Stage primaryStage) {

        group = new Group(new GridPane());
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox, group);
        Scene scene = new Scene(hBox, 1200, 800);
        renderGridPane();

        vBox.minWidthProperty().bind(scene.widthProperty().multiply(0.4));
        vBox.maxWidthProperty().bind(scene.widthProperty().multiply(0.4));

        Scale scale = new Scale();
        scale.xProperty().bind(scene.widthProperty().add(vBox.widthProperty().negate()).divide(gridPane.widthProperty()));
        scale.yProperty().bind(scene.heightProperty().divide(gridPane.heightProperty()));
        group.getTransforms().add(scale);

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

        gridPane = new GridPane();
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

                        double colorValue = (double) map.animalAt( new Vector2d(i,j) ).energy / this.maxEnergy;

                        Circle circle = new Circle(15);
                        circle.setFill(javafx.scene.paint.Color.color(1-colorValue, 1-colorValue, 1-colorValue));

                        if(engine.getDayStatistics() != null && Objects.equals(engine.getDayStatistics().getGenome(), engine.GenomToCSVString(map.animalAt(new Vector2d(i, j)).getGenome()))) {
                            circle.setStroke(Color.YELLOW);
                            circle.setStrokeWidth(5);
                        }

                        elementBox = new VBox(circle);
                        elementBox.setAlignment(Pos.CENTER);

                        int finalI = i;
                        int finalJ = j;
                        elementBox.setOnMouseClicked((event) -> Platform.runLater(()->{
                            engine.setTrackedAnimal(map.animalAt(new Vector2d(finalI, finalJ)));
                            updateTrackedAnimal();
                        }));

                    }

                    if ( plants.containsKey(new Vector2d(i,j)) ) {
                        elementBox.setStyle("-fx-background-color: green;");
                    }

                    gridPane.add( elementBox , i - map.getLowerLeftVector().x + 1, map.getUpperRightVector().y - j + 1, 1, 1);
                }
            }
        }

        group.getChildren().remove(0);
        group.getChildren().add(0, gridPane);

        updateStatistics();
        updateTrackedAnimal();

    }

    private void updateStatistics() {

        VBox Statistics = new VBox(10);

        DayStat dayStat = engine.getDayStatistics();
        if (dayStat != null) {

            Label StatisticsTitleLabel = new Label("Statistics");
            StatisticsTitleLabel.setStyle("-fx-padding: 10 0 0 0; -fx-font-size: 15pt; -fx-font-weight: bold");
            HBox StatisticsTitle = new HBox(StatisticsTitleLabel);
            StatisticsTitle.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(0, StatisticsTitle);

            Label dayLabel = new Label("Day: " + dayStat.getDay());
            HBox day = new HBox(dayLabel);
            day.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(1, day);

            Label noAnimalsLabel = new Label("Total number of animals: " + dayStat.getTotal_animals());
            HBox noAnimals = new HBox(noAnimalsLabel);
            noAnimals.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(2, noAnimals);

            Label noPlantsLabel = new Label("Total number of plants: " + dayStat.getTotal_plants());
            HBox noPlants = new HBox(noPlantsLabel);
            noPlants.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(3, noPlants);

            Label noFreeFieldsLabel = new Label("Total number of free fields: " + dayStat.getFree_fields());
            HBox noFreeFields = new HBox(noFreeFieldsLabel);
            noFreeFields.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(4, noFreeFields);

            Label genomeLabel = new Label("Most popular genome: " + dayStat.getGenome());
            HBox genome = new HBox(genomeLabel);
            genome.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(5, genome);

            Label averageEnergyLabel = new Label("Average energy level: " + dayStat.getAvg_energy());
            HBox averageEnergy = new HBox(averageEnergyLabel);
            averageEnergy.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(6, averageEnergy);

            Label averageLifespanLabel = new Label("Average animals lifespan: " + dayStat.getAvg_lifespan_of_dead() + " days");
            HBox averageLifespan = new HBox(averageLifespanLabel);
            averageLifespan.setAlignment(Pos.CENTER);
            Statistics.getChildren().add(7, averageLifespan);
        }

        vBox.getChildren().remove(0);
        vBox.getChildren().add(0, Statistics);
    }

    private void updateTrackedAnimal() {

        VBox TrackedAnimal = new VBox(10);

        AnimalStat animalStat = engine.getAnimalStatistics();

        if (animalStat == null) {
            Label TrackedAnimalTitleLabel = new Label("Click on an animal to track it");
            TrackedAnimalTitleLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight: bold");
            HBox TrackedAnimalTitle = new HBox(TrackedAnimalTitleLabel);
            TrackedAnimalTitle.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(0, TrackedAnimalTitle);
        }

        else {

            Label TrackedAnimalTitleLabel = new Label("Tracking animal");
            TrackedAnimalTitleLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight: bold");
            HBox TrackedAnimalTitle = new HBox(TrackedAnimalTitleLabel);
            TrackedAnimalTitle.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(0, TrackedAnimalTitle);

            Label genomeLabel = new Label("Genome: " + animalStat.getGenom());
            HBox genome = new HBox(genomeLabel);
            genome.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(1, genome);

            Label activePartLabel = new Label("Active part: " + animalStat.getCurrent());
            HBox activePart = new HBox(activePartLabel);
            activePart.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(2, activePart);

            Label energyLabel = new Label("Energy: " + animalStat.getEnergy());
            HBox energy = new HBox(energyLabel);
            energy.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(3, energy);

            Label plantsLabel = new Label("Number of plants eaten: " + animalStat.getPlants_eated());
            HBox plants = new HBox(plantsLabel);
            plants.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(4, plants);

            Label noChildrenLabel = new Label("Number of children: " + animalStat.getN_of_children());
            HBox noChildren = new HBox(noChildrenLabel);
            noChildren.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(5, noChildren);

            Label lifespanLabel = new Label("Lifespan: " + animalStat.getLifespan() + " days");
            HBox lifespan = new HBox(lifespanLabel);
            lifespan.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(6, lifespan);

            Label diedDayLabel;
            if (animalStat.getDay_of_die() == -1) diedDayLabel = new Label("Animal is alive");
            else diedDayLabel = new Label("Died on day: " + animalStat.getDay_of_die());
            HBox diedDay = new HBox(diedDayLabel);
            diedDay.setAlignment(Pos.CENTER);
            TrackedAnimal.getChildren().add(7, diedDay);
        }

        vBox.getChildren().remove(1);
        vBox.getChildren().add(1, TrackedAnimal);
    }

    public void mapRefresh() {
        Platform.runLater(this::renderGridPane);
    }
}
