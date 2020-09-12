package com.onlyleft.sqleight;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Spliterator.ORDERED;

public class SqlEight {
    private static void defaultRethrowHandler(SQLException e) {
        throw new IllegalStateException(e);
    }

    public static <T> Stream<T> queryForStream(Connection connection, String query, Preparer preparer, Extractor<T> function, SQLExceptionHandler seh) {
        try {
            PreparedStatement preparedStatement = preparer.prepare(connection.prepareStatement(query));
            ResultSet resultSet = preparedStatement.executeQuery();
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new ResultSetIterator<>(function, resultSet), ORDERED), false)
                    .onClose(() -> {
                        try {
                            preparedStatement.close();
                            resultSet.close();
                        }
                        catch (SQLException e) {
                            seh.handle(e);
                        }
                    });
        }
        catch (SQLException e) {
            seh.handle(e);
        }

        return Stream.empty();
    }

    public static <T> List<T> queryForList(Connection connection, String query, Preparer preparer, Extractor<T> function, SQLExceptionHandler seh) {
        List<T> results = new ArrayList<>();
        try (
                PreparedStatement preparedStatement = preparer.prepare(connection.prepareStatement(query));
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                results.add(function.extract(resultSet));
            }
        } catch (SQLException e) {
            seh.handle(e);
        }

        return results;
    }

    public static <T> Optional<T> queryForOptional(Connection connection, String query, Preparer prep, Extractor<T> function, SQLExceptionHandler seh) {
        try (
                PreparedStatement preparedStatement = prep.prepare(connection.prepareStatement(query));
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            if (resultSet.next()) {
                return Optional.ofNullable(function.extract(resultSet));
            }
        } catch (SQLException e) {
            seh.handle(e);
        }

        return Optional.empty();
    }

    public static <T> Stream<T> queryForStream(Connection connection, String query, Preparer preparer, Extractor<T> function) {
        return queryForStream(connection, query, preparer, function, SqlEight::defaultRethrowHandler);
    }

    public static <T> List<T> queryForList(Connection connection, String query, Preparer preparer, Extractor<T> function) {
        return queryForList(connection, query, preparer, function, SqlEight::defaultRethrowHandler);
    }

    public static <T> Optional<T> queryForOptional(Connection connection, String query, Preparer preparer, Extractor<T> function) {
        return queryForOptional(connection, query, preparer, function, SqlEight::defaultRethrowHandler);
    }

    public static <T> Stream<T> queryForStream(Connection connection, String query, Extractor<T> function, SQLExceptionHandler seh) {
        return queryForStream(connection, query, (preparedStatement -> preparedStatement), function, seh);
    }

    public static <T> List<T> queryForList(Connection connection, String query, Extractor<T> function, SQLExceptionHandler seh) {
        return queryForList(connection, query, (preparedStatement -> preparedStatement), function, seh);
    }

    public static <T> Optional<T> queryForOptional(Connection connection, String query, Extractor<T> function, SQLExceptionHandler seh) {
        return queryForOptional(connection, query, (preparedStatement -> preparedStatement), function, seh);
    }

    public static <T> Stream<T> queryForStream(Connection connection, String query, Extractor<T> function) {
        return queryForStream(connection, query, (preparedStatement -> preparedStatement), function, SqlEight::defaultRethrowHandler);
    }

    public static <T> List<T> queryForList(Connection connection, String query, Extractor<T> function) {
        return queryForList(connection, query, (preparedStatement -> preparedStatement), function, SqlEight::defaultRethrowHandler);
    }

    public static <T> Optional<T> queryForOptional(Connection connection, String query, Extractor<T> function) {
        return queryForOptional(connection, query, (preparedStatement -> preparedStatement), function, SqlEight::defaultRethrowHandler);
    }


}
