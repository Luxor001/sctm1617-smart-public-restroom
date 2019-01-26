package io.rest.observer;



import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import model.manager.FakeRestroomSM;
import model.manager.structures.restroom.RestroomInfo;

public class RestroomDataOutput<T extends Object> implements Observer<T> {
	
	private Observable<Object> fakeRestroom;
	private Disposable fakeRestroomDisposable;
	private boolean isSubscribed=false;
	private RestClient client;
	
	public RestroomDataOutput(FakeRestroomSM fakeRestroom) throws Exception
	{
		this.fakeRestroom=fakeRestroom;
		client=new RestClient("https://luxor001.duckdns.org/smart-public-restroom/api/devices/send");
	}
	
	
	
	public void subscribeToFakeRestroom()
	{
		if (!isSubscribed)
			fakeRestroom.subscribe((Observer<Object>)this);
		isSubscribed=true;
		
	}
	
	public void unsubscribeToFakeRestroom()
	{
		if (isSubscribed)
			this.fakeRestroomDisposable.dispose();
		isSubscribed=false;
	}

	@Override
	public void onSubscribe(Disposable d) {
		this.fakeRestroomDisposable=d;
		
		
	}

	@Override
	public void onNext(T t) {
		if (t instanceof RestroomInfo)
		{
			RestroomInfo data=(RestroomInfo) t;
			RestResponse response=client.sendData(data);
			if (response != null && !response.result) unsubscribeToFakeRestroom();
			
		}
		
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplete() {
		
		
	}

}
