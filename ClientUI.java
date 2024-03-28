import java.awt.BorderLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class ClientUI {

    public static void main(String[] args) throws IOException, InterruptedException {

        // Maybe a login Window

        SimpleClient myClient = new SimpleClient();

        // Links the client ui with the UI
        TextArea chatArea = BuildMainPannel(myClient);

        // Start connection With a client Name and defining the output area for the
        // return messages.
        myClient.StartConnection("Default Client Name", chatArea);

        // check if we can make a connection to the server

        boolean chatInSession = true;
        while (chatInSession) {

            // myClient.SendMessage(inputChat);
            Thread.sleep(2000);
            // String serverOutput = client1.receiveMessage();

        }
        System.out.print("Exited Client UI Loop");
    }

    private static JFrame BuildLoginUI() {

        JFrame jFrame = new JFrame("Simple Chat Login");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(450, 300);

        JLabel welcomeLabel = new JLabel("Welcome");
        jFrame.getContentPane().add(welcomeLabel);
        jFrame.setVisible(true);
        return jFrame;

    }

    private static TextArea BuildMainPannel(SimpleClient client) {

        // Creates Client Main Window Frame
        JFrame jFrame = new JFrame("Simple Chat Client");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(450, 300);

        // Area to display Messages
        TextArea messageArea = new TextArea();
        messageArea.setEditable(false);

        jFrame.getContentPane().add(BorderLayout.CENTER, messageArea);

        // Bannel For the Bottom Bar
        JPanel chatPanel = new JPanel();

        // Elements for botom bar
        JLabel label = new JLabel("Type messages here");
        JTextField chatField = new JTextField(20);

        // Button for message sending
        JButton button = new JButton("Send");

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    client.SendMessage(chatField.getText());
                } catch (IOException e1) {
                    System.out.print("Error.. Button pressed but could not send message.");
                    e1.printStackTrace();
                }

            }

        });

        chatPanel.add(label);
        chatPanel.add(chatField);
        chatPanel.add(button);

        jFrame.getContentPane().add(BorderLayout.SOUTH, chatPanel);

        jFrame.setVisible(true);

        return messageArea;

    }

}
