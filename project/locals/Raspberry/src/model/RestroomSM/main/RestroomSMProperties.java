package model.RestroomSM.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import model.manager.structures.restroom.RestroomCellProperties;
import model.manager.structures.restroom.RestroomInfo;
import model.manager.structures.restroom.RestroomPhotoResistorProperty;
import model.manager.structures.restroom.RestroomSinkProperties;
import model.structures.IntervalInteger;

public class RestroomSMProperties {
	
	private final String ROOT;
	private final String PORTNAMES_ROOT;
	private final String INFO_ROOT;
	private final String SINK_ROOT;
	private final String CELL_ROOT;
	private final String MANAGER_ROOT;
	
	private static RestroomSMProperties smProperties;
	private Properties properties;
	private String[] portNames;
	private RestroomInfo restroomInfo;
	private RestroomSinkProperties sinkProperties;
	private RestroomCellProperties[] cellProperties;
	private int ttd,randomTime;
	private String url;
	
	
	
	private RestroomSMProperties() throws Exception
	{
		this.ROOT=String.format("%s", RestroomKeywork.ROOT);
		this.PORTNAMES_ROOT=String.format("%s.%s", ROOT,RestroomKeywork.PORT);
		this.INFO_ROOT = String.format("%s.%s",ROOT,RestroomKeywork.INFO);
		this.SINK_ROOT = String.format("%s.%s", ROOT,RestroomKeywork.SINK);
		this.CELL_ROOT = String.format("%s.%s", ROOT,RestroomKeywork.CELL);
		this.MANAGER_ROOT = String.format("%s.%s",ROOT,RestroomKeywork.MANAGER);
		
		Init();
	}
	
	private void Init() throws Exception
	{
		this.properties=new Properties();
		properties.load(new FileInputStream(new File("config.properties")));
		LoadData(properties);
		
	}
	
	private void LoadData(Properties properties) throws Exception
	{
		Set<String> restroomPropertyKeys=GetRestroomPropertyKeys(properties);
		Set<String> infoPropertyKeys=GetAllInfoPropertyKeys(restroomPropertyKeys);
		Set<String> cellPropertyKeys=GetAllCellPropertyKeys(restroomPropertyKeys);
		Set<String> sinkPropertyKeys=GetAllSinkPropertyKeys(restroomPropertyKeys);
		Set<String> managerPropertyKeys=GetAllManagerPropertyKeys(restroomPropertyKeys);
		
		
		portNames=GetArduinoDevices(restroomPropertyKeys);
		int numberOfCellModules,numberOfSinkModules;
		numberOfCellModules=GetNumbersOfCellsByCellPropertyKeys(cellPropertyKeys);
		numberOfSinkModules=GetNumbersOfSinksBySinkPropertyKeys(sinkPropertyKeys);
		
		if (portNames.length!= numberOfCellModules + numberOfSinkModules)
			throw new Exception("The number of arduino devices that should be connected doesn't match with the numbers of theyr properties,check your config");
		
		managerPropertyKeys.forEach(key->{
			if (key.contains(RestroomKeywork.TTD.toString()))
				this.ttd=Integer.parseInt(this.properties.getProperty(key));
			if (key.contains(RestroomKeywork.RANDOMTIMESENDING.toString()))
				this.randomTime=Integer.parseInt(this.properties.getProperty(key));
			if (key.contains(RestroomKeywork.REST_URL.toString()))
				this.url=this.properties.getProperty(key);
		});
		
		InitRestroomInfo(infoPropertyKeys);
		InitRestroomCells(cellPropertyKeys, numberOfCellModules);
		InitRestroomSinks(sinkPropertyKeys,numberOfSinkModules);
		
		
		
		
	}
	
	private Set<String> GetAllManagerPropertyKeys(Set<String> restroomPropertyKeys) {
		// TODO Auto-generated method stub
		return restroomPropertyKeys.stream()
				.filter(key->key.startsWith(MANAGER_ROOT))
				.collect(Collectors.toSet());
	}

	private Set<String> GetRestroomPropertyKeys(Properties properties)
	{
		return properties.keySet()
				.stream()
				.map(key->((String)key))
				.filter(key->((String)key).startsWith(this.ROOT))
				.collect(Collectors.toSet());
	}
	
