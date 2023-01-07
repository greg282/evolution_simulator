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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import org.json.*;

public class App extends Application {

    TextField mapHeightInput = new TextField();
    TextField mapWidthInput = new TextField();
    RadioButton globe = new RadioButton("Globe");
    RadioButton portal = new RadioButton("Hell's portal");
    TextField startingPlantsInput = new TextField();
    TextField providedEnergyInput = new TextField();
    TextField growingPlantsInput = new TextField();
    RadioButton equators = new RadioButton("Forested equators");
    RadioButton corpses = new RadioButton("Toxic corpses");
    TextField startingAnimalsInput = new TextField();
    TextField startingEnergyInput = new TextField();
    TextField maxEnergyInput = new TextField();
    TextField breedReadyInput = new TextField();
    TextField breedEnergyInput = new TextField();
    TextField mutationMinimumInput = new TextField();
    TextField mutationMaximumInput = new TextField();
    RadioButton randomness = new RadioButton("Full randomness");
    RadioButton correction = new RadioButton("Slight correction");
    TextField genomeLengthInput = new TextField();
    RadioButton predestination = new RadioButton("Full predestination");
    RadioButton madness = new RadioButton("A bit of madness");

    RadioButton saveTocsv = new RadioButton("Save to CSV file");
    RadioButton none = new RadioButton("None");
    TextField refreshTimeInput = new TextField();

