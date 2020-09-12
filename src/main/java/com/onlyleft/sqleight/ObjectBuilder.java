package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ObjectBuilder<E> {
    public static <E> ObjectBuilder<E> of(Supplier<E> constructor, ResultSet resultSet) {
        return new ObjectBuilder<>(resultSet, constructor);
    }

    private final ResultSet resultSet;
    private final E result;

    private ObjectBuilder(ResultSet resultSet, Supplier<E> constructor) {
        this.resultSet = resultSet;
        this.result = constructor.get();
    }

    public <T> ObjectBuilder<E> extract(String columnName, SqlBiFunction<ResultSet, String, T> getter, BiConsumer<E, T> setter) throws SQLException {
        FieldExtractor.extract(resultSet, columnName, result, getter, setter);
        return this;
    }
    public <T> ObjectBuilder<E> extract(int position, SqlBiFunction<ResultSet, Integer, T> getter, BiConsumer<E, T> setter) throws SQLException {
        FieldExtractor.extract(resultSet, position, result, getter, setter);
        return this;
    }

    public E get() {
        return result;
    }
}
