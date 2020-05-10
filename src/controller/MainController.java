package controller;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
	// TODO Auto-generated constructor stub
	public MainController()
	{
		
	}
	
	@FXML
	private Button prev;
	
	@FXML
	public void initialize()
	{
		System.out.println("实例化测试");
		
	}
	
	public void click()
	{
		System.out.println("上一首被点击");
	}
	
	
}
