package controller.arduino.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import akka.dispatch.sysmsg.LatestFirstSystemMessageList;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.RestroomSM.main.RestroomSMProperties;
import model.arduino.sensors.ArduinoReadingSensor;
import model.arduino.sensors.ArduinoSensor;
import model.arduino.sensors.ArduinoWritingSensor;
import model.sensors.ISensor;
import model.sensors.enumerators.SENSOR_GENERIC;
import model.sensors.enumerators.SENSOR_TYPE;

public class IOComm implements Observer<String> {
	private static Optional<IOComm> ioComm = Optional.empty();

	/***
	 * HASHMAP TO IDENTIFY ARDUINO SENSORS KEY: PIN OF ARDUINO SENSOR VALUE: ARDUINO
	 * SENSOR
	 */
	//private HashMap<Integer, ArduinoSensor<Object>> arduinoSensors;

	/***
	 * CLASS FOR COMMUNICATING WITH ARDUINO PHISICAL DEVICE
	 */
	private HashMap<String,ArduinoRxTx> rxtxDevices=new HashMap<String,ArduinoRxTx>(); //key: PORT NAME
	private HashMap<String,HashMap<Integer,ArduinoSensor<Object>>> devicesSensors=new HashMap<String,HashMap<Integer,ArduinoSensor<Object>>>(); //key: PORT NAME k-key: arduino port number
	private boolean isConnected = false;
	private boolean log=false;

	public static IOComm GetInstance(String[] arduinoPorts) {
		if (!ioComm.isPresent())
			ioComm = Optional.ofNullable(new IOComm(arduinoPorts));
		return ioComm.get();
	}

	private boolean mappingBegin(ArduinoRxTx arduinoRxTx) {
		int attempt = 0;

		try {
			Optional<String> message = Optional.empty();
			arduinoRxTx.sendCommand("GET_MAPPING");
			while (attempt < 10) {
				message = arduinoRxTx.getOldestMessage();
				if (message.isPresent() && message.get().contains("GET_MAPPING_BEGIN"))
				{
					String[] splitted= message.get().split(":");
					arduinoRxTx.setDeviceName(splitted[splitted.length-1]);
					return true;
				}
				Thread.sleep(200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		log("Mapping failed",true);
		return false;

	}

	@SuppressWarnings("unchecked")
	private void createArduinoSensor(String[] sensorsInfo,String portName) {

		if (!devicesSensors.containsKey(portName))
			devicesSensors.put(portName, new HashMap<Integer,ArduinoSensor<Object>>());
		
		
		int pin = 0;
		String sensorName, sensorPortType;

		try {
			int randomTimeSending = RestroomSMProperties.GetInstance().getRandomTime();
			pin = Integer.parseInt(sensorsInfo[0]);
			sensorName = sensorsInfo[1];
			sensorPortType = sensorsInfo[2];
			log("CREATING SENSOR OF TYPE " + sensorName + " ON PIN " + pin,false);
			Optional<SENSOR_TYPE> sensorType = SENSOR_TYPE.GetByName(sensorName);
			Random random = new Random();
			if (sensorType.isPresent()) {

				ArduinoSensor<? extends Object> sensor;
				if (sensorPortType.startsWith("O_")) {
					if (sensorPortType.contains("ANL") || sensorPortType.contains("REAL"))
						sensor = new ArduinoReadingSensor(UUID.randomUUID(),SENSOR_GENERIC.SCALAR, sensorType.get(),
								(long) 1000 + Math.abs(random.nextLong()) % randomTimeSending, pin,portName);
					else
						sensor = new ArduinoReadingSensor(UUID.randomUUID(),SENSOR_GENERIC.BOOLEAN, sensorType.get(),
								(long) 1000 + Math.abs(random.nextLong()) % randomTimeSending, pin,portName);
				} else
					sensor = new ArduinoWritingSensor<Object>(UUID.randomUUID(), sensorType.get(),
							1000 + Math.abs(random.nextLong()) % randomTimeSending, 60, pin,portName);
				
				devicesSensors.get(portName).put(pin, (ArduinoSensor<Object>)sensor);
					
			} else
				log("CANNOT CREATING SENSOR WITH PIN " + pin,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean getMapping(ArduinoRxTx arduinoRxTx) {
		Optional<String> message = arduinoRxTx.getOldestMessage();
		List<String> messages = new ArrayList<String>();
		try {
			while (true) {
				message = arduinoRxTx.getOldestMessage();
				if (message.isPresent()) {
					if (message.get().contains("GET_MAPPING_END"))
						break;
					else
						messages.add(message.get());

				}
			}
			messages.forEach(string -> {
				String[] splitted = string.split(",");
				if (splitted.length != 3)
				{
					System.out.println(string);
					log(string,false);
					throw new FormatterClosedException();
				}
				createArduinoSensor(string.split(","),arduinoRxTx.getPortName());
			});

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private boolean mapping(ArduinoRxTx arduinoRxTx) {
		if (mappingBegin(arduinoRxTx))
			return getMapping(arduinoRxTx);
		
		return false;
	}

	private void init(String[] arduinoPorts) {
		
		for(String arduinoPort : arduinoPorts)
		{
			ArduinoRxTx arduinoRxTx = ArduinoRxTx.GetIstance(arduinoPort);
			
			isConnected = mapping(arduinoRxTx);
			if (isConnected) {
				arduinoRxTx.subscribeToReceiver(this);
				if (!rxtxDevices.containsKey(arduinoPort))
					rxtxDevices.put(arduinoPort, arduinoRxTx);
			}
		}

	}

	private IOComm(String[] arduinoPorts) {
		init(arduinoPorts);

	}

	public synchronized void sendWriteRequestToArduino(int pin,String portName, int value) {
		if (isConnected && (value == 0 || value == 1)) {
			String message = String.format("WRITE,%d,%d", pin, value);
			rxtxDevices.get(portName).sendCommand(message);
		} else
			log(String.format("CANNOT SEND REQUEST FOR PIN %d", pin),true);
	}

	public synchronized void sendReadRequestToArduino(int pin,String portName) {

		log(String.format("ARDUINO SENSOR CONNECTED TO %d WANTS TO READ", pin),false);
		if (isConnected) {
			String message = String.format("READ,%d", pin);
			rxtxDevices.get(portName).sendCommand(message);
		} else
			log("Cannot send request",true);
	}

	public HashMap<String,HashMap<Integer,ArduinoSensor<Object>>> getSensors() {
		return devicesSensors;
	}

	

	@Override
	public void onSubscribe(Disposable d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNext(String t) {
		try {
			log(t, false);
			String[] splitted = t.split(",");
			if (splitted.length != 4 && !splitted[1].equals("READ"))
				throw new Exception();
			devicesSensors.get(splitted[0]).get(Integer.parseInt(splitted[2])).onNext((Object) splitted[3]);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}

	public void log(String message, boolean err) {
		if (log)
		{
			if (!err)
				System.out.println(String.format("[IOComm] %s", message));
			else
				System.err.println(String.format("[IOComm] %s", message));
		}
	}

	public boolean isLog() {
		return log;
	}

	public void setLog(boolean log) {
		this.log = log;
	}
	
	public String getDeviceNameByPort(String portName)
	{
		return rxtxDevices.get(portName).getDeviceName();
	}
	
	public List<String> getDeviceNames()
	{
		return rxtxDevices.values().stream().map(arduinoRxTx->arduinoRxTx.getDeviceName()).collect(Collectors.toList());
	}
	
	public HashMap<Integer,ArduinoSensor<Object>> getSensorsMapByPortName(String portName)
	{
		return devicesSensors.get(portName);
	}

}
