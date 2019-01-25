package controller.arduino.io.tests;





import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.NEW;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.rest.observer.RestClient;
import io.rest.observer.RestroomDataOutput;
import controller.arduino.io.IOComm;
import model.manager.FakeRestroomSM;
import model.manager.RestroomSM;
import model.manager.structures.restroom.RestroomDataStructure;
import model.manager.structures.restroom.RestroomInfo;

public class FakeArduinoIOComm {
	
	private FakeRestroomSM sm;
	private Properties prop=new Properties();
	
	
	@Before
	public void InitProperties()
	{
		try
		{
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(100);
		}
		
	}
	

	@Test
	public void CreateStructure(){
		
		sm=new FakeRestroomSM(0,2000,false);
		//sm.SetRestroomInfo("Via Fornaci 7/A", new double[] {44.1429691,12.239455}, "Belli&Vecchiotti Srl", "ArduinoDueMega2560");
		sm.SetRestroomInfo(prop);
		
		Observer<Object> observer=new Observer<Object>() {
			
			@Override
			public void onSubscribe(Disposable d) {
				// TODO Auto-generated method stub
				System.out.println("Subscribed");
			}
			
			@Override
			public void onNext(Object object) {
				// TODO Auto-generated method stub
				RestroomInfo t=null;
				if (object instanceof RestroomInfo )
					 t= (RestroomInfo) object;
				System.out.println(new Gson().toJson(t));
				
				
			}
			
			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete() {
				
				
			}
		};
		
		sm.subscribe(observer);
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		observer.onComplete();
		
		
	}
	
	@Test
	public void TestClient()
	{
		sm=new FakeRestroomSM(0,5000,false);
		//sm.SetRestroomInfo("Via Fornaci 7/A", new double[] {44.1429691,12.239455}, "Belli&Vecchiotti Srl", "ArduinoDueMega2560");
		sm.SetRestroomInfo(prop);
		try
		{
			RestroomDataOutput<Object> output=new RestroomDataOutput<Object>(sm);
			output.subscribeToFakeRestroom();
			Thread.sleep(1000000);
			output.onComplete();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Stopped");
		try
		{
			Thread.sleep(10000);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
