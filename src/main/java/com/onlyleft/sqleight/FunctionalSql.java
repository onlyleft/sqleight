package com.onlyleft.sqleight;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FunctionalSql {

    public static QueryConnection usingDatasource(DataSource dataSource) {
        return new QueryConnection(dataSource);
    }

    public static class QueryConnection {
        private final DataSource ds;

        private QueryConnection(DataSource dataSource) {
            this.ds = dataSource;
        }

        public <T> List<T> queryForList(final String query, Preparer preparer, Extractor<T> function) {
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
                // TODO make this more generic
            }

            return results;
        }

        public <T> List<T> queryForList(final String query, Extractor<T> function) {
            return queryForList(query, (preparedStatement -> preparedStatement), function);
        }

        public <T> Optional<T> queryForOptional(final String query, Extractor<T> function) {
            return queryForOptional(query, (preparedStatement -> preparedStatement), function);
        }

        public <T> Optional<T> queryForOptional(final String query, Preparer prep, Extractor<T> function) {
            try (
                    Connection connection = ds.getConnection();
                    PreparedStatement preparedStatement = prep.prepare(connection.prepareStatement(query));
                    ResultSet resultSet = preparedStatement.executeQuery()
            ) {
                if (resultSet.next()) {
                    return Optional.of(function.extract(resultSet));
                }
            } catch (SQLException e) {
                // TODO make this more generic
            }

            return Optional.empty();
        }
    }



}
