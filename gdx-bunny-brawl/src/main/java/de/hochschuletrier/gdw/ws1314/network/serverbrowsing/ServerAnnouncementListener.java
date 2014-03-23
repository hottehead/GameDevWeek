package de.hochschuletrier.gdw.ws1314.network.serverbrowsing;

import java.util.ArrayList;

public interface ServerAnnouncementListener{
	public void serverAnnouncementsChanged(ArrayList<ServerAnnouncement> announcements);
}
