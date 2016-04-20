package me.game.networking;

import java.io.*;
import java.net.*;

public class Client implements Runnable
{
	public static boolean IsConnectedToServer = false;
	public static String LatestServerReply = "";
	public boolean waitingforreply = false;
	public boolean running = true;
	public Thread thread = null;
	public static Client connection = null;

	public String format;
	public Socket socket;
	public DataOutputStream dout;
	public DataInputStream din;

	public Client( String host, int port) {
		try {
			socket = new Socket( host, port );
			din = new DataInputStream( socket.getInputStream() );
			dout = new DataOutputStream( socket.getOutputStream() );
			IsConnectedToServer = true;
			thread = new Thread( this );
			thread.start();
		}catch( IOException ie ){
			DisconnectFromServer();
		}

		running = true;
	}


	public static void processMessage(String message) {
		try {
			connection.dout.writeUTF(message);
		} catch( Exception ie ){
			ie.printStackTrace();
			System.out.println( ie ); 
		}

	}

	public void run() {
		try {
			while (running) {
				if(IsConnectedToServer == true){
					String message = null;
					try{
						message = din.readUTF();
						LatestServerReply = message;
						GetServerMessages.CheckServerMessages(message);
						waitingforreply = false;
					}catch(Exception e){
						IsConnectedToServer = false;
						e.printStackTrace();
					}
				}
			}
		}catch( Exception ie ){
			ie.printStackTrace();
			IsConnectedToServer = false;
		} finally {
			try{
				if(dout != null){
					dout.close();
				}
				if(din != null){
					din.close();
				}
				if(socket != null){
					socket.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public static void DisconnectFromServer(){
		try {
			connection.din.close();
			connection.dout.close();
			connection.socket.close();
		}catch (Exception e) {}
		try{connection.socket = null;}catch(Exception e){}
		try{connection.din = null;}catch(Exception e){}
		try{connection.dout = null;}catch(Exception e){}
		try{connection.thread = null;}catch(Exception e){}
		try{connection.running = false;}catch(Exception e){}
		try{connection = null;}catch(Exception e){}
	}

	public static boolean ConnectToServer(String ip, int port){
		try{
			connection = new Client(ip, port);
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}
}