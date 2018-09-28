/*
Copyright (c) 2018 @thirdkind

Released under the MIT license
https://github.com/YukinobuKurata/YouTubeMagicBuyButton/blob/master/MIT-LICENSE.txt
*/
package processing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.IntBuffer;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ProcessingImage {

	public Image   image;
	public WritableImage   wimage;
	public String wpath;
	public String ext;
	public String sresult;
	public String simagesize;
	public ImageView original;
	public ImageView iv;
	public HBox   hbox;
	
	private final int fHUE=0;
	private final int tHUE=1;
	private final int fSATURATION=2;
	private final int tSATURATION=3;
	private final int fBRIGHTNESS=4;
	private final int tBRIGHTNESS=5;
	int []ivalues={0,360,0,100,0,100};
	int []pvalues={0,360,0,100,0,100};
	String []valnames={"HUE(From):","HUE(To):","SATURATION(From):","SATURATION(To):","BRIGHTNESS(From):","BRIGHTNESS(To):"};
	
	public ProcessingImage() {
		this.hbox=new HBox();
	}
	
	public HBox createImageArea() throws Exception {

		this.original = new ImageView();
		this.iv = new ImageView();

		
		// original image (shown left pane)
		ScrollPane imagePane1 = new ScrollPane();
		imagePane1.setVmax(400);
		imagePane1.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		imagePane1.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		imagePane1.setContent(this.original);
					
		// processed image(shown right pane)
		ScrollPane imagePane2 = new ScrollPane();
		imagePane2.setVmax(400);
		imagePane2.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		imagePane2.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		imagePane2.setContent(this.iv);
				
		imagePane1.setMinWidth(500);
		imagePane2.setMinWidth(500);
		hbox.getChildren().addAll(imagePane1,imagePane2);
		
		return hbox;
	}
	
	public String getExtension(String fullpath) {
		  int index = fullpath.lastIndexOf('.');
		  if (index!=-1)
		  {
		    return fullpath.substring(index+1, fullpath.length());
		  }
		  
		  return "";
	}
	
	public void OpenImage(Stage primaryStage) {
	
		FileChooser fc = new FileChooser();
        fc.setTitle("Open Image File");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg","*.jpeg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));
        File selectedFile = fc.showOpenDialog(primaryStage);
    
        if (selectedFile != null) {
        	this.wpath=selectedFile.getAbsolutePath().trim();
        	File file =new File(this.wpath);
        	FileInputStream fis = null;
        	int width=0;
    		int height=0;
        	try{
                fis = new FileInputStream(file);
        		
        		this.image = new Image(fis);
        		width = (int)this.image.getWidth();
        		height = (int)this.image.getHeight();
    		    this.simagesize = "ImageSize:    "+Integer.toString(width)+" x "+Integer.toString(height)+" ("+Long.toString(width*height)+")";
    	    	this.wimage = new WritableImage(width, height);
    	    	long res = this.extractImage(ivalues, 0);
    	    	this.sresult = "Result(pixel):  "+Long.toString(res);
    	    	this.original.setImage(this.image);
    	    	this.iv.setImage(this.wimage);
    			
    	    }
        	catch(Exception e){
        		String message = e.getMessage();
        		message = (message != null && message.length() > 0) ?
        			    message : this.wpath + " is not found or inaccessible.("+Integer.toString(width)+"/"+Integer.toString(height)+")";
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Error");
        		alert.getDialogPane().setHeaderText("File open error");
        		alert.getDialogPane().setContentText(message);
        		alert.showAndWait();
        	}
        } else {
        	Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Warning");
    		alert.getDialogPane().setContentText("File is not choosed");
    		alert.show();
        }
        this.original.setOnZoom(new EventHandler<ZoomEvent>() {
	        @Override public void handle(ZoomEvent event) {
	        	original.setScaleX(original.getScaleX() * event.getZoomFactor());
	        	original.setScaleY(original.getScaleY() * event.getZoomFactor());
	            event.consume();
	        }
		});
        this.iv.setOnZoom(new EventHandler<ZoomEvent>() {
	        @Override public void handle(ZoomEvent event) {
	        	iv.setScaleX(iv.getScaleX() * event.getZoomFactor());
	        	iv.setScaleY(iv.getScaleY() * event.getZoomFactor());
	            event.consume();
	        }
	    });

		
	}


	public void SaveAsImage(Stage primaryStage, boolean overwrite) {
	
		if (overwrite==true) {
			try{
    	    	saveImage(this.wimage,this.wpath, getExtension(this.wpath));
    	    }catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
			return;
		}
		
		FileChooser fc = new FileChooser();
        fc.setTitle("Save Image File");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg","*.jpeg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));
        
        File savefile = fc.showSaveDialog(primaryStage);
        if (savefile != null) {
        	String path=savefile.getAbsolutePath().trim();
    		try{
    	    	saveImage(this.wimage,path, getExtension(path));
    	    }catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } 
		
	}
	
	private boolean saveImage(WritableImage img, String base, String fmt) throws IOException{
	    File f = new File(base);
	    return ImageIO.write(SwingFXUtils.fromFXImage(img, null), fmt, f);
	}

	/*this func() is  for debug*/
	public static String toBinary( int number ) {
	    StringBuilder b = new StringBuilder(32);
	    for ( int i = 31; i >=0; i-- ) {
	        b.append( (number & 1L << i ) >> i );
	    }
	    return b.toString();
	}
	
	//image processing in pixel
	public long extractImage(int []values, int mode)
    {
        // get image size
        int     width   = (int) this.image.getWidth();
        int     height  = (int) this.image.getHeight();
        pvalues=values;
         
        // copy image
        PixelWriter     writer      = this.wimage.getPixelWriter();
         
        // get pixels value
        WritablePixelFormat<IntBuffer>  format  = WritablePixelFormat.getIntArgbInstance();
        int[]  pixels  = new int[ width * height ]; 
        this.image.getPixelReader().getPixels( 0 , 0 , width , height ,
                                        format, pixels, 0 , width );
         
        long counter=0;
        for( int y = 0 ; y < height ; y++ ){
            for( int x = 0 ; x < width ; x++ )
            {
                // get pixel value
                int index   = ( y * width ) + x;
                int pixel   = pixels[ index ];
                 
                int a       = ( pixel >> 24 ) & 0xFF;  //α
                int r       = ( pixel >> 16 ) & 0xFF;  //red
                int g       = ( pixel >> 8  ) & 0xFF;  //green
                int b       = pixel & 0xFF;            //blue
                
                Color p_col = Color.rgb(r, g, b);
                
				int hue=(int)p_col.getHue();
				int sat=(int)(p_col.getSaturation()*100);
				int bright=(int)(p_col.getBrightness()*100);
			
				if(
				   ((values[fHUE]<=values[tHUE] && (hue<values[fHUE] || hue>values[tHUE]))||(values[fHUE]>=values[tHUE] && hue>values[tHUE] && hue<values[fHUE])) 
					||
					((values[fSATURATION]<=values[tSATURATION] && (sat<values[fSATURATION] || sat>values[tSATURATION]))||(values[fSATURATION]>=values[tSATURATION] && sat>values[tSATURATION] && sat<values[fSATURATION]) )
					||
					((values[fBRIGHTNESS]<=values[tBRIGHTNESS] && (bright<values[fBRIGHTNESS] || bright>values[tBRIGHTNESS]))||(values[fBRIGHTNESS]>=values[tBRIGHTNESS] && bright>values[tBRIGHTNESS] && bright<values[fBRIGHTNESS]))
					  ){
					  r=g=b=0xff;     //set white
					  if(mode == 2) { //invert
						  r=g=b=0x00; //set black
					  }
				  }else {
					  if(mode == 1) {          //make black and white image
						  r=g=b=0x00; //set black
					  } else if (mode ==2) {   //intvert
						  r=g=b=0xFF; //set black
					  }
					  counter++;
				  }
                 
                pixel           = ( a << 24 ) | ( r << 16 ) | ( g << 8 ) | b;
                pixels[ index ] = pixel;
            }
        }
        writer.setPixels(0 , 0 , width , height , format, pixels, 0 , width);
        

    	this.sresult = "Result(pixel): "+Long.toString(counter);
        return counter;
    }
	


	public void SaveSettings(Stage primaryStage) {
		
		FileChooser fc = new FileChooser();
        fc.setTitle("Save Settings as Text File");
        fc.getExtensionFilters().addAll(
                new ExtensionFilter("Text Files", "*.txt"));
        
        File savefile = fc.showSaveDialog(primaryStage);
        if (savefile != null) {
        	String path=savefile.getAbsolutePath().trim();
        	try{
        		File file = new File(path);
        		FileWriter filewriter = new FileWriter(file);
        		filewriter.write(LocalDateTime.now()+"\r\n");
        		filewriter.write("Image name:"+this.wpath+"\r\n");
        		filewriter.write(this.simagesize+"\r\n");
        		filewriter.write(this.sresult+"\r\n");
        		for (int i =0; i<6; i++) {
        		    filewriter.write(valnames[i]+Integer.toString(pvalues[i])+"\r\n");
        		}

        		filewriter.close();
        	}catch(IOException e){
        		  System.out.println(e);
        	}
        } 
	}
	
}