	private void InitRestroomInfo(Set<String> restroomInfoPropertyKeys)
	{
		Optional<String> id=Optional.empty(),city=Optional.empty(),device=Optional.empty(),company=Optional.empty();
		Optional<Double> latitude=Optional.empty(),longitude=Optional.empty();
		Iterator<String> iterator = restroomInfoPropertyKeys.iterator();
		while(iterator.hasNext())
		{
			String key=iterator.next();
			String value=properties.getProperty(key);
			if (key.contains(RestroomKeywork.ID.toString()))
				id = Optional.of(value);
			else if (key.contains(RestroomKeywork.CITY.toString()))
				city=Optional.of(value);
			else if (key.contains(RestroomKeywork.DEVICE.toString()))
				device=Optional.of(value);
			else if (key.contains(RestroomKeywork.COMPANY.toString()))
				company=Optional.of(value);
			else if (key.contains(RestroomKeywork.LAT.toString()))
				latitude=Optional.of(Double.parseDouble(value));
			else if (key.contains(RestroomKeywork.LONG.toString()))
				longitude=Optional.of(Double.parseDouble(value));
		}
		
		restroomInfo=new RestroomInfo(id.get());
		if (city.isPresent()) restroomInfo.setCityAddress(city.get());
		if (device.isPresent()) restroomInfo.setDevice(device.get());
		if (company.isPresent()) restroomInfo.setCompany(company.get());
		if (latitude.isPresent()) restroomInfo.setLatitude(latitude.get());
		if (longitude.isPresent()) restroomInfo.setLongitude(longitude.get());
	}
	
	private void InitRestroomCells(Set<String> restroomCellPropertyKeys,int numberOfCellModules)
	{
		this.cellProperties=new RestroomCellProperties[numberOfCellModules];
		for(int in=1; in<=numberOfCellModules;in++)
		{
			
			InitRestroomCellByProperty(restroomCellPropertyKeys,in,Optional.of(RestroomKeywork.PAPER.toString()));
			InitRestroomCellByProperty(restroomCellPropertyKeys,in,Optional.of(RestroomKeywork.LIGHT.toString()));
			InitRestroomCellByProperty(restroomCellPropertyKeys,in,Optional.of(RestroomKeywork.DOOR.toString()));
			
		}
	}
	
	private void InitRestroomCellByProperty(Set<String> restroomCellPropertyKeys,int in,Optional<String> property)
	{
		Set<String> keys=GetCellPropertyKeysByCellNumberAndPropertyType(restroomCellPropertyKeys, in, property);
		Optional<String> treesholdKey=keys.stream()
			.filter(key->key.split("\\.")[4].compareTo(RestroomKeywork.TREESHOLD.toString())==0).findFirst();
		Optional<String> negativeKey=keys.stream()
				.filter(key->key.split("\\.")[4].compareTo(RestroomKeywork.NEGATIVE.toString())==0).findFirst();
		if (this.cellProperties[in-1]==null)
			this.cellProperties[in-1]=new RestroomCellProperties();
		if (treesholdKey.isPresent())
			this.cellProperties[in-1].paperAvaiablePR.treeshold=Integer.parseInt(this.properties.getProperty(treesholdKey.get()));
		if (negativeKey.isPresent())
			this.cellProperties[in-1].paperAvaiablePR.isNegative=Boolean.parseBoolean(this.properties.getProperty(negativeKey.get()));
	}
	
	private void InitRestroomSinks(Set<String> sinkPropertyKeys,int numberOfSinks)
	{
		this.sinkProperties=new RestroomSinkProperties();
		int numberOfTrashes=GetNumberOfTrashesBySink(sinkPropertyKeys);
		int numberOfSoaps=GetNumberOfSoapsBySink(sinkPropertyKeys);
		this.sinkProperties.soapProperties=new IntervalInteger[numberOfSoaps];
		this.sinkProperties.trashProperties=new IntervalInteger[numberOfTrashes];
		
		for(int index=1; index<=numberOfSoaps; index++)
		{
			Set<String> soapProperties=GetSinkObjectProperties(sinkPropertyKeys, RestroomKeywork.SOAP.toString(), index);
			Optional<String> soapMin=soapProperties.stream().filter(key->key.contains(RestroomKeywork.MIN.toString())).findFirst();
			Optional<String> soapMax=soapProperties.stream().filter(key->key.contains(RestroomKeywork.MAX.toString())).findFirst();
			if (this.sinkProperties.soapProperties[index-1]==null)
				this.sinkProperties.soapProperties[index-1]=new IntervalInteger();
			if (soapMin.isPresent())
				this.sinkProperties.soapProperties[index-1].Min=Integer.parseInt(this.properties.getProperty(soapMin.get()));
			if (soapMax.isPresent())
				this.sinkProperties.soapProperties[index-1].Max=Integer.parseInt(this.properties.getProperty(soapMax.get()));
		}
		
		for(int index=1;index<=numberOfTrashes;index++)
		{
			Set<String> trashProperties=GetSinkObjectProperties(sinkPropertyKeys, RestroomKeywork.TRASH.toString(), index);
			Optional<String> trashMin=trashProperties.stream().filter(key->key.contains(RestroomKeywork.MIN.toString())).findFirst();
			Optional<String> trashMax=trashProperties.stream().filter(key->key.contains(RestroomKeywork.MAX.toString())).findFirst();
			this.sinkProperties.trashProperties[index-1]=new IntervalInteger();
			if (trashMin.isPresent())
				this.sinkProperties.trashProperties[index-1].Min=Integer.parseInt(this.properties.getProperty(trashMin.get()));
			if (trashMax.isPresent())
				this.sinkProperties.trashProperties[index-1].Max=Integer.parseInt(this.properties.getProperty(trashMax.get()));
		}
		
	}
	
