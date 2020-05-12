import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChildCommsHWB extends Thread {
    private final static int INCOME_PORT = 44444;

    public boolean LWA1Online;
    public boolean LWA2Online;
    public boolean LWA3Online;
    private S_HWB parent;
    private ArrayList<DedicatedChildCommsHWB> dedicatedChildCommsList;

    public ChildCommsHWB(S_HWB s_hwb) {
        LWA1Online = false;
        LWA2Online = false;
        LWA3Online = false;
        dedicatedChildCommsList = new ArrayList<>();
        this.parent = s_hwb;
    }

    @Override
    public void run() {
        try {
            //creem el nostre socket
            ServerSocket serverSocket = new ServerSocket(INCOME_PORT);
            while (true){
                Socket socket = serverSocket.accept();
                newDedicatedChildComms(socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void newDedicatedChildComms(Socket socket) {
        DedicatedChildCommsHWB dedicatedChildCommsHWA = new DedicatedChildCommsHWB(socket/*, s_hwa*/, this);
        dedicatedChildCommsHWA.start();
        dedicatedChildCommsList.add(dedicatedChildCommsHWA);
    }

    public void notifyChildrensToConnect() {
        for (int i = 0; i < dedicatedChildCommsList.size(); i++){
            dedicatedChildCommsList.get(i).connectToAnalogues();
        }
    }

    public void childsDone() {
        parent.myNotify();
    }

    public void childsWork() {
        for (DedicatedChildCommsHWB dedicatedChild : dedicatedChildCommsList) {
            dedicatedChild.work();
        }
    }
}
