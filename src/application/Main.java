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
			//��ȡfxml��ʽ
			FXMLLoader fx = new FXMLLoader();
			
			//����fxml��ʽ
			fx.setLocation(fx.getClassLoader().getResource("fxml/base.fxml"));
			
			//������������fxml�Ƿ���ȷ����
//			System.out.println(fx.getLocation());

			//���������ʽ��ʾ��scene�Ｔ�ɡ�
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
