package com.kodilla;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Field extends ImageView implements Serializable {

    public Field(Image image) {
        super(image);
    }

}
