package hello;

import hello.UserData;
import cn.bubi.baas.base.BlockchainAddress;
import cn.bubi.baas.base.security.SecureIdentity;
import cn.bubi.baas.data.DataService;
import cn.bubi.baas.sdk.*;

public class BlockchainService {
    private static final String CHANNEL_ADDRESS = "hackson_channel";
    private static final String CHANNEL_PRIVATE_KEY = "hackson_channel_key";
    private static final String USER_ADDRESS = "hackson_user";
    private static final String USER_PRIVATE_KEY = "hackson_user_key";

    private static final String BAAS_HOST = "39.106.70.242";
    private static final int BAAS_PORT = 8080;
    private static final BlockchainServiceFactory serviceFactory = new BlockchainServiceFactory(BAAS_HOST, BAAS_PORT);

    public static void main(String[] args) {
        UserData data = new UserData();
        data.setId("12342312412312");
        data.setName("Jacky");

        insert(data);

    }

    private static void insert(UserData data) {
        SecureIdentity channel = new SecureIdentity(CHANNEL_ADDRESS, CHANNEL_PRIVATE_KEY);
        SecureIdentity user = new SecureIdentity(USER_ADDRESS, USER_PRIVATE_KEY);

        BlockchainSession session = serviceFactory.createSession(channel, user);

        BlockchainAddress dataBlockchainAddress = session.getSecureKeyGenerator().generateBubiAddress();

        TransactionTemplate tx = session.beginTransaction();
        DataService dataService = tx.forService(DataService.class);

        ActionResultHolder<String> insertResultHolder = tx.prepare(dataService.insert(dataBlockchainAddress, data), String.class);

        PreparedTransaction ptx = tx.complete();

        String txHash = ptx.getHash();
        String dataAddress = insertResultHolder.getResult();

        ptx.sign(USER_ADDRESS, USER_PRIVATE_KEY);

        ptx.commit();
    }
}
