package controller;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import javax.net.ssl.TrustManagerFactory;


import application.ReadDir;
import application.ReadLrc;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

	//��ı���
	public static List<Media> mp3_list = new ArrayList<Media>();//��¼��ǰmp3�����б�
	private boolean mode = false;//��ȡ����Ϊ0����ȡ��ƵΪ1
	private static String media_type[]= {"mp3","wmv","flac","mp4","avi","mkv" };//��ȡ���ļ���ʽ
	public String select_directory;
	public static ReadDir rd;//��ȡ�ļ��е����ʵ��������
	public static ReadLrc[] store_lrc;//ÿ�������ļ��ĸ�ʴ洢,����������Ƶ������Ƶ���Լ�̨��
	public MediaPlayer mp;//�������ؼ�
	private int index = 0;//��ǰ�б����ֲ��ŵ�index,��[0-music_number-1]
	public boolean isplay = false;//�Ƿ����ڲ��ŵĲ���ֵ
	public boolean hasmusic = false;
	private boolean mouse_press = false;
	
	@FXML
	private Button music_directory;//����ǽ�����ѡ���ļ��еİ�ť
	@FXML
	private ImageView cover;//���ַ���
	@FXML
	private Button prev;
	@FXML
	private Button after;
	@FXML
	private Button play;
	@FXML
	private AnchorPane whole;
	@FXML
	private Slider progress;
	@FXML
	private Slider volume;
	
	public MainController()
	{
		
	}
	
	public void initialize()//���������ʼ������
	{
//		System.out.println("ʵ��������");
		String xx = System.getProperty("user.dir");
		select_directory = xx + "\\bin\\res\\";
//		System.out.println(select_directory);
		choose_direc(select_directory);
	}
	
	
	
	
	public void read_m3ulist(boolean mode, String path)//��ȡ�ļ������ɵ�mp3�ļ��Ĳ����б�
	{
		if(mode == false)//mode ==0,��ȡ�����б�
		{
			try {
				BufferedReader bf = new BufferedReader(new FileReader(path));
				String line;
				while((line = bf.readLine())!=null)
				{
//					System.out.println(line);
					//����ֻ������Ƶ����
					if(line.endsWith("mp3") || line.endsWith("wmv") || line.endsWith("flac")) {
						File temp_file = new File(line);
						String url = temp_file.toURI().toURL().toExternalForm();
//						System.out.println(url);
						Media media = new Media(url);
						mp3_list.add(media);
//						System.out.println(media);
					}
					if(line.endsWith("mp4") || line.endsWith("avi") || line.endsWith("mkv")) {
					//����������Ƶ����������д���
					
					}
					
				}
//				System.out.println(mp3_list.get(0));
				mp = new MediaPlayer(mp3_list.get(index));
				if(mp != null) {
					hasmusic = true;
					setCover(mp);
					mp.play();mp.pause();//��������ì�ܵ�д������Ϊ��ʼ�����޷���ʾͼƬ����ֻ���뵽���ַ���
					bf.close();
				}
				else {
					hasmusic = false;
					System.out.println("��ǰĿ¼�޷���Ҫ����ļ�");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {//mode == 1,��ȡ��Ƶ�б� ������ʱ��������
			
		}
	}
	
	public void choose_direc(String path)//�����ʼ��
	{
		try {
			System.out.println("��ʼ������");
			System.out.println(path);
			rd = new ReadDir();
			rd.createmusicList(select_directory, media_type);
//			for(Iterator<String> it = rd.get_MusicName().iterator(); it.hasNext();)
//			{
//				System.out.println(it.next());
//			}
//			for(Iterator<String> it = rd.get_VideoName().iterator(); it.hasNext();)
//			{
//				System.out.println(it.next());
//			}
			store_lrc = new ReadLrc[rd.get_MusicName().size()];
			for(int num = 0; num < rd.get_MusicName().size(); num++)
			{	
				String tp = select_directory + "\\" + rd.get_MusicName().get(num) + ".lrc";
//				System.out.println(tp);
				store_lrc[num] = new ReadLrc(tp);
			}
//			int q=0,w=0;
//			for(int num = 0; num < rd.get_MusicName().size(); num++)
//			{
//				System.out.println(store_lrc[num].getMusicName());
//				if(store_lrc[num].getMusicName().equals("����"))
//				{
//					q++;
//				}
//				else w++;
//			}
//			System.out.println("�и��"+ w +"��, �޸��" + q +"��");
			read_m3ulist(mode, select_directory + "\\music.m3u");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void choose_direc()//ѡ���ļ��к�Ķ�ȡ�ļ����ܣ������ɲ����б�
	{
		//�ͷ���һ�ζ�ȡ����Դ
		index = 0;
		if(mp != null)
		mp.dispose();
		mp3_list.clear();
		isplay = false;
		hasmusic = false;
		rd.getClear();
		for(int i = 0; i < store_lrc.length; i++)
			store_lrc[i] = null;
		select_directory = "";
		//��ʼִ�ж�ȡ�ļ��в���
		DirectoryChooser dire = new DirectoryChooser();
		dire.setTitle("��ѡ�����������ļ���");
		dire.setInitialDirectory(new File(System.getProperty("java.class.path")));
		File newFolder = dire.showDialog(new Stage());
		if(newFolder!=null)
//		System.out.println(newFolder);
		select_directory = newFolder.getPath();
		try {
//			System.out.println("�ļ���ȡ");
			rd = new ReadDir();
			rd.createmusicList(select_directory, media_type);
//			for(Iterator<String> it = rd.get_MusicName().iterator(); it.hasNext();)
//			{
//				System.out.println(it.next());
//			}
//			for(Iterator<String> it = rd.get_VedioName().iterator(); it.hasNext();)
//			{
//				System.out.println(it.next());
//			}
			store_lrc = new ReadLrc[rd.get_MusicName().size()];
			for(int num = 0; num < rd.get_MusicName().size(); num++)
			{	
				String tp = select_directory + "\\" + rd.get_MusicName().get(num) + ".lrc";
//				System.out.println(tp);
				store_lrc[num] = new ReadLrc(tp);
			}
//			int q=0,w=0;
//			for(int num = 0; num < rd.get_MusicName().size(); num++)
//			{
//				System.out.println(store_lrc[num].getMusicName());
//				if(store_lrc[num].getMusicName().equals("����"))
//				{
//					q++;
//				}
//				else w++;
//			}
//			System.out.println("�и��"+ w +"��, �޸��" + q +"��");
			read_m3ulist(mode, select_directory + "\\music.m3u");

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Next()//��һ��
	{
		System.out.println(index);
		if(hasmusic) {
			if(index < mp3_list.size()-1)
			{
				index++;
			}
			else {
				index = 0;
			}
			mp.dispose();
			mp = new MediaPlayer(mp3_list.get(index));
			setCover(mp);
			mp.play();
		}
	}
	
	public void Before()//��һ��
	{
		if(hasmusic) {
			if(index == 0)
			{
				index = mp3_list.size()-1;
			}
			else {
				index--;
			}
			mp.dispose();
			mp = new MediaPlayer(mp3_list.get(index));
			setCover(mp);
			mp.play();
		}
	}
	
	public void PlayMusic()//���Ű�ť
	{
		if(hasmusic) {
			if(!isplay) {
				mp.play();
				isplay = true;
				System.out.println("�����Ǹ�ʵ������");
				store_lrc[index].printLrc(getLyric());
			}
			else {
				mp.pause();
				isplay = false;
			}
		}
	}
	
	public void setCover(MediaPlayer player)
	{
		player.setOnReady(new Runnable() {
			//����׼��״̬��ʾ����
			@Override
			public void run() {
				ObservableMap<String,Object> obmap = mp3_list.get(index).getMetadata();
				System.out.println("ͼƬ��" + (Image)obmap.get("image"));
				cover.setImage((Image)obmap.get("image"));
				player.volumeProperty().bind(volume.valueProperty());
				progress.setValue(0);
				progress.setMin(0);
				progress.setMax(player.getTotalDuration().toSeconds());
				
			}
		});
		
		
		player.setOnPlaying(new Runnable() {
			//���ֲ��Ź������Զ��ƶ�������
			@Override
			public void run() {
				// TODO Auto-generated method stub
				player.currentTimeProperty().addListener(new ChangeListener<Duration>(){

					@Override
					public void changed(ObservableValue<? extends Duration> observable, Duration oldValue,
							Duration newValue) {
						if(!mouse_press)
						progress.setValue(mp.getCurrentTime().toSeconds());
					}
					
				});
			}
		});
		
		player.setOnEndOfMedia(new Runnable() {
			//���ֲ������Զ�������һ��
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(index < mp3_list.size()-1)
				{
					index++;
				}
				else {
					index = 0;
				}
				mp.dispose();
				mp = new MediaPlayer(mp3_list.get(index));
				setCover(mp);
				mp.play();
			}
		});
		//���������϶�����
		progress.setOnMousePressed(e->{
			mouse_press = true;
		});
		progress.setOnMouseReleased(e->{						
			mp.seek(Duration.seconds(progress.getValue()));
			mouse_press = false;
		});
	}
	
	public void getMusicBar()//��ȡ����Ƶ�ײ���
	{
//		mp.setAudioSpectrumListener(new AudioSpectrumListener() {
//			
//			@Override
//			public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
//				// timestamp��ǰ����ʱ�䣬 duration��ü���һ�Σ�magnitudeƵ�����ݣ����鳤��0-128����Χ��-60-0
//				
//			}
//		});
	}

	public Map<Long,String> getLyric()//��ȡ��ǰ���ָ��
	{
		return store_lrc[index].getLyric();
	}
	
	public String getMusicName()//��ȡ��ǰ��������
	{
		return store_lrc[index].getMusicName();
	}
	
	public String getSingerName()//��ȡ��ǰ��������
	{
		return store_lrc[index].getSingerName();
	}
	
	/*����������ͼƬ�ĺ�����ʱ��������Ĵ���*/
//	String img = xx + "\\bin\\img\\Logo.png";
//	System.out.println(img);
//	String img = "D:\\java\\workspace\\player\\bin\\img\\artistsIcon.png";
//	File tp = new File(img);
//	System.out.println(tp.exists());
//	try {
//		prev.setStyle(new String("-fx-background-image:url('" + tp.toURI().toURL() + "');")
//				+ "-fx-background-size:stretch stretch;");
//	} catch (MalformedURLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
}
