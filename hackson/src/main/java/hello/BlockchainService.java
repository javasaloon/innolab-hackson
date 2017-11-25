package hello;

import cn.bubi.baas.account.AccountManageService;
import cn.bubi.baas.account.Application;
import cn.bubi.baas.account.ApplicationInfo;
import cn.bubi.baas.account.tenant.Tenant;
import cn.bubi.baas.account.tenant.TenantInfo;
import cn.bubi.baas.account.tenant.TenantManageService;
import cn.bubi.baas.base.BlockchainCertificate;
import cn.bubi.baas.base.security.SecureIdentity;
import cn.bubi.baas.sdk.BlockchainServiceFactory;
import cn.bubi.baas.sdk.BlockchainSession;
import cn.bubi.baas.sdk.PreparedTransaction;
import cn.bubi.baas.sdk.TransactionTemplate;

public class BlockchainService {
    private static final String ROOT_ADDRESS = "a001d3d4a9f436505c02f00945dced61cb9a5fd01df144";
    private static final String ROOT_PRIVATE_KEY = "c00155912ecc141ba36968b7f5e1452f9f09659e16a19638127b45c0956bc7389d0aee";

    private static final String BAAS_HOST = "39.106.70.242";
    private static final int BAAS_PORT = 8080;
    private static final BlockchainServiceFactory serviceFactory = new BlockchainServiceFactory(BAAS_HOST, BAAS_PORT);

    public static void main(String[] args) {

        Tenant tenant = createTenant();

        Application application = createApp(tenant);






    }

    private static Application createApp(Tenant tenant) {
        Application application;SecureIdentity channel = new SecureIdentity(ROOT_ADDRESS, ROOT_PRIVATE_KEY);
        BlockchainSession session = serviceFactory.createSession(channel);

        TransactionTemplate tx = session.beginTransaction();

        try {
            AccountManageService accountManageService = tx.forService(AccountManageService.class);
            BlockchainCertificate blockchainCertificate = session.getSecureKeyGenerator().generateBubiCertificate();

            ApplicationInfo info = new ApplicationInfo();
            info.setDescription("InnoLab Hackson App");


            application = accountManageService.register(blockchainCertificate, info);

            tx.prepare(application, Application.class);
            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                ptx.sign(ROOT_ADDRESS, ROOT_PRIVATE_KEY);
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

        application.setTenant(tenant.getCode());
        return application;
    }

    private static Tenant createTenant() {
        SecureIdentity channel = new SecureIdentity(ROOT_ADDRESS, ROOT_PRIVATE_KEY);
        BlockchainSession session = serviceFactory.createSession(channel);

        TransactionTemplate tx = session.beginTransaction();

        Tenant tenant = null;

        try {
            TenantManageService tenantManageService = tx.forService(TenantManageService.class);

            TenantInfo tenantInfo = new TenantInfo();
            tenantInfo.setDescription("InnoLab Tenant");


            BlockchainCertificate blockchainCertificate = session.getSecureKeyGenerator().generateBubiCertificate();
            tenant = tenantManageService.register(blockchainCertificate, tenantInfo);

            tx.prepare(tenant, Tenant.class);
            PreparedTransaction ptx = tx.complete();
            try {
                String txHash = ptx.getHash();
                System.out.printf(txHash);
                ptx.sign(ROOT_ADDRESS, ROOT_PRIVATE_KEY);
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
        return tenant;
    }


}
