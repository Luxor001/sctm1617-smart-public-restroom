package model.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.swing.event.CellEditorListener;

import com.google.gson.Gson;

import akka.util.Index;
import controller.arduino.io.IOComm;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.RestroomSM.main.RestroomSMProperties;
import model.arduino.sensors.ArduinoSensor;
import model.manager.structures.DataSet;
import model.manager.structures.RMEntrySet;
import model.manager.structures.restroom.RestroomCellProperties;
import model.manager.structures.restroom.RestroomDataStructure;
import model.manager.structures.restroom.RestroomInfo;
import model.sensors.ISensor;
import model.sensors.enumerators.SENSOR_TYPE;
import model.structures.IntervalInteger;
import io.rest.*;
import io.rest.observer.RestClient;
import io.rest.observer.RestResponse;


/**
 * @author andre
 * A CUSTOM SENSOR MANAGER USED BY SMARTCITY PROJECT
 * THIS CODE IS STRICTLY TIED TO SMARTCITY PROJECT BUSINESS LOGIC
 */
public class RestroomSM extends AbstractSensorManager<RestroomInfo> implements ISensorsManager{
	
	private boolean log;
	private RestroomDataStructure data;
	private RestroomInfo info;
	private RestClient client;
	private RestroomSMProperties properties;
	private long t1=System.currentTimeMillis(),t2,timeToDeliver;
	
	
	/**
	 * It maps restroom cells data to arduino port names
	 */
	private HashMap<String,Integer> devicePortToCellMap=new HashMap<String,Integer>();
	
	/**
	 * It maps restroom 
	 */
	private HashMap<String,Integer> devicePortToSinkMap = new HashMap<String,Integer>();
	
	public IOComm arduinoIOComm;
	
	

	
	public RestroomSM(long id,long timeToDeliver,String[] serialPorts, boolean log) throws Exception
	{
		super(id,log);
		this.timeToDeliver=timeToDeliver;
		this.log=log;
		properties= RestroomSMProperties.GetInstance();
		client=new RestClient(RestroomSMProperties.GetInstance().getUrl());
		
		info = RestroomSMProperties.GetInstance().getRestroomInfo();
		init(serialPorts);
		
		

	}
	/*private void initializeSensorsMap()
	{
		sensorsMap=new HashMap<SENSOR_TYPE,ISensor[]>();
		sensorsMap.put(SENSOR_TYPE.MOTION_SENSOR, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.WATER_LEVER_SENSOR, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.BUTTON, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.IR_RECEIVER, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.TEMP_AND_HUMIDITY, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.PRESSURE, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.ULTRASONIC, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.IR_EMISSION, new ISensor[3]);
		sensorsMap.put(SENSOR_TYPE.LIGHT, new ISensor[3]);
	}*/
	
	
	
	private void init(String[] serialPorts)
	{
		arduinoIOComm=IOComm.GetInstance(serialPorts);
		
		TreeMap<String,String> cellsPortNames=new TreeMap<String,String>();
		TreeMap<String,String> sinksPortNames=new TreeMap<String,String>();
		for(String serialPort : serialPorts)
		{
			
			String deviceName=arduinoIOComm.getDeviceNameByPort(serialPort).toUpperCase();
			if (deviceName.contains("CELL"))
			{
				cellsPortNames.put(deviceName, serialPort);
			}
			else if (deviceName.contains("SINK"))
			{
				sinksPortNames.put(deviceName,serialPort);
			}
		}
		
		//INIT RDS with right count of cells and sinks
		this.data=new RestroomDataStructure(cellsPortNames.size(), sinksPortNames.size());
		
		//MAP for each cell and sink the right data substructure
		for(int index=0; index<cellsPortNames.size(); index++)
			devicePortToCellMap.put((String)cellsPortNames.values().toArray()[index], index);
		
		for(int index=0; index<sinksPortNames.size(); index++)
			devicePortToSinkMap.put((String)sinksPortNames.values().toArray()[index], index);
		
		SetSensorsFathers();
		
		
	}
	
	private void SetSensorsFathers()
	{
		for(String portName : devicePortToCellMap.keySet())
		{
			arduinoIOComm.getSensorsMapByPortName(portName).values().stream()
				.filter(sensor->sensor.getSensorType().equals(SENSOR_TYPE.PHOTO_RESISTOR))
				.findFirst()
				.get()
				.setDependency(arduinoIOComm.getSensorsMapByPortName(portName).values().stream()
						.filter(sensor->sensor.getSensorType().equals(SENSOR_TYPE.LASER_EMIT))
						.findFirst()
						.get());
			arduinoIOComm.getSensorsMapByPortName(portName).values().stream()
			.filter(sensor->sensor.getSensorType().equals(SENSOR_TYPE.PHOTO_RESISTOR))
			.skip(1)
			.findFirst()
			.get()
			.setDependency(arduinoIOComm.getSensorsMapByPortName(portName).values().stream()
					.filter(sensor->sensor.getSensorType().equals(SENSOR_TYPE.LASER_EMIT))
					.skip(1)
					.findFirst()
					.get());
			
		}
		
	}
	
	
	
	

