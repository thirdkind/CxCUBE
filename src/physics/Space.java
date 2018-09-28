/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/
package physics;

import java.util.HashSet;
import java.util.Set;

import javafx.scene.AmbientLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
//import javafx.scene.input.SwipeEvent;
import javafx.scene.paint.Color;
import physics.MousePosition.Difference;

public class Space {
    
    private SubScene mainView;
    private TransformGroup transform;
    private Set<Content> contents = new HashSet<>();
    private Camera camera;
    //private Parallel camera;
    public MousePosition mousePosition = new MousePosition();
    
    public Space(double size, Camera camera, int x, int y, double areaSize) {
    //public Space(double size, Parallel camera, int x, int y, double areaSize) {
    	
        this.transform = new TransformGroup(this.createCenter(size));
        this.transform.rotate(45,-45); // 初期アングルを設定

        
        this.mainView = new SubScene(this.transform.getGroup(), areaSize, areaSize, true, SceneAntialiasing.BALANCED);
        this.mainView.setFill(Color.TRANSPARENT);
        this.mainView.setLayoutX(x);
        this.mainView.setLayoutY(y);

        this.transform.add(new AmbientLight(Color.rgb(255,255,255, 1.0)));
        
        this.mainView.setCamera(camera.getCamera());
        camera.moveBackAndFront(size);
        this.camera = camera;
    }
    
    private ViewPoint createCenter(double size) {
        return new ViewPoint(-size/2.0 , -size / 2.0, -size / 2.0);
    }
    
    public void handleMouseDrag(MouseEvent e) {
        Difference diff = this.mousePosition.difference(e);
        
        if (e.isPrimaryButtonDown()) { // 左ドラッグに合わせて回転
            this.transform.rotate( -diff.y*0.5, -diff.x*0.5);
        	
        } 
        
    }

    public void handleTouchScroll(ScrollEvent se) {
            if (!se.isInertia()) {
            	//並行移動の場合の処理
                //this.camera.translate(se.getDeltaX(),se.getDeltaY(),0.0);
            	
            	//拡大縮小処理
                double scaleBase = se.isControlDown() ? 10.0 : se.isShiftDown() ? 5.0 : 1.0;
                double zoom= (se.getDeltaY() / 100) * scaleBase+1.0;
                this.camera.zoom(zoom);
            }
    }

    public void handleZoom(ZoomEvent ze) {
            if (!ze.isInertia()) {
            	double zoom=ze.getZoomFactor();
            	this.camera.zoom(zoom);
            	
            }
    }

    public void add(Content content) {
        this.transform.add(content.getNode());
        this.contents.add(content);
    }
    
    public SubScene getSubScene() {
        return this.mainView;
    }

    public void reDraw(double size) {
    	this.camera.moveBackAndFront(size);
    }
    
    public void refresh() {
        this.contents.forEach(Content::refresh);
    }
}