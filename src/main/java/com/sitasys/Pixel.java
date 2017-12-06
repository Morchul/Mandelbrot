package com.sitasys;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * One Pixel
 */
class Pixel extends Rectangle {
    private static boolean red,green,blue;

    /**
     * Return a Pixel
     * @param x pos x
     * @param y pos y
     * @param color color = anz iterations
     * @param iterations max iterations
     */
    Pixel(double x,double y,int color,int iterations){
        setHeight(1);
        setWidth(1);
        setLayoutX(x);
        setLayoutY(y);
        setFill(getColor(color,iterations));
    }

    /**
     * Get the color
     * @param convergenceValue anz Iterations
     * @param iterations max Iterations
     * @return Color for the count of Iterations
     */
    private Color getColor(int convergenceValue,int iterations){
        double t1 = (double) convergenceValue / iterations;
        double c1 = Math.min(255 * 2 * t1, 255);
        double c2 = Math.max(255 * (2 * t1 - 1), 0);

        if (convergenceValue != iterations) {
            double r,g,b;
            if(red){r = c1 / 255.0;}
            else{r = c2 / 255.0;}
            if(green){g = c1 / 255.0;}
            else{g = c2 / 255.0;}
            if(blue){b = c1 / 255.0;}
            else{b = c2 / 255.0;}
            return (Color.color(r,g,b));
        } else {
            return(Color.BLACK); // Convergence Color
        }
    }
    static void setRed(boolean r){red = r;}
    static void setGreen(boolean g){green = g;}
    static void setBlue(boolean b){blue = b;}
}
