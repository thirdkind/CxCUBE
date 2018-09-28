/*
Copyright (c) 2018 @thirdkind

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt
*/

package processing;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import processing.ProcessingImage;
import javafx.application.Application;

//FXの場合は、Applicationを継承する
public class Main extends Application {
	private ProcessingImage imgProc = new ProcessingImage();
	
    public static void main(String[] args) {
        launch(args);
    }
   
    @Override
    public void start(Stage primaryStage) throws Exception {
        createWorld(primaryStage);

    }
    
    public void createWorld(Stage primaryStage) throws Exception {
    	
    	 primaryStage.setResizable(false);
    	 
    	 //メニュー作成
    	MenuBar menu = createMenu(primaryStage);
    	
    	////画像表示ペイン
       	HBox iv_tile =imgProc.createImageArea();
       	
    	//色相設定エリア
    	ColorSettings colorRanger= new ColorSettings();
        Group root=new Group(colorRanger.hbox); 
        root.getChildren().addAll(new AmbientLight(Color.rgb(255,255,255, 1.0)));
        
        
        colorRanger.extractCol.setOnAction(( ActionEvent ) ->{
        	try{
        		
        		int i=0;
        		//int count=0;
        	
	         	int val=Integer.parseInt(colorRanger.fromHueSpinner.getEditor().getText());
	         	if(colorRanger.values[i] != val) {colorRanger.values[i] = val; }
	         	i++;
	         	val=Integer.parseInt(colorRanger.toHueSpinner.getEditor().getText());
	         	if(colorRanger.values[i] != val) {colorRanger.values[i] = val;}
	         	i++;
	         	val=Integer.parseInt(colorRanger.fromSatSpinner.getEditor().getText());
	         	if(colorRanger.values[i] != val) {colorRanger.values[i] = val;}
	         	i++;
	         	val=Integer.parseInt(colorRanger.toSatSpinner.getEditor().getText());
	         	if(colorRanger.values[i] != val) {colorRanger.values[i] = val;}
	         	i++;
	         	val=Integer.parseInt(colorRanger.fromBriSpinner.getEditor().getText());
	         	if(colorRanger.values[i] != val) {colorRanger.values[i] = val;}
	         	i++;
	         	val=Integer.parseInt(colorRanger.toBriSpinner.getEditor().getText());
	         	if(colorRanger.values[i] != val) {colorRanger.values[i] = val; }
	         	i++;
       			colorRanger.setScale(0);
         		colorRanger.ShowCubes(); 

           		imgProc.extractImage(colorRanger.values, 0);
            	colorRanger.setResultString(imgProc.sresult, imgProc.simagesize);

         		}
            	catch(Exception e) {
            		//エラーダイアログ埋める
            		System.out.println(e.getMessage());
            		System.exit(-1);
            	}
            	
         });

        colorRanger.createBin.setOnAction(( ActionEvent ) ->{
       		imgProc.extractImage(colorRanger.values, 1);
        	colorRanger.setResultString(imgProc.sresult, imgProc.simagesize);
         });
        
        colorRanger.invertCol.setOnAction(( ActionEvent) ->{
        	imgProc.extractImage(colorRanger.values,2);
        	colorRanger.setResultString(imgProc.sresult, imgProc.simagesize);
        });
      
        
        BorderPane pane= new BorderPane();
        pane.setTop(menu);
        pane.setCenter(iv_tile);
        pane.setBottom(root);
        
        Scene scene = new Scene(pane, 1200, 800);
        
        primaryStage.setScene(scene);	//SceneをStateに追加する。
        primaryStage.show();  			//stageが表示される。
    }

    /* メニューバー作成 */
    private MenuBar createMenu(Stage primaryStage) {
    	//メニュー
    	MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Menu");

        //イメージファイルを開く
        MenuItem openFile = new MenuItem("Open Image File");
        menuFile.getItems().addAll(openFile);
        openFile.setOnAction((ActionEvent t) -> {
        	this.imgProc.OpenImage(primaryStage);
        });
        
        //イメージファイルを保存
        MenuItem saveFile = new MenuItem("Save Image");
        menuFile.getItems().addAll(saveFile);
        saveFile.setOnAction((ActionEvent t) -> {
        	this.imgProc.SaveAsImage(primaryStage, true);
        });
        
        //イメージファイルを名前をつけて保存
        MenuItem saveAsFile = new MenuItem("Save Image As ...");
        menuFile.getItems().addAll(saveAsFile);
        saveAsFile.setOnAction((ActionEvent t) -> {
        	this.imgProc.SaveAsImage(primaryStage, false);
        });

        //設定ファイルと計測値を保存
        MenuItem saveSettings = new MenuItem("Save Settings");
        menuFile.getItems().addAll(saveSettings);
        saveSettings.setOnAction((ActionEvent t) -> {
        	this.imgProc.SaveSettings(primaryStage);
        });
        
        //終了処理
        MenuItem exit = new MenuItem("Exit");
        menuFile.getItems().addAll(exit);
        exit.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        
        //ヘルプ
        //Menu menuHelp = new Menu("Help");
        menuBar.getMenus().addAll(menuFile);
        
        return menuBar;
    	
    }

}
