package Viplayer;

import Viplayer.Video;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import application.Main;

public class mediapane extends Application{

	MenuBar m1 = new MenuBar();   //设置菜单栏
	Video video = new Video();    //功能类实例化
	
	Media media = null;           //视频对象
	MediaPlayer MediaPlayer;
	MediaView view;
	
	ImageView playview;             //播放/暂停图片
	ImageView pauseview;
	
	VBox V1 = new VBox();   //布局菜单，视频，进度条等
	HBox H1 = new HBox();   //整体区域，存放各个板块
	HBox H2 = new HBox();   //用于存放播放暂停按钮的区域
	HBox H3 = new HBox();   //用于存放视频开始的图片
	HBox H4 = new HBox();   //用于存放音量条
	HBox H5 = new HBox();   //用于存放H2和H4整体
	HBox H6 = new HBox();   //用于存放进度条和时间
	
	Scene scene = new Scene(H1);  //设置场景
	
	Slider processSlider = new Slider(); //进度条
	Slider volumeSlider = new Slider();  //音量条
	Slider speedSlider = new Slider();
	Label processLabel = new Label();      //进度时间
	Label speedName = new Label("倍速"); //倍速名字
	Label volumeName = new Label("声音");  //音量名字
	
	int count = 0;   //判断是播放还是暂停
	int judge = 0;   //判断是否是初次打开视频文件
		
