package controller.arduino.io.tests;





import org.junit.Test;


import com.google.gson.Gson;


import io.reactivex.Observer;

import io.reactivex.disposables.Disposable;


import controller.arduino.io.IOComm;
import model.RestroomSM.main.RestroomSMProperties;
import model.manager.RestroomSM;
import model.manager.structures.restroom.RestroomDataStructure;

public class ArduinoIOComm {
	private IOComm com;
	private RestroomSM sm;
	
	
	

	@Test
	public void CreateStructure(){
		
		
		
		
		try {
			sm=new RestroomSM(0,RestroomSMProperties.GetInstance().getTtd(),new String[] {"COM4","COM5"},true);
			Thread.sleep(2000);
			sm.Start();
			Thread.sleep(20000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
