package de.hochschuletrier.gdw.ws1314.network.serverbrowsing;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBrowser extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ServerBrowser.class);
	private volatile boolean shutdown = false;
	private DatagramSocket socket;
	private ConcurrentLinkedQueue<ServerAnnouncement> pendingAnnouncement = new ConcurrentLinkedQueue<>();

	// Following members are main-thread-only:
	private ArrayList<ServerAnnouncement> currentAnnouncements = new ArrayList<>();
	private ArrayList<ServerAnnouncementListener> listeners = new ArrayList<>();

	public ServerBrowser() throws SocketException{
		setDaemon(true);
		socket = new DatagramSocket();
		start();
	}

	@Override
	public void run(){
		try{
			while(!shutdown){
				DatagramPacket dgram = new DatagramPacket(new byte[4], 4);
				socket.receive(dgram);
				byte[] data = dgram.getData();
				String address = dgram.getAddress().getHostAddress();
				int port = (data[0] << 8) | data[1];
				int clients = data[2];
				boolean busy = data[3] != 0;
				pendingAnnouncement.add(new ServerAnnouncement(address, port, clients, busy));
			}
		}
		catch (IOException e){
			logger.error("ServerBroswer: IOException", e);
		}

	}

	public void shutdown(){
		shutdown = true;
		socket.close();
	}

	// Called from main thread:
	public void refreshServerList(){
		currentAnnouncements.clear();
		fireListeners();
		try{
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			while(ifaces.hasMoreElements()){
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				for(InterfaceAddress ifaceAddr:iface.getInterfaceAddresses()){
					InetAddress bCast = ifaceAddr.getBroadcast();
					if(bCast != null){
						socket.send(new DatagramPacket(new byte[4], 4, new InetSocketAddress(bCast, ServerAnnouncer.ANNOUNCER_PORT)));
					}
				}
			}
		}
		catch (SocketException e){
			logger.error("ServerBrowser: refreshServerList: SocketException",e);
		}
		catch (IOException e){
			logger.error("ServerBrowser: refreshServerList: IOException",e);
		}

	}

	public void processPendingAnnouncements(){
		ServerAnnouncement announcement;
		boolean modified = false;
		while((announcement = pendingAnnouncement.poll()) != null){
			modified = true;
			currentAnnouncements.add(announcement);
		}
		if(modified) fireListeners();
	}

	private void fireListeners(){
		for(ServerAnnouncementListener listener:listeners){
			listener.serverAnnouncementsChanged(currentAnnouncements);
		}
	}

	public boolean addServerAnnouncementListener(ServerAnnouncementListener l){
		return listeners.add(l);
	}

	public boolean removeServerAnnouncementListener(ServerAnnouncementListener l){
		return listeners.remove(l);
	}
	
	
}
