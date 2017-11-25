package com.innolab.hackson;

import cn.bubi.baas.account.AccountManageService;
import cn.bubi.baas.account.User;
import cn.bubi.baas.account.UserInfo;
import cn.bubi.baas.account.tenant.Tenant;
import cn.bubi.baas.account.tenant.TenantInfo;
import cn.bubi.baas.account.tenant.TenantManageService;
import cn.bubi.baas.base.BlockchainCertificate;
import cn.bubi.baas.base.security.SecureIdentity;
import cn.bubi.baas.contract.ContractManageService;
import cn.bubi.baas.sdk.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ChaincodeManager {
    BlockchainService blockchainService = new BlockchainService();
    BlockchainDao dao = new BlockchainDao();
    BlockchainAccount tenant = dao.getAccount("tenant");
    BlockchainAccount app = dao.getAccount("app");
    BlockchainAccount operator = dao.getAccount("operator");
    private static final String BAAS_HOST = "39.106.70.242";
    private static final int BAAS_PORT = 8080;
    private static final BlockchainServiceFactory serviceFactory = new BlockchainServiceFactory(BAAS_HOST, BAAS_PORT);


    public static void main(String[] args) {
        ChaincodeManager manager = new ChaincodeManager();

//        manager.deployChaincode();
        manager.executeChaincode();

    }



    public void executeChaincode() {

        System.out.println("deployChaincode ... ");
        BlockchainAccount blockchainAccount = dao.read("chaincode", BlockchainAccount.class);

        SecureIdentity appId = new SecureIdentity(app.getAddress(), app.getPrivKey());
        SecureIdentity operatorId = new SecureIdentity(operator.getAddress(), operator.getPrivKey());
        BlockchainSession session = serviceFactory.createSession(appId, operatorId);

        TransactionTemplate tx = session.beginTransaction();
        BlockchainAccount newAccount = new BlockchainAccount();
        try {
            ContractManageService service = tx.forService(ContractManageService.class);


            ChaincodeData input = new ChaincodeData();
            service.execute(blockchainAccount.getAddress(), input);

            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                ptx.sign(operator.getAddress(), operator.getPrivKey());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(newAccount);

        dao.saveAccount("chaincode", newAccount);

    }

    public void deployChaincode() {

        System.out.println("deployChaincode ... ");


        SecureIdentity appId = new SecureIdentity(app.getAddress(), app.getPrivKey());
        SecureIdentity operatorId = new SecureIdentity(operator.getAddress(), operator.getPrivKey());
        BlockchainSession session = serviceFactory.createSession(appId, operatorId);

        TransactionTemplate tx = session.beginTransaction();
        BlockchainAccount newAccount = new BlockchainAccount();
        try {
            ContractManageService service = tx.forService(ContractManageService.class);
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();


            String chaincode = readChaincode();
            service.define(identity, chaincode);

            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);

                newAccount.setAddress(identity.getAddress());
                newAccount.setPrivKey(identity.getAuthPrivateKey().getEncodedValue());

                ptx.sign(operator.getAddress(), operator.getPrivKey());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(newAccount);

        dao.saveAccount("chaincode", newAccount);

    }


    public String readChaincode() {
        BufferedReader br = null;
        FileReader fr = null;
        StringBuffer stringBuffer = new StringBuffer();
        try {

            fr = new FileReader("hackson.js");
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
                stringBuffer.append(sCurrentLine);
            }

            return stringBuffer.toString();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        return stringBuffer.toString();
    }


}
