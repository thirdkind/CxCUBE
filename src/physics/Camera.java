/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/

package physics;

import javafx.scene.PerspectiveCamera;
//import javafx.scene.ParallelCamera;
import javafx.scene.transform.Rotate;

public class Camera {
    private PerspectiveCamera camera;
	//private ParallelCamera camera;
    
    public Camera() {
        this.camera = new PerspectiveCamera(true);
    	//this.camera = new ParallelCamera();

        this.camera.setFarClip(1800.0);
        this.camera.setTranslateZ(1000);
        Rotate rotateZ = new Rotate(180, Rotate.Z_AXIS);
        Rotate rotateY = new Rotate(180, Rotate.Y_AXIS);
        this.camera.getTransforms().addAll(rotateZ, rotateY);
        
    }
    
PerspectiveCamera getCamera() {
//ParallelCamera getCamera() {
        return this.camera;
    }
    
    public void translate(double dx, double dy, double dz) {
        this.camera.setTranslateX(this.camera.getTranslateX() + dx);
        this.camera.setTranslateY(this.camera.getTranslateY() + dy);
        this.camera.setTranslateZ(this.camera.getTranslateZ() + dz);
    }

    public void zoom(double zoom) {
        this.camera.setScaleX(this.camera.getScaleX()*(1/zoom));
        this.camera.setScaleY(this.camera.getScaleY()*(1/zoom));
    }
    
    public void moveBackAndFront(double dz) {
        //this.translate(0.0, 0.0, dz);
    }
}
