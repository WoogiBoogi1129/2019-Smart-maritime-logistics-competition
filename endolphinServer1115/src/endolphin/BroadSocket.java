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
 * 외부에서 WebSocketServer.jsp로 접속을 하게 되면 아래의 클래스가 실행이 된다.
 * @author gomgun
 * 
 *
 */
@ServerEndpoint("/communication")
public class BroadSocket {

	private static List<Session> clients = Collections.synchronizedList((new ArrayList<Session>()));

	
	/**
	* 웹 소켓으로부터 메시지가 오면 호출한다.
	* @param message 메시지
	* @param userSession
	* @throws IOException
	*/

	@OnMessage // onMessage가 호출이 되면 현재 클래스에서 아래의 자바 함수가 수행된다.
	public void onMessage(String message, Session userSession) throws IOException {
		System.out.println("onMessage 호출");
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
	* 웹 소켓이 접속되면 유저리스트에 세션을 넣는다.
	* @param userSession 웹 소켓 세션
	*/
	@OnOpen
	public void onOpen(Session userSession) {
		clients.add(userSession);
	
	}

	
	/**
	* 웹소켓을 닫으면 해당 유저를 유저리스트에서 뺀다.
	* @param userSession
	*/
	@OnClose
	public void onClose(Session userSession) {
		clients.remove(userSession);
	}
	


}