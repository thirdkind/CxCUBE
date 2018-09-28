/*
Copyright (c) 2018 @thirdkind

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt
*/
package processing;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import physics.Axis;
import physics.Camera;
import physics.Cubes;
import physics.CubesShape;
import physics.Point;
import physics.Space;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

public class ColorSettings {
	
	int depth=16;
	private DataMap datamap;
	
	private Cubes[] cubes = new Cubes[depth*depth*depth];
	private CubesShape[] cubesShape = new CubesShape[depth*depth*depth];
	    
	public HBox hbox;
	public Group piechart;
	public GridPane settings;
    public Button extractCol;
    public Button createBin;
    public Button invertCol;
    
    private Arc rangeArc;
    private Arc rangeArc_in;
    
    private Label fromRGB = new Label("#000000");
    private Label toRGB = new Label("#FFFFFF");

    private Label label_imagesize = new Label("Image Size(pixel)");
    private Label label_result = new Label("Result(pixel)");
    
    private final double SLIDER_WIDTH=70;
	private final double centerX=150+SLIDER_WIDTH;
    private final double centerY=150;
    private final double CIRCLE_SIZE=115;
    private final double ARC_CIRCLE_SIZE=CIRCLE_SIZE+12;//外側レンジ円
    private final double ARCIN_CIRCLE_SIZE=CIRCLE_SIZE-12; //内側レンジ円
    private final double AREA_SIZE=280;
    private double fixer=AREA_SIZE/100;
    
	private final int fHUE=0;
	private final int tHUE=1;
	private final int fSATURATION=2;
	private final int tSATURATION=3;
	private final int fBRIGHTNESS=4;
	private final int tBRIGHTNESS=5;
	int []values={0,360,0,100,0,100}; //色相・明度・彩度初期設定値
	
	private final int DETAIL_COUNT=7;
	private final int FIX_ANGLE=30;

    //色相数値設定
    Spinner<Integer> fromHueSpinner= new Spinner<Integer>(0,360,0,1);
    Spinner<Integer> toHueSpinner = new Spinner<Integer>(0,360,360,1);
    Spinner<Integer> fromSatSpinner = new Spinner<Integer>(0,100,0,1);
    Spinner<Integer> toSatSpinner = new Spinner<Integer>(0,100,100,1);
    Spinner<Integer> fromBriSpinner = new Spinner<Integer>(0,100,0,1);
    Spinner<Integer> toBriSpinner = new Spinner<Integer>(0,100,100,1);

    Saturation slSaturation;
    Brightness slBrightness;
	

