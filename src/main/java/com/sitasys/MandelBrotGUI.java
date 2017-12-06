package com.sitasys;

import com.sun.javafx.geom.Vec2d;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * Main GUI
 */
class MandelBrotGUI extends BorderPane{

    private static TextField REAL_MIN = new TextField();
    private static TextField REAL_MAX = new TextField();
    private static TextField IMAGINARY_MIN = new TextField();
    private static TextField IMAGINARY_MAX = new TextField();
    private TextField Iterations = new TextField();
    private TextField juliaConstant = new TextField();
    private TextField resolution = new TextField();
    private CheckBox julia = new CheckBox();
    private CheckBox r = new CheckBox();
    private CheckBox g = new CheckBox();
    private CheckBox b = new CheckBox();
    static Pane content;
    private static double rmax,rmin,imax,imin;
    private double imageWidth, imageHeight = 0;

    /**
     * Return the mainGUI as BorderPane
     */
    MandelBrotGUI(){
        BorderPane bp = new BorderPane();
        FlowPane top = new FlowPane();
        Button b = new Button("Start");
        // Action when you press start
        b.setOnAction(e ->{
            setColor();
            setRealandImaginary();
            setImageSize();
            bp.setCenter(null);
            content = new Pane();
            content.setOnMouseClicked(event -> {
                if(event.isControlDown()) {
                    double newr = ((rmax - rmin) / imageWidth * event.getSceneX()) + rmin;
                    double newi = ((imax - imin) / imageHeight * (imageHeight - (event.getSceneY() - (content.getLayoutY() + top.getHeight())))) + imin;
                    if (event.getButton() == MouseButton.SECONDARY) { //max
                        REAL_MAX.setText(newr + "");
                        IMAGINARY_MAX.setText(newi + "");
                    } else if (event.getButton() == MouseButton.PRIMARY) { //min
                        REAL_MIN.setText(newr + "");
                        IMAGINARY_MIN.setText(newi + "");
                    }
                }
            });

            bp.setCenter(content);
            draw();
        });

        Button export = new Button("Export");
        export.setOnAction(event -> exportImage());

        resolution.setText("600");
        Iterations.setText("100");
        REAL_MAX.setText("1");
        REAL_MIN.setText("-2.5");
        IMAGINARY_MAX.setText("1.2");
        IMAGINARY_MIN.setText("-1.2");
        juliaConstant.setText("0,0");
        r.setSelected(true);


        top.getChildren().addAll(
                new Label("Real Max."),REAL_MAX,
                new Label("Real Min."),REAL_MIN,
                new Label("Imaginary Max."),IMAGINARY_MAX,
                new Label("Imaginary Min."),IMAGINARY_MIN);
        bp.setTop(top);

        FlowPane fp = new FlowPane();
        fp.getChildren().addAll(b,
                new Label("Iterations:"),
                Iterations,
                new Label("Resolution:"),
                resolution,
                new Label("Julia:"),
                julia,
                new Label("Julia Constant c:"),
                juliaConstant,
                new Label("Red"),
                r,
                new Label("Green"),
                g,
                new Label("Blue"),
                this.b,
                export);

        this.setTop(fp);
        this.setCenter(bp);
    }

    /**
     * Set the Image Resolution size
     */
    private void setImageSize(){
        //resolutionmin = resolution.getText();
        //resolutionmax = resolutionmin * (max / min);

        if((imax-imin) > (rmax-rmin)){
            imageWidth = Double.parseDouble(resolution.getText());
            imageHeight = imageWidth * ((imax-imin) / (rmax-rmin));
        }else{
            imageHeight = Double.parseDouble(resolution.getText());
            imageWidth = imageHeight * ((rmax-rmin) / (imax-imin));
        }
    }
    private void setRealandImaginary(){
        rmax = Double.parseDouble(REAL_MAX.getText());
        rmin = Double.parseDouble(REAL_MIN.getText());
        imax = Double.parseDouble(IMAGINARY_MAX.getText());
        imin = Double.parseDouble(IMAGINARY_MIN.getText());
    }
    private void setColor() {
        Pixel.setRed(r.isSelected());
        Pixel.setGreen(g.isSelected());
        Pixel.setBlue(b.isSelected());
    }

    /**
     * export the pane as .png
     */
    private void exportImage(){
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));

        //Prompt user to select a file
        File file = fileChooser.showSaveDialog(null);

        if(file != null){
            try {
                //Pad the capture area
                WritableImage writableImage = content.snapshot(new SnapshotParameters(), null);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                //Write the snapshot to the chosen file
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) { ex.printStackTrace(); }
        }
    }

    /**
     * Start a Thread to draw the Mandelbrot- or Juliamenge
     */
    private void draw(){
        Thread t = new Thread(new Draw(getJuliaConstant(), Double.parseDouble(resolution.getText()),getIterations(),getJulia()));
        t.start();
    }

    static double getImaginary_min(){ return imin; }

    static double getImaginary_max(){ return imax; }

    static double getReal_min(){ return rmin; }

    static double getReal_max(){ return rmax; }

    private int getIterations(){ return Integer.parseInt(Iterations.getText());}

    private boolean getJulia(){ return julia.isSelected();}

    private Vec2d getJuliaConstant(){
        String[] s = juliaConstant.getText().split(",");
        return new Vec2d(Double.parseDouble(s[0]),Double.parseDouble(s[1]));
    }
}
