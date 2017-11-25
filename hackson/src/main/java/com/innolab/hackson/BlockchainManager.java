package com.innolab.hackson;

import cn.bubi.baas.asset.AssetQuantity;
import cn.bubi.baas.asset.AssetQueryService;

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

    public void issueAssets(BossAccount boss, int amount) {
        blockchainService.issueAsset(boss.getAsset().getAddress(), boss.getAssetAccount().getAddress(), amount, boss.getBlockchainAccount());
    }

    public long queryAsserts(BossAccount boss) {
        AssetQuantity[] assetQuantities = blockchainService.queryAsset(null, boss.getAssetAccount().getAddress(), boss.getBlockchainAccount());
        if (assetQuantities.length>0){
            return assetQuantities[0].getAmount();
        }
        return 0;
    }

    public void transferAsserts(BossAccount boss, BossAccount player, int amount) {

        blockchainService.transferAsset(boss.getAsset().getAddress(), boss.getAssetAccount().getAddress(),
                player.getAssetAccount().getAddress(), amount, boss.getBlockchainAccount());



    }

    public void transferAsserts(long boss_id, long player_id, int amount) {

        try {
            BossAccount boss = dao.read("boss" + boss_id, BossAccount.class);
            BossAccount player = dao.read("player" + player_id, BossAccount.class);
            blockchainService.transferAsset(boss.getAsset().getAddress(), boss.getAssetAccount().getAddress(),
                    player.getAssetAccount().getAddress(), amount, boss.getBlockchainAccount());

            System.out.println("after transfer boss ---------- > " + queryAsserts(boss));
            System.out.println("after transfer player ---------- > " + queryAsserts(player));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BlockchainManager manager = new BlockchainManager();
        List<BossAccount> bossList = manager.getAccounts(true, "boss");
        for (BossAccount boss : bossList) {
            System.out.println(boss);
            manager.issueAssets(boss, 10000);
            System.out.println("boss ---------- >" + manager.queryAsserts(boss));
        }

//        List<BossAccount> playerList = manager.getAccounts(false, "player");
//        for (BossAccount player : playerList) {
//            System.out.println(player);
//            System.out.println("player ---------- > " + manager.queryAsserts(player));
//
////            manager.transferAsserts(bossList.get(1), player, 10);
//
//
//        }
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
