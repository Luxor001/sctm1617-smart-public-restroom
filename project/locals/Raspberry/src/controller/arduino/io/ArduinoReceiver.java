package controller.arduino.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import scala.NotImplementedError;

public class ArduinoReceiver extends Observable<String> implements SerialPortEventListener {
	
	
	BufferedReader input;
	Queue<String> receivedMessages;
	Optional<Observer<String>> observer;
	private String deviceName="NO NAME";
	private String devicePort="NO PORT";
	
	public ArduinoReceiver(BufferedReader input)
	{
		this.input=input;
		this.receivedMessages=new LinkedBlockingQueue<String>();
		this.observer=Optional.empty();
		
	}
	
	
	
	
	
	
	@Override
	public void serialEvent(SerialPortEvent oEvent)
	{
		new Runnable() {
			
			@Override
			public void run() {
				switch(oEvent.getEventType())
				{
					case SerialPortEvent.DATA_AVAILABLE:
					{
						try {
							String receivedMessage=input.readLine().trim();
							System.out.println(String.format("[FROM ARDUINO %s.%s] RECEIVED: %s",devicePort,deviceName,receivedMessage));
							receivedMessages.add(receivedMessage);
							
							if (observer.isPresent()) observer.get().onNext(devicePort+","+receivedMessage);
							
						} catch (IOException e) {

							e.printStackTrace();
						}
					}
					default:
						break;
						
				}
			}
		}.run();;
	}
	
	public Optional<String> getOldestMessage()
	{
		return Optional.ofNullable(receivedMessages.poll());
	}




	@Override
	protected void subscribeActual(Observer<? super String> observer) {
		// TODO Auto-generated method stub
		
		this.observer=Optional.ofNullable((Observer<String>) observer);
	}






	public void setDeviceName(String deviceName) {
		this.deviceName=deviceName;
		
	}






	public void setPort(String portName) {
		// TODO Auto-generated method stub
		this.devicePort=portName;
	}

}