    public void start(Stage primaryStage) {

        int spacingValue = 10;

        Label configSelectLabel = new Label("Select configuration:");
        configSelectLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight: bold");
        HBox configSelect = new HBox(configSelectLabel);
        configSelect.setAlignment(Pos.CENTER);

        Button configNormal = new Button("Normal");
        Button configChaos = new Button("Chaos");
        Button configBigMap = new Button("Big map");
        configNormal.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        configChaos.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        configBigMap.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        HBox config = new HBox(configNormal, configChaos, configBigMap);
        config.setSpacing(spacingValue);
        config.setAlignment(Pos.CENTER);

        configNormal.setOnAction(e -> loadConfig("configNormal.json"));
        configChaos.setOnAction(e -> loadConfig("configChaos.json"));
        configBigMap.setOnAction(e -> loadConfig("configBigMap.json"));

        Label configAlternativeLabel = new Label("...or set alternative configuration:");
        configAlternativeLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight: bold");
        HBox configAlternative = new HBox(configAlternativeLabel);
        configAlternative.setAlignment(Pos.CENTER);

        Label mapDimensionsLabel = new Label("Map height and width:");
        HBox mapDimensions = new HBox(mapDimensionsLabel, mapHeightInput, mapWidthInput);
        mapDimensions.setSpacing(spacingValue);
        mapDimensions.setAlignment(Pos.CENTER);

        Label mapVariantLabel = new Label("Map variant:");
        ToggleGroup mapVariantGroup = new ToggleGroup();
        globe.setToggleGroup(mapVariantGroup);
        portal.setToggleGroup(mapVariantGroup);
        HBox mapVariant = new HBox(mapVariantLabel, globe, portal);
        mapVariant.setSpacing(spacingValue);
        mapVariant.setAlignment(Pos.CENTER);

        Label startingPlantsLabel = new Label("Starting number of plants:");
        HBox startingPlants = new HBox(startingPlantsLabel, startingPlantsInput);
        startingPlants.setSpacing(spacingValue);
        startingPlants.setAlignment(Pos.CENTER);

        Label providedEnergyLabel = new Label("Energy provided by eating plant:");
        HBox providedEnergy = new HBox(providedEnergyLabel, providedEnergyInput);
        providedEnergy.setSpacing(spacingValue);
        providedEnergy.setAlignment(Pos.CENTER);

        Label growingPlantsLabel = new Label("Number of plants growing each day:");
        HBox growingPlants = new HBox(growingPlantsLabel, growingPlantsInput);
        growingPlants.setSpacing(spacingValue);
        growingPlants.setAlignment(Pos.CENTER);

        Label plantsVariantLabel = new Label("Plant growth variant:");
        ToggleGroup plantsVariantGroup = new ToggleGroup();
        equators.setToggleGroup(plantsVariantGroup);
        corpses.setToggleGroup(plantsVariantGroup);
        HBox plantsVariant = new HBox(plantsVariantLabel, equators, corpses);
        plantsVariant.setSpacing(spacingValue);
        plantsVariant.setAlignment(Pos.CENTER);

        Label startingAnimalsLabel = new Label("Starting number of animals:");
        HBox startingAnimals = new HBox(startingAnimalsLabel, startingAnimalsInput);
        startingAnimals.setSpacing(spacingValue);
        startingAnimals.setAlignment(Pos.CENTER);

        Label startingEnergyLabel = new Label("Starting animal energy:");
        HBox startingEnergy = new HBox(startingEnergyLabel, startingEnergyInput);
        startingEnergy.setSpacing(spacingValue);
        startingEnergy.setAlignment(Pos.CENTER);

        Label maxEnergyLabel = new Label("Maximum animal energy:");
        HBox maxEnergy = new HBox(maxEnergyLabel, maxEnergyInput);
        maxEnergy.setSpacing(spacingValue);
        maxEnergy.setAlignment(Pos.CENTER);

        Label breedReadyLabel = new Label("Energy needed to consider animal ready to breed:");
        HBox breedReady = new HBox(breedReadyLabel, breedReadyInput);
        breedReady.setSpacing(spacingValue);
        breedReady.setAlignment(Pos.CENTER);

        Label breedEnergyLabel = new Label("Energy of the parents used during breed:");
        HBox breedEnergy = new HBox(breedEnergyLabel, breedEnergyInput);
        breedEnergy.setSpacing(spacingValue);
        breedEnergy.setAlignment(Pos.CENTER);

        Label mutationNumberLabel = new Label("Minimum and maximum number of mutations in children:");
        HBox mutationNumber = new HBox(mutationNumberLabel, mutationMinimumInput, mutationMaximumInput);
        mutationNumber.setSpacing(spacingValue);
        mutationNumber.setAlignment(Pos.CENTER);

        Label mutationVariantLabel = new Label("Mutation variant:");
        ToggleGroup mutationVariantGroup = new ToggleGroup();
        randomness.setToggleGroup(mutationVariantGroup);
        correction.setToggleGroup(mutationVariantGroup);
        HBox mutationVariant = new HBox(mutationVariantLabel, randomness, correction);
        mutationVariant.setSpacing(spacingValue);
        mutationVariant.setAlignment(Pos.CENTER);

        Label genomeLengthLabel = new Label("Animal genome length:");
        HBox genomeLength = new HBox(genomeLengthLabel, genomeLengthInput);
        genomeLength.setSpacing(spacingValue);
        genomeLength.setAlignment(Pos.CENTER);

        Label behaviorVariantLabel = new Label("Behavior variant:");
        ToggleGroup behaviorVariantGroup = new ToggleGroup();
        predestination.setToggleGroup(behaviorVariantGroup);
        madness.setToggleGroup(behaviorVariantGroup);
        HBox behaviorVariant = new HBox(behaviorVariantLabel, predestination, madness);
        behaviorVariant.setSpacing(spacingValue);
        behaviorVariant.setAlignment(Pos.CENTER);
        //
        Label savingStatsLabel = new Label("Choose statistics saving option:");
        ToggleGroup savingStatsGroup = new ToggleGroup();
        saveTocsv.setToggleGroup(savingStatsGroup);
        none.setToggleGroup(savingStatsGroup);
        HBox savingStats = new HBox(savingStatsLabel, saveTocsv, none);
        savingStats.setSpacing(spacingValue);
        savingStats.setAlignment(Pos.CENTER);

        Label refreshTimeLabel = new Label("Refresh time (ms):");
        HBox refreshTime = new HBox(refreshTimeLabel, refreshTimeInput);
        refreshTime.setSpacing(spacingValue);
        refreshTime.setAlignment(Pos.CENTER);

        Button startButton = new Button("Start");
        startButton.setStyle("-fx-font-size: 18pt; -fx-padding: 5 20 5 20;");
        HBox start = new HBox(startButton);
        start.setAlignment(Pos.CENTER);

        Label invalidArgumentLabel = new Label("");
        HBox invalidArgument = new HBox(invalidArgumentLabel);
        invalidArgument.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(
                configSelect,
                config,
                configAlternative,
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
                savingStats,
                refreshTime,
                start,
                invalidArgument
        );
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(vbox, 800, 1000);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Evolution Simulator (Settings)");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
        });

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                try {

                    // looking for empty textFields and ToggleGroups
                    if (mapHeightInput.getText().isEmpty()) throw new IllegalArgumentException("'Map height' text field is empty!");
                    if (mapWidthInput.getText().isEmpty()) throw new IllegalArgumentException("'Map width' text field is empty!");
                    if (mapVariantGroup.getSelectedToggle() == null) throw new IllegalArgumentException("Map variant was not selected!");
                    if (savingStatsGroup.getSelectedToggle() == null) throw new IllegalArgumentException("Saving Stats was not selected!");
                    if (startingPlantsInput.getText().isEmpty()) throw new IllegalArgumentException("'Starting number of plants' text field is empty!");
                    if (providedEnergyInput.getText().isEmpty()) throw new IllegalArgumentException("'Energy provided by eating' plant text field is empty!");
                    if (growingPlantsInput.getText().isEmpty()) throw new IllegalArgumentException("'Number of plants growing each day' text field is empty!");
                    if (plantsVariantGroup.getSelectedToggle() == null) throw new IllegalArgumentException("Plant growth variant was not selected!");
                    if (startingAnimalsInput.getText().isEmpty()) throw new IllegalArgumentException("'Starting number of animals' text field is empty!");
                    if (startingEnergyInput.getText().isEmpty()) throw new IllegalArgumentException("'Starting animal energy' text field is empty!");
                    if (maxEnergyInput.getText().isEmpty()) throw new IllegalArgumentException("'Maximum animal energy' text field is empty!");
                    if (breedReadyInput.getText().isEmpty()) throw new IllegalArgumentException("'Energy needed to consider animal ready to breed' text field is empty!");
                    if (breedEnergyInput.getText().isEmpty()) throw new IllegalArgumentException("'Energy of the parents used during breed' text field is empty!");
                    if (mutationMinimumInput.getText().isEmpty()) throw new IllegalArgumentException("'Minimum number of mutations' text field is empty!");
                    if (mutationMaximumInput.getText().isEmpty()) throw new IllegalArgumentException("'Maximum number of mutations' text field is empty!");
                    if (mutationVariantGroup.getSelectedToggle() == null) throw new IllegalArgumentException("Mutation variant was not selected!");
                    if (genomeLengthInput.getText().isEmpty()) throw new IllegalArgumentException("'Animal genome length' text field is empty!");
                    if (behaviorVariantGroup.getSelectedToggle() == null) throw new IllegalArgumentException("Behavior variant was not selected!");
                    if (refreshTimeInput.getText().isEmpty()) throw new IllegalArgumentException("'Refresh time' text field is empty!");

                    // checking if a number is above the lower bounds
                    if (Integer.parseInt(mapHeightInput.getText()) <= 0) throw new IllegalArgumentException("Map height is less than or equal to zero");
                    if (Integer.parseInt(mapWidthInput.getText()) <= 0) throw new IllegalArgumentException("Map width is less than or equal to zero");
                    if (Integer.parseInt(startingPlantsInput.getText()) < 0) throw new IllegalArgumentException("Starting number of plants is less than zero");
                    if (Integer.parseInt(providedEnergyInput.getText()) < 0) throw new IllegalArgumentException("Energy provided by eating is less than zero");
                    if (Integer.parseInt(growingPlantsInput.getText()) < 0) throw new IllegalArgumentException("Number of plants growing each day is less than zero");
                    if (Integer.parseInt(startingAnimalsInput.getText()) < 0) throw new IllegalArgumentException("Starting number of animals is less than zero");
                    if (Integer.parseInt(startingEnergyInput.getText()) < 0) throw new IllegalArgumentException("Starting animal energy is less than zero");
                    if (Integer.parseInt(maxEnergyInput.getText()) < 0) throw new IllegalArgumentException("Maximum animal energy is less than zero");
                    if (Integer.parseInt(breedReadyInput.getText()) <= 0) throw new IllegalArgumentException("Energy needed to consider animal ready to breed is less than or equal to zero");
                    if (Integer.parseInt(breedEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Energy of the parents used during breed is less than or equal to zero");
                    if (Integer.parseInt(mutationMinimumInput.getText()) < 0) throw new IllegalArgumentException("Minimum number of mutations is less than zero");
                    if (Integer.parseInt(mutationMaximumInput.getText()) < 0) throw new IllegalArgumentException("Maximum number of mutations is less than zero");
                    if (Integer.parseInt(genomeLengthInput.getText()) <= 0) throw new IllegalArgumentException("Animal genome length is less than or equal to zero");
                    if (Integer.parseInt(refreshTimeInput.getText()) <= 0) throw new IllegalArgumentException("Refresh time is less than or equal to zero");

                    // checking relations between parameters
                    if (Integer.parseInt(startingPlantsInput.getText()) > Integer.parseInt(mapHeightInput.getText()) * Integer.parseInt(mapWidthInput.getText())) throw new IllegalArgumentException("Starting number of plants is bigger than number of all map fields");
                    if (Integer.parseInt(growingPlantsInput.getText()) > Integer.parseInt(mapHeightInput.getText()) * Integer.parseInt(mapWidthInput.getText())) throw new IllegalArgumentException("Number of plants growing each day is bigger than number of all map fields");
                    if (Integer.parseInt(startingEnergyInput.getText()) > Integer.parseInt(maxEnergyInput.getText())) throw new IllegalArgumentException("Starting animal energy is bigger than maximum animal energy");
                    if (Integer.parseInt(breedReadyInput.getText()) > Integer.parseInt(maxEnergyInput.getText())) throw new IllegalArgumentException("Energy needed to consider animal ready to breed is bigger than maximum animal energy");
                    if (Integer.parseInt(breedEnergyInput.getText()) > Integer.parseInt(breedReadyInput.getText())) throw new IllegalArgumentException("Energy of the parents used during breed is bigger than energy needed to consider animal ready to breed");
                    if (Integer.parseInt(mutationMinimumInput.getText()) > Integer.parseInt(mutationMaximumInput.getText())) throw new IllegalArgumentException("Minimum number of mutations is bigger than maximum number of mutations");
                    if (Integer.parseInt(mutationMaximumInput.getText()) > Integer.parseInt(genomeLengthInput.getText())) throw new IllegalArgumentException("Maximum number of mutations is bigger than animal genome length");

                    Simulation simulation = new Simulation();
                    simulation.init(
                            Integer.parseInt(mapHeightInput.getText()),
                            Integer.parseInt(mapWidthInput.getText()),
                            Integer.parseInt(startingPlantsInput.getText()),
                            Integer.parseInt(providedEnergyInput.getText()),
                            Integer.parseInt(growingPlantsInput.getText()),
                            Integer.parseInt(startingAnimalsInput.getText()),
                            Integer.parseInt(startingEnergyInput.getText()),
                            Integer.parseInt(maxEnergyInput.getText()),
                            Integer.parseInt(breedReadyInput.getText()),
                            Integer.parseInt(breedEnergyInput.getText()),
                            Integer.parseInt(mutationMinimumInput.getText()),
                            Integer.parseInt(mutationMaximumInput.getText()),
                            Integer.parseInt(genomeLengthInput.getText()),
                            globe.isSelected(),
                            equators.isSelected(),
                            randomness.isSelected(),
                            predestination.isSelected(),
                            saveTocsv.isSelected(),
                            Integer.parseInt(refreshTimeInput.getText())
                    );
                    simulation.start(new Stage());
                    invalidArgumentLabel.setText("");
                    invalidArgumentLabel.setTextFill(Color.BLACK);

                }  catch (NumberFormatException exception) {
                    System.out.println("Could not convert string to integer");
                    invalidArgumentLabel.setText("Invalid argument(s)! A text was given instead of a number");
                    invalidArgumentLabel.setTextFill(Color.RED);

                } catch (IllegalArgumentException exception) {
                    System.out.println(exception.getMessage());
                    invalidArgumentLabel.setText(exception.getMessage());
                    invalidArgumentLabel.setTextFill(Color.RED);
                }
            }
        });

    }

    // function that loads a predefined simulation configuration from a .json file
    private void loadConfig(String configFilename) {
        try {

            String configContent = new String(Files.readAllBytes(Paths.get(World.class.getResource(configFilename).toURI())));
            JSONObject obj = new JSONObject(configContent);

            mapHeightInput.setText(obj.getJSONObject("map").getJSONObject("dimensions").getString("height"));
            mapWidthInput.setText(obj.getJSONObject("map").getJSONObject("dimensions").getString("width"));
            if (Objects.equals(obj.getJSONObject("map").getString("variant"), "globe")) globe.setSelected(true);
            else portal.setSelected(true);
            startingPlantsInput.setText(obj.getJSONObject("plants").getString("startingNumber"));
            providedEnergyInput.setText(obj.getJSONObject("plants").getString("energyProvided"));
            growingPlantsInput.setText(obj.getJSONObject("plants").getString("growingEachDay"));
            if (Objects.equals(obj.getJSONObject("plants").getString("growthVariant"), "equators")) equators.setSelected(true);
            else corpses.setSelected(true);
            startingAnimalsInput.setText(obj.getJSONObject("animals").getString("startingNumber"));
            startingEnergyInput.setText(obj.getJSONObject("animals").getString("startingEnergy"));
            maxEnergyInput.setText(obj.getJSONObject("animals").getString("maxEnergy"));
            breedReadyInput.setText(obj.getJSONObject("animals").getJSONObject("breed").getString("energyRequire"));
            breedEnergyInput.setText(obj.getJSONObject("animals").getJSONObject("breed").getString("energyUsed"));
            mutationMinimumInput.setText(obj.getJSONObject("animals").getJSONObject("mutations").getString("minimum"));
            mutationMaximumInput.setText(obj.getJSONObject("animals").getJSONObject("mutations").getString("maximum"));
            if (Objects.equals(obj.getJSONObject("animals").getJSONObject("mutations").getString("variant"), "randomness")) randomness.setSelected(true);
            else correction.setSelected(true);
            genomeLengthInput.setText(obj.getJSONObject("animals").getString("genomeLength"));
            if (Objects.equals(obj.getJSONObject("animals").getString("behaviourVariant"), "predestination")) predestination.setSelected(true);
            else madness.setSelected(true);
            refreshTimeInput.setText(obj.getJSONObject("other").getString("refreshTime"));

        } catch (IOException | URISyntaxException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }
}
