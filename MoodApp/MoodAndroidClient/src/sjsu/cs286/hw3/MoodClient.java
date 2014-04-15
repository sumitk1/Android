package sjsu.cs286.hw3;

import java.io.*;
import java.net.*;

import android.util.Log;

public class MoodClient {
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	

	MoodClient() {
	}

	
	public String testConnect (String serverURL) {
		
		String message = "";
		
		Log.i("MoodClient_testConnect ", "serverURL -" + serverURL + "-");
		
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket(serverURL, 7890);
			
			Log.i("testConnect", "Connected to server "+ serverURL +" on port 7890");
			
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			// 3: Communicating with the server
			
			try {
				message = (String) in.readObject();
				Log.i("serverSays ->", "message = "+ message);
								
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("data received in unknown format");
			}
		
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
				Log.i("CloseConnection", "in Test Connection finally block");
			} catch (Exception ioException) {
				ioException.printStackTrace();
			}
		}
		
		return message;	
	}
	
	public String getTopic (String serverURL) {
		
		String message = "";
		
		Log.i("MoodClient_getTopic ", "serverURL -" + serverURL + "-");
		
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket(serverURL, 7890);
			
			Log.i("getTopic", "Connected to server "+ serverURL +" on port 7890");
			
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			// 3: Communicating with the server
			
			try {
				message = (String) in.readObject();
				Log.i("serverSays ->", "message = "+ message);
				
				String messageToSend = "GetTopic:\n";
				Log.i("CommandSend", "-" + messageToSend + "-");
				
				sendMessage(messageToSend);
				message = (String) in.readObject();
				Log.i("serverSaysAfter ->", "message = "+ message);
				
				
				
			} catch (ClassNotFoundException classNot) {
				System.err.println("data received in unknown format");
			}
		
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
				Log.i("CloseConnection", " in getTopic close connection block");
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		return message;
		
	}
	
	public String getMoods (String serverURL) {
		
		String message = "";
		
		Log.i("MoodClient_getMoods ", "serverURL -" + serverURL + "-");
		
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket(serverURL, 7890);
			
			Log.i("getMoods", "Connected to server "+ serverURL +" on port 7890");
			
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			// 3: Communicating with the server
			
			try {
				message = (String) in.readObject();
				Log.i("serverSays ->", "message = "+ message);
				
				String messageToSend = "GetMoods:\n";
				Log.i("CommandSend", "-" + messageToSend + "-");
				
				sendMessage(messageToSend);
				message = (String) in.readObject();
				Log.i("serverSaysAfter ->", "message = "+ message);
				
			} catch (ClassNotFoundException classNot) {
				System.err.println("data received in unknown format");
			}
		
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		return message;
		
	}

	
	public String register (String userName, String serverURL) {
		
		String message = "";
		
		Log.i("MoodClient_register ", "userName - " + userName);
		Log.i("MoodClient_register ", "serverURL -" + serverURL + "-");
		
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket(serverURL, 7890);
			
			Log.i("ServerConnected", "Connected to server "+ serverURL +" on port 7890");
			
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			// 3: Communicating with the server
			
			try {
				message = (String) in.readObject();
				Log.i("serverSays ->", "message = "+ message);
				
				String messageToSend = "Register:" + userName;
				Log.i("CommandSend", "-" + messageToSend + "-");
				
				sendMessage(messageToSend);
				message = (String) in.readObject();
				Log.i("serverSaysAfter ->", "message = "+ message);
				
				
				
			} catch (ClassNotFoundException classNot) {
				System.err.println("data received in unknown format");
			}
		
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
		return "Okay";
	}

	public void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			Log.i("MoodClient_sendMessage ", "[" + msg + "] ");
			
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}


}