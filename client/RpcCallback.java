package de.tet.inputmaskTemplate.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.tet.inputmaskTemplate.client.logic.Callback;

/**
 * Objects of this class provides a generic Callback-Function for asynchronous RPC-Calls.
 * @author Martin Predki
 *
 * @param <T> Type of the RPC-Call
 */
public class RpcCallback<T> implements AsyncCallback<T> {

	private T result;
	private Callback<T> callable;

	public RpcCallback(Callback<T> callable)
	{
		this.callable = callable;
	}
	
	@Override
	public void onFailure(Throwable caught) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(T result) {
		this.result = result;
		
		if(this.callable != null)
			this.callable.call(result);
	}	
	
	public T getResult()
	{
		return this.result;
	}
}
