package net.fluance.cockpit.app.config.cron.whiteboard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import net.fluance.cockpit.app.config.cron.WebsocketRegexUtil;
import net.fluance.cockpit.app.service.domain.WhiteBoardService;
import net.fluance.cockpit.app.web.controller.WhiteBoardController;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardDTO;

@Component
public class WhiteboardCronHandler {
	public Map<String, WhiteBoardDTO> whiteBoards = new ConcurrentHashMap<String, WhiteBoardDTO>();
	@Autowired
	private WhiteBoardService whiteboardService;
	@Autowired
	private SimpMessagingTemplate template;

	public void check(String location,Map<String, String> parameters) {
		
		
		Integer limit = Integer.parseInt(WhiteBoardController.DEFAULT_LIMIT);
		if(parameters.containsKey("limit")) {
			limit = Integer.parseInt(parameters.get("limit"));
		}
		Integer offset = 0;
		if(parameters.containsKey("offset")) {
			offset = Integer.parseInt(parameters.get("offset"));
		}
		
		if (whiteBoards.containsKey(location)) {
			WhiteBoardDTO whiteboard = whiteboardService.getWhiteBoardIfItChanged(whiteBoards.get(location), limit, offset);
			if (whiteboard != null) {
				whiteBoards.put(location, whiteboard);
				this.template.convertAndSend(location, whiteboard);
			}
		} else {
			Long id = Long.valueOf(WebsocketRegexUtil.cutBySlash(location, 5));
			String serviceId = WebsocketRegexUtil.cutBySlash(location, 7);
			
			
			WhiteBoardDTO newWhiteboard = whiteboardService.getAWhiteBoard(id, serviceId, null, null, null, null, null, limit, offset, null, null);
			whiteBoards.put(location, newWhiteboard);
		}
	}
}
