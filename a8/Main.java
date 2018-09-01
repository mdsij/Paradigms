import java.net.*;
import java.io.*;
import java.util.Date;
import java.awt.Desktop;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.ArrayList;


class Main
{
    static ArrayList<Integer> clients = new ArrayList<Integer>();
    static int marioX = 200;
    static int goombaX = 400;
    static ArrayList<String> chat = new ArrayList<String>();
    
	static String getServerTime()
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
			"EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormat.format(calendar.getTime());
	}

	static void sendLine(PrintWriter out, String line)
	{
		out.print(line); // Send over the socket
		out.print("\r\n");
		System.out.println(line); // Print it to the console too, just to make debugging easier
	}

	static void onGet(OutputStream os, String url) throws Exception
	{
		PrintWriter out = new PrintWriter(os, true);
		String filename = url.substring(1); // cut off the initial "/"
		File f = new File(filename);
		Path path = Paths.get(filename);
		String dateString = getServerTime();
		System.out.println("----------The server replied: ----------");
		if(f.exists() && !f.isDirectory())
		{
			// Read the file from disk
			byte[] fileContents = Files.readAllBytes(path);

			// Send the headers
			sendLine(out, "HTTP/1.1 200 OK");
			sendLine(out, "Content-Type: " + Files.probeContentType(path));
			sendLine(out, "Content-Length: " + Integer.toString(fileContents.length));
			sendLine(out, "Date: " + dateString);
			sendLine(out, "Last-Modified: " + dateString);
			sendLine(out, "Connection: close");
			sendLine(out, "");
			out.flush();

			// Send the payload
			os.write(fileContents);
			String blobHead = fileContents.length < 60 ? new String(fileContents) : new String(fileContents, 0, 60) + "...";
			System.out.println(blobHead);
		}
		else
		{
			// Make an error message
			String payload = "404 - File not found: " + filename;

			// Send HTTP headers
			sendLine(out, "HTTP/1.1 200 OK");
			sendLine(out, "Content-Type: text/html");
			sendLine(out, "Content-Length: " + Integer.toString(payload.length()));
			sendLine(out, "Date: " + dateString);
			sendLine(out, "Last-Modified: " + dateString);
			sendLine(out, "Connection: close");
			sendLine(out, "");

			// Send the payload
			sendLine(out, payload);
		}
	}
    static void checkClients(int id)
    {
        for (int i = 0; i < clients.size(); i++) //checks if current ID is in list
        {
            if (clients.get(i) == id) return; //breaks if it is
        }
        clients.add(id); //adds ID to list of clients
    }
    static int storeLog(Json obj)
    {
        checkClients((int)obj.getLong("id"));
        if (obj.getString("action").equals("move"))
        {
            //check where ID is
            if (clients.get(0).intValue() == ((int)obj.getLong("id"))) 
            {
                marioX = (int) obj.getLong("pos"); //update marioX
                return 0;
            }
            if (clients.get(1).intValue() == ((int)obj.getLong("id"))) 
            {
                goombaX = (int) obj.getLong("pos"); //update goombaX
                return 1;
            }
        }
        else if (obj.getString("action").equals("update"))
        {
            if (clients.get(0).intValue() == ((int)obj.getLong("id"))) {return 0;}
            if (clients.get(1).intValue() == ((int)obj.getLong("id"))) {return 1;}
        }
        else if (obj.getString("action").equals("chat"))
        {
            String player = "Player " + ((int)obj.getLong("id")) + ":";
            if (clients.get(0).intValue() == ((int)obj.getLong("id")))
            {
                chat.add(player + obj.getString("val"));
                return 0;
            }
            if (clients.get(1).intValue() == ((int)obj.getLong("id")))
            {
                chat.add(player + obj.getString("val"));
                return 1;
            }
        }
        return -1;
    }
    static Json marshall(String payload)
    {
        Json ob = Json.newObject();
        ob = Json.parse(payload);
        return ob;
    }
    static String updateChat(Json message)
    {
        //return string of json ob consisting of chat storeLog
        Json ob = Json.newObject();
        Json tmpList = Json.newList();
        ob.add("chat", tmpList);
        for (int i = (int)message.getLong("log"); i < chat.size(); i++)
        {
            Json tmp = Json.newObject();
            tmp.add("chat", chat.get(i));
            tmpList.add(tmp);
        }
        return ob.toString();
    }
	static void onPost(OutputStream os, String url, char[] incomingPayload)
	{
		// Parse the incoming payload
 		System.out.println("----------------------------------------");
		String payload = String.valueOf(incomingPayload);
		System.out.println("Received the following payload: " + payload);
        
        
        Json message = marshall(payload);
        int ID = storeLog(message);
        String chats = updateChat(message);
		// Make a response
		//sends ID, goomba and mario position
        String response = "{\"ID\":" + ID + ",\"goombaX\":" + goombaX + ",\"marioX\":" + marioX + ",\"chat\":" + chats + ",\"chatSize\":" + chat.size() + "}";
		// Send HTTP headers
		System.out.println("----------The server replied: ----------");
		String dateString = getServerTime();
		PrintWriter out = new PrintWriter(os, true);
		sendLine(out, "HTTP/1.1 200 OK");
		sendLine(out, "Content-Type: application/json");
		sendLine(out, "Content-Length: " + Integer.toString(response.length()));
		sendLine(out, "Date: " + dateString);
		sendLine(out, "Last-Modified: " + dateString);
		sendLine(out, "Connection: close");
		sendLine(out, "");
		
		// Send the response
		sendLine(out, response);
		out.flush();
	}
	public static void main(String[] args) throws Exception
	{
		// Make a socket to listen for clients
		int port = 1234;
		String ServerURL = "http://localhost:" + Integer.toString(port) + "/page.html";
		ServerSocket serverSocket = new ServerSocket(port);

		// Start the web browser
		if(Desktop.isDesktopSupported())
			Desktop.getDesktop().browse(new URI(ServerURL));
		else
			System.out.println("Please direct your browser to " + ServerURL);

		// Handle requests from clients
		while(true)
		{
			Socket clientSocket = serverSocket.accept(); // This call blocks until a client connects
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			OutputStream os = clientSocket.getOutputStream();

			// Read the HTTP headers
			String headerLine;
			int requestType = 0;
			int contentLength = 0;
			String url = "";
			System.out.println("----------A client said: ----------");
			while ((headerLine = in.readLine()) != null)
			{
				System.out.println(headerLine);
				if(headerLine.length() > 3 && headerLine.substring(0, 4).equals("GET "))
				{
					requestType = 1;
					url = headerLine.substring(4, headerLine.indexOf(" ", 4));
				}
				else if(headerLine.length() > 4 && headerLine.substring(0, 5).equals("POST "))
				{
					requestType = 2;
					url = headerLine.substring(5, headerLine.indexOf(" ", 5));
				}
				else if(headerLine.length() > 15 && headerLine.substring(0, 16).equals("Content-Length: "))
					contentLength = Integer.parseInt(headerLine.substring(16));
				if(headerLine.length() < 2) // Headers are terminated by a "\r\n" line
					break;
			}

			// Send a response
			if(requestType == 1)
			{
				onGet(os, url);
			}
			else if(requestType == 2)
			{
				// Read the incoming payload
				char[] incomingPayload = new char[contentLength];
				in.read(incomingPayload, 0, contentLength);
				String blobHead = incomingPayload.length < 60 ? new String(incomingPayload) : new String(incomingPayload, 0, 60) + "...";
				System.out.println(blobHead);
				onPost(os, url, incomingPayload);
			}
			else
				System.out.println("Received bad headers. Ignoring.");

			// Hang up
			os.flush();
			clientSocket.close();
		}
	}
}