	Color indexColor=Color.WHITE;
	Color barColor=Color.WHITE;
	int tmp_angle[];
	
	
	public ColorSettings() throws Exception {

    	this.datamap= new DataMap(depth);
		Space space3D=this.createContent();
		
        this.tmp_angle= new int[DETAIL_COUNT];
        
    	this.hbox= new HBox();
        this.settings = new GridPane(); //設定や表示
    	this.piechart = new Group(); //色相設定
 		
 	    this.settings.setPadding(new Insets(30, 30, 30, 50));
 	    this.settings.setVgap(10);
 	    this.settings.setHgap(10);
 	     	

        //Settings 
        Label from = new Label("From");
        Label to   = new Label("To");
        Label labelH = new Label("Hue:");
        Label labelS = new Label("Saturation:");
        Label labelB = new Label("Brightness:");

        extractCol = new Button("Extract");
        createBin = new Button("Make binary");
        invertCol = new Button("Invert");
 	   
        //piechart group
        Circle circle_bk= new Circle(centerX,centerY,ARCIN_CIRCLE_SIZE);
        circle_bk.setStroke(Color.BLACK);
        circle_bk.setStrokeWidth(45);
        circle_bk.setStrokeType(StrokeType.OUTSIDE);
        circle_bk.setFill(Color.TRANSPARENT);
        
        //Color Circle
        Arc[] arcs= new Arc[361];
        Arc[] uarcs= new Arc[10];
        this.rangeArc = new Arc(centerX,centerY,ARC_CIRCLE_SIZE,ARC_CIRCLE_SIZE,0,360);
        this.rangeArc_in = new Arc(centerX,centerY,ARCIN_CIRCLE_SIZE,ARCIN_CIRCLE_SIZE,0,360);
        Rectangle rect = new Rectangle(0,0,AREA_SIZE+SLIDER_WIDTH+40,AREA_SIZE+SLIDER_WIDTH+20);
        Rectangle rect2 = new Rectangle(SLIDER_WIDTH+AREA_SIZE+50,AREA_SIZE+25);//透明枠
        rect2.setFill(Color.TRANSPARENT);
        
        /* Horiznal Slider for setting Saturation */
        this.slSaturation = new Saturation(SLIDER_WIDTH, AREA_SIZE+8, 0);
        this.slSaturation.rectSlider.setOnMouseClicked((MouseEvent me)->{
        	int code=0;
       	    if(me.getButton()==MouseButton.PRIMARY) { //★★
        		code=fSATURATION;
        		this.slSaturation.startX = me.getX();
        		this.values[fSATURATION]=(int)(this.slSaturation.startX/fixer);
        		this.fromSatSpinner.getEditor().setText(Integer.toString(this.values[fSATURATION]));
        	} else{
        		code=tSATURATION;
        		this.slSaturation.endX = me.getX();
        		this.values[tSATURATION]=(int)(this.slSaturation.endX /fixer);
        		this.toSatSpinner.getEditor().setText(Integer.toString(this.values[tSATURATION]));
        	}
        	this.setScale(code);
            this.ShowCubes(); 
        });
        
        slSaturation.rectSlider.setOnMouseMoved((MouseEvent me)->{
       	   if(me.getButton()==MouseButton.PRIMARY) { 
        		this.slSaturation.fNum.setText(Integer.toString((int)(me.getX()/fixer)));
				this.slSaturation.fNum.setLayoutX(me.getX()+SLIDER_WIDTH-6);
				this.slSaturation.fLine.setLayoutX(me.getX()+SLIDER_WIDTH-2);
        	} else{
        		this.slSaturation.tNum.setText(Integer.toString((int)(me.getX()/fixer)));
        		this.slSaturation.tNum.setLayoutX(me.getX()+SLIDER_WIDTH-6);
        		this.slSaturation.tLine.setLayoutX(me.getX()+SLIDER_WIDTH-2);
        	}
        });
        
        slSaturation.rectSlider.setOnMouseExited((MouseEvent me)->{
            if(me.getButton()==MouseButton.PRIMARY) { 
        		this.slSaturation.fNum.setText(Integer.toString(this.values[fSATURATION]));
        		this.slSaturation.fNum.setLayoutX(this.values[fSATURATION]*fixer+SLIDER_WIDTH-6);
           		this.slSaturation.fLine.setLayoutX(this.values[fSATURATION]*fixer+SLIDER_WIDTH-2);
            } else{
        		this.slSaturation.tNum.setText(Integer.toString(this.values[tSATURATION]));
        		this.slSaturation.tNum.setLayoutX(this.values[tSATURATION]*fixer+SLIDER_WIDTH-6);
        		this.slSaturation.tLine.setLayoutX(this.values[tSATURATION]*fixer+SLIDER_WIDTH-2);
        	}
        });
        
        /* Vertical Slider for setting Brightness */
        slBrightness = new Brightness(0, 8, 0 );
        slBrightness.rectSlider.setOnMouseClicked((MouseEvent me)->{
	        int code=0;
            if(me.getButton()==MouseButton.PRIMARY) { 
	        	code=fBRIGHTNESS;
	        	this.slBrightness.startY = me.getY();
	        	this.values[fBRIGHTNESS]=(int)(100-this.slBrightness.startY/fixer);
	        	this.fromBriSpinner.getEditor().setText(Integer.toString(this.values[fBRIGHTNESS]));
	        } else{
	        	code=tBRIGHTNESS;
	        	this.slBrightness.endY = me.getY();
	        	this.values[tBRIGHTNESS]=(int)(100-this.slBrightness.endY/fixer);
	        	this.toBriSpinner.getEditor().setText(Integer.toString(this.values[tBRIGHTNESS]));
	        }
        	this.setScale(code);
            this.ShowCubes(); 
        });
        
        slBrightness.rectSlider.setOnMouseMoved((MouseEvent me)->{
            if(me.getButton()==MouseButton.PRIMARY) { 
        		this.slBrightness.fNum.setText(Integer.toString((int)(100-me.getY()/fixer))+"-");
        		this.slBrightness.fNum.setLayoutY(me.getY());
        	} else{
        		this.slBrightness.tNum.setText(Integer.toString((int)(100-me.getY()/fixer))+"-");
        		this.slBrightness.tNum.setLayoutY(me.getY());
        	}
        }); 
        
        slBrightness.rectSlider.setOnMouseExited((MouseEvent me)->{
            if(me.getButton()==MouseButton.PRIMARY) { 
        		this.slBrightness.fNum.setText(Integer.toString(this.values[fBRIGHTNESS])+"-");
        		this.slBrightness.fNum.setLayoutY((100-this.values[fBRIGHTNESS])*fixer);
        	} else{
        		this.slBrightness.tNum.setText(Integer.toString(this.values[tBRIGHTNESS])+"-");
        		this.slBrightness.tNum.setLayoutY((100-this.values[tBRIGHTNESS])*fixer);
        	}
        });
         
        this.piechart.getChildren().addAll(rect2, rect, space3D.getSubScene(), circle_bk, slSaturation.getNode(), slBrightness.getNode());
        
        /* degree index */
        Arc[] index= new Arc[4];
        for(int i = 0; i< 4; i++) {
            index[i] = new Arc(centerX, centerY, ARC_CIRCLE_SIZE+5, ARC_CIRCLE_SIZE+5,-30+i*90, 0.5); 
        	index[i].setStroke(indexColor);
        	index[i].setStrokeLineCap(StrokeLineCap.BUTT);
        	index[i].setStrokeWidth(10);
        	this.piechart.getChildren().add(index[i]);
        }
        
        // detail color selector
        int startAngle=360-FIX_ANGLE-(4*4+2);
        for(int i=0; i<DETAIL_COUNT; i++){
  		   if(startAngle+i*4>360){
  			 startAngle-=360;
  		   }
           uarcs[i]= new Arc(centerX,centerY,CIRCLE_SIZE+18,CIRCLE_SIZE+18, startAngle+i*4, 3.0);
           uarcs[i].setType(ArcType.OPEN);
           uarcs[i].setStrokeWidth(8);
           uarcs[i].setStrokeLineCap(StrokeLineCap.BUTT);
           String i_string = Integer.toString(i);
           uarcs[i].setOnMouseClicked(( MouseEvent me)->{
        	   
        	   int ibase = tmp_angle[Integer.parseInt(i_string)];
        	   int code = 0;
        	   
        	   if(me.getButton()==MouseButton.PRIMARY) { 
        		   fromHueSpinner.getEditor().setText(Integer.toString(ibase));
        		   this.values[fHUE] = ibase;
        		   code=fHUE;
        	   }else{
        		   toHueSpinner.getEditor().setText(Integer.toString(ibase));
        		   this.values[tHUE]  = ibase;
        		   code=tHUE;
        	   }
        	   this.setScale(code);
               ShowCubes();
           }); 
	       this.piechart.getChildren().add(uarcs[i]);
        }
        

        // Spinner
        spinnerSettings(fromHueSpinner, 0, fHUE);
        spinnerSettings(toHueSpinner, 0, tHUE);
        spinnerSettings(fromSatSpinner, 1, fSATURATION);
        spinnerSettings(toSatSpinner, 1, tSATURATION);
        spinnerSettings(fromBriSpinner, 1, fBRIGHTNESS);
        spinnerSettings(toBriSpinner, 1, tBRIGHTNESS);
        
        // put settings object on GridPane 
        GridPane.setConstraints(from, 1, 0);
        GridPane.setConstraints(to, 2, 0);
        GridPane.setConstraints(labelH, 0, 1);
        GridPane.setConstraints(fromHueSpinner, 1, 1);
        GridPane.setConstraints(toHueSpinner, 2, 1);
        GridPane.setConstraints(labelS, 0, 2);
        GridPane.setConstraints(fromSatSpinner, 1, 2);
        GridPane.setConstraints(toSatSpinner, 2, 2);
        GridPane.setConstraints(labelB, 0, 3);
        GridPane.setConstraints(fromBriSpinner, 1, 3);
        GridPane.setConstraints(toBriSpinner, 2, 3);
        GridPane.setConstraints(fromRGB, 1, 4);
        GridPane.setConstraints(toRGB, 2, 4);
        
        GridPane.setConstraints(extractCol, 0, 7);
        GridPane.setConstraints(createBin, 1, 7);
        GridPane.setConstraints(invertCol, 2, 7);
        GridPane.setConstraints(label_imagesize, 4, 0);
        GridPane.setConstraints(label_result, 4, 1);
        
        
        this.settings.getChildren().addAll(from, labelH, fromHueSpinner, labelB, fromBriSpinner, labelS, fromSatSpinner,fromRGB);
        this.settings.getChildren().addAll(to, toHueSpinner, toBriSpinner, toSatSpinner, toRGB);//,SBPicker.getNode());
        this.settings.getChildren().addAll(extractCol, createBin, invertCol, label_result,label_imagesize);

        
        //選択レンジ描画（外枠）
        this.rangeArc.setStroke(indexColor);
        this.rangeArc.setFill(Color.TRANSPARENT);
        this.rangeArc.setStrokeWidth(2);
        this.rangeArc.setType(ArcType.OPEN); 

        this.piechart.getChildren().addAll(this.rangeArc);
        
        
        
        // Color Selector
        for(int i=0; i<360; i++){
           arcs[i]= new Arc(centerX,centerY,CIRCLE_SIZE,CIRCLE_SIZE,i-FIX_ANGLE, 1.2);
           arcs[i].setStroke(Color.hsb(i, 1.0, 1.0));
           arcs[i].setStrokeWidth(20);
           arcs[i].setStrokeLineCap(StrokeLineCap.BUTT);
           arcs[i].setType(ArcType.OPEN);
           this.piechart.getChildren().add(arcs[i]);
           String i_string = Integer.toString(i);
           
           arcs[i].setOnMouseClicked(( MouseEvent e)->{
        	   int code;
        	   if(e.getButton()==MouseButton.PRIMARY) {
        		   fromHueSpinner.getEditor().setText(i_string);
        		   code=fHUE;
        	   } else { 
        		   toHueSpinner.getEditor().setText(i_string);
        		   code=tHUE;
        	   }	
        	   this.values[fHUE]  = Integer.parseInt(fromHueSpinner.getEditor().getText());
        	   this.values[tHUE]  = Integer.parseInt(toHueSpinner.getEditor().getText());
        	   this.setScale(code);
               ShowCubes();  		        
           });
           
           arcs[i].setOnMouseEntered(( MouseEvent )->{
        	   int angle= Integer.parseInt(i_string);
        	   DisplayExpArcs(angle,uarcs);
           });  
 
        }
        
        //選択レンジ描画（内枠）
        this.rangeArc_in.setStroke(indexColor);
        this.rangeArc_in.setFill(Color.TRANSPARENT);
        this.rangeArc_in.setStrokeWidth(2);
        this.rangeArc_in.setType(ArcType.OPEN); 
        

        Circle eventCircle= new Circle(centerX,centerY,ARCIN_CIRCLE_SIZE-2);
        eventCircle.setStroke(Color.TRANSPARENT);
        eventCircle.setFill(Color.TRANSPARENT);
        

        //Cube's events
        eventCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, space3D.mousePosition::save);
        eventCircle.addEventHandler(MouseEvent.MOUSE_DRAGGED, space3D::handleMouseDrag);
        eventCircle.addEventHandler(ScrollEvent.SCROLL, space3D::handleTouchScroll);
        eventCircle.addEventHandler(ZoomEvent.ZOOM, space3D::handleZoom);
        this.piechart.getChildren().addAll(this.rangeArc_in, eventCircle);
       
