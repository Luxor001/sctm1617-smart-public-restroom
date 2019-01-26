package model.arduino.sensors.enumerator;

import java.util.Arrays;
import java.util.Optional;

public enum SENSOR_COMMAND {
	/*
	 * TO SEND A WRITE REQUEST COMMAND
	 */
	SEND(0),
	/*
	 * TO SEND A READ REQUEST COMMAND
	 */
	RECEIVE(1),
	/*
	 * TO ENABLE AN ARDUINO SENSOR OBSERVER TO SEND REQUEST
	 */
	WAKE_SEND(2),
	/*
	 * TO COMPLETE THE ARDUINO SEND REQUEST
	 */
	DONE(3);
	private final int value;
	SENSOR_COMMAND(int id)
	{
		value=id;
	}
	
	public Optional<String> getCommandString(SENSOR_COMMAND command)
	{
		switch(command)
		{
		case SEND:
			return Optional.of("Write");
			
		case RECEIVE:
			return Optional.of("Send");
			
		case WAKE_SEND:
			return Optional.of("Wake");
		case DONE:
			return Optional.of("Done");
		default: return Optional.empty();
		}
	}
	
	public Optional<SENSOR_COMMAND> getById(int id)
	{
		return Arrays.stream(values()).filter(enumerator->enumerator.value==id).findFirst();
	}
	
	public static boolean TryParse(String text)
	{
		return Arrays.stream(values()).filter(enumerator->enumerator.toString().equals(text)).findAny().isPresent();
	}
	
	

}
