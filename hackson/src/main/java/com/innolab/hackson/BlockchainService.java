package com.innolab.hackson;

import cn.bubi.baas.account.*;
import cn.bubi.baas.account.Application;
import cn.bubi.baas.account.tenant.Tenant;
import cn.bubi.baas.account.tenant.TenantInfo;
import cn.bubi.baas.account.tenant.TenantManageService;
import cn.bubi.baas.asset.*;
import cn.bubi.baas.base.BlockchainCertificate;
import cn.bubi.baas.base.security.SecureIdentity;
import cn.bubi.baas.data.DataService;
import cn.bubi.baas.sdk.*;

public class BlockchainService {
    private static final String ROOT_ADDRESS = "a001d3d4a9f436505c02f00945dced61cb9a5fd01df144";
    private static final String ROOT_PRIVATE_KEY = "c00155912ecc141ba36968b7f5e1452f9f09659e16a19638127b45c0956bc7389d0aee";

    private static final String BAAS_HOST = "39.106.70.242";
    private static final int BAAS_PORT = 8080;
    private static final BlockchainServiceFactory serviceFactory = new BlockchainServiceFactory(BAAS_HOST, BAAS_PORT);


    public static void main(String[] args) {
        BlockchainDao dao = new BlockchainDao();
        BlockchainService blockchainService = new BlockchainService();

//        BlockchainAccount tenant = blockchainService.createTenant();
//        dao.saveAccount("tenant", tenant);
        BlockchainAccount tenant = dao.getAccount("tenant");
        System.out.println(tenant);


//        BlockchainAccount app = blockchainService.createApp(tenant);
//        dao.saveAccount("app", app);

        BlockchainAccount app = dao.getAccount("app");
        System.out.println(app);

//        BlockchainAccount operator = blockchainService.createOperator(app);
//        dao.saveAccount("operator", operator);

        BlockchainAccount operator = dao.getAccount("operator");
        System.out.println(operator);

//        BlockchainAccount user1 = blockchainService.createUser(app, operator);
//        dao.saveAccount("user1", user1);
//        BlockchainAccount user2 = blockchainService.createUser(app, operator);
//        dao.saveAccount("user2", user2);


        BlockchainAccount user1 = dao.getAccount("user1");
        System.out.println(user1);
        BlockchainAccount user2 = dao.getAccount("user2");
        System.out.println(user2);

//        Card asset = blockchainService.createAsset(user1);
//        dao.saveAccount("user1-card", asset);
//        CardAccount assetAccount = blockchainService.createAssetAccount(user1);
//        dao.saveAccount("user1-asset-account", assetAccount);

        Card asset = dao.read("user1-card", Card.class);
        System.out.println(asset);
        CardAccount assetAccount = dao.read("user1-asset-account", CardAccount.class);
        System.out.println(assetAccount);


        String txHash = blockchainService.issueAsset(asset.getAddress(), assetAccount.getAddress(), 3, user1);

        blockchainService.queryAsset(asset.getAddress(), assetAccount.getAddress(), 3, user1);

//
//        String result = blockchainService.insertData(user);
    }

    //???

