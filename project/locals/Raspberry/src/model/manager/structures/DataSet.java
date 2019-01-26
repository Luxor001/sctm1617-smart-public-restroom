package model.manager.structures;
import model.sensors.enumerators.*;
import model.structures.Vector3;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Optional;

import com.google.gson.Gson;

public class DataSet {
	
	
	private HashMap<SENSOR_TYPE,Object> data=new HashMap<SENSOR_TYPE,Object>();
	private final long sensorManagerId;
	
	public DataSet(long sensorManagerId) {
		this.sensorManagerId=sensorManagerId;
	}
	
	public DataSet(long sensorManagerId,ArrayList<SENSOR_TYPE> typeList) {
		this.sensorManagerId=sensorManagerId;
		for(SENSOR_TYPE type:typeList)
		{
			data.put(type, Optional.empty());
		}
	}

	
	public void addSensorType(SENSOR_TYPE sensorType)
	{
		if (!data.containsKey(sensorType))
			data.put(sensorType, Optional.empty());
		else
			System.err.println("[DATASET] The sensor type is already in there!");
	}
	
	public void removeSensorType(SENSOR_TYPE sensorType)
	{
		if (data.containsKey(sensorType))
			data.remove(sensorType, data.get(sensorType));
		else
			System.err.println("[DATASET] The sensor type not exists");
	}
	
	public long getSensorManagerId()
	{
		return sensorManagerId;
	}
	
	public Optional<Object> getDataBySensorType(SENSOR_TYPE type)
	{
		return Optional.ofNullable(data.get(type));
	}
	
	
	public void updateData(SENSOR_TYPE type, Object value)
	{
		this.data.replace(type, value);
		
	}
	
	
	@Override
	public String toString() {
		StringBuilder stringBuilder=new StringBuilder();
		
		try
		{
			
			stringBuilder.append("{\r\n");
			for(Entry<SENSOR_TYPE,Object> set: data.entrySet())
			{
				SENSOR_TYPE key=set.getKey();
				Object value=(Object)set.getValue();
				if (value instanceof Double)
				{
					double doubleValue=((Double)value).doubleValue();
					stringBuilder.append(String.format("[%s:%f]\r\n", key.toString(),doubleValue));
				}
				else if (value instanceof Vector3)
				{
					Vector3 vector=(Vector3)value;
					stringBuilder.append(String.format("[%s:%f|%f|%f]\r\n", key.toString(),
							vector.x,
							vector.y,
							vector.z));
				}
				else throw new Exception("Type "+value.getClass().getSimpleName()+" not implemented yet");
			}
			
			
		}
		catch(Exception e)
		{
			stringBuilder.append("Type data not implemented yet! "+e.getMessage());
		}
		finally
		{
			stringBuilder.append("\r\n}");
		}
		return stringBuilder.toString();
	}
	
	
	
	
	
	
	
	
	
	
	

}
