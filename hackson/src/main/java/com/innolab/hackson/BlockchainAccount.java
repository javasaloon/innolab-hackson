package com.innolab.hackson;

import cn.bubi.baas.account.Account;

public class BlockchainAccount {
//    Account account;

    String privKey;

    String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public void setAccount(Account account) {
//        this.account = account;
//    }

    public void setPrivKey(String privKey) {
        this.privKey = privKey;
    }

//    public Account getAccount() {
//        return account;
//    }

    public String getPrivKey() {
        return privKey;
    }

    @Override
    public String toString() {
        return " ======================> " + address + "; " + privKey;
    }
}
