package edu.udacity.java.nano.chat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.udacity.java.nano.chat.Message.messageType;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;



@Component
@ServerEndpoint("/chat")
public class WebSocketChatServer {
	
    private static Map<String, Session> onlineSessions = new ConcurrentHashMap<>();


    private static void sendMessageToAll(String msg) {
        
    	for(Session s : onlineSessions.values())
    	{
    		try {
    			/*
    			 * Got how to send messages back to client from this website
    			 * https://www.pegaxchange.com/2018/01/28/websocket-server-java/
    			 */
				s.getBasicRemote().sendText(msg);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
    	}
    	
    }

    
    @OnOpen
    public void onOpen(Session session) {
        
    	onlineSessions.putIfAbsent(session.getId(), session);
    	String msg = "{\"type\":\""+messageType.ENTER+"\", \"onlineCount\":\""+onlineSessions.size()+"\"}";
    	sendMessageToAll(msg);
    		
    }

    
    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        
    	Message message = null;
		try {
			/*
			 * Got how to parse String to JSON using the example given here
			 * https://www.java67.com/2016/10/3-ways-to-convert-string-to-json-object-in-java.html
			 */
			message = new ObjectMapper().readValue(jsonStr, Message.class);
		} catch (JsonParseException e) {
			
			e.printStackTrace();
		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
		
		String msg = "{\"type\":\""+messageType.SPEAK+"\",\"onlineCount\":\""+onlineSessions.size()+"\",\"username\""
				+ ":\""+message.getUsername()+"\",\"msg\":\""+message.getMsg()+"\"}";
		sendMessageToAll(msg);
    	
    }

    
    @OnClose
    public void onClose(Session session) {
        
    	onlineSessions.remove(session.getId());
    	String msg = "{\"type\":\""+messageType.QUIT+"\", \"onlineCount\":\""+onlineSessions.size()+"\"}";
    	sendMessageToAll(msg);
    }

    
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
