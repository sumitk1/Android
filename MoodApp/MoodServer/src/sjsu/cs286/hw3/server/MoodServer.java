package sjsu.cs286.hw3.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MoodServer {
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message, topic;

	HashMap<String, Integer> moods = new HashMap<String, Integer>();

	MoodServer() {
	}

	void run() {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(7890, 20);

			// 2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());

			// 3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Mood Server");

			// 4. The two parts communicate via the input and output streams
			do {
				try {
					message = (String) in.readObject();
					System.out.println("client>" + message);

					int colonPosition = message.indexOf(":");

					String command = message.substring(0, colonPosition);
					String content = message.substring(colonPosition + 1,
							message.length());

					if (command.equals("Register")) {

						if (moods.get(content) != null) {
							message = "Sorry";
							sendMessage("Sorry\n");
						} else {
							moods.put(content, 0);
							sendMessage("Okay\n");
						}
						
						// Get a set of the entries
						Set set = moods.entrySet();

						// Get an iterator
						Iterator i = set.iterator();

						// Display elements
						while (i.hasNext()) {
							Map.Entry me = (Map.Entry) i.next();
							System.out.print(me.getKey() + ": ");
							System.out.println(me.getValue());
						}

						sendMessage("Okay");

					} else if (command.equals("GetTopic")) {
						
						if (topic.equals("") || topic.equals(null)) {
							sendMessage("NoTopic");
						} else {
							sendMessage(topic);
						}
                        	

					} else if (command.equals("GetMoods")) {
						
						if (moods.isEmpty()) {
							sendMessage("NoMoods");
						} else {
							sendMessage(moods.toString());
						}
                        	

					} else if (command.equals("Topic")) {
						
						topic = content;
                        for (Object key: moods.keySet()) {
                            moods.put(key.toString(), 0);
                        }
                        sendMessage("Okay\n");	

					} else if (command.equals("Mood")) {
						
						int split = content.indexOf(":");
                        int mood = Integer.parseInt(content.substring(0, split));
                        String name = content.substring(split+1);
                        if(moods.get(name) != null && mood>=0 && mood<=5) {
                            moods.put(name, mood);
                            sendMessage("Okay\n");
                        }
                        else{
                            message = "Sorry";                                           
                            sendMessage("Sorry\n"); 
                        }

					} else if (command.equals("Status")) {
						
						String status = "Topic:"+topic+"\n";
                        for (Object key: moods.keySet())
                            status += "Mood:"+moods.get(key)+":"+key+"\n";                                            
                        sendMessage(status);

					} else {

						message = "Sorry";
						sendMessage("Sorry");
					}

					if (message.equals("Bye"))
						sendMessage("Bye");
				} catch (ClassNotFoundException classnot) {
					System.err.println("Data received in unknown format");
				} catch (IndexOutOfBoundsException indexOut) {
					System.err.println("Command does not have colon");
				}

			} while (!message.equals("Bye"));

		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {

			// 4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server sent ->" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String args[]) {
		MoodServer server = new MoodServer();
		while (true) {
			server.run();
		}
	}
}