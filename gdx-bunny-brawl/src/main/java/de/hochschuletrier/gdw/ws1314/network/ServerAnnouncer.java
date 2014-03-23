package de.hochschuletrier.gdw.ws1314.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerAnnouncer extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(ServerAnnouncer.class);
	public static int ANNOUNCER_PORT=54294;
	private volatile boolean shutdown = false;
	private short port;
	private volatile boolean busy=false;
	private volatile byte clients;
	private DatagramSocket socket;
	
	public ServerAnnouncer(short port) throws SocketException{
		setDaemon(true);
		this.port=port;
		socket = new DatagramSocket(new InetSocketAddress(ANNOUNCER_PORT));
		start();
	}

	@Override
	public void run(){
		try{
			while(!shutdown){
				DatagramPacket request = new DatagramPacket(new byte[4], 4);
				socket.receive(request);
				byte[] data = new byte[]{(byte) (port>>>8),(byte) (port&0xFF),clients,(byte) (busy?1:0)};
				DatagramPacket response = new DatagramPacket(data, data.length, request.getSocketAddress());
				socket.send(response);
			}
		}
		catch (IOException e){
			logger.error("Server-Announcer: IOException",e);
		}
	}

	public byte getClients(){
		return clients;
	}

	public void setClients(byte clients){
		this.clients = clients;
	}

	
	public boolean isBusy(){
		return busy;
	}

	public void setBusy(boolean busy){
		this.busy = busy;
	}

	public void shutdown(){
		shutdown = true;
		socket.close();
	}
}
