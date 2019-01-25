package controller.arduino.io.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controller.arduino.io.ArduinoRxTx;
import controller.arduino.io.IOComm;
import model.arduino.sensors.ArduinoSensor;
import model.manager.ISensorsManager;
import model.manager.SensorsManager;

public class ArduiniRxTxTest {
	ArduinoRxTx instance;
	IOComm com;
	int port;
	@Before
	public void Init()
	{
		instance=ArduinoRxTx.GetIstance("COM3");
		
	}
	
	@Test
	public void blinkLed()
	{
		GetMapping();
		
		for(int i=0;i<10; i++)
		{
			try
			{
				instance.sendCommand(String.format("WRITE,%d,%d", port,i%2==0?1:0));
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	@Before
	@Test
	public void InstanceCreation()
	{
		
	}
	
	
	
	
	@Test
	public void GetMapping()
	{
		boolean startMapping=false,endMapping=false;
		instance.sendCommand("GET_MAPPING");
		int attempt=0;
		Optional<String> message=Optional.empty();
		while(attempt<30 && !endMapping )
		{
			try
			{
				message=instance.getOldestMessage();
				if (message.isPresent())
				{
					if (message.get().contains("GET_MAPPING_BEGIN"))
						startMapping=true;
					if (startMapping && !endMapping && message.get().split(",").length==3 && message.get().split(",")[1].equals("LED_BUILTIN"))
					{
						System.out.println("FOUND");
						this.port=Integer.parseInt(message.get().split(",")[0]);
					}
					if (startMapping && message.get().contains("GET_MAPPING_END"))
						endMapping=true;
						
				}
				attempt++;
				Thread.sleep(100);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		Assert.assertTrue(this.port!=0);
		
	}
	
	
	
	
	
	@Before
	public void IOCommTest()
	{
		com=IOComm.GetInstance(new String[] {"COM3"});
		com.setLog(true);
		
		
		
	}
	
		
		
	
	
	

}
