package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResultSetIterator<T> implements Iterator<T>, AutoCloseable {
    private final Extractor<T> extractor;
    private final ResultSet resultSet;
    private boolean hasNext;
    private final SQLExceptionHandler handler;

    static <T> ResultSetIterator<T> of(Extractor<T> extractor, ResultSet resultSet, SQLExceptionHandler handler) {
        return new ResultSetIterator<T>(extractor, resultSet, handler);
    }

    private ResultSetIterator(Extractor<T> extractor, ResultSet resultSet, SQLExceptionHandler handler) {
        this.extractor = extractor;
        this.resultSet = resultSet;
        this.handler = handler;
    }

    @Override
    public boolean hasNext() {
        if (!hasNext) {
            try {
                hasNext = resultSet.next();
            } catch (SQLException e) {
                handler.handle(e);
            }
        }

        return hasNext;
    }

    @Override
    public T next() {
        try {
            if (hasNext || resultSet.next()) {
                T extracted = extractor.extract(resultSet);
                hasNext = false;
                return extracted;
            }
        } catch (SQLException e) {
            handler.handle(e);
        }
        throw new NoSuchElementException();
    }

    @Override
    public void close() throws Exception {
        if (resultSet != null && !resultSet.isClosed()) {
            resultSet.close();
        }
    }
}
