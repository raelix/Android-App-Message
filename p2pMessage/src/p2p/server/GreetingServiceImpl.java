package p2p.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.w3c.dom.*;

import javax.xml.parsers.*;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import p2p.client.GreetingService;
import p2p.shared.FieldVerifier;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
GreetingService {

	public static String dataBase = "users.xml";
	
	@Override
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException(
					"Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo
				+ ".<br><br>It looks like you are using:<br>" + userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public String getDocument() {
		final char apiciAscii = (char) 34;				//utilizzo questa chiave di prova che in futuro verrà recuperata attraverso metodo POST dalla pagina e si occuperà di salvarla nel database PS questa chiave viene generata
														////in android e quindi è recuperabile solo da li.
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		String line = "";
		try {
			br = new BufferedReader(new FileReader(new File (dataBase)));
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			try {
			FileWriter writer= new FileWriter(new File (dataBase));
			writer.write("<Devices>");	
			writer.write("\n");
			writer.write("<User active="+apiciAscii+"true"+apiciAscii+"");
			writer.write(" key="+apiciAscii+"APA91bERc0R_oWu8lq-JDJ-7WwmSpAdXNLnT3wBZe3sfvcNzkQfOWvh75eD34b0zhDXyhRMtilaJBV8ORUsVuHLi7-0YrZ4V7YemlakXHQe3GK2Lja90IuNX2I0fmTfb1Z__1gktfrx0R9CqAUM2sVHO8qHP7AMkiA"+apiciAscii+" name="+apiciAscii+"gm"+apiciAscii+"/>");
			writer.write("\n");
			writer.write("</Devices>");
			writer.flush();
			writer.close();
//			P2pMessage.refresh();
			return "file creato, installa l'app per aggiungere i contatti o recupera la tua chiave";
			} catch ( IOException e1) {
				return "non è stato possibile ne' leggere ne' creare un nuovo database, hai bisogno di maggiori privilegi";
				
			}
			
		}   

		return sb.toString();}

	@Override
	public String addDocument(String names, String key, String value) {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		Document document = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(dataBase);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element root = document.getDocumentElement();
		Element rootElement = document.getDocumentElement();
		root.normalize();
		rootElement.normalize();
		Element name = document.createElement("User");
	
		document.normalize();
		document.normalizeDocument();
		name.setAttribute("name",names);
		name.setAttribute("key",key);
		name.setAttribute("active",value);
		rootElement.appendChild(name);
		root.appendChild(name);
		root.normalize();
		rootElement.normalize();
		DOMSource source = new DOMSource(document);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		document.normalize();
		document.normalizeDocument();
		root.normalize();
		rootElement.normalize();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(dataBase);
			transformer.transform(source, result);
		} catch (TransformerException e) {
			
			e.printStackTrace();
			return "save error";
		}
		return "new user saved";

	}






}
