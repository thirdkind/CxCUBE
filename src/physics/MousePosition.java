/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/
package physics;

import javafx.scene.input.MouseEvent;

public class MousePosition {

    private double x;
    private double y;
    
    public void save(MouseEvent e) {
        this.x = e.getSceneX();
        this.y = e.getSceneY();
    }
    
    public Difference difference(MouseEvent e) {
        double nowX = e.getSceneX();
        double nowY = e.getSceneY();
        
        double dx = this.x - nowX;
        double dy = this.y - nowY;
        
        this.x = nowX;
        this.y = nowY;
        
        return new Difference(dx, dy);
    }
    
    public static class Difference {
        public final double x;
        public final double y;
        
        private Difference(double dx, double dy) {
            this.x = dx;
            this.y = dy;
        }
    }
}
