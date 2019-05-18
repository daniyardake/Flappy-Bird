/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappysquare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author daniyar
 */
public class FlappySquare extends Application {

    public static Pane appRoot = new Pane(); // game
    public static Pane gameRoot = new Pane(); //barriers 
    public static ArrayList<Barrier> barriers = new ArrayList<>(); //all barriers
    Bird bird = new Bird();//creating our square-bird
    public static int score = 0;//score of the game
    public static int hs;
    public static int newHs = 0;
    public Label scoreLabel = new Label("Your Score: " + score); //score label
    public Label highestScoreLabel = new Label("Highest Score" + hs);

    public Parent createGame() throws FileNotFoundException, IOException {
        highestScore();

        gameRoot.setPrefSize(600, 600);

        for (int i = 0; i < 100; i++) {
            int space = (int) (Math.random() * 100 + 80); //width of the barrier 80-180
            int height = new Random().nextInt(600 - space); //height any random number 0 - 600
            Barrier bar = new Barrier(height); //new Barrier
            bar.setTranslateX(i * 350 + 600);//every 350 pixels there will be a new barrier
            bar.setTranslateY(0);//start at top
            barriers.add(bar);

            Barrier newBar = new Barrier(600 - space - height);
            newBar.setTranslateX(i * 350 + 600);
            newBar.setTranslateY(height + space);
            barriers.add(newBar);

            gameRoot.getChildren().addAll(bar, newBar);
        }

        gameRoot.getChildren().add(bird);//add bird to our root
        appRoot.getChildren().addAll(gameRoot, scoreLabel, highestScoreLabel); //add game root app root
        highestScoreLabel.setTranslateY(20);
        
        appRoot.setId("pane");//needed for addidng background image from css
        return appRoot;
    }

    public void update() throws FileNotFoundException, IOException {

        if (score > hs) {
            newHs = score;
        } else {
            newHs = hs;
        }

        if (bird.speed.getY() < 5) {
            bird.speed = bird.speed.add(0, 1);
        }//gravitation

        bird.moveX((int) bird.speed.getX());
        bird.moveY((int) bird.speed.getY());

        scoreLabel.setText("Your Score: " + score);

        highestScoreLabel.setText("Highest score: " + newHs);

        bird.translateXProperty().addListener((ovs, old, newValue) -> {
            int offset = newValue.intValue();
            if (offset > 200) {
                gameRoot.setLayoutX(-(offset - 200));
            }
        });

    }

    public void highestScore() throws FileNotFoundException, IOException {
        //FileWriter writer = new FileWriter("hs.txt");
        try (FileReader fr = new FileReader("hs.txt")) {
            Scanner scan = new Scanner(fr);
            String temp = null;
            temp = scan.nextLine();
            hs = Integer.parseInt(temp);
            fr.close();
        }
    }

    public void lost() throws IOException {
        if (score > hs) {
            File file = new File("hs1.txt");
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file, true);
            fw.write(Integer.toString(newHs + 1) + System.lineSeparator());
            System.out.println("new hs:" + newHs);

            
            fw.close();

            File prevFile = new File("hs.txt");
            prevFile.delete();

            file.renameTo(prevFile);
            
            showAlert(Alert.AlertType.CONFIRMATION,"New Highest Score ","Congratulations! It is new high score: "+score);
            
        } else {
            showAlert(Alert.AlertType.ERROR,"You lost ","Your score: "+score);
            
            }
        

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
            
            Scene scene = new Scene(createGame()); //creating scene
            scene.setOnMouseClicked(event -> bird.jump());
            scene.setOnKeyTyped(event -> bird.jump());
            
            
            scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());//adding background image from css file           
            
            primaryStage.setScene(scene);
            primaryStage.show();

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {                   
                    try {
                        if (bird.lost == false) {
                            update();
                        } else {

                            this.stop();
                            lost();
                            primaryStage.close();                           
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(FlappySquare.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };

            timer.start();
        

    }
    
    
    public void showAlert(Alert.AlertType alertType, String title, String message) {
        //showing alert
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
