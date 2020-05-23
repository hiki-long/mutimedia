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

//	public String lrc_path = "D:\\java\\workspace\\player\\src\\res\\����� - �����.lrc";
	public String lrc_path;//��������·��
	private String[] base_info = new String[2];//����������
	private String filename;//Ŀǰ���ŵĸ���
	private int played_time;//���Ŵ���
	private boolean haslrc;//�Ƿ��и��
//	private List<Map<Long,String>> lyric;
	Map<Long,String> lyric_map = new HashMap<Long,String>();//��ʴ洢��ϣ��
	
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
	
	
	
	public Map<Long,String> parse(String path)//��ȡ���ָ��
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
						String min = matcher.group(1);//����
						String sec = matcher.group(2);//��
						String mill = matcher.group(3);//10����
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
				System.out.println("û���ҵ�ָ����lrc�ļ�,������Ϊ������");
				filename = "";
				haslrc = false;
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isPureMusic()//�Ƿ�Ϊ������
	{
		return haslrc;
	}
	
	public String getSingerName()//�����ݳ�������
	{
		return this.base_info[0];
	}
	
	public String getMusicName()//���ظ�������
	{
		return this.base_info[1];
	}
	
	public Map<Long,String> getLyric()//���ظ��
	{
		return this.lyric_map;
	}
	
	public int get_playtime()//��ȡ���Ŵ���
	{
		return this.played_time;
	}
	
	public void set_playtime(int time)
	{
		this.played_time = time;
	}
	
	private long getLongTime(String min, String sec, String mill)
	{
		int m = Integer.parseInt(min);//����
		int s = Integer.parseInt(sec);//����
		int ms = Integer.parseInt(mill);//10ms��
		if(s >= 60)
		{
			System.out.println("��ʲ�Ӧ��ʱ������60����");
		}
		long time = m*60*1000 + s*1000 + 10*ms;
		return time;
	}
	
	
	public void printLrc(Map<Long,String> map)//���Ը���Ƿ����
	{
		if(map == null || map.isEmpty())
		{
			System.out.println("��ǰΪ������");
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
//					System.out.println("ʱ��:" + entry.getKey() + "  \t���:" + entry.getValue());
//				}
//			}
//			System.out.println(map.size());
//			System.out.println(map.get((long)81070));
		}
	}
	
	
	public String[] get_info()//��ȡ���ֻ�����Ϣ���ݳ��ߺ����ֱ���
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
		else return new String[]{"δ֪����","����"};
	}
	
}