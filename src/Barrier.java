/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flappysquare;

import java.io.Serializable;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author daniyar
 */
public class Barrier extends Pane implements Serializable {

    Rectangle rect;
    public int height;

    public Barrier(int height) {
        this.height = height;
        rect = new Rectangle(20, height, Color.GREEN);

        getChildren().add(rect);

    }
}
