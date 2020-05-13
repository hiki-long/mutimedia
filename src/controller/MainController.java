package controller;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

import javax.net.ssl.TrustManagerFactory;

import application.ReadDir;
import application.ReadLrc;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class MainController {

	//类的变量
	public static List<Media> mp3_list = new ArrayList<Media>();//记录当前mp3播放列表
	private boolean mode = false;//读取音乐为0，读取视频为1
	private static String media_type[]= {"mp3","wmv","flac","mp4","avi","mkv" };//读取的文件格式
	public String select_directory;
	public static ReadDir rd;//读取文件夹的类的实例化对象
	public static ReadLrc[] store_lrc;//每个音乐文件的歌词存储,这个类存着音频名和视频名以及台词
	public MediaPlayer mp;//播放器控件
	private int index = 0;//当前列表音乐播放的index,从0-音乐数量-1
	public boolean isplay = false;//是否正在播放的布尔值
	public boolean hasmusic = false;
	
	@FXML
	private Button music_directory;//这个是界面中选择文件夹的按钮
	@FXML
	private ImageView cover;//音乐封面
	@FXML
	private Button prev;
	@FXML
	private Button after;
	@FXML
	private Button play;
	@FXML
	private AnchorPane whole;
	
	
	public MainController()
	{
		
	}
	
	public void initialize()//界面载入初始化函数
	{
//		System.out.println("实例化测试");
		String xx = System.getProperty("user.dir");
		select_directory = xx + "\\bin\\res\\";
		System.out.println(select_directory);
		choose_direc(select_directory);
		
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
					if(line.endsWith("mp3") || line.endsWith("wmv") || line.endsWith("flac")) {
						File temp_file = new File(line);
						String url = temp_file.toURI().toURL().toExternalForm();
//						System.out.println(url);
						Media media = new Media(url);
						mp3_list.add(media);
//						System.out.println(media);
					}
				}
//				System.out.println(mp3_list.get(0));
				mp = new MediaPlayer(mp3_list.get(index));
				if(mp != null) {
					hasmusic = true;
					setCover(mp);
					mp.play();mp.pause();//这里自相矛盾的写法是因为开始载入无法显示图片，我只能想到这种方法
					bf.close();
				}
				else {
					hasmusic = true;
					System.out.println("当前目录无符合要求的文件");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {//mode == 1,读取视频列表 这里暂时不做处理
			
		}
	}
	
	public void choose_direc(String path)//载入初始化
	{
		try {
			System.out.println("初始化数据");
			System.out.println(path);
			rd = new ReadDir();
			rd.createmusicList(select_directory, media_type);
			for(Iterator<String> it = rd.get_MusicName().iterator(); it.hasNext();)
			{
				System.out.println(it.next());
			}
			for(Iterator<String> it = rd.get_VideoName().iterator(); it.hasNext();)
			{
				System.out.println(it.next());
			}
			store_lrc = new ReadLrc[rd.get_MusicName().size()];
			for(int num = 0; num < rd.get_MusicName().size(); num++)
			{	
				String tp = select_directory + "\\" + rd.get_MusicName().get(num) + ".lrc";
				System.out.println(tp);
				store_lrc[num] = new ReadLrc(tp);
			}
			int q=0,w=0;
			for(int num = 0; num < rd.get_MusicName().size(); num++)
			{
				System.out.println(store_lrc[num].getMusicName());
				if(store_lrc[num].getMusicName().equals("无题"))
				{
					q++;
				}
				else w++;
			}
			System.out.println("有歌词"+ w +"首, 无歌词" + q +"首");
			read_m3ulist(mode, select_directory + "\\music.m3u");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void choose_direc()//选择文件夹后的读取文件功能，并生成播放列表
	{
		//释放上一次读取的资源
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
		//开始执行读取文件夹操作
		DirectoryChooser dire = new DirectoryChooser();
		dire.setTitle("请选择音乐所在文件夹");
		dire.setInitialDirectory(new File(System.getProperty("java.class.path")));
		File newFolder = dire.showDialog(new Stage());
		if(newFolder!=null)
//		System.out.println(newFolder);
		select_directory = newFolder.getPath();
		try {
			System.out.println("文件读取");
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
				System.out.println(tp);
				store_lrc[num] = new ReadLrc(tp);
			}
			int q=0,w=0;
			for(int num = 0; num < rd.get_MusicName().size(); num++)
			{
				System.out.println(store_lrc[num].getMusicName());
				if(store_lrc[num].getMusicName().equals("无题"))
				{
					q++;
				}
				else w++;
			}
			System.out.println("有歌词"+ w +"首, 无歌词" + q +"首");
			read_m3ulist(mode, select_directory + "\\music.m3u");

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Next()//下一首
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
	
	public void Before()//上一首
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
	
	public void PlayMusic()//播放按钮
	{
		if(hasmusic) {
			if(!isplay) {
				mp.play();
				isplay = true;
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
			
			@Override
			public void run() {
				ObservableMap<String,Object> obmap = mp3_list.get(index).getMetadata();
				System.out.println("图片：" + (Image)obmap.get("image"));
				cover.setImage((Image)obmap.get("image"));
			}
		});
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
	
}
