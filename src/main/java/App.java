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
    TextField startingPlantsInput = new TextField(); //Sprawdzić czy mniejsza lub równa liczbie wszystkich pól na mapie!!!
    TextField providedEnergyInput = new TextField();
    TextField growingPlantsInput = new TextField(); //Sprawdzić czy mniejsza lub równa liczbie wszystkich pól na mapie!!!
    RadioButton equators = new RadioButton("Forested equators");
    RadioButton corpses = new RadioButton("Toxic corpses");
    TextField startingAnimalsInput = new TextField();
    TextField startingEnergyInput = new TextField(); //Sprawdzić czy mniejsza lub równa energii maksymalnej!!!
    TextField maxEnergyInput = new TextField();
    TextField breedReadyInput = new TextField();
    TextField breedEnergyInput = new TextField(); //Sprawdzić czy mniejsza lub równa od breedReady!!!
    TextField mutationMinimumInput = new TextField(); //Sprawdzić czy mutationMinimumInput <= mutationMaximumInput!!!
    TextField mutationMaximumInput = new TextField(); //Sprawdzić czy mniejsza od długości genu!!!
    RadioButton randomness = new RadioButton("Full randomness");
    RadioButton correction = new RadioButton("Slight correction");
    TextField genomeLengthInput = new TextField();
    RadioButton predestination = new RadioButton("Full predestination");
    RadioButton madness = new RadioButton("A bit of madness");
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

        Label configAlternativeLabel = new Label("...or set alternative configuration:");
        configAlternativeLabel.setStyle("-fx-font-size: 15pt; -fx-font-weight: bold");
        HBox configAlternative = new HBox(configAlternativeLabel);
        configAlternative.setAlignment(Pos.CENTER);

        configNormal.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                loadConfig("configNormal.json");
            }
        });

        configChaos.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                loadConfig("configChaos.json");
            }
        });

        configBigMap.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                loadConfig("configBigMap.json");
            }
        });

        //?dodać jungle height i width?
        //?dodać daily energy cost?

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

        Label providedEnergyLabel = new Label("Energy provided by eating one plant:");
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

        Label maxEnergyLabel = new Label("Max animal energy:");
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
                    if (Integer.parseInt(mapHeightInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(mapWidthInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(startingPlantsInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(providedEnergyInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(growingPlantsInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(startingAnimalsInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(startingEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(startingEnergyInput.getText()) > Integer.parseInt(maxEnergyInput.getText())) throw new IllegalArgumentException("Starting energy bigger than max energy");
                    if (Integer.parseInt(maxEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(breedReadyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(breedEnergyInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(breedEnergyInput.getText()) > Integer.parseInt(breedReadyInput.getText())) throw new IllegalArgumentException("Error: breedEnergy bigger than breedReady");
                    if (Integer.parseInt(mutationMinimumInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(mutationMaximumInput.getText()) < 0) throw new IllegalArgumentException("Integer less than zero");
                    if (Integer.parseInt(genomeLengthInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");
                    if (Integer.parseInt(refreshTimeInput.getText()) <= 0) throw new IllegalArgumentException("Integer less than or equal to zero");

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
                            Integer.parseInt(refreshTimeInput.getText())
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
