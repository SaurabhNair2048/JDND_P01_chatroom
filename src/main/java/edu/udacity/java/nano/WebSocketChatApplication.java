package edu.udacity.java.nano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class WebSocketChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketChatApplication.class, args);
    }

    
    @GetMapping("/")
    public ModelAndView login() {
        return new ModelAndView("/login");
    }

    
    @GetMapping("/index")
    public ModelAndView index(String username, HttpServletRequest request) throws UnknownHostException {
    	ModelAndView mnv = new ModelAndView();
    	mnv.addObject("username", username);
    	/*
    	 * Found out about sending the websocketurl object from the student hub chats
    	 * So this part is mostly as has been discussed in those chats
    	 */
    	String webSocketUrl = "ws://"+request.getServerName();
    	webSocketUrl += ":8080";//+request.getServerPort();
    	webSocketUrl += request.getContextPath();
    	webSocketUrl += "/chat";
    	mnv.addObject("webSocketUrl", webSocketUrl);
    	mnv.setViewName("/chat");
    	return mnv;
    }
}
