package com.innolab.hackson;

public class BlockchainManager {
    BlockchainService blockchainService = new BlockchainService();
    BlockchainDao dao = new BlockchainDao();
    BlockchainAccount tenant = dao.getAccount("tenant");
    BlockchainAccount app = dao.getAccount("app");
    BlockchainAccount operator = dao.getAccount("operator");

    public BossAccount createBossAccount(boolean isBoss, String name) {

        BossAccount bossAccount = new BossAccount();

        BlockchainAccount user = blockchainService.createUser(app, operator);
        bossAccount.setBlockchainAccount(user);

        CardAccount assetAccount = blockchainService.createAssetAccount(user);
        bossAccount.setAssetAccount(assetAccount);

        if (isBoss) {
            Card asset = blockchainService.createAsset(user);
            bossAccount.setAsset(asset);

            dao.saveAccount(name, bossAccount);
        } else {
            dao.saveAccount(name, bossAccount);
        }

        return bossAccount;
    }


    public static void main(String[] args) {
        BlockchainManager manager = new BlockchainManager();
    }



    private static void initUsers(BlockchainManager manager) {
        for (int i = 0; i < 3; i++) {
            manager.createBossAccount(true, "boss" + i);
        }
        for (int i = 0; i < 10; i++) {
            manager.createBossAccount(false, "player" + i);
        }
    }
}
