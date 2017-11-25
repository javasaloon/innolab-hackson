package hello;

import cn.bubi.baas.account.Account;

public class BlockchainAccount {
    Account account;

    String privKey;


    public void setAccount(Account account) {
        this.account = account;
    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

    public Account getAccount() {
        return account;
    }

    public String getPrivKey() {
        return privKey;
    }
}
