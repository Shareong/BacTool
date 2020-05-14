import ledger.Command;

public class Application {
    public static void main(String[] args) {
        try {
            String method = args[0];
            Command command = new Command();
            if (method.equalsIgnoreCase("init")) {
                command.initAssetContract(args[1]);
            } else if (method.equalsIgnoreCase("balanceOf")) {
                command.balanceOf(args[1], args[2]);
            } else if (method.equalsIgnoreCase("approve")) {
                command.approve(args[1], args[2], args[3]);
            } else if (method.equalsIgnoreCase("transfer")) {
                command.transfer(args[1], args[2], args[3]);
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
