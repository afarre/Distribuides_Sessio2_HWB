import socket.S_HWB;
import xarxa.X_HWB;

public class Main {

    public static void main(String[] args) {
        Menu menu = new Menu();
        int opcio = menu.showMenu();

        if (opcio == 1){
            new S_HWB().start();

        }else {
            X_HWB x_hwb = new X_HWB();
            x_hwb.start();
        }
    }

}
