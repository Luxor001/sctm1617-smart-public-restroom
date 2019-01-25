package model.sensors.enumerators;

import java.util.Arrays;
import java.util.Optional;

public enum SENSOR_GENERIC {
	BOOLEAN(1),
	SCALAR(2),
	VECTOR3(3),
	IMAGE(4),
	COORDINATES(5);
	
	private final int id;
	
	
	private SENSOR_GENERIC(int id) {
		this.id=id;
		
	}
	
	public Optional<SENSOR_GENERIC> getSensorGenericById(int id)
	{
		return Arrays.stream(values()).filter(f->f.id==id).findFirst();
	}
	
	public static boolean TryParseInt(String text)
	{
		try
		{
			Integer.parseInt(text);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
	
	public static boolean TryParseBoolean(String text)
	{
		try
		{
			Boolean.parseBoolean(text);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name();
	}
}
