/*
Copyright (c) 2018 @thirdkind

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt
*/
package processing;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import physics.Content;

public class Saturation implements Content{

    //private static final double LINE_THICKNESS = 1.0;
	private Group group = new Group();
    
	public Rectangle rect = new Rectangle();
	public Rectangle rect_bk = new Rectangle();
	public Rectangle rectSlider = new Rectangle();
	public Rectangle indicator = new Rectangle();
	public Rectangle indicator2 = new Rectangle();

	public Label fNum;
	public Label tNum;
	public Label fLine;
	public Label tLine;

	private final int WIDTH=280;
	public double startX;
	public double endX;
	
	Color barColor=Color.WHITE;
	Color textColor=Color.WHITE;
	
	public Saturation(double posx, double posy, int hue){
		
        fNum= new Label("0",null);
        tNum= new Label("100", null);
        
        fLine= new Label("|",null);
        this.fLine.setLayoutX(posx-2);
        this.fLine.setLayoutY(posy+20);
        this.fLine.setTextFill(textColor);
        
        this.fNum.setLayoutX(posx-6);
        this.fNum.setLayoutY(posy+40);
        this.fNum.setAlignment(Pos.TOP_CENTER);
        this.fNum.setContentDisplay(ContentDisplay.TOP);
        this.fNum.setTextAlignment(TextAlignment.CENTER);
        this.fNum.setTextFill(textColor);
        
        tLine= new Label("|",null);
        this.tLine.setLayoutX(posx+WIDTH-2);
        this.tLine.setLayoutY(posy+20);
        this.tLine.setTextFill(textColor);
        
        this.tNum.setLayoutX(posx+WIDTH-6);
        this.tNum.setLayoutY(posy+40);
        this.tNum.setAlignment(Pos.TOP_CENTER);
        this.tNum.setContentDisplay(ContentDisplay.TOP);
        this.tNum.setTextAlignment(TextAlignment.CENTER);
        this.tNum.setTextFill(textColor);
        
        Label mid= new Label("|");
        mid.setLayoutX(posx+WIDTH/2-2);
        mid.setLayoutY(posy+22);
        mid.setTextFill(textColor);
        
        Label mid2= new Label("50");
        mid2.setLayoutX(posx+WIDTH/2-6);
        mid2.setLayoutY(posy+40);
        mid2.setTextFill(textColor);
        
		this.startX=0;
		this.endX=WIDTH;
		this.rect.setLayoutX(posx);
		this.rect.setLayoutY(posy+2);
		this.rect.setWidth(WIDTH);
		this.rect.setHeight(24);
		
		Stop[] stops = new Stop[] { new Stop(0, Color.hsb(hue, 0.0, 0.0)), new Stop(1, Color.hsb(hue, 1.0, 1.0))};
		LinearGradient lg1 = new LinearGradient(0, 0, 1,0, true, CycleMethod.NO_CYCLE, stops);
		this.rect.setFill(lg1);
		
		//背景枠
		this.rect_bk.setLayoutX(posx);
		this.rect_bk.setLayoutY(posy+2);
		this.rect_bk.setWidth(WIDTH);
		this.rect_bk.setHeight(28);
		this.rect_bk.setStroke(textColor);
		this.rect_bk.setFill(Color.TRANSPARENT);

		this.rectSlider.setLayoutX(posx);
		this.rectSlider.setLayoutY(posy+2);
		this.rectSlider.setWidth(WIDTH+2);
		this.rectSlider.setHeight(40);
		this.rectSlider.setFill(Color.TRANSPARENT);

		//選択値
		this.indicator.setLayoutX(posx);
		this.indicator.setLayoutY(posy+2);
		this.indicator.setWidth(WIDTH);
		this.indicator.setHeight(28);
		this.indicator.setFill(barColor);
		
		this.indicator2.setLayoutX(posx);
		this.indicator2.setLayoutY(posy+2);
		this.indicator2.setWidth(WIDTH);
		this.indicator2.setHeight(28);
		this.indicator2.setFill(barColor);
		
		this.group.getChildren().addAll(mid, mid2, this.rect_bk, this.indicator2, this.indicator,this.rect, this.fNum, this.tNum, this.fLine, this.tLine,this.rectSlider);

        //this.rect.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
	}
	

	public void ChangeScale(int hue, double min, double max) {
		Stop[] stops = new Stop[] { new Stop(0, Color.hsb(hue, 0.0, 0.01*min)), new Stop(1, Color.hsb(hue,1.0, 0.01*max))};
		LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		this.rect.setFill(lg1);
	}

    @Override
    public Node getNode() {
        return this.group;
    }

    @Override
    public void refresh() {
    }
}
