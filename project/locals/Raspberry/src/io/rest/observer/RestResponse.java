package io.rest.observer;

public class RestResponse {
	public boolean result;
	public String message;
	public RestResponse() {result=false; message="";}
	public RestResponse(boolean result, String message)
	{
		this.result=result;
		this.message=message;
	}
	
	
}
