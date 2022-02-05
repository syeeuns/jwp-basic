package core.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class JdbcTemplate {

  public void update(String sql, PreparedStatementSetter pss) throws DataAccessException {
    try (Connection con = ConnectionManager.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql)) {
      pss.setValues(pstmt);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  public void update(String sql, Object... values) throws DataAccessException {
    try (Connection con = ConnectionManager.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql)) {
      for (int i = 0; i < values.length; i++) {
        pstmt.setObject(i + 1, values[i]);
      }
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataAccessException(e);
    }
  }

  public <T> List<T> query(
      String sql,
      PreparedStatementSetter pss,
      RowMapper<T> rowMapper
  ) throws DataAccessException {
    ResultSet rs = null;
    try (Connection con = ConnectionManager.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql)) {
      pss.setValues(pstmt);
      rs = pstmt.executeQuery();

      List<T> result = new ArrayList<>();
      while (rs.next()) {
        result.add(rowMapper.mapRow(rs));
      }
      return result;
    } catch(SQLException e) {
      throw new DataAccessException(e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } catch(SQLException e) {
        throw new DataAccessException(e);
      }
    }
  }

  @Nullable
  public <T> T queryForObject(
      String sql,
      PreparedStatementSetter pss,
      RowMapper<T> rowMapper
  ) throws DataAccessException {
    List<T> result = query(sql, pss, rowMapper);
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }
}
