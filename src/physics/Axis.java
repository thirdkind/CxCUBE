/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/
package physics;


import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

// Axis class for color cube
public class Axis implements Content {
    private static final double AXIS_THICKNESS = 1.1;

    private Group group = new Group();
    
    public Axis(double length) {
        this.createAxis(length);
    }
    
    private void createAxis(double length) {
        Box x = new Box(length, AXIS_THICKNESS, AXIS_THICKNESS);
        x.setMaterial(new PhongMaterial(Color.RED));
        x.setTranslateX(length/2.0);
        
        Box y = new Box(AXIS_THICKNESS, length, AXIS_THICKNESS);
        y.setMaterial(new PhongMaterial(Color.GREEN));
        y.setTranslateY(length/2.0);
        
        Box z = new Box(AXIS_THICKNESS, AXIS_THICKNESS, length);
        z.setMaterial(new PhongMaterial(Color.BLUE));
        z.setTranslateZ(length/2.0);  
        
        this.group.getChildren().addAll(x, y, z);
    }
    
    @Override
    public Node getNode() {
        return this.group;
    }

    @Override
    public void refresh() {
    }
}