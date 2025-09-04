package net.microfalx.jdbcpool;

import java.io.Closeable;

/**
 * An extension of a {@link javax.sql.DataSource} which is also {@link Closeable}.
 */
public interface DataSource extends javax.sql.DataSource, Closeable {

    @Override
    void close();
}