        this.hbox.getChildren().addAll(this.piechart, this.settings);
        
        return;
    }

    public void ShowCubes() {
    	
    	double len=360.0;
    	int fhue=this.values[fHUE] ;
    	int thue=this.values[tHUE] ;
    	
    	this.rangeArc.setStartAngle(fhue-FIX_ANGLE);
    	this.rangeArc_in.setStartAngle(fhue-FIX_ANGLE);
    	if(fhue< thue) {
    		len = thue-fhue;
    	}else if(fhue > thue){
        	len=(360 - fhue) + thue;
    	}else{
    		len=0;
    	}
    	
    	this.rangeArc.setLength(len);
    	this.rangeArc_in.setLength(len);
    		
    	this.datamap.setColor(this.values[fHUE], this.values[tHUE], this.values[fSATURATION], this.values[tSATURATION], this.values[fBRIGHTNESS], this.values[tBRIGHTNESS], this.cubesShape); 		 

    }
    
    private void DisplayExpArcs(int angle, Arc[] expArcs) {

   		 double startAngle;
   		 
   		startAngle=angle-FIX_ANGLE-(4*((DETAIL_COUNT-1)/2)+2); //拡大表示用アングル計算 1セクタ　幅４　中央セクタ＋DERAIL_COUNTぶん表示
   		if(startAngle<0){
   			startAngle+=360;
   		}
   		
   		angle-=4;
   		if(angle<0) {
   			angle+=360;
   		}
   		
   	   	for(int i=0; i<DETAIL_COUNT; i++){
   	   		if(startAngle+i*4>360){
   	   			startAngle-=360;
   	   		}
   	   		if(angle+i>360){
   	   			startAngle-=360;
   	   		}
   	   		double brightness=1.0;
   	   		expArcs[i].setStartAngle(startAngle+i*4.0);
   	   		expArcs[i].setLength(3.0);
   	   		expArcs[i].setStroke(Color.hsb(angle+i, 1.0, brightness)); 
   	   		this.tmp_angle[i]=angle+i;
    	 }
    }

    private void errDialog(int errCode){
    	final String []errCodeMessage= new String[10];
    	errCodeMessage[0]="Please input value from 0 to 360 integer.";
    	errCodeMessage[1]="Please input value from 0 to 100 integer.";
    	
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.getDialogPane().setHeaderText("Invalid Value");
		alert.getDialogPane().setContentText(errCodeMessage[errCode]);
		alert.show();
    }
    
    
    private void spinnerSettings(Spinner<Integer> spin, int errCode, int code){
    	spin.setMaxWidth(80);     
    	spin.setEditable(true);
    	
    	spin.setOnMouseClicked(( MouseEvent )->{
             this.values[code] = Integer.parseInt(spin.getEditor().getText());
             this.setScale(code);
             this.ShowCubes(); 
         }); 
         
    	 
         spin.getEditor().setOnMouseClicked(( MouseEvent )->{
         	int count=0, i=0;
         	try{
	         	int val=Integer.parseInt(this.fromHueSpinner.getEditor().getText());
	         	if(this.values[i] != val) {this.values[i] = val; count++;}
	         	i++;
	         	val=Integer.parseInt(this.toHueSpinner.getEditor().getText());
	         	if(this.values[i] != val) {this.values[i] = val; count++;}
	         	i++;
	         	val=Integer.parseInt(this.fromSatSpinner.getEditor().getText());
	         	if(this.values[i] != val) {this.values[i] = val; count++;}
	         	i++;
	         	val=Integer.parseInt(this.toSatSpinner.getEditor().getText());
	         	if(this.values[i] != val) {this.values[i] = val; count++;}
	         	i++;
	         	val=Integer.parseInt(this.fromBriSpinner.getEditor().getText());
	         	if(this.values[i] != val) {this.values[i] = val; count++;}
	         	i++;
	         	val=Integer.parseInt(this.toBriSpinner.getEditor().getText());
	         	if(this.values[i] != val) {this.values[i] = val; count++;}
	         	i++;

         		this.setScale(code);
	         	if(count>0) {
	         		this.ShowCubes(); 
	         	}
         	}
	        	catch(Exception e){ 
	        		errDialog(0);
	        	}
	        	
         }); 
         
         spin.getEditor().setOnKeyPressed((KeyEvent)->{
         	if (KeyEvent.getCode() == KeyCode.ENTER) {
 	        	try{
 	        		int val= Integer.parseInt(spin.getEditor().getText());
 	        		this.values[code]=val;
 	        		if(val<0 || val>360){
 	        			this.values[code]=0;
 		        		spin.getEditor().setText("0");
 	                    this.setScale(code);
 		        		errDialog(0);
 	        		}else {

 	                    this.setScale(code);
 	                    this.ShowCubes(); 
 	        		}
 	        	}
 	        	catch(Exception e){
 	        		this.values[code]=0;
 	        		spin.getEditor().setText("0");
                    this.ShowCubes(); 
 	        		errDialog(0);
 	        	}
 	        	
         	}
         });      
    }

    
    private Space createContent() throws Exception {
    
    	 // Cube 3D space
	    final double size = 256;
	    Space space = new Space(size, new Camera(), (int)(centerX/2+8), (int)(centerY/2-30), ARCIN_CIRCLE_SIZE*2); 
	    space.add(new Axis(size));
	  
	    int i=0;
	    int x,y,z,r,g,b;
	
	    try {
	        int cs=256/depth;
	        for(r=0; r < depth; r++) {
	        	for(g=0; g<depth; g++) {
	        		for(b=0; b<depth; b++) {
	        		/* for Perspective Camera */
	        			x=(int)this.datamap.locations[r][g][b].x*cs+8; 
	        			y=(int)this.datamap.locations[r][g][b].y*cs+8; 
	        			z=(int)this.datamap.locations[r][g][b].z*cs+8;
	        			cubes[i] = new Cubes(new Point(x,y,z));
		           
	        			cubesShape[i] = new CubesShape(cubes[i],  (double)cs, (double)cs, (double)cs, Color.rgb(r*cs,g*cs,b*cs));
	        				
	        			space.add(cubesShape[i]);    
	        			i++;
	        		}
	        	}
	        }
    	}
    	catch(Exception e) {
    		System.out.println(e.getMessage());
    		System.exit(-1);
    	}
    	
        return space;

    }

    public void setResultString(String sresult, String simagesize) {
        this.label_result.setText(sresult);
        this.label_imagesize.setText(simagesize);
    }

    
    public void setScale(int code) {
    	
    	
	    if(code==fHUE || code==fSATURATION || code==fBRIGHTNESS){
			this.slSaturation.ChangeScale(this.values[fHUE], this.values[fBRIGHTNESS], this.values[tBRIGHTNESS]);
			this.slBrightness.ChangeScale(this.values[fHUE], this.values[fSATURATION],  this.values[tSATURATION]);
	    } else if(code==tHUE || code==tSATURATION || code==tBRIGHTNESS) {		
	    	this.slSaturation.ChangeScale(this.values[tHUE], this.values[fBRIGHTNESS], this.values[tBRIGHTNESS]);
			this.slBrightness.ChangeScale(this.values[tHUE], this.values[fSATURATION],  this.values[tSATURATION]);
	    }
	    
	    this.fromBriSpinner.getEditor().setText(Integer.toString(this.values[fBRIGHTNESS]));
	    this.fromSatSpinner.getEditor().setText(Integer.toString(this.values[fSATURATION]));
	    this.slBrightness.startY=(100-this.values[fBRIGHTNESS])*fixer;
	    this.slSaturation.startX=this.values[fSATURATION]*fixer;
  		this.slBrightness.fNum.setText(Integer.toString(this.values[fBRIGHTNESS])+"-");
        this.slBrightness.fNum.setLayoutY((100-this.values[fBRIGHTNESS])*fixer);
        this.slSaturation.fNum.setText(Integer.toString(this.values[fSATURATION]));
      	this.slSaturation.fNum.setLayoutX(this.values[fSATURATION]*fixer+SLIDER_WIDTH-6);
      	this.slSaturation.fLine.setLayoutX(this.values[fSATURATION]*fixer+SLIDER_WIDTH-2);
      	
	    this.toBriSpinner.getEditor().setText(Integer.toString(this.values[tBRIGHTNESS]));
	  	this.toSatSpinner.getEditor().setText(Integer.toString(this.values[tSATURATION]));
	    this.slBrightness.endY=(100-this.values[tBRIGHTNESS])*fixer;
	    this.slSaturation.endX=this.values[tSATURATION]*fixer;
        this.slBrightness.tNum.setText(Integer.toString(this.values[tBRIGHTNESS])+"-");
        this.slBrightness.tNum.setLayoutY((100-this.values[tBRIGHTNESS])*fixer);
      	this.slSaturation.tNum.setText(Integer.toString(this.values[tSATURATION]));
        this.slSaturation.tNum.setLayoutX(this.values[tSATURATION]*fixer+SLIDER_WIDTH-6); 
        this.slSaturation.tLine.setLayoutX(this.values[tSATURATION]*fixer+SLIDER_WIDTH-2); 
	     
     	if(this.slBrightness.startY>=this.slBrightness.endY){
	        this.slBrightness.indicator.setY(this.slBrightness.endY);
	    	this.slBrightness.indicator.setHeight(this.slBrightness.startY-this.slBrightness.endY);
	    	this.slBrightness.indicator2.setFill(Color.TRANSPARENT);
     	}else { 
     		this.slBrightness.indicator.setY(0);
     		this.slBrightness.indicator.setHeight(this.slBrightness.startY);
			this.slBrightness.indicator2.setFill(barColor);
	    	this.slBrightness.indicator2.setY(this.slBrightness.endY);
	    	this.slBrightness.indicator2.setHeight(AREA_SIZE-this.slBrightness.endY);
     	}
	
		// Saturation
		if(this.slSaturation.startX<=this.slSaturation.endX){
			this.slSaturation.indicator.setX(this.slSaturation.startX);
			this.slSaturation.indicator.setWidth(this.slSaturation.endX-this.slSaturation.startX);
			this.slSaturation.indicator2.setFill(Color.TRANSPARENT);
		}else { //case end<start
			this.slSaturation.indicator.setX(0);
			this.slSaturation.indicator.setWidth(this.slSaturation.endX);
			this.slSaturation.indicator2.setFill(barColor);
			this.slSaturation.indicator2.setX(this.slSaturation.startX);
			this.slSaturation.indicator2.setWidth(AREA_SIZE-this.slSaturation.startX);
		     
	    }
		
		if((this.values[fHUE]==0)&&(this.values[tHUE]==360)) {
			this.fromRGB.setText("#000000");
			this.toRGB.setText("#FFFFFF");
		} else {
			Color col = Color.hsb(this.values[fHUE]*1.0, this.values[fSATURATION]/100.0, this.values[fBRIGHTNESS]/100.0);
			this.fromRGB.setText("#"+String.format("%02x", (int)(col.getRed()*255))+String.format("%02x", (int)(col.getGreen()*255))+String.format("%02x", (int)(col.getBlue()*255)));
			Color tcol= Color.hsb(this.values[tHUE]*1.0, this.values[tSATURATION]/100.0, this.values[tBRIGHTNESS]/100.0);
			this.toRGB.setText("#"+String.format("%02x", (int)(tcol.getRed()*255))+String.format("%02x", (int)(tcol.getGreen()*255))+String.format("%02x", (int)(tcol.getBlue()*255)));
		}
    }

}