	private Set<String> GetSinkObjectProperties(Set<String> sinkPropertyKeys,String sinkObject,int objectNumber)
	{
		return sinkPropertyKeys.stream()
				.filter(key->key.split("\\.")[3].compareTo(sinkObject)==0 && key.split("\\.")[4].compareTo(objectNumber+"")==0)
				.collect(Collectors.toSet());
	}
	
	private String[] GetArduinoDevices(Set<String> keys)
	{
		String[] portNames;
		List<String> portsNamesKeys= keys.stream()
				.filter(key->key.startsWith(PORTNAMES_ROOT))
				.distinct()
				.collect(Collectors.toList());
		portNames=new String[portsNamesKeys.size()];
		for(int i=0;i<portsNamesKeys.size(); i++)
		{
			portNames[i]=properties.getProperty(portsNamesKeys.get(i));
		}
		
		return portNames;
	}

	private int GetNumberOfArduinoDevices(Set<String> keys)
	{
		return (int)keys.stream()
				.filter(key->key.startsWith(PORTNAMES_ROOT))
				.distinct()
				.count();
	}
	
	private int GetNumberOfTrashesBySink(Set<String> sinkPropertyKeys)
	{
		return (int)sinkPropertyKeys.stream()
				.filter(key->key.split("\\.")[3].compareTo(RestroomKeywork.TRASH.toString())==0)
				.map(key->key.split("\\.")[4])
				.distinct().count();
	}
	
	private int GetNumberOfSoapsBySink(Set<String> sinkPropertyKeys)
	{
		return (int)sinkPropertyKeys.stream()
				.filter(key->key.split("\\.")[3].compareTo(RestroomKeywork.SOAP.toString())==0)
				.map(key->key.split("\\.")[4])
				.distinct().count();
	}
	
	
	
	private Set<String> GetAllInfoPropertyKeys(Set<String> restroomPropertyKeys)
	{
		return restroomPropertyKeys.stream()
				.map(key->(String)key)
				.filter(key->key.startsWith(INFO_ROOT))
				.collect(Collectors.toSet());
	}
	
	private Set<String> GetAllCellPropertyKeys(Set<String> restroomPropertyKeys)
	{
		return restroomPropertyKeys.stream()
				.map(key->((String)key))
				.filter(key->key.startsWith(CELL_ROOT))
				.collect(Collectors.toSet());
	}
	
	private Set<String> GetCellPropertyKeysByCellNumberAndPropertyType(Set<String> cellPropertyKeys,int cellNumber,Optional<String> propertyKey)
	{
		return cellPropertyKeys.stream()
				.filter(key-> key.split("\\.")[2].equals(cellNumber+"") &&
						(propertyKey.isPresent() && key.split("\\.")[3].equals(propertyKey.get()) || !propertyKey.isPresent()))
				.collect(Collectors.toSet());
	}
	
	private Set<String> GetAllSinkPropertyKeys(Set<String> restroomPropertyKeys)
	{
		return restroomPropertyKeys.stream()
				.map(key->(String)key)
				.filter(key->key.startsWith(SINK_ROOT))
				.collect(Collectors.toSet());
	}
	
	private int GetNumbersOfCellsByCellPropertyKeys(Set<String> cellPropertyKeys)
	{
		
		Set<String> cellsKeys=cellPropertyKeys.stream()
				.filter(key->key!=null && !key.isEmpty())
				.map(key->{
					
					return new String(key.split("\\.")[2]);
					})
				.distinct()
				.collect(Collectors.toSet());
		return (int) cellsKeys.size();
	}
	
	private int GetNumbersOfSinksBySinkPropertyKeys(Set<String> sinkPropertyKeys)
	{
		return (int) sinkPropertyKeys.stream()
				.map(key->((String)key).split("\\.")[2])
				.distinct()
				.count();
	}
	
	public static RestroomSMProperties GetInstance() throws Exception
	{
		if (smProperties==null)
			smProperties=new RestroomSMProperties();
		return smProperties;
	}

	public String[] getPortNames() {
		return portNames;
	}

	public RestroomInfo getRestroomInfo() {
		return restroomInfo;
	}

	public RestroomSinkProperties getSinkProperties() {
		return sinkProperties;
	}

	public RestroomCellProperties[] getCellProperties() {
		return cellProperties;
	}

	public int getTtd() {
		return ttd;
	}

	public int getRandomTime() {
		return randomTime;
	}
	
	public String getUrl()
	{
		return this.url;
	}

}
