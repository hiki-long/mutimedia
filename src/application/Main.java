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
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Main extends Application {
	private double xOffset;
	private double yOffset;
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
//			DragUtil.addDragListener(primaryStage, root);
			Scene scene = new Scene(root,1280,720);
			scene.setOnMousePressed(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					xOffset = event.getScreenX()-primaryStage.getX();
					yOffset = event.getScreenY()-primaryStage.getY();
				}
				
			});
			scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					primaryStage.setX(event.getScreenX()-xOffset);
					primaryStage.setY(event.getScreenY()-yOffset);
				}
			});
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
