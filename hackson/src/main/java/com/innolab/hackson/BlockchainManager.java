package com.innolab.hackson;

import java.util.ArrayList;
import java.util.List;

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

    public List<BossAccount> getAccounts(boolean isBoss, String name) {
        List<BossAccount> accountList = new ArrayList<>();

        if (isBoss) {
            for (int i = 0; i < 3; i++) {
                BossAccount account = dao.read("boss" + i, BossAccount.class);
                accountList.add(account);
            }

        } else {
            for (int i = 0; i < 10; i++) {
                BossAccount account = dao.read("player" + i, BossAccount.class);
                accountList.add(account);
            }
        }
        return accountList;
    }

    public static void main(String[] args) {
        BlockchainManager manager = new BlockchainManager();
        List<BossAccount> list = manager.getAccounts(true, "boss");
        for (BossAccount boss : list) {
            System.out.println(boss);
        }
        list = manager.getAccounts(false, "player");
        for (BossAccount boss : list) {
            System.out.println(boss);
        }
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
