package com.onlyleft.sqleight;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;

public class ObjectBuilder<E> {
    private ResultSet resultSet;
    private E result;

    public ObjectBuilder(ResultSet resultSet, Class<E> clazz) {
        this.resultSet = resultSet;
        try {
            this.result = clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException("Must be able to construct an empty instance", e);
        }
    }

    public <T> ObjectBuilder<E> extract(String columnName, SqlBiFunction<ResultSet, String, T> getter, BiConsumer<E, T> setter) throws SQLException {
        FieldExtractor.extract(resultSet, columnName, result, getter, setter);
        return this;
    }
    public <T> ObjectBuilder<E> extract(int position, SqlBiFunction<ResultSet, Integer, T> getter, BiConsumer<E, T> setter) throws SQLException {
        FieldExtractor.extract(resultSet, position, result, getter, setter);
        return this;
    }

    public E getResult() {
        return result;
    }
}
