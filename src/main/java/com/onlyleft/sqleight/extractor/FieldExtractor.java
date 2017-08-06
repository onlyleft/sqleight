package com.onlyleft.sqleight.extractor;

import com.onlyleft.sqleight.SqlBiFunction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class FieldExtractor {
    public static <E, T> void extract(ResultSet resultSet, String columnName, E bean, SqlBiFunction<ResultSet, String, T> getter, BiConsumer<E, T> setter) throws SQLException {
        setter.accept(bean, getter.apply(resultSet, columnName));
    }
}