	@Override
	public void onSubscribe(Disposable d) {
		ISensor sensor=(ISensor)d;
		log("Connected to sensor "+sensor.getGuid());
		
	}

	
	/**
	 * It receives data from arduino reactive sensors and update restroom data structure WITH RESTROOM BUSINESS LOGIC
	 */
	@Override
	public void onNext(Object entry) {
		System.out.println(new Gson().toJson(entry));
		
		if (entry instanceof RMEntrySet )
		{
			
			
			int min=0,max=1024,percentual=0;
			
			RMEntrySet t = (RMEntrySet)entry;
			try
			{
				
			Optional<Object> optional_value=(Optional<Object>) t.getObject();
				if (optional_value.isPresent())
				{
					RestroomCellProperties cellProperties=properties.getCellProperties()[devicePortToCellMap.get(t.getPortName())];
					IntervalInteger soapProperties=properties.getSinkProperties().soapProperties[devicePortToSinkMap.get(t.getPortName())];
					IntervalInteger trashProperties=properties.getSinkProperties().trashProperties[devicePortToSinkMap.get(t.getPortName())];
					switch(t.getType())
					{
					case PHOTO_RESISTOR:
						int value=((Double)optional_value.get()).intValue();
						
						int treeshold;
						boolean negative;
						
						switch(t.getPin())
						{
							case 63:		//DOOR
								treeshold=cellProperties.doorPR.treeshold;
								negative = cellProperties.doorPR.isNegative;
								if (value>treeshold && !negative || value<treeshold && negative)
									data.roomsInfo[devicePortToCellMap.get(t.getPortName())].closed=true;
								else
									data.roomsInfo[devicePortToCellMap.get(t.getPortName())].closed=false;
								break;
							case 64:		//PAPER
								treeshold=cellProperties.paperAvaiablePR.treeshold;
								negative = cellProperties.paperAvaiablePR.isNegative;
								if (value>treeshold && !negative || value<treeshold && negative)
									data.roomsInfo[devicePortToCellMap.get(t.getPortName())].paperAvaiable=false;
								else
									data.roomsInfo[devicePortToCellMap.get(t.getPortName())].paperAvaiable=true;
								break;
							case 65:		//LIGHT
								treeshold=cellProperties.lightWorkingPR.treeshold;
								negative = cellProperties.lightWorkingPR.isNegative;
								if (value>treeshold && !negative || value<treeshold && negative)
									data.roomsInfo[devicePortToCellMap.get(t.getPortName())].lightWorking=true;
								else
									data.roomsInfo[devicePortToCellMap.get(t.getPortName())].lightWorking=false;
								break;
						}
						break;
					case TEMP_AND_HUMIDITY:
						Double humidityValue=(Double) optional_value.get();
						data.roomsInfo[devicePortToCellMap.get(t.getPortName())].umidity=humidityValue.intValue();
						break;
					case PRESSURE:
						int pressureValue=((Double)optional_value.get()).intValue();
						min=soapProperties.Min;
						max=soapProperties.Max;
						percentual=((max-min)*100)/pressureValue;
						data.soapDispensersCapacities[devicePortToSinkMap.get(t.getPortName())]=percentual;
						break;
					case ULTRASONIC_ECHO:
						int distanceValue=((Double)optional_value.get()).intValue();
						min=trashProperties.Min;max=trashProperties.Max;percentual=((max-min)*100)/distanceValue;
						data.trashCapacities[devicePortToSinkMap.get(t.getPortName())]=percentual;
						break;
					default:
						break;
					}
				}
			}
			catch(ClassCastException cce)
			{
				log(String.format("%s\nCANNOT CAST OBJECT: %s, value %s", cce.getMessage(), t.getObject().getClass().getName(),t.getObject().toString()));
				cce.printStackTrace();
			}
			
		}
		if (t2-t1>this.timeToDeliver)
		{
			log("Delivering");
			//ADD YOUR OBJECT TO DELIVER THE DATA
			info.setSensorData(data);
			RestResponse response= client.sendData(info);
			if (!response.result)
				log(response.message);
			
			t1=t2;
		}
		t2=System.currentTimeMillis();
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
		System.err.println(e.getMessage());
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public ArrayList<ISensor> getSensorsList() throws Exception {
		/*ArrayList<ISensor> list=new ArrayList<ISensor>();
		this.sensorsMap.values().parallelStream().forEach(array->{
			Arrays.stream(array).forEach(sensor->list.add(sensor));
		});*/
		return new ArrayList<ISensor>();
	}

	@Override
	public Optional<ISensor> getSensorByUUID(UUID id) throws Exception {
		/*for(ISensor[] sensorsArray:sensorsMap.values())
		{
			for(ISensor iSensor:sensorsArray)
			{
				if (iSensor.getGuid().compareTo(id)==0)
					return Optional.ofNullable(iSensor);
			}
		}*/
		return Optional.empty();
	}

	@Override
	public Optional<ISensor> getSensorByType(SENSOR_TYPE type) throws Exception {
		// for(ISensor[] sensorsArray:sensorsMap.values())
		/*for(ISensor[] sensorsArray:sensorsMap.values())
		{
			for(ISensor iSensor:sensorsArray)
			{
				if (iSensor.getSensorType().compareTo(type)==0)
					return Optional.ofNullable(iSensor);
			}
		}*/
		return Optional.empty();
	}

	@Override
	public boolean addSensor(ISensor sensor) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeSensor(ISensor sensor) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void subscribeActual(Observer<? super Object> observer) {
		/*
		Observer<? super RestroomDataStructure> observerRDS=(Observer<? super RestroomDataStructure>) observer;
		observers.add(observerRDS);
		this.sensorsMap.values().forEach(array->{
			Arrays.stream(array).forEach(sensor->sensor.start());
		});
		*/
		
		Start();
	}
	
	public void Start()
	{
		for(String portName : devicePortToCellMap.keySet())
		{
			arduinoIOComm.getSensorsMapByPortName(portName).forEach((k,v)->{
				v.subscribe(this);
				v.start();
			});
		}
		
		for(String portName : devicePortToSinkMap.keySet())
		{
			arduinoIOComm.getSensorsMapByPortName(portName).forEach((k,v)->{v.subscribe(this); v.start();});
			
		}
	}
	
	

	
	
	

	

}
