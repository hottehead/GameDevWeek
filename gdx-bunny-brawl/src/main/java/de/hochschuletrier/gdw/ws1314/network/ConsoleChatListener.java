package de.hochschuletrier.gdw.ws1314.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleChatListener implements ChatListener{
	private static final Logger logger = LoggerFactory.getLogger(ConsoleChatListener.class);

	@Override
	public void chatMessage(String sender, String text){
		logger.info("{}: {}", sender, text);
	}
}
