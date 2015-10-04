package p2p.client;

import java.util.LinkedList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class P2pMessage implements EntryPoint,ClickHandler  {

	private final static GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	Button sendButton;
	String message;
	final TextBox msgField = new TextBox();
	ListBox listUser = new ListBox();
	VerticalPanel vp = new VerticalPanel();
	LinkedList<Users> users = new LinkedList<Users>();
	TextBox nome = new TextBox();
	TextBox kiave = new TextBox();
	TextBox valore = new TextBox();
	Button momentaneo = new Button("momentaneo");
	Button momentaneo1 = new Button("momentaneo1");
	static boolean oneTimes = false;
	
	@Override
	public void onModuleLoad() {
		sendButton = new Button("Send Message");
		msgField.setText("Put message here");
		sendButton.addStyleName("sendButton");
momentaneo.addClickHandler(this);
momentaneo1.addClickHandler(this);
		msgField.setFocus(true);
		msgField.selectAll();
		sendButton.addClickHandler(this);

		vp.add(msgField);
		vp.add(new HTML("<br />"));
		vp.add(sendButton);
		vp.add(new HTML("<br />"));
		vp.add(listUser);
		vp.add(new HTML("<br />"));
		vp.add(nome);
		vp.add(new HTML("<br />"));
		vp.add(kiave);
		vp.add(new HTML("<br />"));
		vp.add(valore);
		vp.add(new HTML("<br />"));
		vp.add(momentaneo);
		vp.add(new HTML("<br />"));
		vp.add(momentaneo1);
		RootPanel.get().add(vp);
		vp.setCellHorizontalAlignment(msgField, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(sendButton, HasHorizontalAlignment.ALIGN_CENTER);
		vp.setCellHorizontalAlignment(listUser, HasHorizontalAlignment.ALIGN_CENTER);
		vp.getElement().setAttribute("align", "center");
		
		
		greetingService.getDocument(new AsyncCallback<String>() {


			@Override
			public void onFailure(Throwable caught) {
				Window.alert("error");

			}

			@Override
			public void onSuccess(String result) {
				 Window.alert(result);
				parseMessage(result);

			}
		});
	}


	public void addName(){
		for(Users usr: users){
			listUser.addItem(usr.getName());
		}
		listUser.setVisibleItemCount(1);
		String value = com.google.gwt.user.client.Window.Location.getParameter("id");
		String value1 = com.google.gwt.user.client.Window.Location.getParameter("name");
		if(!oneTimes)
		if( value != null && !value.contentEquals("")  &&  !value.isEmpty()  &&  value != "null"  &&  !value1.contentEquals("")  && !value1.isEmpty()  &&  value1 != null  && value1 != "null" ){
			aggiungoXML(value1, value, "true");
//			Window.alert(value+value1);
			
		}
	}

	public void createXML(String name,String key,String enable){
		Document doc = XMLParser.createDocument();

		Element root = doc.createElement("Devices");
		doc.appendChild(root);

		Element node1 = doc.createElement("User");
		node1.setAttribute("value",name);
		root.appendChild(node1);

		Element node2 = doc.createElement("key");
		node2.setAttribute("value",key);
		root.appendChild(node2);

		Element node3 = doc.createElement("enable");
		node3.setAttribute("value",enable);
		root.appendChild(node3);

		System.out.println(doc.toString());
		parseMessage(doc.toString());
	}


	public void parseMessage(String messageXml) {
		try {

			Document messageDom = XMLParser.parse(messageXml);
			for(int i = 0; i < messageDom.getElementsByTagName("User").getLength();i++){
				Node fromNode = messageDom.getElementsByTagName("User").item(i);
				String user = ((Element)fromNode).getAttribute("name");
				String key = ((Element)fromNode).getAttribute("key");
				String active = ((Element)fromNode).getAttribute("active");
				users.add(new Users(user, key,active));
			}
			addName();
		} catch (DOMException e) {
//			Window.alert("Could not parse data fetch from server.");
			Window.Location.reload();
			
		}
	}

	
	
	public void httpRequest(String message, final String key, final String name){
		
		if(message == "" || message == null || message.isEmpty())
			message ="contenuto del messaggio nullo";
		String postUrl="http://raelixx.ns0.it:80/won/sendScript.php?&id="+key+"&msg="+message;
		//		createXML(name, key, "true");
		//        String requestData="?&id=mio&msg=ciao%20come%20staiiiii";
		final char apiciAscii = (char) 34;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, postUrl);
		try {
			// builder.sendRequest(requestData.toString(), new RequestCallback() 
			builder.sendRequest(postUrl.toString(), new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode())
					{
						if(response.getText().contains("success"+apiciAscii+":1,"))
							Window.alert("Il messaggio e' stato spedito Correttamente a: "+name+" con id: "+key);
						else if(response.getText().contains("failure"+apiciAscii+":1,"))
							Window.alert("Messaggio Non Spedito");
						else Window.alert(response.getText());
					} else {
						Window.alert("Received HTTP status code other than 200 : "+ response.getStatusText());
						
					}

				}
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
				}
			});
		} catch (RequestException e) {
			Window.alert(e.getMessage());
		}
	}
	
	
	
	public void httpNativeMessage(String message, final String key, final String name){

		if(message == "" || message == null || message.isEmpty())
			message ="contenuto del messaggio nullo";
		String postUrl="http://raelixx.ns0.it:80/won/sendScript.php?&id="+key+"&msg="+message;
		//		createXML(name, key, "true");
		//        String requestData="?&id=mio&msg=ciao%20come%20staiiiii";
		final char apiciAscii = (char) 34;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, postUrl);
		builder.setHeader("Authorization: key=AIzaSyDEZDpgDCEIcTX7O3WxOFEtpgi2p5iUDrg", "Content-Type: application/json");
		builder.setRequestData("APA91bERc0R_oWu8lq-JDJ-7WwmSpAdXNLnT3wBZe3sfvcNzkQfOWvh75eD34b0zhDXyhRMtilaJBV8ORUsVuHLi7-0YrZ4V7YemlakXHQe3GK2Lja90IuNX2I0fmTfb1Z__1gktfrx0R9CqAUM2sVHO8qHP7AMkiA");
		try {
			// builder.sendRequest(requestData.toString(), new RequestCallback() 
			builder.sendRequest(postUrl.toString(), new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode())
					{
						if(response.getText().contains("success"+apiciAscii+":1,"))
							Window.alert("Il messaggio e' stato spedito Correttamente a: "+name+" con id: "+key);
						else if(response.getText().contains("failure"+apiciAscii+":1,"))
							Window.alert("Messaggio Non Spedito");
//						else Window.alert(response.getText());
					} else {
						Window.alert("Received HTTP status code other than 200 : "+ response.getStatusText());
						
					}

				}
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
				}
			});
		} catch (RequestException e) {
			Window.alert(e.getMessage());
		}
	}

	public void prova(String nome,String kiave){
		
	
		String postUrl="http://raelixx.ns0.it:80/won/addKey.php?&id="+kiave+"&usr="+nome;
		//		createXML(name, key, "true");
		//        String requestData="?&id=mio&msg=ciao%20come%20staiiiii";
		final char apiciAscii = (char) 34;
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, postUrl);
		try {
			// builder.sendRequest(requestData.toString(), new RequestCallback() 
			builder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode())
					{
						
//					 Window.alert(response.getText());
					} else {
						Window.alert("Received HTTP status code other than 200 : "+ response.getStatusText());
						
					}

				}
				@Override
				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
				}
			});
		} catch (RequestException e) {
			Window.alert(e.getMessage());
		}
	}
	
	public  void aggiungoXML(String nome,String kiave,String valore){
		for(Users usr: users){
			if(usr.getName().contentEquals(nome) || usr.getKey().contentEquals(kiave)){
				Window.alert("Dati di registazione errati");
				return;
				}
			}
		prova(nome,kiave);
		greetingService.addDocument(nome, kiave, valore, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.toString());
				return;
			}

			@Override
			public void onSuccess(String result) {
				Window.alert(result);
				
				Window.Location.assign(GWT.getHostPageBaseURL());
				return;
			}
		});
	}
	
	@Override
	public void onClick(ClickEvent event) {
		if(event.getSource() == sendButton){
			message = msgField.getText();
			for(Users usr: users){
				if(usr.getName().equalsIgnoreCase(listUser.getItemText(listUser.getSelectedIndex()))){
					httpRequest(message,usr.getKey(),usr.getName());
				return;
				}
			}

		}
		if(event.getSource() == momentaneo1){prova( nome.getText(), kiave.getText());}
		if(event.getSource() == momentaneo){
			if(nome.getText() == "" || kiave.getText() == "" || nome.getText() == null || nome.getText().isEmpty() || kiave.getText()== null || kiave.getText().isEmpty() || valore.getText()== null || valore.getText().isEmpty()  || valore.getText() == ""){
				Window.alert("Dati di registazione errati");
			return;
			}
			for(Users usr: users){
				if(usr.getName().contentEquals(nome.getText()) || usr.getKey().contentEquals(kiave.getText()) || usr.getActive().contentEquals(valore.getText())){
					Window.alert("Dati di registazione errati");
					return;
					}
			}
			
			aggiungoXML(nome.getText(), kiave.getText(), valore.getText());
		}
	}
}
