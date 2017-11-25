package com.innolab.hackson;

public class BlockchainManager {
    BlockchainService blockchainService = new BlockchainService();
    BlockchainDao dao = new BlockchainDao();

    public BossAccount createBossAccount() {
        BlockchainAccount tenant = dao.getAccount("tenant");
        System.out.println(tenant);
        BlockchainAccount app = dao.getAccount("app");
        System.out.println(app);
        BlockchainAccount operator = dao.getAccount("operator");
        System.out.println(operator);

        BossAccount bossAccount = new BossAccount();
        BlockchainAccount user = blockchainService.createUser(app, operator);
        Card asset = blockchainService.createAsset(user);
        CardAccount assetAccount = blockchainService.createAssetAccount(user);

        bossAccount.setBlockchainAccount(user);
        bossAccount.setAsset(asset);
        bossAccount.setAssetAccount(assetAccount);

        dao.saveAccount("boss", bossAccount);
        return bossAccount;
    }

    public static void main(String[] args) {
        BlockchainManager manager = new BlockchainManager();
        manager.createBossAccount();
    }
}
