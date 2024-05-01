package ru.otus.webServer.server;

import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.webServer.helpers.FileSystemHelper;
import ru.otus.webServer.services.TemplateProcessor;
import ru.otus.webServer.servlet.ClientsServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersWebServerBase implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";
    private static final String ROLE_NAME_ADMIN = "admin";
    private final Server server;
    protected final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;
    private final LoginService loginService;

    public UsersWebServerBase(int port, TemplateProcessor templateProcessor, DBServiceClient dbServiceClient, LoginService loginService) {
        server = new Server(port);
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.loginService = loginService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/clients"));

        server.setHandler(sequence);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        Constraint constraint = Constraint.from(ROLE_NAME_ADMIN);

        List<ConstraintMapping> constraintMappings = new ArrayList<>();
        Arrays.stream(paths).forEachOrdered(path -> {
            ConstraintMapping mapping = new ConstraintMapping();
            mapping.setPathSpec(path);
            mapping.setConstraint(constraint);
            constraintMappings.add(mapping);
        });

        ConstraintSecurityHandler security = new ConstraintSecurityHandler();

        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new Handler.Wrapper(servletContextHandler));

        return security;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients");
        return servletContextHandler;
    }

}
