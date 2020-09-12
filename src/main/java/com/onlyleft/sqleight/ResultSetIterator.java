package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResultSetIterator<T> implements Iterator<T>, AutoCloseable {
    private final Extractor<T> extractor;
    private final ResultSet resultSet;
    private boolean hasNext;

    public ResultSetIterator(Extractor<T> extractor, ResultSet resultSet) {
        this.extractor = extractor;
        this.resultSet = resultSet;
    }

    @Override
    public boolean hasNext() {
        if (!hasNext) {
            try {
                hasNext = resultSet.next();
            } catch (SQLException e) {
                throw new RuntimeException("Unable to get next result", e);
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
            throw new RuntimeException("Unable to extract result", e);
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
