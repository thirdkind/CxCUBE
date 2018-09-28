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
import physics.Point;

public class Nodes implements Content {
    private static final double NODE_THICKNESS = 0.2;
    private static final int L_NODE=0;
 //   private static final int V_NODE=1; //
    private static final int A_NODE=2;

    private Group group = new Group();
    
    public Nodes(double length, int flag, Point point) {
    	if(flag==L_NODE) {
    		this.createLnode(length, point);
    	} else if(flag==A_NODE){
    		this.createAnode(length, point);
    	}else {
    		this.createVnode(length, point);
    	}
    }
    
    private void createLnode(double length, Point point) {
        Box x = new Box(length/2.0, NODE_THICKNESS, NODE_THICKNESS);
        x.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
        x.setTranslateX(point.x+5);
        x.setTranslateY(point.y-20);
        x.setTranslateZ(point.z);
        
        Box y = new Box(NODE_THICKNESS, length, NODE_THICKNESS);
        y.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
        y.setTranslateY(point.y-10);
        y.setTranslateX(point.x);
        y.setTranslateZ(point.z);
        
        this.group.getChildren().addAll(x, y);
    }

    
    private void createVnode(double length, Point point) {
        
        Box y = new Box(NODE_THICKNESS, length/2.0, NODE_THICKNESS);
        y.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
        y.setTranslateY(point.y-12);
        y.setTranslateX(point.x);
        y.setTranslateZ(point.z);
        
        this.group.getChildren().addAll (y);
    }

    
    private void createAnode(double length, Point point) {
        
        Box y = new Box(NODE_THICKNESS, length, NODE_THICKNESS);
        y.setMaterial(new PhongMaterial(Color.BLUEVIOLET));
        y.setTranslateY(point.y-12);
        y.setTranslateX(point.x-20);
        y.setTranslateZ(point.z);
        
        this.group.getChildren().addAll (y);
    }
    

    @Override
    public Node getNode() {
        return this.group;
    }

    @Override
    public void refresh() {
    }
}
