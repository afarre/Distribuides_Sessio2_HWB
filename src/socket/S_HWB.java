package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class S_HWB extends Thread{
    private final static int INCOME_PORT = 33334;
    private final static int OUTGOING_PORT = 33333;
    private final static String TOKEN_B = "TOKEN_B";
    private final static String TOKEN_A = "TOKEN_A";

    private DataInputStream diStream;
    private DataOutputStream doStream;
    private Socket outSocket;

    private ChildCommsHWB childCommsHWB;

    public S_HWB(){
        childCommsHWB = new ChildCommsHWB(this);
        childCommsHWB.start();
    }

    private void handShake() {
        try {
            String read = diStream.readUTF();
            System.out.println("I'm B. I recieved the follwing message: \"" + read + "\"");
            doStream.writeUTF("I'm process B. Writing handshake to A.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void run() {
        createIncomeConnection();
        createOutcomeConnection();
        //handShake();
        System.out.println("Waiting for childs to connect...\n");
        while (true){
            readFromHWA();
        }
    }

    private void readFromHWA() {
        try {
            System.out.println("Waiting for a message from HWA...");
            String read = diStream.readUTF();
            if (read.equals(TOKEN_A)) {
                System.out.println("I'm B. I received the following message: " + read);
                childCommsHWB.childsWork();
            }else {
                readFromHWA();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToHWA() {
        try {
            System.out.println("Writing token to A\n");
            doStream.writeUTF(TOKEN_B);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createIncomeConnection() {
        try {
            //creem el nostre socket
            ServerSocket serverSocket = new ServerSocket(INCOME_PORT);
            //esperem a la conexio del HeavyWeight_B
            Socket incomeSocket = serverSocket.accept();
            diStream = new DataInputStream(incomeSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOutcomeConnection() {
        boolean wait = true;
        while (wait) {
            // Averiguem quina direccio IP hem d'utilitzar
            InetAddress iAddress;
            try {
                iAddress = InetAddress.getLocalHost();
                String IP = iAddress.getHostAddress();
                outSocket = new Socket(String.valueOf(IP), OUTGOING_PORT);
                doStream = new DataOutputStream(outSocket.getOutputStream());
            } catch (ConnectException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outSocket != null) {
                    wait = false;
                }
            }
        }
    }

    public void notifyHWA() {
        writeToHWA();
    }
}
