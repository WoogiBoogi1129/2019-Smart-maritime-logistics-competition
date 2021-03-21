package endolphin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 * �ܺο��� WebSocketServer.jsp�� ������ �ϰ� �Ǹ� �Ʒ��� Ŭ������ ������ �ȴ�.
 * @author gomgun
 * 
 *
 */
@ServerEndpoint("/communication")
public class BroadSocket {

	private static List<Session> clients = Collections.synchronizedList((new ArrayList<Session>()));

	
	/**
	* �� �������κ��� �޽����� ���� ȣ���Ѵ�.
	* @param message �޽���
	* @param userSession
	* @throws IOException
	*/

	@OnMessage // onMessage�� ȣ���� �Ǹ� ���� Ŭ�������� �Ʒ��� �ڹ� �Լ��� ����ȴ�.
	public void onMessage(String message, Session userSession) throws IOException {
		System.out.println("onMessage ȣ��");
		System.out.println(message);
		synchronized (clients) {
			// Iterate over the connected sessions
			// and broadcast the received message
			for (Session client : clients) {
				if (!client.equals(userSession)) {
					
					client.getBasicRemote().sendText(message);
						
				}
			}
		}

	}

	/**
	* �� ������ ���ӵǸ� ��������Ʈ�� ������ �ִ´�.
	* @param userSession �� ���� ����
	*/
	@OnOpen
	public void onOpen(Session userSession) {
		clients.add(userSession);
	
	}

	
	/**
	* �������� ������ �ش� ������ ��������Ʈ���� ����.
	* @param userSession
	*/
	@OnClose
	public void onClose(Session userSession) {
		clients.remove(userSession);
	}
	


}