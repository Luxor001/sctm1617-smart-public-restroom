package controller.main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import model.RestroomSM.main.RestroomSMProperties;
import model.manager.FakeRestroomSM;
import model.manager.RestroomSM;

public class Start {
	
	private static RestroomSM restroomSM;
	
	
	
	
	
	
	
	public static void main(String [] args)
	{
		try {
			restroomSM=new RestroomSM(0, RestroomSMProperties.GetInstance().getTtd(), RestroomSMProperties.GetInstance().getPortNames(), false);
			restroomSM.Start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

}
