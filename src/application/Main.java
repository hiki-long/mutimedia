package application;
	
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

import controller.MainController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.DirectoryChooser;
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
			//读取fxml样式
			FXMLLoader fx = new FXMLLoader();
			
			//载入fxml样式
			fx.setLocation(fx.getClassLoader().getResource("fxml/base.fxml"));
			
			//这里用来测试fxml是否正确读入
//			System.out.println(fx.getLocation());

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
