package net.fluance.cockpit.app.config.cron;

public class WebsocketRegexUtil {
	private final static String WHITEBOARD = "\\/topics\\/whiteboards\\/companies\\/[0-9]*\\/services\\/[0-9a-zA-Z]*.*";
	
	public static Cron getKind(String url) {
		if(url.matches(WHITEBOARD)) {
			return Cron.WHITEBOARD;
		}
		return Cron.NONE;
	}
	
	public static String cutBySlash(String url,int position) {
		String[] array = url.split("/");
		return array[position-1];
	}
}
