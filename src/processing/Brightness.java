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
import physics.Content;

//Brightness setting bar
public class Brightness  implements Content{

	private Group group = new Group();
    
	public Rectangle rect = new Rectangle();
	public Rectangle rect_bk = new Rectangle();
	public Rectangle rectSlider = new Rectangle();
	public Rectangle indicator = new Rectangle();
	public Rectangle indicator2 = new Rectangle();

	public Label fNum= new Label("0");
	public Label tNum= new Label("100");

	private final int HEIGHT=280;

	public double startY;
	public double endY;
	Color barColor=Color.WHITE;
	Color textColor=Color.WHITE;
	
	
	public Brightness(double posx, double posy, int hue) {
	
        //Image image = new Image(getClass().getResourceAsStream("arrow.gif"));
        
        //ラベル from
        //this.fNum= new Label("0",new ImageView(image));
        this.fNum= new Label("0-",null);
        this.fNum.setLayoutX(posx);
        this.fNum.setLayoutY(posy+HEIGHT-8);
        this.fNum.setAlignment(Pos.TOP_RIGHT);
        this.fNum.setContentDisplay(ContentDisplay.RIGHT);
        this.fNum.setMinWidth(40);
        this.fNum.setTextFill(textColor);

        //ラベルto
        this.tNum= new Label("100-",null);
        this.tNum.setLayoutX(posx);
        this.tNum.setLayoutY(posy-8);
        this.tNum.setAlignment(Pos.TOP_RIGHT);
        this.tNum.setContentDisplay(ContentDisplay.RIGHT);
        this.tNum.setMinWidth(40);
        this.tNum.setTextFill(textColor);
        

    	Label mid= new Label("50-");
        mid.setLayoutX(posx+20);
        mid.setLayoutY(posy+HEIGHT/2-8);
        mid.setTextFill(textColor);
    	
        //スライダー
		this.startY=0;
		this.endY=HEIGHT;
		this.rect.setLayoutX(posx+45);
		this.rect.setLayoutY(posy);
		this.rect.setWidth(24);
		this.rect.setHeight(HEIGHT);
		Stop[] stops = new Stop[] { new Stop(0, Color.hsb(hue, 0.0, 0.0)), new Stop(1, Color.hsb(hue, 0.0, 1.0))};
		LinearGradient lg1 = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
		this.rect.setFill(lg1);

		this.rect_bk.setLayoutX(posx+40);
		this.rect_bk.setLayoutY(posy);
		this.rect_bk.setWidth(28);
		this.rect_bk.setHeight(HEIGHT);
		this.rect_bk.setStroke(textColor);
		this.rect_bk.setFill(Color.TRANSPARENT);
		
		this.rectSlider.setLayoutX(posx+30);
		this.rectSlider.setLayoutY(posy);
		this.rectSlider.setWidth(40);
		this.rectSlider.setHeight(HEIGHT);
		this.rectSlider.setFill(Color.TRANSPARENT);
		
		
		Rectangle spacer = new Rectangle();
		spacer.setLayoutX(posx);
		spacer.setLayoutY(0);
		spacer.setWidth(70);
		spacer.setFill(Color.TRANSPARENT);

		//選択値
		this.indicator.setLayoutX(posx+40);
		this.indicator.setLayoutY(posy);
		this.indicator.setWidth(28);
		this.indicator.setHeight(HEIGHT);
		this.indicator.setFill(barColor);

		this.indicator2.setLayoutX(posx+40);
		this.indicator2.setLayoutY(posy);
		this.indicator2.setWidth(28);
		this.indicator2.setHeight(HEIGHT);
		this.indicator2.setFill(barColor);
		
		this.group.getChildren().addAll(mid, this.rect_bk, this.indicator2, this.indicator, this.rect, this.fNum, this.tNum, this.rectSlider);

        //this.rect.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClicked);
	}
	
	public void ChangeScale(int hue, double fsat, double tsat) {
		Stop[] stops = new Stop[] { new Stop(0, Color.hsb(hue, 0.01*fsat, 0.0)), new Stop(1, Color.hsb(hue, 0.01*tsat, 1.0))};
		LinearGradient lg1 = new LinearGradient(0, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops);
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
