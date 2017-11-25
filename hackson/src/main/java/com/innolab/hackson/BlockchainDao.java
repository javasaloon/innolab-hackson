package com.innolab.hackson;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class BlockchainDao {
    ObjectMapper mapper = new ObjectMapper();


    public void saveAccount(String type, BlockchainAccount account) {
        try {
            File file = new File(type + ".json");

            mapper.writeValue(file, account);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public BlockchainAccount getAccount(String type) {
        String jsonInString = null;
        try {
            File file = new File(type + ".json");

            return mapper.readValue(file, BlockchainAccount.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        BlockchainDao dao = new BlockchainDao();
        BlockchainAccount account = new BlockchainAccount();
        account.setAddress("test");
        account.setPrivKey("key");

        dao.saveAccount("tenant", account);
    }
}
