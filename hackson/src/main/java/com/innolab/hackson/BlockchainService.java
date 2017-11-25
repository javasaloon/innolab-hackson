package com.innolab.hackson;

import cn.bubi.baas.account.*;
import cn.bubi.baas.account.Application;
import cn.bubi.baas.account.tenant.Tenant;
import cn.bubi.baas.account.tenant.TenantInfo;
import cn.bubi.baas.account.tenant.TenantManageService;
import cn.bubi.baas.base.BlockchainCertificate;
import cn.bubi.baas.base.security.SecureIdentity;
import cn.bubi.baas.sdk.*;

public class BlockchainService {
    private static final String ROOT_ADDRESS = "a001d3d4a9f436505c02f00945dced61cb9a5fd01df144";
    private static final String ROOT_PRIVATE_KEY = "c00155912ecc141ba36968b7f5e1452f9f09659e16a19638127b45c0956bc7389d0aee";

    private static final String BAAS_HOST = "39.106.70.242";
    private static final int BAAS_PORT = 8080;
    private static final BlockchainServiceFactory serviceFactory = new BlockchainServiceFactory(BAAS_HOST, BAAS_PORT);

    public static void main(String[] args) {

        BlockchainService blockchainService = new BlockchainService();

        BlockchainAccount tenant = blockchainService.createTenant();

        BlockchainAccount app = blockchainService.createApp(tenant);

        BlockchainAccount operator = blockchainService.createOperator(app);

        BlockchainAccount user = blockchainService.createUser(app, operator);


    }

    private BlockchainAccount createUser(BlockchainAccount app, BlockchainAccount operator) {

        System.out.println("create user ... ");

        SecureIdentity appId = new SecureIdentity(app.getAccount().getAddress(), app.getPrivKey());
        SecureIdentity operatorId = new SecureIdentity(operator.getAccount().getAddress(), operator.getPrivKey());
        BlockchainSession session = serviceFactory.createSession(appId, operatorId);

        TransactionTemplate tx = session.beginTransaction();
        BlockchainAccount newAccount = new BlockchainAccount();
        try {
            AccountManageService accountManageService = tx.forService(AccountManageService.class);
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();

            UserInfo info = new UserInfo();
            info.setDescription("InnoLab Hackson User");


            User newEntry = accountManageService.register(identity, info);

            ActionResultHolder<User> resultHolder = tx.prepare(newEntry, User.class);
            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                newEntry = resultHolder.getResult();
                newAccount.setAccount(newEntry);
                newAccount.setPrivKey(identity.getAuthPrivateKey().getEncodedValue());
                ptx.sign(app.getAccount().getAddress(), app.getPrivKey());
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
        return newAccount;
    }

    private BlockchainAccount createOperator(BlockchainAccount parentAccount) {
        System.out.println("create operator ... ");
        String address = parentAccount.getAccount().getAddress();
        String privKey = parentAccount.getPrivKey();

        SecureIdentity channel = new SecureIdentity(address, privKey);
        BlockchainSession session = serviceFactory.createSession(channel);

        TransactionTemplate tx = session.beginTransaction();
        BlockchainAccount newAccount = new BlockchainAccount();
        try {
            AccountManageService accountManageService = tx.forService(AccountManageService.class);
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();

            AppOperatorInfo info = new AppOperatorInfo();
            info.setDescription("InnoLab Hackson Operator");


            AppOperator newEntry = accountManageService.register(identity, info);

            ActionResultHolder<AppOperator> resultHolder = tx.prepare(newEntry, AppOperator.class);
            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                newEntry = resultHolder.getResult();
                ptx.sign(address, privKey);
                newAccount.setAccount(newEntry);
                newAccount.setPrivKey(identity.getAuthPrivateKey().getEncodedValue());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                ptx.rollback();
                throw e;
            }
        } catch (Exception e) {
            tx.cancel();
            throw e;
        }
        System.out.println(newAccount);
        return newAccount;
    }


    private BlockchainAccount createApp(BlockchainAccount account) {
        System.out.println("create app ... ");
        Application application;
        String address = account.getAccount().getAddress();
        String privKey = account.getPrivKey();
        SecureIdentity channel = new SecureIdentity(address, privKey);
        BlockchainSession session = serviceFactory.createSession(channel);

        TransactionTemplate tx = session.beginTransaction();
        BlockchainAccount newAccount = new BlockchainAccount();
        try {
            AccountManageService accountManageService = tx.forService(AccountManageService.class);
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();

            ApplicationInfo info = new ApplicationInfo();
            info.setDescription("InnoLab Hackson App");


            application = accountManageService.register(identity, info);

            ActionResultHolder<Application> resultHolder = tx.prepare(application, Application.class);
            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                application = resultHolder.getResult();
                ptx.sign(address, privKey);
                newAccount.setAccount(application);
                newAccount.setPrivKey(identity.getAuthPrivateKey().getEncodedValue());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                ptx.rollback();
                throw e;
            }
        } catch (Exception e) {
            tx.cancel();
            throw e;
        }
        System.out.println(newAccount);
        return newAccount;
    }

    private BlockchainAccount createTenant() {
        System.out.println("create tenant ... ");
        SecureIdentity channel = new SecureIdentity(ROOT_ADDRESS, ROOT_PRIVATE_KEY);
        BlockchainSession session = serviceFactory.createSession(channel);

        TransactionTemplate tx = session.beginTransaction();

        BlockchainAccount newAccount = new BlockchainAccount();

        try {
            TenantManageService tenantManageService = tx.forService(TenantManageService.class);

            TenantInfo tenantInfo = new TenantInfo();
            tenantInfo.setDescription("InnoLab Tenant");


            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();
            Tenant tenant = tenantManageService.register(identity, tenantInfo);

            ActionResultHolder<Tenant> resultHolder = tx.prepare(tenant, Tenant.class);
            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                ptx.sign(ROOT_ADDRESS, ROOT_PRIVATE_KEY);
                tenant = resultHolder.getResult();
                newAccount.setAccount(tenant);
                newAccount.setPrivKey(identity.getAuthPrivateKey().getEncodedValue());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                ptx.rollback();
                throw e;
            }

        } catch (Exception e) {
            tx.cancel();
            throw e;
        }
        System.out.println(newAccount);

        return newAccount;
    }


}
