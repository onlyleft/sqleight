package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class BeanBuilder<E> {
    private ResultSet resultSet;
    private E result;

    public BeanBuilder(ResultSet resultSet, Class<E> clazz) {
        this.resultSet = resultSet;
        try {
            this.result = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Must be able to construct an empty instance", e);
        }
    }

    public <T> BeanBuilder<E> extract(String columnName, SqlBiFunction<ResultSet, String, T> getter, BiConsumer<E, T> setter) throws SQLException {
        FieldExtractor.extract(resultSet, columnName, result, getter, setter);
        return this;
    }

    public E getResult() {
        return result;
    }
}