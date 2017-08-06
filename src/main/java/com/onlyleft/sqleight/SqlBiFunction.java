package com.onlyleft.sqleight;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlBiFunction<T, U, R> {
    R apply(T first, U second) throws SQLException;
}
