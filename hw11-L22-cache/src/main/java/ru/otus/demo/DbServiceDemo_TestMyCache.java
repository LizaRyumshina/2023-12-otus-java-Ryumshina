package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo_TestMyCache {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo_TestMyCache.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws InterruptedException {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);


        for (Long i=1L; i<=100L; i++) {
            var clientSaved = dbServiceClient.saveClient(
                    new Client(
                            i,
                            "Client"+i,
                            new Address(null, "AnyStreet"),
                            List.of(new Phone(null, "13-555-22"), new Phone(null, "14-666-333"))));
        }

        long startTime = System.currentTimeMillis();
        for (Long i=1L; i<=100L; i++) {
            var client = dbServiceClient.getClient(i);
            log.info("Client:{}", client);
        }
        long endTime = System.currentTimeMillis();

        System.gc();
        Thread.sleep(100);

        long startTimeAfterGc = System.currentTimeMillis();
        for (Long i=1L; i<=100L; i++) {
            var client = dbServiceClient.getClient(i);
           log.info("Client:{}", client);
        }
        long endTimeAfterGc = System.currentTimeMillis();
        log.info("Time Before GC: " + (endTime - startTime));
        log.info("Time After GC: " + (endTimeAfterGc - startTimeAfterGc));
    }
}
