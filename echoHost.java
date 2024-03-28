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

        System.out.println("Starting host on port: " + port);

        while (true) {
            // Accepts client socket connections and starts a thread
            clientConnection connectingClient = new clientConnection(serverSocket.accept());
            connectedClients.add(connectingClient);
            connectingClient.start();
            System.out.println("New Client Connected");
        }
    }

    private class clientConnection extends Thread {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public clientConnection(Socket socket) {
            clientSocket = socket;
        }

        private void sendMessage(String message) {
            out.println(message);
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

                out.println("You connected to chat.");

                // Communication loop
                String inputString;
                while (connectionDesired) {

                    if ((inputString = in.readLine()) != null) {

                        if (inputString.contains("logout")) {
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
