package model.manager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.structures.DataSet;
import model.manager.structures.EntrySet;
import model.sensors.ISensor;
import java.util.UUID;

public abstract class AbstractSensorManager<T extends Object> extends Observable<Object> implements Observer<Object> {

	
	private static long identificatorFactory=0;
	private boolean log;
	protected long id;
	protected DataSet dataSet;
	
	public AbstractSensorManager(long id,boolean log)
	{
		this.id=id;
		this.log=log;
	}

	public void log(String message)
	{
		if (log)
			System.out.println(String.format("[O%d] %s", id,message));
	}
	
	protected final Observer<Object> getObserver()
	{
		return this;
	}
	
	public long getId() {
		return this.id;
	}
	
	public static UUID getNewId()
	{
		identificatorFactory++;
		return UUID.fromString(identificatorFactory+""); 
	}
	
	public abstract boolean addSensor(ISensor sensor) throws Exception;
	public abstract void removeSensor(ISensor sensor) throws Exception;
	
}
