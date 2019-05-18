/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappysquare;

//import java.awt.Toolkit;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author daniyar
 */
public class Bird extends Pane {

    public Point2D speed; //speed vector showing direction of the flight
    public Rectangle rect;
    public boolean lost = false;

    Image imgUp = new Image("http://adaniyar.xyz/wp-content/uploads/2019/05/bluebird-upflap.png");
    Image imgDown = new Image("http://adaniyar.xyz/wp-content/uploads/2019/05/bluebird-downflap.png");

    ImagePattern imageUp = new ImagePattern(imgUp);
    ImagePattern imageDown = new ImagePattern(imgDown);

    public Bird() {
        rect = new Rectangle(20, 20, Color.BLUE);

        speed = new Point2D(0, 0);
        setTranslateY(300);
        setTranslateX(100);
        getChildren().addAll(rect);
    }

    public void moveY(int y) {
        boolean moveDown;

        if (y > 0) {
            moveDown = true;
            rect.setFill(imageDown);
        } else {
            moveDown = false;
            rect.setFill(imageUp);
        }

        for (int i = 0; i < Math.abs(y); i++) {
            for (Barrier b : FlappySquare.barriers) {
                if (this.getBoundsInParent().intersects(b.getBoundsInParent())) {
                    lost = true;
                }
            }

            if (getTranslateY() < 0) {
                setTranslateY(0);
            }
            if (getTranslateY() > 580) {
                lost = true;
            }

            this.setTranslateY(getTranslateY() + (moveDown ? 1 : -1));

        }
    }

    public void moveX(int x) {
        for (int i = 0; i < x; i++) {
            setTranslateX(getTranslateX() + 1);
            for (Barrier b : FlappySquare.barriers) {
                if (getBoundsInParent().intersects(b.getBoundsInParent())) {
                    lost = true;

                }
                if (getTranslateX() + 20 == b.getTranslateX()) {
                    FlappySquare.score++;
                    java.awt.Toolkit.getDefaultToolkit().beep();

                    return;
                }

            }

        }

    }

    public void jump() {
        speed = new Point2D(3, -9);
    }

}
