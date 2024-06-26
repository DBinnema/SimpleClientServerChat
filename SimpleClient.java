import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    boolean connectionDesired;
    private ServerConnection myServerConnection;

    // Default values;
    private int defaultPort = 1177;
    private String defaultIP = "localhost";

    private String Username;

    public SimpleClient(String clientName){

        this.Username = clientName;


    }



    

    public void StartConnection(TextArea outputTextArea) throws IOException {
       

        

        try {
            connectionDesired = true;
            clientSocket = new Socket(defaultIP, defaultPort);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // ServerConnectionFor this client
            myServerConnection = new ServerConnection(Username, outputTextArea);
            myServerConnection.start();
            
            SendMessage(Username);


        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void SendMessage(String msg) throws IOException {
          

      
        out.println(msg);
    }

    public void StopConnection() throws IOException {
        connectionDesired = false;
        in.close();
        out.close();
        clientSocket.close();
    }

    public class ServerConnection extends Thread {

        String serverMessages;

        TextArea outputTextArea;
        String clientUsername;

        public ServerConnection(String clientUsername, TextArea outputTextArea) {
            this.outputTextArea = outputTextArea;
            this.clientUsername =  clientUsername;

        }

        public void run() {


            //Maybe send client ID

            try {
                while ((serverMessages = in.readLine()) != null && connectionDesired) {

                    // While there's messages just print them to console

                    outputTextArea.append("\n" + serverMessages);

                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
