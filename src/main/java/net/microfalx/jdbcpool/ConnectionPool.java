package net.microfalx.jdbcpool;

import net.microfalx.lang.UriUtils;
import net.microfalx.objectpool.ObjectPool;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;
import java.util.Optional;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;
import static net.microfalx.lang.ArgumentUtils.requireNotEmpty;

/**
 * An extension of a {@link ObjectPool} for {@link Connection database connections}.
 */
public interface ConnectionPool extends ObjectPool<Connection> {

    /**
     * Creates a builder used to initialize and create the connection pool.
     *
     * @return the builder
     */
    static Builder create() {
        return create(new ConnectionFactoryImpl());
    }

    /**
     * Creates a builder used to initialize and create the connection pool.
     *
     * @param connectionFactory the connection factory
     * @return the builder
     */
    static Builder create(ConnectionFactory connectionFactory) {
        return new Builder(connectionFactory);
    }

    /**
     * Returns the data source which uses this connection pool.
     *
     * @return a non-null instance
     */
    DataSource getDataSource();

    /**
     * Holds options for an object pool.
     */
    interface Options extends ObjectPool.Options<Connection> {

        /**
         * Returns the URI for the connection pool.
         *
         * @return a non-null instance
         */
        URI getUri();

        /**
         * Returns the username for the connection pool.
         *
         * @return a non-null instance
         */
        String getUserName();

        /**
         * Returns the password for the connection pool.
         *
         * @return a non-null instance
         */
        String getPassword();

        /**
         * Returns the driver class for the connection pool.
         *
         * @return a non-null instance
         */
        Class<?> getDriverClass();

        /**
         * Returns an optional schema to be used when a connection is created.
         *
         * @return a non-null instance
         */
        Optional<String> getSchema();

        /**
         * Returns whether the connection is read-only.
         *
         * @return {@code true} if the connection is read-only, {@code false} otherwise
         */
        boolean isReadOnly();

        /**
         * Returns whether the connection pool uses load balancing.
         *
         * @return {@code true} if load balancing is enabled, {@code false} otherwise
         */
        boolean isLoadBalancing();

        /**
         * Returns whether the database is clustered or not.
         *
         * @return {@code true} if the database is clustered, {@code false} otherwise
         */
        boolean isClustered();

        /**
         * Returns whether the database is in-memory or not.
         *
         * @return {@code true} if the database is in-memory, {@code false} otherwise
         */
        boolean isMemory();

        /**
         * Returns whether the database is embedded (runs in the same process) or not.
         *
         * @return {@code true} if the database is embedded, {@code false} otherwise
         */
        boolean isEmbedded();

        /**
         * Returns whether the connections are local to a node, or using a gateway.
         *
         * @return {@code true} if the connections are local to a node, {@code false} otherwise
         */
        boolean isNode();
    }

    class Builder extends ObjectPool.Builder<Connection> {

        public Builder(ConnectionFactory factory) {
            super(factory, new ConnectionOptionsImpl());
        }

        /**
         * Sets the URI for the connection pool.
         *
         * @param uri the new URI
         * @return self
         */
        public Builder uri(String uri) {
            requireNotEmpty(uri);
            getOptions().uri = requireNonNull(UriUtils.parseUri(uri));
            return this;
        }

        /**
         * Sets the URI for the connection pool.
         *
         * @param uri the new URI
         * @return self
         */
        public Builder uri(URI uri) {
            getOptions().uri = requireNonNull(uri);
            return this;
        }

        /**
         * Sets the username for the connection pool.
         *
         * @param userName the username
         * @return self
         */
        public Builder userName(String userName) {
            getOptions().userName = requireNotEmpty(userName);
            return this;
        }

        /**
         * Sets the password for the connection pool.
         *
         * @param password the password
         * @return self
         */
        public Builder password(String password) {
            getOptions().password = requireNonNull(password);
            return this;
        }

        /**
         * Sets the driver class name for the connection pool.
         *
         * @param driverClassName the driver class name
         * @return self
         */
        public Builder driver(String driverClassName) {
            getOptions().driverClassName = requireNotEmpty(driverClassName);
            return this;
        }

        /**
         * Sets an optional schema to be used when a connection is created.
         *
         * @param schema the schema name
         * @return self
         */
        public Builder setSchema(String schema) {
            getOptions().schema = schema;
            return this;
        }

        /**
         * Sets the connection to be read-only.
         *
         * @param readOnly {@code true} to allow only read operations, {@code false} otherwise
         * @return self
         */
        public Builder readOnly(boolean readOnly) {
            getOptions().readOnly = readOnly;
            return this;
        }

        /**
         * Sets the load balancing flag.
         *
         * @param loadBalancing {@code true} to enable load balancing, {@code false} otherwise
         * @return self
         */
        public Builder loadBalancing(boolean loadBalancing) {
            getOptions().loadBalancing = loadBalancing;
            return this;
        }

        /**
         * Sets whether the database is clustered or not.
         *
         * @param clustered {@code true} if the database is clustered, {@code false} otherwise
         * @return self
         */
        public Builder clustered(boolean clustered) {
            getOptions().clustered = clustered;
            return this;
        }

        /**
         * Sets whether the database is in-memory or not.
         *
         * @param memory {@code true} if the database is in-memory, {@code false} otherwise
         * @return self
         */
        public Builder memory(boolean memory) {
            getOptions().memory = memory;
            return this;
        }

        /**
         * Sets whether the database is embedded or not.
         *
         * @param embedded {@code true} if the database is embedded (runs in the same process), {@code false} otherwise
         * @return self
         */
        public Builder embedded(boolean embedded) {
            getOptions().embedded = embedded;
            return this;
        }

        /**
         * Sets whether the connections are local to a node, or using a gateway.
         *
         * @param node {@code true} if the connections are local to a node, {@code false} otherwise
         * @return self
         */
        public Builder node(boolean node) {
            getOptions().node = node;
            return this;
        }

        @Override
        public ConnectionPool build() {
            return (ConnectionPool) super.build();
        }

        private ConnectionOptionsImpl getOptions() {
            return (ConnectionOptionsImpl) options;
        }

        @Override
        protected ObjectPool<Connection> create() {
            return new CollectionPoolImpl((ConnectionOptionsImpl) options);
        }
    }
}
