package com.onlyleft.sqleight;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlEight {
    private static final Logger LOGGER = Logger.getLogger(SqlEight.class.getName());

    private static SQLExceptionHandler defaultLoggingHandler = (SQLException e) -> LOGGER.log(Level.SEVERE, "Could not complete query", e);

    public static <T> List<T> queryForList(DataSource ds, String query, Preparer preparer, Extractor<T> function, SQLExceptionHandler seh) {
        List<T> results = new ArrayList<>();
        try (
                Connection connection = ds.getConnection();
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

    public static <T> Optional<T> queryForOptional(DataSource ds, String query, Preparer prep, Extractor<T> function, SQLExceptionHandler seh) {
        try (
                Connection connection = ds.getConnection();
                PreparedStatement preparedStatement = prep.prepare(connection.prepareStatement(query));
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            if (resultSet.next()) {
                return Optional.of(function.extract(resultSet));
            }
        } catch (SQLException e) {
            seh.handle(e);
        }

        return Optional.empty();
    }

    public static <T> List<T> queryForList(DataSource ds, String query, Preparer preparer, Extractor<T> function) {
        return queryForList(ds, query, preparer, function, defaultLoggingHandler);
    }

    public static <T> Optional<T> queryForOptional(DataSource ds, String query, Preparer preparer, Extractor<T> function) {
        return queryForOptional(ds, query, preparer, function, defaultLoggingHandler);
    }

    public static <T> List<T> queryForList(DataSource ds, String query, Extractor<T> function, SQLExceptionHandler seh) {
        return queryForList(ds, query, (preparedStatement -> preparedStatement), function, seh);
    }

    public static <T> Optional<T> queryForOptional(DataSource ds, String query, Extractor<T> function, SQLExceptionHandler seh) {
        return queryForOptional(ds, query, (preparedStatement -> preparedStatement), function, seh);
    }

    public static <T> List<T> queryForList(DataSource ds, String query, Extractor<T> function) {
        return queryForList(ds, query, (preparedStatement -> preparedStatement), function, defaultLoggingHandler);
    }

    public static <T> Optional<T> queryForOptional(DataSource ds, String query, Extractor<T> function) {
        return queryForOptional(ds, query, (preparedStatement -> preparedStatement), function, defaultLoggingHandler);
    }


}
