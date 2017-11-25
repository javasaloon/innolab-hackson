package com.innolab.hackson;

public class BossAccount {
    BlockchainAccount blockchainAccount;
    Card asset;
    CardAccount assetAccount;

    public BlockchainAccount getBlockchainAccount() {
        return blockchainAccount;
    }

    public void setBlockchainAccount(BlockchainAccount blockchainAccount) {
        this.blockchainAccount = blockchainAccount;
    }

    public Card getAsset() {
        return asset;
    }

    public void setAsset(Card asset) {
        this.asset = asset;
    }

    public CardAccount getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(CardAccount assetAccount) {
        this.assetAccount = assetAccount;
    }
}
