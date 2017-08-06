package com.onlyleft.sqleight.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ObjectExtractor<T> {
    T extract(ResultSet resultSet) throws SQLException;
}
