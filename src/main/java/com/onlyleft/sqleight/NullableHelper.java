package com.onlyleft.sqleight;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NullableHelper {

    public static Boolean getBoolean(ResultSet resultSet, int position) throws SQLException {
        boolean value = resultSet.getBoolean(position);
        if (!value && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Boolean getBoolean(ResultSet resultSet, String name) throws SQLException {
        boolean value = resultSet.getBoolean(name);
        if (!value && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Byte getByte(ResultSet resultSet, int position) throws SQLException {
        byte value = resultSet.getByte(position);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Byte getByte(ResultSet resultSet, String name) throws SQLException {
        byte value = resultSet.getByte(name);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Short getShort(ResultSet resultSet, int position) throws SQLException {
        short value = resultSet.getShort(position);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Short getShort(ResultSet resultSet, String name) throws SQLException {
        short value = resultSet.getShort(name);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Integer getInteger(ResultSet resultSet, int position) throws SQLException {
        int value = resultSet.getInt(position);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Integer getInteger(ResultSet resultSet, String name) throws SQLException {
        int value = resultSet.getInt(name);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Long getLong(ResultSet resultSet, int position) throws SQLException {
        long value = resultSet.getLong(position);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Long getLong(ResultSet resultSet, String name) throws SQLException {
        long value = resultSet.getLong(name);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Float getFloat(ResultSet resultSet, int position) throws SQLException {
        float value = resultSet.getFloat(position);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Float getFloat(ResultSet resultSet, String name) throws SQLException {
        float value = resultSet.getFloat(name);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Double getDouble(ResultSet resultSet, int position) throws SQLException {
        double value = resultSet.getDouble(position);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
    public static Double getDouble(ResultSet resultSet, String name) throws SQLException {
        double value = resultSet.getDouble(name);
        if (value == 0 && resultSet.wasNull()) {
            return null;
        }
        return value;
    }
}
