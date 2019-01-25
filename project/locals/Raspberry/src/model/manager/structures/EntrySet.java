package model.manager.structures;



import model.sensors.enumerators.*;


public class EntrySet {
	
	
	private String generic; 
	private SENSOR_TYPE sensor;
	private Object value;
	private String timestamp;

	protected void init(SENSOR_TYPE type, Object object)
	{
		this.sensor=type;
		this.value=object;
		this.generic=this.value.getClass().getSimpleName();
		this.timestamp=System.currentTimeMillis()+"";
	}
	
	public EntrySet(SENSOR_TYPE type,Object object)
	{
		init(type,object);
	}
	
	
	
	
	public SENSOR_TYPE getType() {return this.sensor;}
	
	public Object getObject() {return this.value;}

	
	
	
}
