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

	MenuBar m1 = new MenuBar();   //���ò˵���
	Video video = new Video();    //������ʵ����
	
	Media media = null;           //��Ƶ����
	MediaPlayer MediaPlayer;
	MediaView view;
	
	ImageView playview;             //����/��ͣͼƬ
	ImageView pauseview;
	
	VBox V1 = new VBox();   //���ֲ˵�����Ƶ����������
	HBox H1 = new HBox();   //�������򣬴�Ÿ������
	HBox H2 = new HBox();   //���ڴ�Ų�����ͣ��ť������
	HBox H3 = new HBox();   //���ڴ����Ƶ��ʼ��ͼƬ
	HBox H4 = new HBox();   //���ڴ��������
	HBox H5 = new HBox();   //���ڴ��H2��H4����
	HBox H6 = new HBox();   //���ڴ�Ž�������ʱ��
	
	Scene scene = new Scene(H1);  //���ó���
	
	Slider processSlider = new Slider(); //������
	Slider volumeSlider = new Slider();  //������
	Slider speedSlider = new Slider();
	Label processLabel = new Label();      //����ʱ��
	Label speedName = new Label("����"); //��������
	Label volumeName = new Label("����");  //��������
	
	int count = 0;   //�ж��ǲ��Ż�����ͣ
	int judge = 0;   //�ж��Ƿ��ǳ��δ���Ƶ�ļ�
		
	@Override
	public void start(Stage primaryStage) {
		// TODO �Զ����ɵķ������
		
		Image cover = new Image("Viplayer/mediaplay1_2.jpg");   //��ʼ��ͼƬ
		ImageView coverview = new ImageView(cover);

		H3.getChildren().add(coverview);   //����ʼ��ͼƬ����H3��
	    H3.setStyle("-fx-background-color: #000000;");   //����H3����Ϊ��ɫ
	    H3.setAlignment(Pos.CENTER);
		
		initPlay();    //Ӧ�ò���/��ͣ��������ʾ����/��ͣ��ť
		initMenu(primaryStage);    //Ӧ�ò˵�����������ʾ�˵�
		
		processSlider.setPrefWidth(650);
		processLabel.setText("0:00:00/0:00:00");         //���ó�ʼʱ��
		H6.getChildren().addAll(processSlider,processLabel);  //��������ʱ�����H6
		H6.setAlignment(Pos.CENTER_LEFT);
		
		H6.setSpacing(20);
		
		volumeSlider.setMin(0);   
		volumeSlider.setMax(100);
		volumeSlider.setShowTickLabels(true);  //��ʾ��ֵ
		volumeSlider.setShowTickMarks(true);   //��ʾ�̶�
		volumeSlider.setMajorTickUnit(50);   //���̶�50
		volumeSlider.setMinorTickCount(5);   //С�̶�5
		
		speedSlider.setMin(0.5);   
		speedSlider.setMax(2);
		speedSlider.setValue(1);
		speedSlider.setShowTickLabels(true);  //��ʾ��ֵ
		speedSlider.setShowTickMarks(true);   //��ʾ�̶�
		speedSlider.setMajorTickUnit(1);   //���̶�50

		
	    H4.getChildren().addAll(speedName,speedSlider,volumeName,volumeSlider);   //�������ֺ�����������H4
        H4.setAlignment(Pos.CENTER_RIGHT);
	    H4.setSpacing(10);      //H4ÿ��������10
	    
		H5.getChildren().addAll(H2,H4);  //��H2,H4�Ž�H5
		H5.setSpacing(300);
		
		V1 = new VBox(m1,H3,H6,H5);   //��m1��H3,H6,H5����ֱ˳���ŷ�
		V1.setSpacing(10);
		
		scene = new Scene(V1);
		primaryStage.setScene(scene);
		primaryStage.setWidth(800);    //��ʼ��������С
		primaryStage.setHeight(600);
		primaryStage.setTitle("MediaPlay");
		primaryStage.setResizable(false);   //�ر�(���ڿ��޸�)
		primaryStage.show();
		

		//��ҳͼƬ�����ȡ�����ļ�
		try {
			coverview.setOnMouseClicked(e->{
				if(judge == 0) {   //�ж��Ƿ�Ϊ��һ�δ���Ƶ
				    media = video.firstOpenVideo();         //��һ�δ���Ƶ
				    MediaPlayer = new MediaPlayer(media);
				    judge ++;
				}
				view = new MediaView(MediaPlayer);
				view.setFitHeight(450);

			       
				MediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {   //���������������������Ⱥ�ʱ��
		
			           @Override
			           public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
			        	   //����������
			        	   processSlider.setValue(newValue.toSeconds() / MediaPlayer.getTotalDuration().toSeconds() * 100);
			        	   //ʱ�����
			               processLabel.setText(video.DurationToString(newValue) + "/" + video.DurationToString(MediaPlayer.getTotalDuration()));
			           }
			
			    });
				
				video.progressSliderBind(MediaPlayer, processSlider);   //��������������
			    video.volumeSliderBind(MediaPlayer,volumeSlider);       //��������������
			    
			    speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
					public void changed(ObservableValue<? extends Number> observable,Number oldValue,Number newValue) {
						MediaPlayer.setRate(speedSlider.getValue());
					}
				});
			    
			    volumeSlider.setValue(20);  //����������ʼֵΪ20
				
			    H6.getChildren().clear();
			    H6.getChildren().addAll(processSlider,processLabel); 
				H6.setAlignment(Pos.CENTER_LEFT);
				H6.setSpacing(20);
			    
			    H4.getChildren().clear();
			    H4.getChildren().addAll(speedName,speedSlider,volumeName,volumeSlider);   //�������ֺ�����������H4
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
				
				video.setOnKeyPressed(scene, MediaPlayer);  //���̿������������Ⱥ���
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		video.close(primaryStage);
		
	}

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		launch(args);
	}

	//����/��ͣ����
	private void initPlay() {
		Image play = new Image("Viplayer/play.jpg");   //���Ű�ťͼƬ
		playview = new ImageView(play);

		Image pause = new Image("Viplayer/pause.jpg");   //��ͣ��ťͼƬ
		pauseview = new ImageView(pause);
		
		H2.getChildren().add(playview);   //������/��ͣͼƬ����H2
		H2.setPadding(new Insets(0,0,0,10));
		
		playview.setOnMouseClicked(e->{  //play��ť�����Ĺ���
			if(media != null) {
				video.play(MediaPlayer);
				H2.getChildren().clear();
				H2.getChildren().add(pauseview);
				H2.setPadding(new Insets(0,0,0,10));
			}
		});
		
		pauseview.setOnMouseClicked(e->{  //pause��ť�����Ĺ���
			if(media != null) {
				video.pause(MediaPlayer);
				H2.getChildren().clear();
				H2.getChildren().add(playview);
				H2.setPadding(new Insets(0,0,0,10));
			}
		});
	}
	
	//�˵�������
	private void initMenu(Stage video_stage) {
		Menu menuFile = new Menu("��Ƶ");
		Menu menuMusic = new Menu("����");

		m1.getMenus().addAll(menuFile,menuMusic);

		MenuItem menuItemOpen = new MenuItem("����Ƶ�ļ�");
		MenuItem menuMusicOpen = new MenuItem("�����ֲ�����");

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
			//�������ֲ�����
			Main music = new Main();
			music.start(new Stage());
			Stage stage = video_stage;
			stage.close();
			
		});
		
	}
}

