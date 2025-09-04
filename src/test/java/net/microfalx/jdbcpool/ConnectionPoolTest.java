package net.microfalx.jdbcpool;

import net.microfalx.objectpool.PooledObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolTest {

    private ConnectionPool pool;

    @BeforeEach
    void before() {
        this.pool = createPool();
    }

    @Test
    void borrowConnection() {
        Connection connection = pool.borrowObject();
        assertNotNull(connection);
        Connection connection2 = pool.borrowObject();
        assertNotSame(connection, connection2);
    }

    @Test
    void returnConnection() {
        Connection connection = pool.borrowObject();
        assertNotNull(connection);
        pool.returnObject(connection);
        Connection connection2 = pool.borrowObject();
        assertSame(connection, connection2);
    }

    @Test
    void destroyConnection() {
        Connection connection = pool.borrowObject();
        assertNotNull(connection);
        pool.invalidateObject(connection);
    }

    @Test
    void isAvailable() throws SQLException {
        assertTrue(pool.isAvailable());
    }

    @Test
    void valid() throws SQLException {
        Connection connection = pool.borrowObject();
        assertNotNull(connection);
        assertTrue(connection.isValid(500));
    }

    @Test
    void datasource() throws SQLException {
        assertEquals(0, pool.getSize());
        DataSource dataSource = pool.getDataSource();
        Connection connection = dataSource.getConnection();
        assertNotNull(connection);
        assertEquals(1, pool.getSize());
        assertEquals(1, pool.getSize(PooledObject.State.ACTIVE));
        assertEquals(0, pool.getSize(PooledObject.State.IDLE));
        assertEquals(1, pool.getMetrics().getBorrowedCount());
        assertTrue(connection.isValid(500));
        connection.close();
        assertEquals(1, pool.getSize());
        assertEquals(0, pool.getSize(PooledObject.State.ACTIVE));
        assertEquals(1, pool.getSize(PooledObject.State.IDLE));
    }

    private ConnectionPool createPool() {
        return (ConnectionPool) ConnectionPool.create()
                .uri("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1").userName("sa").password("").memory(true)
                .driver("org.h2.Driver")
                .maximum(5).maximumWait(Duration.ofSeconds(5))
                .build();
    }

}