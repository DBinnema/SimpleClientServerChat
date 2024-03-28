import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class echoHost {

    private ServerSocket serverSocket;
    private List<clientConnection> connectedClients;
    private String sessionChatLog;

    public static echoHost host;

    public static void main(String[] args) throws IOException {
        echoHost server = new echoHost();
        server.start(1177);
    }

    public void start(int port) throws IOException {

        serverSocket = new ServerSocket(port);
        connectedClients = new ArrayList<clientConnection>();

        System.out.println("[Server] Starting host on port: " + port);

        while (true) {
            // Accepts client socket connections and starts a thread
            clientConnection connectingClient = new clientConnection(serverSocket.accept(), "Client Name");
            connectedClients.add(connectingClient);
            connectingClient.start();


            System.out.println("[Server]" + connectingClient.getConnectionNameString() + "connected.");


                    



        }
    }

    private class clientConnection extends Thread {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        private String clientName;
        private String prefix = "-";

        private String getConnectionNameString(){
            return clientName;

        }


        public clientConnection(Socket socket, String clientNameString) {
            clientSocket = socket;
            clientName = clientNameString;
        }

        private void sendMessage(String message) {
            out.println(prefix+"["+clientName+"]-"+message);
        }

        private void broadcastMessage(String message) {
            for (clientConnection c : connectedClients) {
                c.sendMessage(message);
            }
        }

        public void run() {

            boolean connectionDesired = true;
            

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //This Should be a server broadcast,
                //Now just announce your connection
                broadcastMessage(clientName+ " Connected");

                // Communication loop
                String inputString;
                while (connectionDesired) {

                    if ((inputString = in.readLine()) != null) {

                        if (inputString.contains("logout")) {

                            broadcastMessage(clientName + "User D/C");





                            connectionDesired = false;
                            break;
                        } else {
                            System.out.println(inputString);
                            broadcastMessage(inputString);
                        }
                    }
                }

                broadcastMessage("User Disconnected");
                // Disconnecting closes....
                connectedClients.remove(this);
                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                System.out.println("Issue with client communication streams...");
            }
        }
    }
}
