package de.hochschuletrier.gdw.ws1314.hud.elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.ws1314.network.ChatListener;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

public class ChatWindow extends Table implements ChatListener {

	private ScrollPane scrollPane;
	private TextField textField;
	private List<ListElement> messages;
	private Skin skin;
	private Logger logger = LoggerFactory.getLogger(ChatWindow.class);

	public ChatWindow(Skin skin) {
		setSkin(skin);
		this.skin = skin;
		NetworkManager.getInstance().addChatListener(this);
		textField = new TextField("Schreibe eine Nachricht", skin);

		textField.addListener(new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ENTER) {
					sendMessage();
				}
				return true;
			}
		});
		messages = new List<ListElement>(skin);
		messages.getItems().add(
				new ListElement("Willkommen im Lobbychat von Bunny Brawl!", skin));

		scrollPane = new ScrollPane(messages, skin);
		scrollPane.setScrollingDisabled(false, false);
		scrollPane.setHeight(10f);
		scrollPane.setForceScroll(true, false);
		scrollPane.setFlickScroll(true);
		scrollPane.setScrollbarsOnTop(false);
		scrollPane.setFadeScrollBars(false);
		this.add(scrollPane).colspan(2).fill().expand();
		this.row();
		this.add(textField).expand(true, false).fill(true, false);
		TextButton send = new TextButton("Send", skin);
		send.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				sendMessage();
			}
		});
		this.add(send);
	}

	public void sendMessage() {
		String message = textField.getText();
		if(message.equals("")) {
			return;
		}
		logger.info("Sending Message: " + message);
		NetworkManager.getInstance().sendChat(message);
		textField.setText("");
	}

	@Override
	public void chatMessage(String sender, String text) {
		logger.info("Putting Message: " + text);
		messages.getItems().add(new ListElement(sender + ": " + text, skin));
		messages.invalidateHierarchy();
		scrollPane.scrollTo(0, scrollPane.getScrollBarHeight(), 0, 0);

		scrollPane.setScrollPercentY(1f);
		scrollPane.setScrollY(scrollPane.getScrollY());
		scrollPane.updateVisualScroll();

	}

}