    public String queryAsset(String assetAddress, String assetAccountAddress, long amount, BlockchainAccount user) {

        System.out.println("create user ... ");

        SecureIdentity userId = new SecureIdentity(user.getAddress(), user.getPrivKey());

        BlockchainSession session = serviceFactory.createSession(userId);  //  ????
        String txHash = null;

        try {
            BlockchainQuerier querier = session.getQuerier();
            AssetQueryService service = querier.forService(AssetQueryService.class);


            AssetQuantity[] assetQuantities = service.queryAssets(assetAccountAddress);

            for (int i = 0; i < assetQuantities.length; i++) {
                AssetQuantity quantity = assetQuantities[i];
                System.out.println("getAmount ========== " + quantity.getAmount());
            }


            return txHash;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String insertData(BlockchainAccount user) {

        System.out.println("insert data ... ");

        SecureIdentity userId = new SecureIdentity(user.getAddress(), user.getPrivKey());

        BlockchainSession session = serviceFactory.createSession(userId);  //  ????
        String txHash = null;
        TransactionTemplate tx = session.beginTransaction();
        try {
            DataService service = tx.forService(DataService.class);

            AssetAccountInfo<String> info = new AssetAccountInfo<>();
            info.setDescription("InnoLab Hackson issue Asset ");
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();

            tx.prepare(service.insert(identity, new UserData()), String.class);

            PreparedTransaction ptx = tx.complete();
            try {
                txHash = ptx.getHash();
                System.out.printf(txHash);
                ptx.sign(user.getAddress(), user.getPrivKey());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(identity.getAddress());
            return identity.getAddress();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String issueAsset(String assetAddress, String assetAccountAddress, long amount, BlockchainAccount user) {

        System.out.println("create user ... ");

        SecureIdentity userId = new SecureIdentity(user.getAddress(), user.getPrivKey());

        BlockchainSession session = serviceFactory.createSession(userId);  //  ????
        String txHash = null;
        TransactionTemplate tx = session.beginTransaction();
        try {
            AssetManageService service = tx.forService(AssetManageService.class);

            AssetAccountInfo<String> info = new AssetAccountInfo<>();
            info.setDescription("InnoLab Hackson issue Asset ");

//????
            service.issue(assetAddress, assetAccountAddress, amount);


            PreparedTransaction ptx = tx.complete();
            try {
                txHash = ptx.getHash();
                System.out.printf(txHash);
                ptx.sign(user.getAddress(), user.getPrivKey());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(txHash);
            return txHash;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CardAccount createAssetAccount(BlockchainAccount user) {

        System.out.println("create user ... ");

        CardAccount cardAccount = new CardAccount();


        SecureIdentity userId = new SecureIdentity(user.getAddress(), user.getPrivKey());

        BlockchainSession session = serviceFactory.createSession(userId);  //  ????

        TransactionTemplate tx = session.beginTransaction();
        try {
            AssetManageService service = tx.forService(AssetManageService.class);
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();


            AssetAccountInfo<String> info = new AssetAccountInfo<>();
            info.setDescription("InnoLab Hackson Asset Account");


            tx.prepare(service.createAccount(identity, info, user.getAddress(), null, false), String.class);

            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                cardAccount.setCode(txHash);
                ptx.sign(user.getAddress(), user.getPrivKey());
                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(identity.getAddress());
            cardAccount.setAddress(identity.getAddress());
            return cardAccount;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cardAccount;
    }


    // ? can user create asset?
    public Card createAsset(BlockchainAccount user) {

        System.out.println("create user ... ");

        SecureIdentity userId = new SecureIdentity(user.getAddress(), user.getPrivKey());

        BlockchainSession session = serviceFactory.createSession(userId);

        TransactionTemplate tx = session.beginTransaction();

        Card card = new Card();

        try {
            AssetManageService service = tx.forService(AssetManageService.class);
            BlockchainCertificate identity = session.getSecureKeyGenerator().generateBubiCertificate();


            AssetInfo<String> info = new AssetInfo();
            info.setDescription("InnoLab Hackson Asset");
            tx.prepare(service.declare(identity, info), String.class);

            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                card.setCode(txHash);
                ptx.sign(user.getAddress(), user.getPrivKey());
                card.setAddress(identity.getAddress());

                try {
                    ptx.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(identity.getAddress());
            return card;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

    public BlockchainAccount createUser(BlockchainAccount app, BlockchainAccount operator) {

        System.out.println("create user ... ");

        SecureIdentity appId = new SecureIdentity(app.getAddress(), app.getPrivKey());
        SecureIdentity operatorId = new SecureIdentity(operator.getAddress(), operator.getPrivKey());
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
                newAccount.setAddress(newEntry.getAddress());
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
        return newAccount;
    }

    public BlockchainAccount createOperator(BlockchainAccount parentAccount) {
        System.out.println("create operator ... ");
        String address = parentAccount.getAddress();
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
                newAccount.setAddress(newEntry.getAddress());
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


    public BlockchainAccount createApp(BlockchainAccount account) {
        System.out.println("create app ... ");
        Application application;
        String address = account.getAddress();
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
                newAccount.setAddress(application.getAddress());
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

    public BlockchainAccount createTenant() {
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
                newAccount.setAddress(tenant.getAddress());
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
