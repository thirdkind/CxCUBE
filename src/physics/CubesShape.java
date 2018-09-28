/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/
package physics;


import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Translate;
import physics.Content;
import physics.Point;
import physics.ViewPoint;

public class CubesShape implements Content {
	

    private Box box;
    private Translate translate = new Translate();
    private Cubes cubes;

    
    public CubesShape(Cubes cubes, double width, double height,  double depth,Color color) {
        this.box = new Box(width, height,depth);
        this.box.setMaterial(new PhongMaterial(color));
        this.box.getTransforms().add(this.translate);
        this.translate(cubes.getLocation());
        this.cubes = cubes;
    }
    
    public CubesShape(Cubes cubes, double width, double height,  double depth, PhongMaterial phong) {
        this.box = new Box(width, height, depth);
        this.box.setMaterial(phong);
        this.box.getTransforms().add(this.translate);
        this.translate(cubes.getLocation());
        this.cubes = cubes;
    }
    
    public CubesShape(Cubes cubes, double width, double height, double depth) {
        this(cubes, width, height,depth, Color.WHITE);
    }
    
    public void translate(Point point) {
        ViewPoint viewPoint = ViewPoint.of(point);
        this.translate.setX(viewPoint.x);
        this.translate.setY(viewPoint.y);
        this.translate.setZ(viewPoint.z);
    }

    public void setColor(Color color) {
        this.box.setMaterial(new PhongMaterial(color));
    }
    
    @Override
    public Node getNode() {
        return this.box;
    }

    @Override
    public void refresh() {
        Point location = this.cubes.getLocation();
        this.translate(location);
    }

}

