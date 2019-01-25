package model.manager.structures.restroom;

public class RoomInfo {
	public int id;
	public boolean paperAvaiable=false;
	public int umidity;
	public boolean lightWorking=false;
	public boolean closed=false;
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(String.format("{paperAvailable:%s ,\numidity:%d ,\nlightWorking:%s ,\nclosed:%s\n}",paperAvaiable,umidity,lightWorking,closed));
		return builder.toString();
	}
}
