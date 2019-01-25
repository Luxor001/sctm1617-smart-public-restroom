package controller.arduino.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;



import scala.NotImplementedError;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.omg.CORBA.Environment;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import io.reactivex.Observer;



public class ArduinoRxTx {

	SerialPort serialPort;
	
	private static HashMap <String,Optional<ArduinoRxTx>> instance=new HashMap<String,Optional<ArduinoRxTx>>();
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE=9600;
	private String portName;
	private String deviceName;
	
	
	private OutputStream output;
	private Queue<String> messagesToSend;
	private ArduinoReceiver receiver;
	
	
	
	public static ArduinoRxTx GetIstance(String portName)
	{
		if (!instance.containsKey(portName))
			instance.put(portName, Optional.of(new ArduinoRxTx(portName)));
		return instance.get(portName).get();
	}
	
	private ArduinoRxTx(String portName) {
		this.portName=portName;
		init();
	}
	
	
	
	private boolean init()
	{
		System.setProperty("gnu.io.rtxt.SerialPorts","/dev/ttyACM0");
		CommPortIdentifier portId=null;
		
		Enumeration<CommPortIdentifier> portEnum=CommPortIdentifier.getPortIdentifiers();
		while(portEnum.hasMoreElements())
		{
			CommPortIdentifier currPortId=(CommPortIdentifier) portEnum.nextElement();
			
			
			if (currPortId.getName().equals(portName))
			{
				portId=currPortId;
				break;
			}
			
		};
		
		if (portId==null)
		{
			System.err.println("Could not find COM port");
			return false;
		}
		try
		{
			serialPort= (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
			output = serialPort.getOutputStream();

			messagesToSend=new LinkedBlockingQueue<String>();
			
			receiver=new ArduinoReceiver(
					new BufferedReader(new InputStreamReader(serialPort.getInputStream(), Charset.defaultCharset())));
			receiver.setPort(portName);
			
			serialPort.addEventListener(receiver);
			serialPort.notifyOnDataAvailable(true);
			Thread.sleep(3000);
			sendCommand("HANDSHAKE");
			
			Optional<String> message=Optional.empty();
			while(true)
			{
				message=getOldestMessage();
				if (message.isPresent())
				{
					System.out.println(message.get());
					if (message.get().equals("Hello"))
						break;
				
				}
				Thread.sleep(1000);
				
			}
			
			return true;
			
		}
		catch(Exception e)
		{
			System.err.println(e.toString());
			return false;
			
		}
	}
	
	
	
	public void subscribeToReceiver(Observer<String> observer)
	{
		receiver.subscribe(observer);
	}
	
	
	
	public synchronized void sendCommand(String command)
	{
		messagesToSend.add(command);
		dequeueNextCommand();
	}
	
	public synchronized Optional<String> getOldestMessage()
	{
		return receiver.getOldestMessage();
	}
	
	private synchronized void dequeueNextCommand()
	{
		new Thread(new Runnable() {
			
			private String formatMessage(String message)
			{
				if (message.split(",").length==3)
					return message;
				else
					return String.format("%s,%d,%d\n", message,0,0);
			}
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!messagesToSend.isEmpty())
				{
					try
					{
						String message=formatMessage(messagesToSend.poll());
						System.out.println("SENDING: "+message);
							output.write(message.getBytes());
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
		receiver.setDeviceName(deviceName);
	}

	public String getPortName() {
		return portName;
	}

}
