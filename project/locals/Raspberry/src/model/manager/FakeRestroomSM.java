package model.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.IntStream;

import com.google.gson.Gson;

import controller.arduino.io.IOComm;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.arduino.sensors.ArduinoSensor;
import model.manager.structures.DataSet;
import model.manager.structures.EntrySet;
import model.manager.structures.RMEntrySet;
import model.manager.structures.restroom.RestroomDataStructure;
import model.manager.structures.restroom.RestroomInfo;
import model.sensors.ISensor;
import model.sensors.enumerators.SENSOR_GENERIC;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_TYPE;
import model.arduino.sensors.virtual.*;

/**
 * @author andre A CUSTOM SENSOR MANAGER USED BY SMARTCITY PROJECT THIS CODE IS
 *         STRICTLY TIED TO SMARTCITY PROJECT BUSINESS
 */
public class FakeRestroomSM extends AbstractSensorManager<RestroomDataStructure> implements ISensorsManager {

	private boolean log;
	private RestroomInfo info;
	private RestroomDataStructure data = new RestroomDataStructure();
	private long t1 = System.currentTimeMillis(), t2, timeToDeliver;
	private Optional<Integer> pattern_duration=Optional.empty();
	public ArrayList<Observer<? super RestroomInfo>> observers = new ArrayList<Observer<? super RestroomInfo>>();
	public HashMap<SENSOR_TYPE, ISensor[]> sensorsMap;
	public IOComm arduinoIOComm;

	public FakeRestroomSM(long id, long timeToDeliver, boolean log) {
		super(id, log);
		this.timeToDeliver = timeToDeliver;
		this.log = log;
		initializeSensorsMap();

	}
	
	public FakeRestroomSM(long id, long timeToDeliver,Optional<Integer> pattern_duration ,boolean log)
	{
		super(id,log);
		this.timeToDeliver=timeToDeliver;
		this.log=log;
		this.pattern_duration=pattern_duration.isPresent()?pattern_duration:Optional.ofNullable(6000);
		initializeSensorsMap();
	}
	
	public void SetRestroomInfo(RestroomInfo info)
	{
		this.info=info;
	}
	
	public void SetRestroomInfo(Properties properties)
	{
		this.info=new RestroomInfo(properties.getProperty("id"));
		this.info.setAddress(new double[] {
				Double.parseDouble(properties.getProperty("latitude")),
				Double.parseDouble(properties.getProperty("longitude")),
		});
		this.info.setCityAddress(properties.getProperty("cityAddress"));
		this.info.setCompany(properties.getProperty("company"));
		this.info.setDevice(properties.getProperty("device"));
		
	}
	
	public void SetRestroomInfo(String cityAddress,double[] coordinates,String companyName,String deviceName)
	{
		this.info=new RestroomInfo();
		this.info.setAddress(coordinates);
		this.info.setCityAddress(cityAddress);
		this.info.setCompany(companyName);
		this.info.setDevice(deviceName);
	}

