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

	//类的变量
	public static List<Media> mp3_list = new ArrayList<Media>();//记录当前mp3播放列表
	private boolean mode = false;//读取音乐为0，读取视频为1
	private static String media_type[]= {"mp3","wmv","flac","mp4","avi","mkv" };//读取的文件格式
	public String select_directory;//选择的文件夹目录
	public static ReadDir rd;//读取文件夹的类的实例化对象
	public static ReadLrc[] store_lrc;//每个音乐文件的歌词存储,这个类存着音频名和视频名以及台词
	public MediaPlayer mp = null;//播放器控件
	private int index = 0;//当前列表音乐播放的index,从[0-music_number-1]
	public boolean isplay = false;//是否正在播放的布尔值
	public boolean hasmusic = false;//是否文件夹下有音乐
	private boolean flush = false;//刷新计算是否播放了一次
	private boolean mouse_press = false;//鼠标是否按下
	private Text test_text;
	private List<String> now_music_lyric = new ArrayList<String>();//当前歌词的顺序列表
	private ObservableList<Song> song_of_table = FXCollections.observableArrayList();
	private TableView<Song> local_table,recent_table;
	private String nowSelectedMusic;
	private String nowSelectedSinger;
	private boolean showlyric = false;
	@FXML
	private ImageView music_directory;//这个是界面中选择文件夹的按钮
	@FXML
	private ImageView cover;//音乐封面
	@FXML
	private ImageView prev;//上一首
	@FXML
	private ImageView after;//下一首
	@FXML
	private ImageView play;//播放
	@FXML
	private AnchorPane whole;//总界面
	@FXML
	private Slider progress;//播放进度条
	@FXML
	private Slider volume;//音量
	@FXML
	private VBox words;//歌词栏
	@FXML
	private VBox bk;//背景图
	@FXML
	private HBox about_window;//右上角窗口栏
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
	
	public void initialize()//界面载入初始化函数
	{
//		System.out.println("实例化测试");
		String xx = System.getProperty("user.dir");
//		System.out.println( System.getProperty("user.dir"));
		select_directory = xx + "\\bin\\res\\";
//		System.out.println(select_directory);
		choose_direc(select_directory);
		load_image(bk, "\\bin\\img\\timg.jpg",0.7);//三个参数为加载图片的控件，图片路径，透明度(0-1.0)
		title.setAlignment(Pos.CENTER);
		playmusic_handle(prev, 0);
		playmusic_handle(play, 1);
		playmusic_handle(after,2);
		window_related_handle(music_directory, "文档", 2);
		window_related_handle(smalled,"缩小",3);
		window_related_handle(windowed,"正方形",4);
		window_related_handle(closed,"关闭",5);
		words.setVisible(false);
		
	}
	
	public void window_related_handle(ImageView img, String img_name,int mode)
	{
		img.setOnMousePressed(e->{
			File image = new File("src/img/"+ img_name + "01.png");
			Image file_image = new Image(image.toURI().toString());
//			System.out.println("被点击");
			img.setImage(file_image);
			
		});
		img.setOnMouseExited(e->{
			File image = new File("src/img/"+ img_name + ".png");
			Image file_image = new Image(image.toURI().toString());
			img.setImage(file_image);
		});
		
		img.setOnMouseReleased(e->{
			if(mode==1)
			{//这里为音视频转化功能做准备
				
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
			{//全屏暂时不会写
				
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
		img.setOnMouseClicked(e->{//设置不同事件
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
	
	public void read_m3ulist(boolean mode, String path)//读取文件夹生成的mp3文件的播放列表
	{
		if(mode == false)//mode ==0,读取音乐列表
		{
			try {
				BufferedReader bf = new BufferedReader(new FileReader(path));
				String line;
				while((line = bf.readLine())!=null)
				{
//					System.out.println(line);
					//这里只做了音频处理
					if(line.endsWith("mp3") || line.endsWith("wmv") || line.endsWith("flac")) {
						File temp_file = new File(line);
						String url = temp_file.toURI().toURL().toExternalForm();
//						System.out.println(url);
						Media media = new Media(url);
						mp3_list.add(media);
//						System.out.println(media);
					}
					if(line.endsWith("mp4") || line.endsWith("avi") || line.endsWith("mkv")) {
					//这里由做视频处理的人自行处理
					
					}
					
				}
				for(int mus_num = 0; mus_num < store_lrc.length; mus_num++) {
					Map<Long,String> lyr = store_lrc[mus_num].getLyric();
					if(lyr != null) {
						for(long t : lyr.keySet()) {//给当前歌词打上标记
							mp3_list.get(mus_num).getMarkers().put(lyr.get(t),Duration.millis(t) );
						}
					}
					else {
//						System.out.println("当前为纯音乐");
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
				//开始播放
				mp = new MediaPlayer(mp3_list.get(index));
				if(mp != null) {
					hasmusic = true;
					setCover(mp);
					mp.play();mp.pause();//这里自相矛盾的写法是因为开始载入无法显示图片，我只能想到这种方法
					bf.close();
				}
				else {
					hasmusic = false;
					System.out.println("当前目录无符合要求的文件");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {//mode == 1,读取视频列表 这里暂时不做处理
			
		}
	}
	
	public TableView<Song> init_table(TableView<Song> tableview,int table_mode)
	{
		//初始化本地列表与最近播放列表
		tableview = new TableView<Song>(song_of_table);
		tableview.setPrefWidth(600);
		tableview.setPrefHeight(488);
		TableColumn<Song, String> music_name = new TableColumn<Song, String>("歌曲名");
		TableColumn<Song, String> musician_name = new TableColumn<Song, String>("歌手名");
		TableColumn<Song, String> music_time = new TableColumn<Song, String>("时长");
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
			TableColumn<Song, Integer> music_count = new TableColumn<Song, Integer>("歌曲播放次数");
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
				{//切换音乐处理
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
						File pause_cion = new File("src/img/暂停00.png");
						play.setImage(new Image(pause_cion.toURI().toString()));
					}
				}
			});
			return row;
		});
		return tableview;
	}
	
	public void choose_direc(String path)//载入初始化
	{
		try {
//			System.out.println("初始化数据");
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
//				if(store_lrc[num].getMusicName().equals("无题"))
//				{
//					q++;
//				}
//				else w++;
//			}
//			System.out.println("有歌词"+ w +"首, 无歌词" + q +"首");
			read_m3ulist(mode, select_directory + "\\music.m3u");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void choose_direc()//选择文件夹后的读取文件功能，并生成播放列表
	{

		//开始执行读取文件夹操作
		
		DirectoryChooser dire = new DirectoryChooser();
		dire.setTitle("请选择音乐所在文件夹");
//		System.out.println(System.getProperty("user.dir"));
		dire.setInitialDirectory(new File(System.getProperty("user.dir")));
		File newFolder = dire.showDialog(new Stage());
		if(newFolder!=null) {
			//释放上一次读取的资源
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
	//			System.out.println("文件读取");
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
	//				if(store_lrc[num].getMusicName().equals("无题"))
	//				{
	//					q++;
	//				}
	//				else w++;
	//			}
	//			System.out.println("有歌词"+ w +"首, 无歌词" + q +"首");
				read_m3ulist(mode, select_directory + "\\music.m3u");
	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void Next()//下一首
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
				File pause_cion = new File("src/img/暂停00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
			}
		}
	}
	
	public void Before()//上一首
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
				File pause_cion = new File("src/img/暂停00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
			}
		}
	}
	
	public void PlayMusic()//播放按钮
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
//				System.out.println("这里是歌词导入测试");
//				store_lrc[index].printLrc(getLyric());
				File pause_cion = new File("src/img/暂停00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
				
			}
			else {
				mp.pause();
				isplay = false;
				File pause_cion = new File("src/img/播放00.png");
				play.setImage(new Image(pause_cion.toURI().toString()));
			}
		}
	}
	
	public void setCover(MediaPlayer player)
	{
		now_music_lyric = this.getorderLyric();
		player.setOnReady(new Runnable() {
			//音乐准备状态显示封面
			@Override
			public void run() {

				ObservableMap<String,Object> obmap = mp3_list.get(index).getMetadata();
//				System.out.println("图片：" + (Image)obmap.get("image"));
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
					getCurrentText("当前音乐为纯音乐");
				}
				else {//预先载入歌词
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
			//音乐播放过程中自动移动进度条
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
			//音乐播放完自动播放下一首
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
			//取出歌词
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
							//太靠前
							continue;
						}
						// 超过最后的了
						if (current_index - 5 + i >= size) {
							break;
						}
						
						String tmp_String = now_music_lyric.get(current_index-5+i);
						
						if (i < 5) { // 前几句
							getOtherText(tmp_String,i);
						} else if (i == 5) { // 当前歌词
							getCurrentText(event.getMarker().getKey());
						} else { // 后几句
							getOtherText(tmp_String,10-i);
						}
						
					}
				}
				else {
					getCurrentText("当前音乐为纯音乐");
				}

//				words.getChildren().add(test_text);
			}
		});
		
		//进度条可拖动功能
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
	
	public void getMusicBar()//获取音乐频谱参数
	{
//		mp.setAudioSpectrumListener(new AudioSpectrumListener() {
//			
//			@Override
//			public void spectrumDataUpdate(double timestamp, double duration, float[] magnitudes, float[] phases) {
//				// timestamp当前音乐时间， duration多久监听一次，magnitude频谱数据，数组长度0-128，范围在-60-0
//				
//			}
//		});
	}

	public Map<Long,String> getLyric()//获取当前音乐歌词
	{
		return store_lrc[index].getLyric();
	}
	
	public String getMusicName()//获取当前音乐名字
	{
		return store_lrc[index].getMusicName();
	}
	
	public String getSingerName()//获取当前歌手名字
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
