package model.manager.structures.restroom;

public class RestroomPhotoResistorProperty {
	public boolean isNegative=false;
	public int treeshold=0;
	
	public static Boolean getStatus(double value,int treeshold,boolean isNegative)
	{
		return isNegative?
				(value<treeshold?true:false):
					(value>treeshold?true:false);
				
	}
	
}