	private void initializeSensorsMap() {

		try {
			this.sensorsMap=new HashMap<SENSOR_TYPE,ISensor[]>();
			// SMOKE
			this.sensorsMap.put(SENSOR_TYPE.SMOKE,
					new ISensor[] { new WorkingRealArduinoVirtualSensor<Boolean>(0, false, 0F, 1F, 0F, 2000,
							SENSOR_TYPE.SMOKE, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,pattern_duration) });
			// PRESSURE
			this.sensorsMap.put(SENSOR_TYPE.PRESSURE, new ISensor[] {
					new WorkingRealArduinoVirtualSensor<Integer>(0, 0, 0F, 1024F, 10F, 5000, SENSOR_TYPE.PRESSURE,
							SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Integer>(1, 400, 0F, 1024F, 10F, 5000, SENSOR_TYPE.PRESSURE,
							SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Integer>(2, 600, 0F, 1024F, 10F, 5000, SENSOR_TYPE.PRESSURE,
							SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration) });
			// ULTRASONIC
			this.sensorsMap.put(SENSOR_TYPE.ULTRASONIC_ECHO, new ISensor[] {
					new WorkingRealArduinoVirtualSensor<Integer>(0, 100, 0F, 1024F, 10F, 10000,
							SENSOR_TYPE.ULTRASONIC_ECHO, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Integer>(1, 700, 0F, 1024F, 10F, 10000,
							SENSOR_TYPE.ULTRASONIC_ECHO, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Integer>(2, 1024, 0F, 1024F, 10F, 10000,
							SENSOR_TYPE.ULTRASONIC_ECHO, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,
							pattern_duration) });

			// MOTION
			this.sensorsMap.put(SENSOR_TYPE.MOTION_SENSOR, new ISensor[] {
					new WorkingRealArduinoVirtualSensor<Boolean>(0, false, 0F, 1F, 0F, 10000,
							SENSOR_TYPE.MOTION_SENSOR, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Boolean>(1, false, 0F, 1F, 0F, 10000,
							SENSOR_TYPE.MOTION_SENSOR, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Boolean>(2, true, 0F, 1F, 0F, 10000,
							SENSOR_TYPE.MOTION_SENSOR, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,
							pattern_duration) });

			// BUTTON
			this.sensorsMap.put(SENSOR_TYPE.BUTTON, new ISensor[] {
					new WorkingRealArduinoVirtualSensor<Boolean>(0, false, 0F, 1F, 0F, 2000, SENSOR_TYPE.BUTTON,
							SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Boolean>(1, false, 0F, 1F, 0F, 2000, SENSOR_TYPE.BUTTON,
							SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Boolean>(2, true, 0F, 1F, 0F, 2000, SENSOR_TYPE.BUTTON,
							SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration) });

			// WATER_LEVER_SENSOR
			this.sensorsMap.put(SENSOR_TYPE.WATER_LEVER_SENSOR,
					new ISensor[] {
							new WorkingRealArduinoVirtualSensor<Integer>(0, 100, 0F, 1024F, 10F, 10000,
									SENSOR_TYPE.WATER_LEVER_SENSOR, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,
									pattern_duration),
							new WorkingRealArduinoVirtualSensor<Integer>(1, 700, 0F, 1024F, 10F, 10000,
									SENSOR_TYPE.WATER_LEVER_SENSOR, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,
									pattern_duration),
							new WorkingRealArduinoVirtualSensor<Integer>(2, 1024, 0F, 1024F, 10F, 10000,
									SENSOR_TYPE.WATER_LEVER_SENSOR, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,
									pattern_duration) });

			// IR_RECEIVER
			this.sensorsMap.put(SENSOR_TYPE.IR_RECEIVER, new ISensor[] {
					new WorkingRealArduinoVirtualSensor<Boolean>(0, false, 0F, 1F, 10F, 10000,
							SENSOR_TYPE.IR_RECEIVER, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Boolean>(1, true, 0F, 1F, 10F, 10000,
							SENSOR_TYPE.IR_RECEIVER, SENSOR_SIMULATION_PATTERN.SINUSOIDAL, pattern_duration),
					new WorkingRealArduinoVirtualSensor<Boolean>(2, true, 0F, 1F, 10F, 10000,
							SENSOR_TYPE.IR_RECEIVER, SENSOR_SIMULATION_PATTERN.SINUSOIDAL,
							pattern_duration) });

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onSubscribe(Disposable d) {
		ISensor sensor = (ISensor) d;
		log("Connected to sensor " + sensor.getGuid());

	}

	@Override
	public void onNext(Object entry) {
		System.out.println("ENTRY: "+new Gson().toJson(entry));
		if (entry instanceof RMEntrySet) {
			RMEntrySet t = (RMEntrySet)entry;
			if (t.getObject() instanceof Integer || t.getObject() instanceof Boolean)
			{
				if (t.getType().equals(SENSOR_TYPE.MOTION_SENSOR))
					data.roomsInfo[t.getRestroomSensorId()].lightWorking=(Boolean) t.getObject();
				else if (t.getType().equals(SENSOR_TYPE.IR_RECEIVER))
					data.roomsInfo[t.getRestroomSensorId()].paperAvaiable=(Boolean) t.getObject();
				else if (t.getType().equals(SENSOR_TYPE.WATER_LEVER_SENSOR))
					data.roomsInfo[t.getRestroomSensorId()].umidity=(Integer) t.getObject();
				else if (t.getType().equals(SENSOR_TYPE.BUTTON))
					data.roomsInfo[t.getRestroomSensorId()].closed=(Boolean) t.getObject();
				else if (t.getType().equals(SENSOR_TYPE.PRESSURE))
					data.soapDispensersCapacities[t.getRestroomSensorId()]=(Integer) t.getObject();
				else if (t.getType().equals(SENSOR_TYPE.ULTRASONIC_ECHO))
					data.trashCapacities[t.getRestroomSensorId()]=(Integer) t.getObject();
				else if (t.getType().equals(SENSOR_TYPE.SMOKE))
					data.smokeDetected = (Boolean) t.getObject();
				log(new Gson().toJson(t));
			}
			

		}
		if (t2 - t1 > this.timeToDeliver) {
			log("Delivering");
			for (Observer<? super RestroomInfo> observer : observers) {
				info.setSensorData(this.data);
				observer.onNext(this.info);
			}
			t1 = t2;
		}
		t2 = System.currentTimeMillis();
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
		ArrayList<ISensor> list = new ArrayList<ISensor>();
		this.sensorsMap.values().parallelStream().forEach(array -> {
			Arrays.stream(array).forEach(sensor -> list.add(sensor));
		});
		return list;
	}

	@Override
	public Optional<ISensor> getSensorByUUID(UUID id) throws Exception {
		for (ISensor[] sensorsArray : sensorsMap.values()) {
			for (ISensor iSensor : sensorsArray) {
				if (iSensor.getGuid().compareTo(id) == 0)
					return Optional.ofNullable(iSensor);
			}
		}
		return Optional.empty();
	}

	@Override
	public Optional<ISensor> getSensorByType(SENSOR_TYPE type) throws Exception {
		// for(ISensor[] sensorsArray:sensorsMap.values())
		for (ISensor[] sensorsArray : sensorsMap.values()) {
			for (ISensor iSensor : sensorsArray) {
				if (iSensor.getSensorType().compareTo(type) == 0)
					return Optional.ofNullable(iSensor);
			}
		}
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
	
	public void Start()
	{
		this.sensorsMap.values().forEach(array -> {
			Arrays.stream(array).forEach(sensor ->{ 
				sensor.getObservable().subscribe(getObserver());
				sensor.start();
			});
		});
	}
	
	public void Stop()
	{
		
	}
	
	

	@Override
	protected void subscribeActual(Observer<? super Object> observer) {
		Observer<? super RestroomInfo> observerRDS = (Observer<? super RestroomInfo>) observer;
		observers.add(observerRDS);
		Start();
		

	}

}
