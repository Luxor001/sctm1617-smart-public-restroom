package model.arduino.sensors;

import java.util.Optional;
import java.util.UUID;

import controller.arduino.io.IOComm;
import io.reactivex.disposables.Disposable;
import model.arduino.sensors.enumerator.SENSOR_COMMAND;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;

public class ArduinoWritingSensor<T extends Object> extends ArduinoSensor<T> {
	
	private T value;
	private boolean firstStep=true;
	private int lastValue=-1;
	private int percentEnabled;
	private int timeToDisable;
	private Optional<IOComm> com=Optional.empty();
	private long periodTime;
	private boolean disabled=false;
	
	/**
	 * @param id: unique id for sensor
	 * @param sensorType: type of sensor of model.sensors.enumerators.SENSOR_TYPE
	 * @param periodTime: timecycle
	 * @param percentEnabled: how much time in percentual sensor should be enabled during timecycle
	 * @param pin: pin on Arduino
	 */
	public ArduinoWritingSensor(UUID id, SENSOR_TYPE sensorType, long periodTime, int percentEnabled, int pin,String portName)
	{
		super(id,sensorType,pin,portName);
		this.periodTime=periodTime;
		this.percentEnabled=percentEnabled;
		this.timeToDisable=(int)(periodTime*percentEnabled)/100;
		this.status=SENSOR_STATUS.READY;
	}
	/**
	 * @param id: unique id for sensor
	 * @param sensorType: type of sensor of model.sensors.enumerators.SENSOR_TYPE
	 * @param periodTime: timecycle
	 * @param percentEnabled: how much time in percentual sensor should be enabled during timecycle
	 * @param pin: pin on Arduino
	 * @param fatherSensor: if is not null, this sensor will wait for fatherSensor signal
	 */
	public ArduinoWritingSensor(UUID id, SENSOR_TYPE sensorType, long periodTime, int percentEnabled, int pin,String portName, ArduinoSensor<Object> fatherSensor)
	{
		super(id,sensorType,pin,portName,fatherSensor);
		this.periodTime=periodTime;
		this.percentEnabled=percentEnabled;
		this.timeToDisable=(int)(periodTime*percentEnabled)/100;
		this.status=SENSOR_STATUS.READY;
	}

	@Override
	public void onSubscribe(Disposable d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNext(Object t) {
		// TODO Auto-generated method stub
		if (t instanceof SENSOR_COMMAND)
		{
			System.out.println("SENSED SENSOR_COMMAND");
			SENSOR_COMMAND command=(SENSOR_COMMAND) t;
			if (this.status==SENSOR_STATUS.READY)
			{
				switch(command)
				{
				case WAKE_SEND:
					changeStatus();
					break;
				case DONE:
					changeStatus();
					break;
				default:
					break;
				}
			}	
		}

	}

	private void changeStatus() {
		lastValue=lastValue==0?1:0;
		if (!com.isPresent())
			com=Optional.ofNullable(IOComm.GetInstance(new String[] {portName}));
		com.get().sendWriteRequestToArduino(pin, portName, lastValue);
		//System.out.println(String.format("%s,%d,%d,%d", portName,pin,lastValue,periodTime));
		
	}
	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}
	
	public void cycle()
	{
		if (!com.isPresent())
			com=Optional.ofNullable(IOComm.GetInstance(new String[] {portName}));
		
		switch(lastValue)
		{
		case 0:
			if (this.t1==time_start || t2 > t1+(periodTime-timeToDisable)) //ENABLED PERIOD
			{
				changeStatus();
				WakeSons();
				//System.out.println(String.format("%s,%d,%d,%d", portName,pin,lastValue,periodTime));
				t1=t2;
				//com.get().sendWriteRequestToArduino(pin,this.portName, lastValue);
			}
			break;
			
		case 1:
			if (t2>t1+timeToDisable&&lastValue==1) //DISABLED PERIOD
			{
				changeStatus();
				//System.out.println(String.format("%s,%d,%d,%d", portName,pin,lastValue,periodTime));
				t1=t2;

			}
			break;
			
		}
		
		
		
		t2=System.currentTimeMillis();
		
		//if (t2>=t1+periodTime) //NORMALIZE
		//	t1=t2;
	}

	@Override
	public void run() {
		time_start=System.currentTimeMillis();
		t1=System.currentTimeMillis();
		lastValue=0;
		do
		{
			
			cycle();
			
		}while(status.equals(SENSOR_STATUS.RUNNING) && !fatherSensor.isPresent());
		status=SENSOR_STATUS.READY;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("[%s,%s,%d] ", sensorType.toString(),status.toString(),pin);
	}

}
