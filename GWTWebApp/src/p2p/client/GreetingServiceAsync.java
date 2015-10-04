package p2p.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)throws IllegalArgumentException;
	void getDocument(AsyncCallback<String> callback);
	void addDocument(String name, String key, String value, AsyncCallback<String> callback);
}
