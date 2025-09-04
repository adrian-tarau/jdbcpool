package net.microfalx.jdbcpool;

import net.microfalx.objectpool.ObjectFactory;
import net.microfalx.objectpool.ObjectPool;
import net.microfalx.objectpool.ObjectPoolUtils;

import java.sql.Connection;

/**
 * An extension of a {@link ObjectFactory} for {@link Connection database connections}.
 */
public interface ConnectionFactory extends ObjectFactory<Connection> {

    @Override
    default Throwable createObjectCreationException(ObjectPool<Connection> pool, Throwable throwable) {
        return ObjectPoolUtils.createObjectCreationException(pool, "connection", throwable);
    }

    @Override
    default Throwable createObjectBorrowException(ObjectPool<Connection> pool, Throwable throwable) {
        return ObjectPoolUtils.createObjectBorrowException(pool, "connection", throwable);
    }
}
