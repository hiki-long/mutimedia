package application;
	
import java.net.URL;
import java.net.URLDecoder;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
//			Slider sl = new Slider(arg0, arg1, arg2)
			//请注意这里替换为自己电脑内音频或者视频的文件路径
			URL url = this.getClass().getClassLoader().getResource("res/藍井エイル - 星が降るユメ.mp3");
			
			/*只是用来显示正常的文件名，否则会有汉字之类的无法正常显示"
			String path = URLDecoder.decode(url.toExternalForm(),"utf-8");
			*/
			
			//这里用来出错时测试url是否正常读入
			System.out.println(url);
			//读取媒体文件
			Media media = new Media(url.toExternalForm());
			//这里是播放音乐的控制器
			MediaPlayer mp = new MediaPlayer(media);
			//读取fxml样式
			FXMLLoader fx = new FXMLLoader();
			//载入fxml样式
			fx.setLocation(fx.getClassLoader().getResource("fxml/base.fxml"));
			
			//这里用来测试fxml是否正确读入
			System.out.println(fx.getLocation());
			
			//将载入的样式显示在scene里即可。
			AnchorPane root = (AnchorPane)fx.load();
			
			Scene scene = new Scene(root,1280,720);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
