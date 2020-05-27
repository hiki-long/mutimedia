package controller;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import javax.net.ssl.TrustManagerFactory;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import application.ReadDir;
import application.ReadLrc;
import application.Song;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaMarkerEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

	//��ı���
	public static List<Media> mp3_list = new ArrayList<Media>();//��¼��ǰmp3�����б�
	private boolean mode = false;//��ȡ����Ϊ0����ȡ��ƵΪ1
	private static String media_type[]= {"mp3","wmv","flac","mp4","avi","mkv" };//��ȡ���ļ���ʽ
	public String select_directory;//ѡ����ļ���Ŀ¼
	public static ReadDir rd;//��ȡ�ļ��е����ʵ��������
	public static ReadLrc[] store_lrc;//ÿ�������ļ��ĸ�ʴ洢,����������Ƶ������Ƶ���Լ�̨��
	public MediaPlayer mp = null;//�������ؼ�
	private int index = 0;//��ǰ�б����ֲ��ŵ�index,��[0-music_number-1]
	public boolean isplay = false;//�Ƿ����ڲ��ŵĲ���ֵ
	public boolean hasmusic = false;//�Ƿ��ļ�����������
	private boolean flush = false;//ˢ�¼����Ƿ񲥷���һ��
	private boolean mouse_press = false;//����Ƿ���
	private Text test_text;
	private List<String> now_music_lyric = new ArrayList<String>();//��ǰ��ʵ�˳���б�
	private ObservableList<Song> song_of_table = FXCollections.observableArrayList();
	private TableView<Song> local_table,recent_table;
	private String nowSelectedMusic;
	private String nowSelectedSinger;
	private boolean showlyric = false;
	@FXML
	private ImageView music_directory;//����ǽ�����ѡ���ļ��еİ�ť
	@FXML
	private ImageView cover;//���ַ���
	@FXML
	private ImageView prev;//��һ��
	@FXML
	private ImageView after;//��һ��
	@FXML
	private ImageView play;//����
	@FXML
	private AnchorPane whole;//�ܽ���
	@FXML
	private Slider progress;//���Ž�����
	@FXML
	private Slider volume;//����
	@FXML
	private VBox words;//�����
	@FXML
	private VBox bk;//����ͼ
	@FXML
	private HBox about_window;//���ϽǴ�����
	@FXML
	private ImageView smalled,closed,windowed;
	@FXML
	private HBox top,bottom;
	@FXML
	private VBox left;
	@FXML
	private Label title,mymusic,localmusic,recentmusic;
	@FXML
	private VBox right;
	@FXML
	private HBox local_func,recent_func;
	
	public MainController()
	{
		
	}
	
	public void initialize()//���������ʼ������
	{
//		System.out.println("ʵ��������");
		String xx = System.getProperty("user.dir");
//		System.out.println( System.getProperty("user.dir"));
		select_directory = xx + "\\bin\\res\\";
//		System.out.println(select_directory);
		choose_direc(select_directory);
		load_image(bk, "\\bin\\img\\timg.jpg",0.7);//��������Ϊ����ͼƬ�Ŀؼ���ͼƬ·����͸����(0-1.0)
		title.setAlignment(Pos.CENTER);
		playmusic_handle(prev, 0);
		playmusic_handle(play, 1);
		playmusic_handle(after,2);
		window_related_handle(music_directory, "�ĵ�", 2);
		window_related_handle(smalled,"��С",3);
		window_related_handle(windowed,"������",4);
		window_related_handle(closed,"�ر�",5);
		words.setVisible(false);
		
	}
	
	public void window_related_handle(ImageView img, String img_name,int mode)
	{
		img.setOnMousePressed(e->{
			File image = new File("src/img/"+ img_name + "01.png");
			Image file_image = new Image(image.toURI().toString());
//			System.out.println("�����");
			img.setImage(file_image);
			
		});
		img.setOnMouseExited(e->{
			File image = new File("src/img/"+ img_name + ".png");
			Image file_image = new Image(image.toURI().toString());
			img.setImage(file_image);
		});
		
		img.setOnMouseReleased(e->{
			if(mode==1)
			{//����Ϊ����Ƶת��������׼��
				
			}
			else if(mode==2)
			{
				choose_direc();
			}
			else if(mode==3)
			{
				((Stage)((ImageView)e.getSource()).getScene().getWindow()).setIconified(true);
			}
			else if(mode==4)
			{//ȫ����ʱ����д
				
			}
			else if(mode==5)
			{
				Stage stage = (Stage)closed.getScene().getWindow();
				stage.close();
				Platform.exit();
			}
		});
	}
	
	public void playmusic_handle(ImageView img,int num) {
		img.setPickOnBounds(true);
		img.setOnMouseClicked(e->{//���ò�ͬ�¼�
			img.setStyle("-fx-opacity:0.5");
			if(num == 0)
			{
				this.Before();
			}
			else if(num==1)
			{
				this.PlayMusic();
			}
			else {
				this.Next();
			}
		});
		
		img.setOnMouseExited(e->{
			img.setStyle("-fx-opacity:1.0");
		});
		
	}
	
	public void load_image(Node node,String related_path,double opacity)
	{
		String img = System.getProperty("user.dir") + related_path;
		File tp = new File(img);
//		System.out.println(tp.exists());
		try {
		   node.setStyle(new String("-fx-background-image:url('" + tp.toURI().toURL() + "');")
		     + "-fx-background-size:stretch stretch;" + "-fx-opacity:"+opacity+";");
		} catch (MalformedURLException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		}
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
				for(int mus_num = 0; mus_num < store_lrc.length; mus_num++) {
					Map<Long,String> lyr = store_lrc[mus_num].getLyric();
					if(lyr != null) {
						for(long t : lyr.keySet()) {//����ǰ��ʴ��ϱ��
							mp3_list.get(mus_num).getMarkers().put(lyr.get(t),Duration.millis(t) );
						}
					}
					else {
//						System.out.println("��ǰΪ������");
					}

					
				}
				List<String> estimate_time =  rd.get_AllMusicPath();
				for(int repeat = 0; repeat < estimate_time.size(); repeat++) {
					try {									
							String music_path = estimate_time.get(repeat);
//							System.out.println(music_path);
							MP3File f = new MP3File(music_path);
							MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
//							System.out.println(audioHeader.getTrackLength());
							store_lrc[repeat].setMusicTotalTime(audioHeader.getTrackLength());
//							System.out.println(store_lrc[repeat].getMusicTotalTime());
							Song temp_song = new Song(store_lrc[repeat].getMusicName(),store_lrc[repeat].getSingerName(),store_lrc[repeat].getMusicTotalTime(),0);
							song_of_table.add(temp_song);
						}
					catch(Exception e){
						e.printStackTrace();
					}
				}

				local_table = init_table(local_table, 0);
				recent_table = init_table(recent_table, 1);
				right.getChildren().add(local_table);
				local_func.setOnMouseClicked(e -> {
					right.getChildren().clear();
					right.getChildren().add(local_table);
					right.setVisible(true);
					words.setVisible(false);
				});
				recent_func.setOnMouseClicked(e -> {
					right.getChildren().clear();
					right.getChildren().add(recent_table);
					right.setVisible(true);
					words.setVisible(false);
				});
				//��ʼ����
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
	
	public TableView<Song> init_table(TableView<Song> tableview,int table_mode)
	{
		//��ʼ�������б�����������б�
		tableview = new TableView<Song>(song_of_table);
		tableview.setPrefWidth(600);
		tableview.setPrefHeight(488);
		TableColumn<Song, String> music_name = new TableColumn<Song, String>("������");
		TableColumn<Song, String> musician_name = new TableColumn<Song, String>("������");
		TableColumn<Song, String> music_time = new TableColumn<Song, String>("ʱ��");
		music_name.setCellValueFactory(new PropertyValueFactory<Song,String>("MusicName"));
		musician_name.setCellValueFactory(new PropertyValueFactory<Song,String>("MusicianName"));
		music_time.setCellValueFactory(new PropertyValueFactory<Song,String>("TotalTime"));
		music_name.setPrefWidth(tableview.getPrefWidth()/(table_mode+3));
		musician_name.setPrefWidth(tableview.getPrefWidth()/(table_mode+3));
		music_time.setPrefWidth(tableview.getPrefWidth()/(table_mode+3));
		tableview.getColumns().add(music_name);
		tableview.getColumns().add(musician_name);
		tableview.getColumns().add(music_time);			
		if(table_mode == 1){
			TableColumn<Song, Integer> music_count = new TableColumn<Song, Integer>("�������Ŵ���");
			music_count.setCellValueFactory(new PropertyValueFactory<Song, Integer>("count"));
			music_count.setPrefWidth(tableview.getPrefWidth()/(table_mode+3));
			tableview.getColumns().add(music_count);
		}
		
		tableview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {

			@Override
			public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
				if(newValue!=null)
				{
//					System.out.println(newValue.getMusicName());
					nowSelectedMusic = newValue.getMusicName();
					nowSelectedSinger = newValue.getMusicianName();
				}
				
			}
		});
		tableview.setRowFactory(tv->{
			TableRow<Song> row = new TableRow<Song>();
			row.setOnMouseClicked(event -> {
				if(event.getClickCount() == 2 && (!row.isEmpty()))
				{//�л����ִ���
//					System.out.println(nowSelectedMusic);						
					String complete_music = nowSelectedSinger +" - " + nowSelectedMusic;
//					System.out.println(complete_music);
//					System.out.println(rd.get_MusicName().indexOf(complete_music));
					index = rd.get_MusicName().indexOf(complete_music);	
					mp.dispose();
					mp = new MediaPlayer(mp3_list.get(index));
					setCover(mp);
					mp.play();
					song_of_table.get(index).setCount(song_of_table.get(index).getCount()+1);
					recent_table.refresh();
					if(!isplay) {
						isplay = true;
						File pause_cion = new File("src/img/��ͣ00.png");
						play.setImage(new Image(pause_cion.toURI().toString()));
					}
				}
			});
			return row;
		});
		return tableview;
	}
	
	public void choose_direc(String path)//�����ʼ��
	{
		try {
//			System.out.println("��ʼ������");
//			System.out.println(path);
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
				char slash = '\\';
				String tp = select_directory + slash + rd.get_MusicName().get(num) + ".lrc";
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

		//��ʼִ�ж�ȡ�ļ��в���
		
		DirectoryChooser dire = new DirectoryChooser();
		dire.setTitle("��ѡ�����������ļ���");
//		System.out.println(System.getProperty("user.dir"));
		dire.setInitialDirectory(new File(System.getProperty("user.dir")));
		File newFolder = dire.showDialog(new Stage());
		if(newFolder!=null) {
			//�ͷ���һ�ζ�ȡ����Դ
			index = 0;
			if(mp != null)
			{
				mp.dispose();
				mp3_list.clear();
				now_music_lyric.clear();
				song_of_table.clear();
				rd.getClear();
				for(int i = 0; i < store_lrc.length; i++)
					store_lrc[i] = null;
			}
			local_table = null;
			recent_table = null;
			nowSelectedMusic = null;
			nowSelectedSinger = null;
			showlyric = false;
			isplay = false;
			hasmusic = false;
			mode = false;
			flush = false;
			mouse_press = false;
			test_text = null;	
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
	}
	
	public void Next()//��һ��
	{
		System.out.println(index);
		if(hasmusic) {
			words.getChildren().clear();
			if(index < mp3_list.size()-1)
			{
				index++;
			}
			else {
				index = 0;
			}
			
			flush = false;			
			if(!flush)
			{
				flush = true;
				store_lrc[index].set_playtime(store_lrc[index].get_playtime()+1);
				song_of_table.get(index).setCount(song_of_table.get(index).getCount()+1);
				recent_table.refresh();
			}
			
			mp.dispose();
			mp = new MediaPlayer(mp3_list.get(index));
			setCover(mp);
			mp.play();
			if(!isplay) {
				isplay = true;
				File pause_cion = new File("src/img/��ͣ00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
			}
		}
	}
	
	public void Before()//��һ��
	{
		if(hasmusic) {
			words.getChildren().clear();
			if(index == 0)
			{
				index = mp3_list.size()-1;
			}
			else {
				index--;
			}
			
			flush = false;
			if(!flush)
			{
				flush = true;
				store_lrc[index].set_playtime(store_lrc[index].get_playtime()+1);
				song_of_table.get(index).setCount(song_of_table.get(index).getCount()+1);
				recent_table.refresh();
			}
			
			mp.dispose();
			mp = new MediaPlayer(mp3_list.get(index));
			setCover(mp);
			mp.play();
			if(!isplay) {
				isplay = true;
				File pause_cion = new File("src/img/��ͣ00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
			}
		}
	}
	
	public void PlayMusic()//���Ű�ť
	{
		
		if(hasmusic) {
			if(!flush)
			{
				flush = true;
				store_lrc[index].set_playtime(store_lrc[index].get_playtime()+1);
				song_of_table.get(index).setCount(song_of_table.get(index).getCount()+1);
				recent_table.refresh();
			}

			if(!isplay) {
				mp.play();
				isplay = true;
//				System.out.println("�����Ǹ�ʵ������");
//				store_lrc[index].printLrc(getLyric());
				File pause_cion = new File("src/img/��ͣ00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
				
			}
			else {
				mp.pause();
				isplay = false;
				File pause_cion = new File("src/img/����00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
			}
		}
	}
	
	public void setCover(MediaPlayer player)
	{
		now_music_lyric = this.getorderLyric();
		player.setOnReady(new Runnable() {
			//����׼��״̬��ʾ����
			@Override
			public void run() {

				ObservableMap<String,Object> obmap = mp3_list.get(index).getMetadata();
//				System.out.println("ͼƬ��" + (Image)obmap.get("image"));
				cover.setImage((Image)obmap.get("image"));
				player.volumeProperty().bind(volume.valueProperty());
				progress.setValue(0);
				progress.setMin(0);
				progress.setMax(player.getTotalDuration().toSeconds());
//				System.out.println(player.getTotalDuration().toSeconds());
//				store_lrc[index].setMusicTotalTime(player.getTotalDuration().toSeconds());
				if(ispure())
				{
					words.getChildren().clear();
					getCurrentText("��ǰ����Ϊ������");
				}
				else {//Ԥ��������
					for(int i = 0; i < 6; i++)
					{
						if(i == 0)
						{
							getCurrentText(now_music_lyric.get(i));
						}
						else {
							getOtherText(now_music_lyric.get(i), 5-i);
						}
					}
				}
				cover.setOnMouseClicked(e -> {
					cover.setStyle("-fx-opacity:0.8");
					if(!showlyric)
					{
						showlyric = true;
						words.setVisible(true);						
						right.setVisible(false);
					}
					else {
						showlyric = false;
						words.setVisible(false);						
						right.setVisible(true);
					}
				});
				cover.setOnMouseExited(e -> {
					cover.setStyle("-fx-opacity:1.0");
				});
				

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
				words.getChildren().clear();
				if(index < mp3_list.size()-1)
				{
					index++;
				}
				else {
					index = 0;
				}
				flush = false;			
				if(!flush)
				{
					flush = true;
					store_lrc[index].set_playtime(store_lrc[index].get_playtime()+1);
					song_of_table.get(index).setCount(song_of_table.get(index).getCount()+1);
					recent_table.refresh();
				}
				mp.dispose();
				mp = new MediaPlayer(mp3_list.get(index));
				setCover(mp);
				mp.play();
			}
		});
		
		player.setOnMarker(new EventHandler<MediaMarkerEvent>() {
			//ȡ�����
			@Override
			public void handle(MediaMarkerEvent event)
			{
				words.getChildren().clear();
				if(!ispure()) {					
	//				System.out.println(event.getMarker().getKey());
					String row = event.getMarker().getKey();
					test_text = new Text(event.getMarker().getKey());
					int size = now_music_lyric.size();
					int current_index = now_music_lyric.indexOf(event.getMarker().getKey());
					for (int i = 0; i < 11; i++)
					{
						if (current_index - 5 + i < 0) {
							//̫��ǰ
							continue;
						}
						// ����������
						if (current_index - 5 + i >= size) {
							break;
						}
						
						String tmp_String = now_music_lyric.get(current_index-5+i);
						
						if (i < 5) { // ǰ����
							getOtherText(tmp_String,i);
						} else if (i == 5) { // ��ǰ���
							getCurrentText(event.getMarker().getKey());
						} else { // �󼸾�
							getOtherText(tmp_String,10-i);
						}
						
					}
				}
				else {
					getCurrentText("��ǰ����Ϊ������");
				}

//				words.getChildren().add(test_text);
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
	
	private void getCurrentText(String lrc) {
		HBox box = new HBox();
		box.setPrefWidth(540);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(8, 0, 8, 0));
		Text text = new Text(lrc);
		text.setFont(Font.font("Arial", 16));
		text.setFill(Color.ORANGERED);
		text.setStrokeWidth(0.4);
		text.setStroke(Color.ORANGE);
		box.getChildren().add(text);
		words.getChildren().add(box);
	}
	
	private void getOtherText(String lrc, int num) {
		HBox box = new HBox();
		box.setPrefWidth(540);
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets((num+1)*1.2, 0, (num+1)*1.2, 0));
		Text text = new Text(lrc);
		text.setFont(Font.font("Arial", 10+num));
		text.setOpacity(0.5 + 0.05* (num + 1));
		text.setFill(Color.ORANGERED);
		text.setStrokeWidth(0.2+0.04*num);
		text.setStroke(Color.ORANGE);
		box.getChildren().add(text);
		words.getChildren().add(box);
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
	
	public List<String> getorderLyric()
	{
		return store_lrc[index].getorderLyric();
	}
	
	public boolean ispure()
	{
		return store_lrc[index].isPureMusic();
	}
	
}
