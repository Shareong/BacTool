import bac.Bac;

public class Application {
    public static void main(String[] args) {
        try {
            String method = args[0];
            Bac bac = new Bac();
            if (method.equalsIgnoreCase("init")) {
                bac.initAssetContract(args[1]);
            } else if (method.equalsIgnoreCase("balanceOf")) {
                bac.balanceOf(args[1], args[2]);
            } else if (method.equalsIgnoreCase("approve")) {
                bac.approve(args[1], args[2], args[3]);
            } else if (method.equalsIgnoreCase("transfer")) {
                bac.transfer(args[1], args[2], args[3]);
            } else {
                System.out.println("method not found");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
