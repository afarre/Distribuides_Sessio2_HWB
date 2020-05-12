public class Main {
    private final static int INCOME_PORT_LWB1 = 65555;
    private final static int INCOME_PORT_LWB2 = 65556;

    private final static int OUTGOING_HWB_PORT = 54444;
    private final static int OUTGOING_LWB1_PORT = 65555;
    private final static int OUTGOING_LWB2_PORT = 65556;

    public static void main(String[] args) {
        Menu menu = new Menu();
        int opcio = menu.showMenu();

        if (opcio == 1){
            new S_HWB().run();

        }else {
            //SM_HWB sm_hwa = new SM_HWB();
        }
    }

}
