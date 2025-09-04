package net.microfalx.jdbcpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

import static net.microfalx.lang.ArgumentUtils.requireNonNull;

class ConnectionHandler implements InvocationHandler {

    private final ConnectionPool pool;
    private final Connection connection;

    public ConnectionHandler(ConnectionPool pool, Connection connection) {
        this.pool = requireNonNull(pool);
        this.connection = requireNonNull(connection);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        return switch (methodName) {
            case "close" -> {
                pool.returnObject(connection);
                yield null;
            }
            case "getName" -> pool.getName();
            case "toString" -> connection.toString();
            default -> method.invoke(connection, args);
        };
    }
}
