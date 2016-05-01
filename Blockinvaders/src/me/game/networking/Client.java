package me.game.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import me.game.pack.Frame;

public class Client implements Runnable
{
	public boolean IsConnectedToServer = false;
	public String LatestServerReply = "";
	public boolean waitingforreply = false;
	public boolean running = true;
	public Thread thread = null;

	private Frame game;
	GetServerMessages gsm;
	
	public String format;
	public Socket socket;
	public DataOutputStream dout;
	public DataInputStream din;

	public Client(String host, int port) {
		this.game = Frame.getInstance();
		gsm = new GetServerMessages(this.game);
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


	public void processMessage(String message) {
		try {
			dout.writeUTF(message);
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
						gsm.CheckServerMessages(message);
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

	public void DisconnectFromServer(){
		try {
			this.din.close();
			this.dout.close();
			this.socket.close();
		}catch (Exception e) {}
		try{this.socket = null;}catch(Exception e){}
		try{this.din = null;}catch(Exception e){}
		try{this.dout = null;}catch(Exception e){}
		try{this.thread = null;}catch(Exception e){}
		try{this.running = false;}catch(Exception e){}
	}
}