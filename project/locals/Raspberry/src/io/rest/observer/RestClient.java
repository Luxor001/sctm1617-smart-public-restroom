package io.rest.observer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;

import org.apache.http.conn.ssl.*;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpEntity;

import com.google.gson.Gson;

import model.manager.structures.restroom.RestroomInfo;

import java.security.KeyStore;
import java.util.UUID;

import javax.net.ssl.SSLContext;


public class RestClient {
	
	private HttpClient httpClient;
	private HttpPost httpPost;
	
	private SSLContext sslcontext;
	
	
	public RestClient(String url) throws Exception
	{
		
			
		SSLContextBuilder builder=new SSLContextBuilder();//.loadTrustMaterial(new File("C:\\Users\\andre\\git\\sensorsmanager\\cert\\certificate.pfx"), "natali".toCharArray(), new TrustSelfSignedStrategy());
		sslcontext=builder.build();
		
		SSLConnectionSocketFactory sslsf=new SSLConnectionSocketFactory(sslcontext,new String[] {"TLSv1"},null,SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		
		httpClient=HttpClientBuilder.create().build();
		this.httpPost=new HttpPost(url);
		this.httpPost.addHeader("content-type","application/json");
		
		
	}
	
	public RestResponse sendData(RestroomInfo info)
	{
		HttpResponse response; 
		try {
			changeGuidHeader(info.getGuid());
			httpPost.setEntity(new StringEntity(new Gson().toJson(info.getSensorData())));
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode()!=200)
				throw new Exception("ERROR: "+response.getStatusLine().getStatusCode());
			
			
			
			RestResponse restResponse=new Gson().fromJson(EntityUtils.toString(response.getEntity()), RestResponse.class);
			response.getEntity().getContent().close();
			return restResponse;
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new RestResponse(false,e.getMessage()) ;
		}
		
	}
	
	private void changeGuidHeader(UUID guid)
	{
		if (httpPost.getHeaders("restroomuid").length>0)
			httpPost.setHeader("restroomuid", guid.toString());
		else
			httpPost.addHeader("restroomuid",guid.toString());
	}

}
