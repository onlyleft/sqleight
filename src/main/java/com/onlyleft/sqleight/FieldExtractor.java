package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class FieldExtractor<E> {
    public static <E, T> void extract(ResultSet resultSet, String columnName, E bean, SqlBiFunction<ResultSet, String, T> getter, BiConsumer<E, T> setter) throws SQLException {
        setter.accept(bean, getter.apply(resultSet, columnName));
    }
    public static <E, T> void extract(ResultSet resultSet, Integer position, E bean, SqlBiFunction<ResultSet, Integer, T> getter, BiConsumer<E, T> setter) throws SQLException {
        setter.accept(bean, getter.apply(resultSet, position));
    }

    private ResultSet resultSet;
    private E target;

    public FieldExtractor(ResultSet resultSet, E target) {
        this.resultSet = resultSet;
        this.target = target;
    }

    public <T> FieldExtractor<E> extract(String columnName, SqlBiFunction<ResultSet, String, T> getter, BiConsumer<E, T> setter) throws SQLException {
        FieldExtractor.extract(resultSet, columnName, target, getter, setter);
        return this;
    }
}
