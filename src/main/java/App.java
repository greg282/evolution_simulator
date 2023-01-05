import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

        Label mapVariantLabel = new Label("Map variant:");
        ToggleGroup mapVariantGroup = new ToggleGroup();
        RadioButton globe = new RadioButton("Globe");
        RadioButton portal = new RadioButton("Hell's portal");
        globe.setToggleGroup(mapVariantGroup);
        portal.setToggleGroup(mapVariantGroup);
        globe.setSelected(true);
        HBox mapVariant = new HBox(mapVariantLabel, globe, portal);
        mapVariant.setSpacing(spacingValue);
        mapVariant.setAlignment(Pos.CENTER);

        Label startingPlantsLabel = new Label("Starting number of plants:");
        TextField startingPlantsInput = new TextField("10"); //Sprawdzić czy mniejsza lub równa liczbie wszystkich pól na mapie!!!
        HBox startingPlants = new HBox(startingPlantsLabel, startingPlantsInput);
        startingPlants.setSpacing(spacingValue);
        startingPlants.setAlignment(Pos.CENTER);

        Label providedEnergyLabel = new Label("Energy provided by eating one plant:");
        TextField providedEnergyInput = new TextField("4");
        HBox providedEnergy = new HBox(providedEnergyLabel, providedEnergyInput);
        providedEnergy.setSpacing(spacingValue);
        providedEnergy.setAlignment(Pos.CENTER);

        Label growingPlantsLabel = new Label("Number of plants growing each day:");
        TextField growingPlantsInput = new TextField("2"); //Sprawdzić czy mniejsza lub równa liczbie wszystkich pól na mapie!!!
        HBox growingPlants = new HBox(growingPlantsLabel, growingPlantsInput);
        growingPlants.setSpacing(spacingValue);
        growingPlants.setAlignment(Pos.CENTER);

        Label plantsVariantLabel = new Label("Plant growth variant:");
        ToggleGroup plantsVariantGroup = new ToggleGroup();
        RadioButton equators = new RadioButton("Forested equators");
        RadioButton corpses = new RadioButton("Toxic corpses");
        equators.setToggleGroup(plantsVariantGroup);
        corpses.setToggleGroup(plantsVariantGroup);
        equators.setSelected(true);
        HBox plantsVariant = new HBox(plantsVariantLabel, equators, corpses);
        plantsVariant.setSpacing(spacingValue);
        plantsVariant.setAlignment(Pos.CENTER);

        Label startingAnimalsLabel = new Label("Starting number of animals:");
        TextField startingAnimalsInput = new TextField("8");
        HBox startingAnimals = new HBox(startingAnimalsLabel, startingAnimalsInput);
        startingAnimals.setSpacing(spacingValue);
        startingAnimals.setAlignment(Pos.CENTER);

        Label startingEnergyLabel = new Label("Starting animal energy:");
        TextField startingEnergyInput = new TextField("10"); //Sprawdzić czy mniejsza lub równa energii maksymalnej!!!
        HBox startingEnergy = new HBox(startingEnergyLabel, startingEnergyInput);
        startingEnergy.setSpacing(spacingValue);
        startingEnergy.setAlignment(Pos.CENTER);

        Label maxEnergyLabel = new Label("Max animal energy:");
        TextField MaxEnergyInput = new TextField("10");
        HBox maxEnergy = new HBox(maxEnergyLabel, MaxEnergyInput);
        maxEnergy.setSpacing(spacingValue);
        maxEnergy.setAlignment(Pos.CENTER);

        Label breedReadyLabel = new Label("Energy needed to consider animal ready to breed:");
        TextField breedReadyInput = new TextField("5");
        HBox breedReady = new HBox(breedReadyLabel, breedReadyInput);
        breedReady.setSpacing(spacingValue);
        breedReady.setAlignment(Pos.CENTER);

        Label breedEnergyLabel = new Label("Energy of the parents used during breed:");
        TextField breedEnergyInput = new TextField("2"); //Sprawdzić czy mniejsza lub równa od breedReady!!!
        HBox breedEnergy = new HBox(breedEnergyLabel, breedEnergyInput);
        breedEnergy.setSpacing(spacingValue);
        breedEnergy.setAlignment(Pos.CENTER);

        Label mutationNumberLabel = new Label("Minimum and maximum number of mutations in children:");
        TextField mutationMinimumInput = new TextField("1"); //Sprawdzić czy mutationMinimumInput <= mutationMaximumInput!!!
        TextField mutationMaximumInput = new TextField("3"); //Sprawdzić czy mniejsza od długości genu!!!
        HBox mutationNumber = new HBox(mutationNumberLabel, mutationMinimumInput, mutationMaximumInput);
        mutationNumber.setSpacing(spacingValue);
        mutationNumber.setAlignment(Pos.CENTER);

        Label mutationVariantLabel = new Label("Mutation variant:");
        ToggleGroup mutationVariantGroup = new ToggleGroup();
        RadioButton randomness = new RadioButton("Full randomness");
        RadioButton correction = new RadioButton("Slight correction");
        randomness.setToggleGroup(mutationVariantGroup);
        correction.setToggleGroup(mutationVariantGroup);
        randomness.setSelected(true);
        HBox mutationVariant = new HBox(mutationVariantLabel, randomness, correction);
        mutationVariant.setSpacing(spacingValue);
        mutationVariant.setAlignment(Pos.CENTER);

        Label genomeLengthLabel = new Label("Animal genome length:");
        TextField genomeLengthInput = new TextField("7");
        HBox genomeLength = new HBox(genomeLengthLabel, genomeLengthInput);
        genomeLength.setSpacing(spacingValue);
        genomeLength.setAlignment(Pos.CENTER);

        Label behaviorVariantLabel = new Label("Behavior variant:");
        ToggleGroup behaviorVariantGroup = new ToggleGroup();
        RadioButton predestination = new RadioButton("Full predestination");
        RadioButton madness = new RadioButton("A bit of madness");
        predestination.setToggleGroup(behaviorVariantGroup);
        madness.setToggleGroup(behaviorVariantGroup);
        predestination.setSelected(true);
        HBox behaviorVariant = new HBox(behaviorVariantLabel, predestination, madness);
        behaviorVariant.setSpacing(spacingValue);
        behaviorVariant.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        HBox start = new HBox(startButton);
        start.setAlignment(Pos.CENTER);

        Label invalidArgumentLabel = new Label("");
        HBox invalidArgument = new HBox(invalidArgumentLabel);
        invalidArgument.setSpacing(spacingValue);
        invalidArgument.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(
                mapDimensions,
                mapVariant,
                startingPlants,
                providedEnergy,
                growingPlants,
                plantsVariant,
                startingAnimals,
                startingEnergy,
                maxEnergy,
                breedReady,
                breedEnergy,
                mutationNumber,
                mutationVariant,
                genomeLength,
                behaviorVariant,
                start,
                invalidArgument

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

                try {
                    if (Integer.parseInt(mapHeightInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(mapWidthInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(startingPlantsInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(providedEnergyInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(growingPlantsInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(startingAnimalsInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(startingEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(startingEnergyInput.getText()) > Integer.parseInt(MaxEnergyInput.getText())) throw new IllegalArgumentException("Starting energy bigger than max energy");
                    if (Integer.parseInt(MaxEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(breedReadyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(breedEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(breedEnergyInput.getText()) > Integer.parseInt(breedReadyInput.getText())) throw new IllegalArgumentException("Error: breedEnergy bigger than breedReady");
                    if (Integer.parseInt(mutationMinimumInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(mutationMaximumInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(genomeLengthInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");

                    Simulation simulation = new Simulation();
                    simulation.init(
                            Integer.parseInt(mapHeightInput.getText()),
                            Integer.parseInt(mapWidthInput.getText()),
                            Integer.parseInt(startingPlantsInput.getText()),
                            Integer.parseInt(providedEnergyInput.getText()),
                            Integer.parseInt(growingPlantsInput.getText()),
                            Integer.parseInt(startingAnimalsInput.getText()),
                            Integer.parseInt(startingEnergyInput.getText()),
                            Integer.parseInt(breedReadyInput.getText()),
                            Integer.parseInt(breedEnergyInput.getText()),
                            Integer.parseInt(mutationMinimumInput.getText()),
                            Integer.parseInt(mutationMaximumInput.getText()),
                            Integer.parseInt(genomeLengthInput.getText()),
                            globe.isSelected(),
                            equators.isSelected(),
                            randomness.isSelected(),
                            predestination.isSelected(),
                            Integer.parseInt(MaxEnergyInput.getText())

                            );
                    simulation.start(new Stage());
                    invalidArgumentLabel.setText("");
                    invalidArgumentLabel.setTextFill(Color.BLACK);

                }  catch (NumberFormatException exception) {
                    System.out.println("Could not convert string to integer");
                    invalidArgumentLabel.setText("Invalid argument! A text was given instead of a number");
                    invalidArgumentLabel.setTextFill(Color.RED);
                    exception.printStackTrace();

                } catch (IllegalArgumentException exception) {
                    System.out.println(exception.getMessage());
                    invalidArgumentLabel.setText("Invalid argument! " + exception.getMessage());
                    invalidArgumentLabel.setTextFill(Color.RED);
                }
            }
        });

    }
}
