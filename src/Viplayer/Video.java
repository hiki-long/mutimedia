package Viplayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.stage.*;

public class Video {
	private double rate=1;
	
	boolean flag = false;
	boolean flag2 = false;
	boolean mouse = false;
	
	private ArrayList<String> list=new ArrayList<String>();
	
	 public String DurationToString(Duration duration){

	        int time = (int)duration.toSeconds();

	        int hour = time /3600;

	        int minute = (time-hour*3600)/60;

	        int second = time %60;

	        return hour + ":" + minute + ":" + second;

	    }
	
	public Media firstOpenVideo() {
		Stage stage=new Stage();
		FileChooser fc=new FileChooser();
		//fc.getExtensionFilters().add(new ExtensionFilter("视频类型",".txt"));
		File file=fc.showOpenDialog(stage);
		Media media=null;
		if(file.exists()) {
			media = new Media(file.getAbsoluteFile().toURI().toString());
			String filename=file.toString();
			int i,num=list.size();
			for(i=0;i<num;i++) {
				if(filename.equals(list.get(i)))
					break;
			}
			if(i==num)
			list.add(file.toString());
		}
		return media;
	}
	
	public MediaPlayer OpenVideo(Media media,MediaPlayer mediaPlayer) {
		Stage stage=new Stage();
		FileChooser fc=new FileChooser();
		//fc.getExtensionFilters().add(new ExtensionFilter("视频类型",".txt"));
		File file=fc.showOpenDialog(stage);
		if(mediaPlayer!=null) {
			mediaPlayer.dispose();
		}
		if(file.exists()) {
			media = new Media(file.getAbsoluteFile().toURI().toString());
			mediaPlayer=new MediaPlayer(media);
			String filename=file.toString();
			int i,num=list.size();
			for(i=0;i<num;i++) {
				if(filename.equals(list.get(i)))
					break;
			}
			if(i==num)
			list.add(file.toString());
		}
		return mediaPlayer;
	}
	
	public void play(MediaPlayer mediaPlayer) {
		mediaPlayer.play();
	}
	
	public void pause(MediaPlayer mediaPlayer) {
		mediaPlayer.pause();
	}
	
	public void setRate(MediaPlayer mediaPlayer,double s) {
		mediaPlayer.setRate(s);
	}
	
	public void progressSliderBind(MediaPlayer mediaPlayer,Slider slider) {
		slider.setOnMousePressed(e->{
			mouse = true; 
		});
		slider.setOnMouseReleased(e->{
			mouse = false;
		});
		
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue) {
				if(mouse == true)
					mediaPlayer.seek(Duration.seconds(slider.getValue() / 100 * mediaPlayer.getTotalDuration().toSeconds()));
			}
		});

	}
	
	public void setVolume(MediaPlayer mediaPlayer,double v) {
		mediaPlayer.setVolume(v);
	}
	
	public void volumeSliderBind(MediaPlayer mediaPlayer,Slider slider) {
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue) {
				mediaPlayer.setVolume(slider.getValue()/100);
			}
		});
		mediaPlayer.volumeProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue) {
				slider.setValue(mediaPlayer.getVolume()*100);
			}
		});
	}
	
	public void setOnKeyPressed(Scene scene,MediaPlayer mediaPlayer) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				if(event.getCode().getName().equals(KeyCode.W.getName())){
					double v = mediaPlayer.getVolume();
					if(v <= 0.9)
						mediaPlayer.setVolume(v + 0.1);
				}
				if(event.getCode().getName().equals(KeyCode.S.getName())){
					double v = mediaPlayer.getVolume();
					if(v >= 0.1)
						mediaPlayer.setVolume(v - 0.1);
				}
				if(event.getCode().getName().equals(KeyCode.D.getName())){
					if(mediaPlayer.currentTimeProperty().getValue().toSeconds() < mediaPlayer.getTotalDuration().toSeconds() - 5)
					mediaPlayer.seek(Duration.seconds(mediaPlayer.currentTimeProperty().getValue().toSeconds()+5));
				}
				if(event.getCode().getName().equals(KeyCode.A.getName())){
					if(mediaPlayer.currentTimeProperty().getValue().toSeconds() > 5)
						mediaPlayer.seek(Duration.seconds(mediaPlayer.currentTimeProperty().getValue().toSeconds()-5));
				}
			}
		});
	}
	
	public void close(Stage stage) {
		stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
	         @Override
	         public void handle(WindowEvent event) {
					File file=new File("D:\\播放列表.txt");			
					//FileOutputStream fos=new FileOutputStream(file);
					FileWriter fw = null;
						try {
							if(!file.exists())
								file.createNewFile();
							fw = new FileWriter(file);
						} catch (IOException e1) {
							// TODO 自动生成的 catch 块
							e1.printStackTrace();
						}
					for(int i=0;i<list.size();i++) {
					try {
						fw.write(list.get(i)+"\n");
						fw.flush();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}	
				}
				try {
					fw.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
					
	         }
		  });
	}
	
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		
	}
	

}
