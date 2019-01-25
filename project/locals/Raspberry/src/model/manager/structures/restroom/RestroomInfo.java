package model.manager.structures.restroom;

import java.util.UUID;



public class RestroomInfo {
	
	private transient UUID _id;
	private String cityAddress;
	private double[] address=new double[2];
	private String company;
	private String device;
	private RestroomDataStructure sensorData;
	
	public RestroomInfo()
	{
		_id=UUID.randomUUID();
	}
	
	public RestroomInfo(String guid)
	{
		_id=UUID.fromString(guid);
	}
	
	public UUID getGuid()
	{
		return _id;
	}
	public String getCityAddress() {
		return cityAddress;
	}
	public void setCityAddress(String cityAddress) {
		this.cityAddress = cityAddress;
	}
	public double[] getAddress() {
		return address;
	}
	public void setAddress(double[] address) {
		this.address = address;
	}
	
	public void setLatitude(double lat)
	{
		this.address[0]=lat;
	}
	public void setLongitude(double log)
	{
		this.address[1]=log;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public RestroomDataStructure getSensorData() {
		return sensorData;
	}
	public void setSensorData(RestroomDataStructure sensorData) {
		this.sensorData = sensorData;
	}

}
