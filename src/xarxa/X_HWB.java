package xarxa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class X_HWB extends Thread {
    private final static int INCOME_PORT = 45691;
    private final static int OUTGOING_PORT = 45678;
    private final static String TOKEN_B = "TOKEN_B";
    private final static String TOKEN_A = "TOKEN_A";

    private DataOutputStream outcomeDoStream;
    private DataInputStream outcomeDiStream;

    private DataOutputStream incomeDoStream;
    private DataInputStream incomeDiStream;
    private Socket outSocket;

    private final static String LWA1 = "LWA1";
    private final static String LWA2 = "LWA2";
    private final static String LWA3 = "LWA3";

    /** Constants per al algoritme de lamport **/
    private final static String LAMPORT_REQUEST = "LamportRequest";
    private final static String RESPONSE_REQUEST = "ResponseRequest";
    private final static String REMOVE_REQUEST = "RemoveRequest";
    private final static String PORT = "PORT";
    private final static String PROCESS = "HWB";
    private final static String TOKEN = "TOKEN";


    @Override
    public void run() {
        try {
            createOutcomeConnection();
            outcomeDoStream.writeUTF(PORT);
            outcomeDoStream.writeInt(INCOME_PORT);
            outcomeDoStream.writeUTF(PROCESS);
            createIncomeConnection();
            System.out.println("Waiting for everyone to be connected...");

            while (true){
                String request = incomeDiStream.readUTF();
                readRequest(request);

            }
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
                outcomeDoStream = new DataOutputStream(outSocket.getOutputStream());
                outcomeDiStream = new DataInputStream(outSocket.getInputStream());
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

    private void createIncomeConnection() {
        try {
            //creem el nostre socket
            ServerSocket serverSocket = new ServerSocket(INCOME_PORT);
            //esperem a la conexio de la xarxa de comunicacions
            Socket incomeSocket = serverSocket.accept();
            incomeDiStream = new DataInputStream(incomeSocket.getInputStream());
            incomeDoStream = new DataOutputStream(incomeSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readRequest(String request) throws IOException {
        switch (request){
            case TOKEN:
                System.out.println("Got work");
                break;
        }
    }
}
