package model.manager.structures;

import model.sensors.enumerators.SENSOR_TYPE;

public class RMEntrySet extends EntrySet {
	
	private int restroomSensorId=0; //TIED TO RESTROOM BUSINESS ONLY
	private String portName="";
	private int pin=0;
	
	public RMEntrySet(int restroomSensorId,int pin,SENSOR_TYPE type, Object object)
	{
		super(type,object);
		super.init(type,object);
		this.restroomSensorId=restroomSensorId;
		this.pin=pin;
	}
	
	public RMEntrySet(int restroomSensorId,String portName,int pin,SENSOR_TYPE type, Object object)
	{
		super(type,object);
		super.init(type, object);
		this.restroomSensorId=restroomSensorId;
		this.portName=portName;
		this.pin=pin;
	}
	
	
	public int getRestroomSensorId() {
		return restroomSensorId;
	}

	public void setRestroomSensorId(int restroomSensorId) {
		this.restroomSensorId = restroomSensorId;
	}
	
	public String getPortName()
	{
		return portName;
	}
	
	public int getPin() {return pin;}

	

}
