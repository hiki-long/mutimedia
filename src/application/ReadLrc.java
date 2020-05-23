package application;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.image.Image;
import jdk.jfr.Unsigned;

public class ReadLrc {

//	public String lrc_path = "D:\\java\\workspace\\player\\src\\res\\肥皂菌 - 万神纪.lrc";
	public String lrc_path;//歌曲所在路径
	private String[] base_info = new String[2];//仅保留名字
	private String filename;//目前播放的歌名
	private int played_time;//播放次数
	private boolean haslrc;//是否有歌词
//	private List<Map<Long,String>> lyric;
	Map<Long,String> lyric_map = new HashMap<Long,String>();//歌词存储哈希表
	
	public ReadLrc()
	{
		
	}
	
	public ReadLrc(String lrc)
	{
		this.lrc_path = lrc;
		int index;
		index = lrc.lastIndexOf("\\");
		if(index == -1)
			index = lrc.lastIndexOf("/");
		if(index != -1)
		filename = lrc_path.substring(index+1);
		else filename = "";
		lyric_map = this.parse(lrc_path);
		base_info = this.get_info();
		played_time = 0;
	}
	
	
	
	public Map<Long,String> parse(String path)//获取音乐歌词
	{
//		List<Map<Long,String>> list = new ArrayList<Map<Long,String>>();
		try {
			String encoding = "utf-8";
			File file = new File(path);
			if(file.isFile()&&file.exists())
			{
				haslrc = true;
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file),encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String regex = "\\[(\\d{1,2}):(\\d{1,2}).(\\d{1,2})\\]";
				Pattern pattern = Pattern.compile(regex);
				String lineStr = null;
				while((lineStr = bufferedReader.readLine())!= null)
				{
					Matcher matcher = pattern.matcher(lineStr);
					while(matcher.find()) {						
						String min = matcher.group(1);//分钟
						String sec = matcher.group(2);//秒
						String mill = matcher.group(3);//10毫秒
						long time = getLongTime(min,sec,mill);
						String text = lineStr.substring(matcher.end());
						lyric_map.put(time,text);
					}				
				}

				read.close();
				bufferedReader.close();
				return lyric_map;
			}
			else {
				System.out.println("没有找到指定的lrc文件,该音乐为纯音乐");
				filename = "";
				haslrc = false;
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isPureMusic()//是否为纯音乐
	{
		return haslrc;
	}
	
	public String getSingerName()//返回演唱者姓名
	{
		return this.base_info[0];
	}
	
	public String getMusicName()//返回歌曲名称
	{
		return this.base_info[1];
	}
	
	public Map<Long,String> getLyric()//返回歌词
	{
		return this.lyric_map;
	}
	
	public int get_playtime()//获取播放次数
	{
		return this.played_time;
	}
	
	public void set_playtime(int time)
	{
		this.played_time = time;
	}
	
	private long getLongTime(String min, String sec, String mill)
	{
		int m = Integer.parseInt(min);//分钟
		int s = Integer.parseInt(sec);//秒数
		int ms = Integer.parseInt(mill);//10ms数
		if(s >= 60)
		{
			System.out.println("歌词不应该时长超过60分钟");
		}
		long time = m*60*1000 + s*1000 + 10*ms;
		return time;
	}
	
	
	public void printLrc(Map<Long,String> map)//测试歌词是否读入
	{
		if(map == null || map.isEmpty())
		{
			System.out.println("当前为纯音乐");
		}
		else {
			for(Map.Entry<Long,String> iterate : map.entrySet())
			{
				System.out.println(iterate.getKey() + "\t" + iterate.getValue());
			}
//			for(Map<Long,String> map : list)
//			{
//				for(Entry<Long,String> entry : map.entrySet()) 
//				{
//					System.out.println("时间:" + entry.getKey() + "  \t歌词:" + entry.getValue());
//				}
//			}
//			System.out.println(map.size());
//			System.out.println(map.get((long)81070));
		}
	}
	
	
	public String[] get_info()//获取音乐基本信息，演唱者和音乐标题
	{
		String mname = filename;
		if(!filename.isEmpty())
		{
			String info[] = mname.split("-");
			String val[] = new String[2];
			val[0] = info[0].trim();
			info = info[1].trim().split("\\.");
			val[1] = info[0];
			return val;
		}
		else return new String[]{"未知歌手","无题"};
	}
	
}