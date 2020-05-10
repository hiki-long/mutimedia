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
			//��ע�������滻Ϊ�Լ���������Ƶ������Ƶ���ļ�·��
			URL url = this.getClass().getClassLoader().getResource("res/�{�������� - �Ǥ�������.mp3");
			
			/*ֻ��������ʾ�������ļ�����������к���֮����޷�������ʾ"
			String path = URLDecoder.decode(url.toExternalForm(),"utf-8");
			*/
			
			//������������ʱ����url�Ƿ���������
			System.out.println(url);
			//��ȡý���ļ�
			Media media = new Media(url.toExternalForm());
			//�����ǲ������ֵĿ�����
			MediaPlayer mp = new MediaPlayer(media);
			//��ȡfxml��ʽ
			FXMLLoader fx = new FXMLLoader();
			//����fxml��ʽ
			fx.setLocation(fx.getClassLoader().getResource("fxml/base.fxml"));
			
			//������������fxml�Ƿ���ȷ����
			System.out.println(fx.getLocation());
			
			//���������ʽ��ʾ��scene�Ｔ�ɡ�
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
