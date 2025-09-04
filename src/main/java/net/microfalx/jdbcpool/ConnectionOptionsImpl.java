package net.microfalx.jdbcpool;

import net.microfalx.lang.ExceptionUtils;

import java.net.URI;
import java.sql.Connection;
import java.util.Optional;

import static java.util.Optional.ofNullable;

/**
 * Holds options for an object pool.
 */
class ConnectionOptionsImpl extends net.microfalx.objectpool.OptionsImpl<Connection> implements ConnectionPool.Options {

    URI uri;
    String userName;
    String password;
    String driverClassName;
    String schema;
    boolean readOnly;
    boolean loadBalancing;

    boolean clustered;
    boolean memory;
    boolean embedded;
    boolean node;

    public URI getUri() {
        return uri;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Class<?> getDriverClass() {
        try {
            return getClass().getClassLoader().loadClass(driverClassName);
        } catch (ClassNotFoundException e) {
            return ExceptionUtils.rethrowExceptionAndReturn(e);
        }
    }

    public Optional<String> getSchema() {
        return ofNullable(schema);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isLoadBalancing() {
        return loadBalancing;
    }

    public boolean isClustered() {
        return clustered;
    }

    public boolean isMemory() {
        return memory;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public boolean isNode() {
        return node;
    }
}
