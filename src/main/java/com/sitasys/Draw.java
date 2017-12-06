package com.sitasys;

import com.sun.javafx.geom.Vec2d;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class Draw implements Runnable {

    private double steps = 500;
    private Vec2d juliaConstant;
    private int iterations;
    private boolean julia;

    Draw(Vec2d juliaConstant,double step,int iterations, boolean julia){
        this.juliaConstant = juliaConstant;
        steps = step;
        this.iterations = iterations;
        this.julia = julia;
    }
    @Override
    public void run() {
        double ymax = MandelBrotGUI.getImaginary_max();
        double ymin = MandelBrotGUI.getImaginary_min();
        double xmin = MandelBrotGUI.getReal_min();
        double xmax = MandelBrotGUI.getReal_max();
        double min = Math.min((ymax-ymin),(xmax-xmin));
        double max = Math.max((ymax-ymin),(xmax-xmin));
        if(max / 2.5 > min || (min <= 0 || max <= 0)){
            System.out.println(max +"\n" +
                    min + "\n" +
                    max / 2.5 +"\n");
            Platform.runLater(() ->{
                MandelBrotGUI.content.getChildren().add(
                        new Label("Error the Different between ΔReal and ΔImaginary is to big!\n" +
                                "Or the max are smaller than the min"));
            });
            return;
        }
        //Draw the Mandelbrot- or Juliamenge
        for(double i = ymax,y = 0;i > ymin; i -= (min / steps),y++){
            for(double r = xmin,x = 0;r < xmax; r += (min / steps),x++){
                int color = calculate(i,r, iterations,julia);
                double xx = x,yy =y;

                //long start = System.nanoTime();
                //long delay = 70000; //TODO Future Task
                //while(start + delay >= System.nanoTime());

                Platform.runLater(() -> {
                    MandelBrotGUI.content.getChildren().add(new Pixel(xx,yy,color,iterations));
                });
            }
        }
    }

    /**
     * Calculate and return the count of Iterations
     * @param i imaginary number of the Constant
     * @param r real number of the Constant
     * @param convergenceSteps max Iterations
     * @param julia is Juliamenge?
     * @return count of Iterations
     */
    private int calculate(double i, double r,int convergenceSteps,boolean julia){
        Vec2d c;
        Vec2d xy;
        if(julia){
            c = juliaConstant;
            xy = new Vec2d(r,i);
        }else {
            c = new Vec2d(r, i);
            xy = new Vec2d(0,0);
        }
        for(int j = 1; j<convergenceSteps;j++){

            double ziT = 2 * xy.x * xy.y;
            double zT = xy.x * xy.x - (xy.y * xy.y);
            xy.x = zT + c.x;
            xy.y = ziT + c.y;

            if(Math.pow(xy.x,2) + Math.pow(xy.y,2) >= 4.0){
                return j;
            }
        }
        return convergenceSteps;
    }
}
