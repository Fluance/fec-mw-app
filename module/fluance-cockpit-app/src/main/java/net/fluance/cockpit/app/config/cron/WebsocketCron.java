package net.fluance.cockpit.app.config.cron;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.fluance.cockpit.app.config.cron.whiteboard.WhiteboardCronHandler;
import net.fluance.cockpit.app.config.websocket.WebSocketConfig;

@Component
public class WebsocketCron {
	private static final Logger log = LoggerFactory.getLogger(WebsocketCron.class);


	@Autowired
	private WebSocketConfig websocketConfig;

	@Autowired
	private WhiteboardCronHandler whiteboardCronHandler;
	
	private boolean isInitialised;

	@Scheduled(fixedRate = 5000)
	public void whiteboardSubscriptionCron() {
		if(!isInitialised) {
		  log.info("[CRON] The Cron for the Whiteboard is working!");
		  isInitialised=true;
		}
		for (String location : getAllNotifications()) {
			log.info("Location:{}", location);
			Map<String, String> parameters = getPropertyFromUrl(location);
			Cron kind = WebsocketRegexUtil.getKind(location);
			switch (kind) {
			case WHITEBOARD:
				 whiteboardCronHandler.check(location,parameters);
				break;
			default:
				break;
			}

		}

	}

	private List<String> getAllNotifications() {
		Set<String> allLocations = new HashSet<String>();
		for (String key : websocketConfig.subscribedUsers.keySet()) {
			allLocations.add(websocketConfig.subscribedUsers.get(key).getDestination());
		}
		return new ArrayList<String>(allLocations);
	}

	private Map<String, String> getPropertyFromUrl(String url) {
		if(!url.contains("?")) {
			return null;
		}
		Map<String, String> parameters = new ConcurrentHashMap<String, String>();
		String urlParameters = url.split("\\?")[1];
		for(String parameter:urlParameters.split("&")) {
			String[] match = parameter.split("=");
			parameters.put(match[0], match[1]);
		}
		
		return parameters;
	}
}
