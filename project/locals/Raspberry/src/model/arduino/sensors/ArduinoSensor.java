package model.arduino.sensors;
import java.util.Optional;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.Observer;
import model.arduino.sensors.enumerator.SENSOR_COMMAND;
import model.manager.structures.EntrySet;
import model.sensors.AbstractSensor;
import model.sensors.enumerators.SENSOR_GENERIC;
import model.sensors.enumerators.SENSOR_TYPE;

public abstract class ArduinoSensor<T> extends AbstractSensor<T> implements Observer<T>{
	/***
	 * Arduino Pin port
	 */
	int pin=0;
	/**
	 * Arduino port name
	 */
	String portName="";
	
	protected SENSOR_GENERIC generic;
	protected long time_start, t1,t2;
	protected Optional<Observable<Object>> fatherSensor=Optional.empty();
	protected Optional<Observer<Object>> sonSensor=Optional.empty();

	public ArduinoSensor(UUID id, SENSOR_TYPE sensorType,int pin,String portName) {
		super(id, sensorType);
		this.pin=pin;
		this.portName=portName;
		// TODO Auto-generated constructor stub
		log("INITIALIZED ARDUINO SENSOR ON PIN "+this.pin);
	}
	
	@SuppressWarnings("unchecked")
	public ArduinoSensor(UUID id, SENSOR_TYPE sensorType, int pin,String portName ,ArduinoSensor<Object> fatherSensor)
	{
		super(id,sensorType);
		this.pin=pin;
		this.portName=portName;
		fatherSensor.setSon((Observer<Object>)this);
		this.fatherSensor=Optional.ofNullable(fatherSensor);
		this.fatherSensor.get().subscribe((Observer<Object>)this);
		
	}
	
	public void setDependency(ArduinoSensor<Object> fatherSensor)
	{
		
		fatherSensor.setSon((Observer<Object>)this);
		this.fatherSensor=Optional.ofNullable(fatherSensor);
		this.fatherSensor.get().subscribe((Observer<Object>)this);
	}
	
	protected void WakeSons()
	{
		if (sonSensor.isPresent())
			sonSensor.get().onNext(SENSOR_COMMAND.WAKE_SEND);
	}
	
	protected void SleepSons()
	{
		if (sonSensor.isPresent())
			sonSensor.get().onNext(SENSOR_COMMAND.DONE);
	}
	
	protected void WakeFathers()
	{
		if (fatherSensor.isPresent())
			((ArduinoSensor<Object>)fatherSensor.get()).onNext(SENSOR_COMMAND.WAKE_SEND);
	}
	
	protected void SleepFathers()
	{
		if (fatherSensor.isPresent())
			((ArduinoSensor<Object>)fatherSensor.get()).onNext(SENSOR_COMMAND.DONE);
	}
	
	

	public int getPin() {return pin;}
	
	public String getPortName() {return this.portName;}
	
	private void setSon(Observer<Object> son)
	{
		this.sonSensor=Optional.ofNullable(son);
	}
	
	
	@Override
	protected void subscribeActual(Observer<? super Object> observer) {
		
			super.subscribeActual(observer);
	}

	
}
