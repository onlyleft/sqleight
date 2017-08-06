package com.onlyleft.sqleight;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface Preparer {
    PreparedStatement prepare(PreparedStatement preparedStatement) throws SQLException;
}
