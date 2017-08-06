package com.onlyleft.sqleight;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLExceptionHandler {
    void handle(SQLException e);
}
