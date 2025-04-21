package online.store.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class SingletonPostgresContainer extends PostgreSQLContainer<SingletonPostgresContainer> {
    private static final SingletonPostgresContainer INSTANCE = new SingletonPostgresContainer();

    private SingletonPostgresContainer() {
        super("postgres:15.2");
        withDatabaseName("testdb");
        withUsername("test");
        withPassword("test");
    }

    public static SingletonPostgresContainer getInstance() {
        return INSTANCE;
    }
}