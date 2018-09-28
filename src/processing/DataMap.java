/*
Copyright (c) 2018 @thirdkind

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt
*/
package processing;

import javafx.scene.paint.Color;
import physics.CubesShape;
import physics.Point;

public class DataMap {

	private int cubes=0;
	public Point locations[][][]=null;
	
	public DataMap(int csize){
	  this.cubes=csize;
	  locations=new Point[cubes][cubes][cubes];
	  int r,g,b;
	  for(r=0; r<cubes; r++) {
		  for(g=0; g<cubes; g++) {
			  for(b=0; b<cubes; b++) {
				  locations[r][g][b]=new Point(r,g,b); 
			  }
		  }
	  }
	}

	public void setColor(int from, int to, int fromSat, int toSat, int fromBright, int toBright, CubesShape[] cshape){
		 int r,g,b;
		 int scale=256 /cubes;
		 int i=0;

	  for(r=0; r<cubes; r++) {
			  for(g=0; g<cubes; g++) {
				  for(b=0; b<cubes; b++) {
					 Color tmp= Color.rgb(r*scale,g*scale,b*scale);
					  int hue=(int)tmp.getHue();

					  int sat=(int)(tmp.getSaturation()*100);
					  int bright=(int)(tmp.getBrightness()*100);
					  
						
					  if(
					   ((from<=to && (hue<from || hue>to))||(from>=to && hue>to && hue<from)) 
						||
						((fromSat<=toSat && (sat<fromSat || sat>toSat))||(fromSat>=toSat && sat>toSat && sat<fromSat) )
						||
						((fromBright<=toBright && (bright<fromBright || bright>toBright))||(fromBright>=toBright && bright>toBright && bright<fromBright))
						  ){
						  cshape[i].setColor(Color.TRANSPARENT);

					  }else {
						  cshape[i].setColor(Color.rgb(r*scale,g*scale,b*scale));
					  }
					  i++;
				  }
			  }
		  }
	}



}