	@Override
	public void start(Stage primaryStage) {
		// TODO 自动生成的方法存根
		
		Image cover = new Image("Viplayer/mediaplay1_2.jpg");   //开始的图片
		ImageView coverview = new ImageView(cover);

		H3.getChildren().add(coverview);   //将开始的图片放入H3中
	    H3.setStyle("-fx-background-color: #000000;");   //设置H3背景为黑色
	    H3.setAlignment(Pos.CENTER);
		
		initPlay();    //应用播放/暂停函数，显示播放/暂停按钮
		initMenu(primaryStage);    //应用菜单栏函数，显示菜单
		
		processSlider.setPrefWidth(650);
		processLabel.setText("0:00:00/0:00:00");         //设置初始时间
		H6.getChildren().addAll(processSlider,processLabel);  //进度条和时间加入H6
		H6.setAlignment(Pos.CENTER_LEFT);
		
		H6.setSpacing(20);
		
		volumeSlider.setMin(0);   
		volumeSlider.setMax(100);
		volumeSlider.setShowTickLabels(true);  //显示数值
		volumeSlider.setShowTickMarks(true);   //显示刻度
		volumeSlider.setMajorTickUnit(50);   //主刻度50
		volumeSlider.setMinorTickCount(5);   //小刻度5
		
		speedSlider.setMin(0.5);   
		speedSlider.setMax(2);
		speedSlider.setValue(1);
		speedSlider.setShowTickLabels(true);  //显示数值
		speedSlider.setShowTickMarks(true);   //显示刻度
		speedSlider.setMajorTickUnit(1);   //主刻度50

		
	    H4.getChildren().addAll(speedName,speedSlider,volumeName,volumeSlider);   //音量名字和音量条加入H4
        H4.setAlignment(Pos.CENTER_RIGHT);
	    H4.setSpacing(10);      //H4每个组件间隔10
	    
		H5.getChildren().addAll(H2,H4);  //将H2,H4放进H5
		H5.setSpacing(300);
		
		V1 = new VBox(m1,H3,H6,H5);   //将m1，H3,H6,H5按垂直顺序排放
		V1.setSpacing(10);
		
		scene = new Scene(V1);
		primaryStage.setScene(scene);
		primaryStage.setWidth(800);    //初始化场景大小
		primaryStage.setHeight(600);
		primaryStage.setTitle("MediaPlay");
		primaryStage.setResizable(false);   //关闭(窗口可修改)
		primaryStage.show();
		

		//首页图片点击读取本地文件
		try {
			coverview.setOnMouseClicked(e->{
				if(judge == 0) {   //判断是否为第一次打开视频
				    media = video.firstOpenVideo();         //第一次打开视频
				    MediaPlayer = new MediaPlayer(media);
				    judge ++;
				}
				view = new MediaView(MediaPlayer);
				view.setFitHeight(450);

			       
				MediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {   //监听器，监听进度条进度和时间
		
			           @Override
			           public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
			        	   //进度条进度
			        	   processSlider.setValue(newValue.toSeconds() / MediaPlayer.getTotalDuration().toSeconds() * 100);
			        	   //时间进度
			               processLabel.setText(video.DurationToString(newValue) + "/" + video.DurationToString(MediaPlayer.getTotalDuration()));
			           }
			
			    });
				
				video.progressSliderBind(MediaPlayer, processSlider);   //进度条拉动功能
			    video.volumeSliderBind(MediaPlayer,volumeSlider);       //音量条拉动功能
			    
			    speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
					public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue) {
						MediaPlayer.setRate(speedSlider.getValue());
					}
				});
			    
			    volumeSlider.setValue(20);  //设置音量初始值为20
				
			    H6.getChildren().clear();
			    H6.getChildren().addAll(processSlider,processLabel); 
				H6.setAlignment(Pos.CENTER_LEFT);
				H6.setSpacing(20);
			    
			    H4.getChildren().clear();
			    H4.getChildren().addAll(speedName,speedSlider,volumeName,volumeSlider);   //音量名字和音量条加入H4
		        H4.setAlignment(Pos.CENTER_RIGHT);
			    H4.setSpacing(10);
			    
			    H5.getChildren().clear();
			    H5.getChildren().addAll(H2,H4);
			    H5.setSpacing(300);
				
			    H3.getChildren().clear();
			    H3.getChildren().add(view);
			    H3.setAlignment(Pos.CENTER);
			    H3.setStyle("-fx-background-color: #000000;");
			    
				V1.getChildren().clear();
				V1.getChildren().addAll(m1,H3,H6,H5);
				V1.setSpacing(10);
				
				video.setOnKeyPressed(scene, MediaPlayer);  //键盘控制音量，进度函数
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		video.close(primaryStage);
		
	}

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		launch(args);
	}

	//播放/暂停函数
	private void initPlay() {
		Image play = new Image("Viplayer/play.jpg");   //播放按钮图片
		playview = new ImageView(play);

		Image pause = new Image("Viplayer/pause.jpg");   //暂停按钮图片
		pauseview = new ImageView(pause);
		
		H2.getChildren().add(playview);   //将播放/暂停图片放入H2
		H2.setPadding(new Insets(0,0,0,10));
		
		playview.setOnMouseClicked(e->{  //play按钮点击后的功能
			if(media != null) {
				video.play(MediaPlayer);
				H2.getChildren().clear();
				H2.getChildren().add(pauseview);
				H2.setPadding(new Insets(0,0,0,10));
			}
		});
		
		pauseview.setOnMouseClicked(e->{  //pause按钮点击后的功能
			if(media != null) {
				video.pause(MediaPlayer);
				H2.getChildren().clear();
				H2.getChildren().add(playview);
				H2.setPadding(new Insets(0,0,0,10));
			}
		});
	}
	
	//菜单栏函数
	private void initMenu(Stage video_stage) {
		Menu menuFile = new Menu("视频");
		Menu menuMusic = new Menu("音乐");

		m1.getMenus().addAll(menuFile,menuMusic);

		MenuItem menuItemOpen = new MenuItem("打开视频文件");
		MenuItem menuMusicOpen = new MenuItem("打开音乐播放器");

		menuFile.getItems().add(menuItemOpen);
		menuMusic.getItems().add(menuMusicOpen);

		menuItemOpen.setOnAction(e->{
			count = 0;
			if(judge == 0) {
			    media = video.firstOpenVideo();
			    MediaPlayer = new MediaPlayer(media);
			    judge ++;
			}
			else
				MediaPlayer = video.OpenVideo(media, MediaPlayer);
			
			view = new MediaView(MediaPlayer);
			view.setFitHeight(450);
		       
			MediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
				
		           @Override
		           public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
		        	   processSlider.setValue(newValue.toSeconds() / MediaPlayer.getTotalDuration().toSeconds() * 100);
		        		
		               processLabel.setText(video.DurationToString(newValue) + "/" + video.DurationToString(MediaPlayer.getTotalDuration()));
		           }
		
		       });
			
			speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue) {
					MediaPlayer.setRate(speedSlider.getValue());
				}
			});
			
			video.progressSliderBind(MediaPlayer, processSlider);
		    video.volumeSliderBind(MediaPlayer,volumeSlider);
		    
			volumeSlider.setValue(20);
		    processSlider.setValue(0);
			
		    H6.getChildren().clear();
		    H6.getChildren().addAll(processSlider,processLabel); 
			H6.setAlignment(Pos.CENTER_LEFT);
			H6.setSpacing(20);
		    
		    H4.getChildren().clear();
		    H4.getChildren().addAll(speedName,speedSlider,volumeName,volumeSlider);   
	        H4.setAlignment(Pos.CENTER_RIGHT);
		    H4.setSpacing(10);      
		    
		    H2.getChildren().clear();
		    H2.getChildren().add(playview);
		    H2.setPadding(new Insets(0,0,0,10));
		    
		    H5.getChildren().clear();
		    H5.getChildren().addAll(H2,H4);
		    H5.setSpacing(300);
		    
		    H3.getChildren().clear();
		    H3.getChildren().add(view);
		    H3.setAlignment(Pos.CENTER);
		    H3.setStyle("-fx-background-color: #000000;");

			V1.getChildren().clear();
			V1.getChildren().addAll(m1,H3,H6,H5);
			V1.setSpacing(10);
			
			video.setOnKeyPressed(scene, MediaPlayer);
		});
		
		menuMusicOpen.setOnAction(e->{
			//启动音乐播放器
			Main music = new Main();
			music.start(new Stage());
			Stage stage = video_stage;
			stage.close();
			
		});
		
	}
}

