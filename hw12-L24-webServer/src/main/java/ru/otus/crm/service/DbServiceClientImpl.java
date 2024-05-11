package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.cachehw.MyCache;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private MyCache<String, Client> cacheClients;

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate, MyCache<String, Client> cacheClients) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cacheClients = cacheClients;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            cacheClients.put(String.valueOf(clientCloned.getId()), clientCloned);
            log.info("updated client: {}", savedClient);
            return savedClient;
        });
    }
    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> client = Optional.ofNullable(cacheClients.get(String.valueOf(id)));
        if (client.isEmpty()){
            client = transactionManager.doInReadOnlyTransaction(session -> {
                var clientOptional = clientDataTemplate.findById(session, id);
                log.info("clientOptional: {}", clientOptional);
                clientOptional.ifPresent(c -> cacheClients.put(String.valueOf(id), c.clone()));
                return clientOptional;
            });

            client.ifPresent(c -> cacheClients.put(String.valueOf(c.getId()), c.clone()));
        }
        return client;
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
