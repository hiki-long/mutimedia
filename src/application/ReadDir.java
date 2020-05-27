package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReadDir {
	private static List<String> fileList = new ArrayList<String>();
	private static List<String> music_fileList = new ArrayList<String>();
	private static String m_type[]= {"mp3","wmv","flac"};
	private static String v_type[]= {"mp4","avi","mkv" };
//	private String media_path = "D:\\java\\workspace\\player\\src\\res";
	private String m3u;
	private static List<String> music_name = new ArrayList<String>();
	private static List<String> video_name = new ArrayList<String>();
	public ReadDir()
	{
		
	}
	
	public List<String> get_MusicName()
	{
		return music_name;
	}
	
	public List<String> get_VideoName()
	{
		return video_name;
	}
	
	public List<String> get_AllMedia()
	{
		return fileList;
	}
	
	public List<String> get_AllMusicPath()
	{
		return music_fileList;
	}
	
	public void createmusicList(String path, String[] type)
	{
		try {
			m3u = path + "\\music.m3u";
			File m3u_file = new File(m3u);
//			System.out.println(m3u_file.getPath());
//			System.out.println(m3u_file.exists());
			if(m3u_file.exists())//ÿ�����붼����ˢ�²����б�
			{
				m3u_file.delete();
			}
			FileWriter fw = new FileWriter(m3u);
			BufferedWriter br = new BufferedWriter(fw);
			getAllvideo(path, m_type, v_type);
			//д���ȡ
			for (Iterator<String> it = fileList.iterator(); it.hasNext();)
			{
				String str = (String)it.next();
				br.write(str);
				br.newLine();
			}
//			�������
//			for (Iterator<String> it = fileList.iterator(); it.hasNext();)
//			{
//				System.out.println((String)it.next());
//			}
			br.close();
//			System.out.println("д�����");
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public static void getAllvideo(String path,String[] m_type,String[] v_type)
	{
		File file = new File(path);
		File[] tempFile = file.listFiles();
		for(int i = 0; i < tempFile.length; i++)
		{
			if(tempFile[i].isDirectory())
			{
				getAllvideo(tempFile[i].getPath(),m_type ,v_type);
			}
			else {
					if(tempFile[i].getName().endsWith(m_type[0])
							||tempFile[i].getName().endsWith(m_type[1])
							||tempFile[i].getName().endsWith(m_type[2]))
					{
						//��Ƶ�ļ���ȥ��׺
						fileList.add(tempFile[i].getPath());
						music_fileList.add(tempFile[i].getPath());
						//���ｫmp3�Ⱥ�׺ȥ��
						String temp[] = tempFile[i].getName().split("\\.");
//						System.out.println(temp[0]);
						music_name.add(temp[0]);
					}
					if(tempFile[i].getName().endsWith(v_type[0])
							||tempFile[i].getName().endsWith(v_type[1])
							||tempFile[i].getName().endsWith(v_type[2]))
					{
						//��Ƶ�ļ���ȥ��׺
						fileList.add(tempFile[i].getPath());
						String temp[] = tempFile[i].getName().split("\\.");
						video_name.add(temp[0]);
					}
			}
		}
	}
	
	public void getClear()
	{//���¶�ȡĿ¼�ĳ�ʼ��
		fileList.clear();
		music_name.clear();
		video_name.clear();
	}
}
