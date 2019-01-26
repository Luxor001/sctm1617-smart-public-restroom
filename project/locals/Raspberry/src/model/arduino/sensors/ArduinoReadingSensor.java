package model.arduino.sensors;

import java.util.Optional;
import java.util.UUID;

import com.google.gson.Gson;


import controller.arduino.io.IOComm;
import io.reactivex.disposables.Disposable;

import model.arduino.sensors.enumerator.SENSOR_COMMAND;
import model.manager.structures.EntrySet;
import model.manager.structures.RMEntrySet;

import model.sensors.enumerators.SENSOR_GENERIC;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;

public class ArduinoReadingSensor extends ArduinoSensor<Object>{

	
	protected long timeForCapture;
	private Optional<Object> lastValue=Optional.empty();
	private Optional<IOComm> com=Optional.empty();
	private SENSOR_GENERIC generic;
	
	
	
	
	public ArduinoReadingSensor(UUID id,SENSOR_GENERIC generic, SENSOR_TYPE sensorType,long timeForCapture,int pin,String portName)
	{
		super(id,sensorType,pin,portName);
		this.status=SENSOR_STATUS.READY;
		this.generic=generic;
		this.timeForCapture=timeForCapture;
		
	}
	
	public ArduinoReadingSensor(UUID id,SENSOR_GENERIC generic, SENSOR_TYPE sensorType, long timeForCapture, int pin,String portName, ArduinoSensor<Object> father)
	{
		super(id,sensorType,pin,portName,father);
		this.status=SENSOR_STATUS.READY;
		this.generic=generic;
		this.timeForCapture=timeForCapture;
	}
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see model.sensors.AbstractSensor#run()
	 * Runs the sensor thread only if sensor isn't a dependant sensor!
	 */
	@Override
	public void run() {
		if (!fatherSensor.isPresent())
		{
			this.time_start=System.currentTimeMillis();
			this.t1=this.time_start;
			this.t2=this.time_start;
			this.log("Running");
			try
			{
				while(this.status==SENSOR_STATUS.RUNNING)
				{
					
					
					if(!updateObserver(new RMEntrySet(0,portName,pin,this.getSensorType(),lastValue)))
						break;
					
						
					t2=System.currentTimeMillis();
					Thread.sleep(timeForCapture);
					if (!com.isPresent())
						com=Optional.ofNullable(IOComm.GetInstance(new String[] {portName}));
					com.get().sendReadRequestToArduino(this.pin,this.portName);
					Thread.sleep(timeForCapture);
					
				}
				log("Running complete!");
				setStatus(SENSOR_STATUS.READY);
			}
			catch(Exception e)
			{
				log("Error while running: "+e.getMessage());
				e.printStackTrace();
				setStatus(SENSOR_STATUS.DAMAGED);
			}
			
		}
	}



	@Override
	public void onSubscribe(Disposable d) {
		
		
	}



	@Override
	public void onNext(Object t) {
		
		//C'è DA SISTEMARE IL TIPO, ENTRA SEMPRE UN TIPO STRING
		
		if (!(t instanceof SENSOR_COMMAND))
		{
			if (generic.equals(SENSOR_GENERIC.SCALAR))
			{
				System.out.println((String)t);
				Double d=Double.parseDouble(((String)t).trim());
				
				lastValue=Optional.ofNullable(d);
				log(String.format("It was a scalar, %s", lastValue));
			}
			else if (generic.equals(SENSOR_GENERIC.BOOLEAN))
			{
				Boolean b=Boolean.parseBoolean((String)t);
				lastValue=Optional.ofNullable(b);
				log(String.format("It was a boolean, %s", lastValue));
			}
			if (!updateObserver(new RMEntrySet(0,portName,pin,this.getSensorType(),lastValue)))
				this.status=SENSOR_STATUS.STOP;
			WakeSons();
			SleepFathers();
			
		}
		else
		{
			SENSOR_COMMAND command=(SENSOR_COMMAND) t;
			if (command.equals(SENSOR_COMMAND.WAKE_SEND) && this.status==SENSOR_STATUS.READY)
			{
				if (!com.isPresent())
					com=Optional.ofNullable(IOComm.GetInstance(new String[] {portName}));
				com.get().sendReadRequestToArduino(this.pin,this.portName);
			}
		}
			
		
	}



	@Override
	public void onError(Throwable e) {
		this.status=SENSOR_STATUS.DAMAGED;
		
	}



	@Override
	public void onComplete() {
		
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("[%s,%s,%d,%s] ", sensorType.toString(),status.toString(),pin,lastValue.isPresent()?lastValue.get():"NOT UPDATED YET");
	}



	

}
