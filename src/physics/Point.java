/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/

package physics;

public class Point {
    public static final Point ORIGIN = new Point(0, 0, 0);
    
    public double x;
    public final double y;
    public final double z;
    
    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point add(double dx, double dy, double dz) {
        return new Point(this.x + dx, this.y + dy, this.z + dz);
    }
    
    @Override
    public String toString() {
        return "Point {" + this.x + ", " + this.y + ", " + this.z + "}";
    }
}
