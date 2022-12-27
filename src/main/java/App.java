import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    public void start(Stage primaryStage) {

        //?dodać jungle height i width?
        //?dodać daily energy cost?
        //?dodać refresh time (ms)?

        int spacingValue = 10;

        Label mapDimensionsLabel = new Label("Map height and width:");
        TextField mapHeightInput = new TextField("10");
        TextField mapWidthInput = new TextField("10");
        HBox mapDimensions = new HBox(mapDimensionsLabel, mapHeightInput, mapWidthInput);
        mapDimensions.setSpacing(spacingValue);
        mapDimensions.setAlignment(Pos.CENTER);

        Label mapVariantLabel = new Label("/* Placeholder - Tutaj selektor wyboru wariantu mapy */");
        HBox mapVariant = new HBox(mapVariantLabel);
        mapVariant.setSpacing(spacingValue);
        mapVariant.setAlignment(Pos.CENTER);

        Label startingPlantsLabel = new Label("Starting number of plants:");
        TextField startingPlantsInput = new TextField("10");
        HBox startingPlants = new HBox(startingPlantsLabel, startingPlantsInput);
        startingPlants.setSpacing(spacingValue);
        startingPlants.setAlignment(Pos.CENTER);

        Label providedEnergyLabel = new Label("Energy provided by eating one plant:");
        TextField providedEnergyInput = new TextField("4");
        HBox providedEnergy = new HBox(providedEnergyLabel, providedEnergyInput);
        providedEnergy.setSpacing(spacingValue);
        providedEnergy.setAlignment(Pos.CENTER);

        Label growingPlantsLabel = new Label("Number of plants growing each day:");
        TextField growingPlantsInput = new TextField("2");
        HBox growingPlants = new HBox(growingPlantsLabel, growingPlantsInput);
        growingPlants.setSpacing(spacingValue);
        growingPlants.setAlignment(Pos.CENTER);

        Label plantsVariantLabel = new Label("/* Placeholder - Tutaj selektor wyboru wariantu wzrostu roslin */");
        HBox plantsVariant = new HBox(plantsVariantLabel);
        plantsVariant.setSpacing(spacingValue);
        plantsVariant.setAlignment(Pos.CENTER);

        Label startingAnimalsLabel = new Label("Starting number of animals:");
        TextField startingAnimalsInput = new TextField("2");
        HBox startingAnimals = new HBox(startingAnimalsLabel, startingAnimalsInput);
        startingAnimals.setSpacing(spacingValue);
        startingAnimals.setAlignment(Pos.CENTER);

        Label startingEnergyLabel = new Label("Starting animal energy:");
        TextField startingEnergyInput = new TextField("10");
        HBox startingEnergy = new HBox(startingEnergyLabel, startingEnergyInput);
        startingEnergy.setSpacing(spacingValue);
        startingEnergy.setAlignment(Pos.CENTER);

        Label breedReadyLabel = new Label("Energy needed to consider animal ready to breed:");
        TextField breedReadyInput = new TextField("5");
        HBox breedReady = new HBox(breedReadyLabel, breedReadyInput);
        breedReady.setSpacing(spacingValue);
        breedReady.setAlignment(Pos.CENTER);

        Label breedEnergyLabel = new Label("Energy of the parents used during breed:");
        TextField breedEnergyInput = new TextField("2");
        HBox breedEnergy = new HBox(breedEnergyLabel, breedEnergyInput);
        breedEnergy.setSpacing(spacingValue);
        breedEnergy.setAlignment(Pos.CENTER);

        Label mutationNumberLabel = new Label("Minimum and maximum number of mutations in children:");
        TextField mutationMinimumInput = new TextField("1");
        TextField mutationMaximumInput = new TextField("3"); //Sprawdzić czy mniejsza od długości genu!!!
        HBox mutationNumber = new HBox(mutationNumberLabel, mutationMinimumInput, mutationMaximumInput);
        mutationNumber.setSpacing(spacingValue);
        mutationNumber.setAlignment(Pos.CENTER);

        Label mutationVariantLabel = new Label("/* Placeholder - Tutaj selektor wyboru wariantu mutacji */");
        HBox mutationVariant = new HBox(mutationVariantLabel);
        mutationVariant.setSpacing(spacingValue);
        mutationVariant.setAlignment(Pos.CENTER);

        Label genomeLengthLabel = new Label("Animal genome length:");
        TextField genomeLengthInput = new TextField("7");
        HBox genomeLength = new HBox(genomeLengthLabel, genomeLengthInput);
        genomeLength.setSpacing(spacingValue);
        genomeLength.setAlignment(Pos.CENTER);

        Label behaviorVariantLabel = new Label("/* Placeholder - Tutaj selektor wyboru wariantu zachowania zwierzakow */");
        HBox behaviorVariant = new HBox(behaviorVariantLabel);
        behaviorVariant.setSpacing(spacingValue);
        behaviorVariant.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        HBox start = new HBox(startButton);
        start.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(
                mapDimensions,
                mapVariant,
                startingPlants,
                providedEnergy,
                growingPlants,
                plantsVariant,
                startingAnimals,
                startingEnergy,
                breedReady,
                breedEnergy,
                mutationNumber,
                mutationVariant,
                genomeLength,
                behaviorVariant,
                start
        );
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(vbox, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Evolution Simulator (Settings)");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                Simulation simulation = new Simulation();
                simulation.init();
                simulation.start(new Stage());
            }
        });

    }
}
