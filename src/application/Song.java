package application;

public class Song {
	private String MusicName;
	private String MusicianName;
	private String TotalTime;
	private int count;
	
	public Song(String a,String b,String c, int d)
	{
		this.MusicName = a;
		this.MusicianName = b;
		this.TotalTime = c;
		this.count = d;
	}
	
	public void setMusicianName(String a)
	{
		this.MusicianName = a;
	}
	
	public void setMusicName(String a)
	{
		this.MusicName = a;
	}
	
	public void setTotal_time(String a)
	{
		this.TotalTime = a;
	}
	
	public void setCount(int a)
	{
		this.count = a;
	}

	public String getMusicName()
	{
		return this.MusicName;
	}
	
	public String getMusicianName()
	{
		return this.MusicianName;
	}
	
	public String getTotalTime()
	{
		return this.TotalTime;
	}
	
	public int getCount()
	{
		return this.count;
	}
}
