package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Extractor<T> {
    T extract(ResultSet resultSet) throws SQLException;
}
