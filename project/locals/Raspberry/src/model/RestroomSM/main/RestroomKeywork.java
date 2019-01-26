package model.RestroomSM.main;

public enum RestroomKeywork {
	ROOT("RESTROOMSENSORMANAGER"),
	INFO("INFO"),
	PORT("PORTNAME"),
	MANAGER("MANAGER"),
	REST_URL("REST_URL"),
	CELL("CELL"),
	LIGHT("LIGHT"),
	TREESHOLD("TREESHOLD"),
	NEGATIVE("NEGATIVE"),
	PAPER("PAPER"),
	DOOR("DOOR"),
	SINK("SINK"),
	TRASH("TRASH"),
	SOAP("SOAP"),
	MIN("MIN"),
	MAX("MAX"),
	TTD("TTD"),
	RANDOMTIMESENDING("RANDOMTIMESENDING"),
	ID("ID"),
	CITY("CITYADDRESS"),
	LAT("LATITUDE"),
	LONG("LONGITUDE"),
	COMPANY("COMPANY"),
	DEVICE("DEVICE");
	
	
	private final String value;
	
	private RestroomKeywork(final String text) {
		this.value=text;
	}
	
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}


}
