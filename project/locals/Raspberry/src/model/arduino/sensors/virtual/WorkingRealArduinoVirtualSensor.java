package model.arduino.sensors.virtual;

import java.util.Optional;

import java.util.UUID;

import model.manager.structures.EntrySet;
import model.manager.structures.RMEntrySet;
import model.sensors.enumerators.SENSOR_GENERIC;
import model.sensors.enumerators.SENSOR_SIMULATION_PATTERN;
import model.sensors.enumerators.SENSOR_STATUS;
import model.sensors.enumerators.SENSOR_TYPE;

import model.sensors.virtual.WorkingRealVirtualSensor;

public class WorkingRealArduinoVirtualSensor<T extends Object> extends WorkingRealVirtualSensor<T> {
	
	
	private int id=0;
	
	public WorkingRealArduinoVirtualSensor(int id, T startValue, double minValue, double maxValue, double maxPeakFromLast,
			int timeForCapture, SENSOR_TYPE sensorType, SENSOR_SIMULATION_PATTERN pattern,
			Optional<Integer> patter_duration) throws Exception {
		super(UUID.randomUUID(), startValue, minValue, maxValue, maxPeakFromLast, timeForCapture, sensorType, pattern, patter_duration);
		// TODO Auto-generated constructor stub
		
		if (pattern!=SENSOR_SIMULATION_PATTERN.RANDOM)
		{
			maxQuadratic=new Double(this.maxValue-this.maxPeakFromLast*1.2);
			minQuadratic=new Double(this.minValue+this.maxPeakFromLast*0.8);
			pulse= 2*Math.PI/this.pattern_duration.get();
			calculatePhase();
			
		}
		this.id=id;
		
		log(String.format("Created with parameters\n min:%f\n max:%f\n peak:%f\n type:%s\n pattern:%s", minValue,maxValue,maxPeakFromLast,sensorType.toString(),pattern.toString()));
		
		
	}
	
	public int GetId()
	{
		return this.id;
	}
	

	@Override
	public void run() {
		this.time_start=System.currentTimeMillis();
		this.t1=this.time_start;
		this.t2=this.time_start;
		this.log("Running");
		try
		{
			while(this.status==SENSOR_STATUS.RUNNING)
			{
				switch(pattern)
				{
				case RANDOM:
					updateRandom();
					break;
				case QUADRATIC:
					updateQuadratic();
					break;
				case SINUSOIDAL:
					updateSinusoidal();
					break;
				default:
					throw new Exception("Simulation pattern not choosed");
				}
				//if (startValue instanceof Integer && this.lastValue.isPresent())
				//	this.lastValue=(Optional<T>) Optional.of((int)lastValue.get());
				
				
				if (this.startValue instanceof Boolean)
				{
					if (!updateObserver(new RMEntrySet(id,0,this.getSensorType(),this.lastValue.get()))) break;
				}
				if (this.startValue instanceof Integer)
				{
					int percentual=new Double(((Double)this.lastValue.get()*100)/(maxValue-minValue)).intValue();
					if(!updateObserver(new RMEntrySet(id,0,this.getSensorType(),percentual))) break;
				}
					
				
					
				t2=System.currentTimeMillis();
				Thread.sleep(timeForCapture);
			}
			log("Running complete!");
			setStatus(SENSOR_STATUS.READY);
		}
		catch(Exception e)
		{
			log("Error while running: "+e.getMessage());
			e.printStackTrace();
			setStatus(SENSOR_STATUS.DAMAGED);
		}
		
	}
	



	

	
	
	

}
