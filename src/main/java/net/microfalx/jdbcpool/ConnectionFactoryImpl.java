package net.microfalx.jdbcpool;

import net.microfalx.objectpool.ObjectPool;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The default implementation of a {@link ConnectionFactory}.
 */
class ConnectionFactoryImpl implements ConnectionFactory {

    @Override
    public Connection makeObject(ObjectPool<Connection> pool) throws Exception {
        ConnectionPool.Options options = (ConnectionPool.Options) pool.getOptions();
        return DriverManager.getConnection(options.getUri().toASCIIString(), options.getUserName(), options.getPassword());
    }

    @Override
    public void destroyObject(ObjectPool<Connection> pool, Connection object) throws Exception {
        object.close();
    }

    @Override
    public boolean validateObject(ObjectPool<Connection> pool, Connection object) throws Exception {
        return object.isValid(500);
    }
}
