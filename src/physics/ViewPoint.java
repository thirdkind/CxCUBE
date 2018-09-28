/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/
package physics;


import physics.Point;

public class ViewPoint {
    public static double RATE = 1.0;
    
    public static void setRate(double size, double realKm) {
        RATE = size / (realKm * 100.0);
    }
    
    public final double x;
    public final double y;
    public final double z;
    
    public static ViewPoint of(Point p) {
        return new ViewPoint(p.x * RATE, p.y * RATE, p.z * RATE);
    }

    public ViewPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point toPoint() {
        return new Point(this.x / RATE, this.y / RATE, this.z / RATE);
    }
    
    @Override
    public String toString() {
        return "ViewPoint {" + this.x + ", " + this.y + ", " + this.z + "}";
    }
}
