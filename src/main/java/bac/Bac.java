package bac;

import java.math.BigInteger;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Bac {
    private Credentials credentials;
    private Web3j web3j;
    final BigInteger gasPrice = new BigInteger("1");
    final BigInteger gasLimit = new BigInteger("2100000000");

    public Bac() {
        try {
            ApplicationContext context =
                    new ClassPathXmlApplicationContext("classpath:application.xml");
            Service service = context.getBean(Service.class);
            service.run();

            ChannelEthereumService channelService = new ChannelEthereumService();
            channelService.setChannelService(service);
            channelService.setTimeout(5000);
            web3j = Web3j.build(channelService, service.getGroupId());

            AccountConfig accountConfig = context.getBean(AccountConfig.class);
            credentials = accountConfig.getCredentials();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initAssetContract(String amount) {
        ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
        try {
            BAC001 bac001 =
                    BAC001.deploy(
                                    web3j,
                                    credentials,
                                    contractGasProvider,
                                    "htlc ledger",
                                    "HTLCOIN",
                                    BigInteger.valueOf(1),
                                    new BigInteger(amount))
                            .send();
            String assetAddress = bac001.getContractAddress();
            System.out.println("assetAddress: " + assetAddress);
            System.out.println("owner: " + credentials.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public void balanceOf(String assetAddress, String address) {
        try {
            ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
            BAC001 bac001 = BAC001.load(assetAddress, web3j, credentials, contractGasProvider);
            System.out.println("balance: " + bac001.balance(address).send());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public void approve(String assetAddress, String address, String amount) {
        try {
            ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
            BAC001 bac001 = BAC001.load(assetAddress, web3j, credentials, contractGasProvider);
            bac001.approve(address, new BigInteger(amount)).send();
            System.out.println("approve successfully");
            System.out.println(
                    "amount: " + bac001.allowance(credentials.getAddress(), address).send());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }

    public void transfer(String assetAddress, String address, String amount) {
        try {
            ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
            BAC001 bac001 = BAC001.load(assetAddress, web3j, credentials, contractGasProvider);
            bac001.send(address, new BigInteger(amount), new byte[0]).send();
            System.out.println("transfer successfully");
            System.out.println("sender: " + bac001.balance(credentials.getAddress()).send());
            System.out.println("receiver: " + bac001.balance(address).send());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
