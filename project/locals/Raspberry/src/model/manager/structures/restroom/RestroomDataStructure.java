package model.manager.structures.restroom;

///
/*
 * Smartcity restroom data structure
 */
public class RestroomDataStructure {
	public boolean smokeDetected=false;
	public int[] trashCapacities = new int[3];
	public int[] soapDispensersCapacities= new int[3];
	public RoomInfo [] roomsInfo = new RoomInfo[3];
	
	public RestroomDataStructure()
	{
		smokeDetected=false;
		trashCapacities=new int[] {0,0,0};
		soapDispensersCapacities=new int[] {0,0,0};
		roomsInfo=new RoomInfo[] {
				new RoomInfo(),
				new RoomInfo(),
				new RoomInfo()
		};
	}
	
	public RestroomDataStructure(int roomsCount, int sinksCount)
	{
		smokeDetected=false;
		trashCapacities=new int[sinksCount];
		soapDispensersCapacities= new int[sinksCount];
		roomsInfo = new RoomInfo[roomsCount];
		initRooms();
	}
	
	private void initRooms()
	{
		for(int index=0; index<roomsInfo.length;index++)
			roomsInfo[index]=new RoomInfo();
	}
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder builder=new StringBuilder();
		builder.append("{\n");
		//trashCapacities string
		builder.append("trashCapabilities: [");
		for(int i=0; i<trashCapacities.length; i++)
			if (i<trashCapacities.length-1)
				builder.append(String.format("%d,", trashCapacities[i]));
			else
				builder.append(String.format("%d],\n", trashCapacities[i]));
		
		builder.append("soapDispenserCapacities: [");
		for(int i=0; i<soapDispensersCapacities.length; i++)
			if (i<soapDispensersCapacities.length-1)
				builder.append(String.format("%d,",soapDispensersCapacities[i]));
			else 
				builder.append(String.format("$d],\n", soapDispensersCapacities[i]));
		
		builder.append("roomsInfo: \n");
		builder.append("[\n");
		for(int i=0; i<roomsInfo.length;i++)
			if (i<roomsInfo.length-1)
				builder.append(roomsInfo[i].toString()+",\n");
			else
				builder.append(roomsInfo[i].toString()+"\n");
		builder.append("]\n}");
		
		return builder.toString();
	}
	
	
}

