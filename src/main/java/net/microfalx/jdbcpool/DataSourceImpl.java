package net.microfalx.jdbcpool;

import net.microfalx.lang.IOUtils;

import java.io.PrintWriter;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.time.Duration;
import java.util.logging.Logger;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

class DataSourceImpl implements DataSource {

    private final static Logger JAVA_LOGGER = Logger.getLogger(DataSourceImpl.class.getName());

    private static final Class<?>[] INTERFACES = new Class[]{Connection.class};

    private final ConnectionPool pool;
    private final ClassLoader classLoader = getClass().getClassLoader();
    private PrintWriter logWriter;
    private Duration loginTimeout = Duration.ofSeconds(5);

    public DataSourceImpl(ConnectionPool pool) {
        this.pool = requireNonNull(pool);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return wrap(pool.borrowObject());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        if (logWriter == null) {
            logWriter = new PrintWriter(IOUtils.getNullOutputStream(), true);
        }
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = Duration.ofSeconds(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return (int) loginTimeout.toSeconds();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return JAVA_LOGGER;
    }

    private Connection wrap(Connection connection) {
        return (Connection) Proxy.newProxyInstance(classLoader, INTERFACES, new ConnectionHandler(pool, connection));
    }

    @Override
    public void close() {
        pool.close();
    }
}
