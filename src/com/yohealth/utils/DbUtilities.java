package com.yohealth.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public class DbUtilities {

    public void setNullableString(PreparedStatement statement, int parameterIndex, String value) throws SQLException {
        if (value != null) {
            statement.setString(parameterIndex, value);
        } else {
            statement.setNull(parameterIndex, Types.VARCHAR);
        }
    }
    public void setNullableFloat(PreparedStatement statement, int parameterIndex, Float value) throws SQLException {
        if (value != null) {
            statement.setFloat(parameterIndex, parameterIndex);
        } else {
            statement.setNull(parameterIndex, Types.FLOAT);
        }
    }
    public void setNullableLong(PreparedStatement statement, int parameterIndex, Long value) throws SQLException {
        if (value != null) {
            statement.setLong(parameterIndex, parameterIndex);
        } else {
            statement.setNull(parameterIndex, Types.BIGINT);
        }
    }

    public void setNullableDate(PreparedStatement statement, int parameterIndex, Date value) throws SQLException {
        if (value != null) {
            statement.setDate(parameterIndex, new java.sql.Date(value.getTime()));
        } else {
            statement.setNull(parameterIndex, Types.DATE);
        }
    }
}
