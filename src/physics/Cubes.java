/*
Copyright (c) 2015 opengl-8080
https://github.com/opengl-8080/

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt

changed by @thirdkind in 2018
*/

package physics;

import java.util.Objects;

import physics.Point;

public class Cubes {
	  
    private Point location;

    public Cubes(Point location) {
        Objects.requireNonNull(location);
        this.location = location;

    }

    public Point getLocation() {
        return this.location;
    }
 
    public void setLocation(Point location) {
        Objects.requireNonNull(location);
        this.location = location;
    }

}
