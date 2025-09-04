package net.microfalx.jdbcpool;

import net.microfalx.objectpool.ObjectPoolImpl;

import javax.sql.DataSource;
import java.sql.Connection;

class CollectionPoolImpl extends ObjectPoolImpl<Connection> implements ConnectionPool {

    private final DataSourceImpl dataSource = new DataSourceImpl(this);

    public CollectionPoolImpl(ConnectionPool.Options options) {
        super(options);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
