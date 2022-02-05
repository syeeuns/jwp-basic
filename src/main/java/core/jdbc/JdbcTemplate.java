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

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List query(
      String sql,
      PreparedStatementSetter pss,
      RowMapper rowMapper
  ) throws DataAccessException {
    ResultSet rs = null;
    try (Connection con = ConnectionManager.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql)) {
      pss.setValues(pstmt);
      rs = pstmt.executeQuery();

      List result = new ArrayList<>();
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
  @SuppressWarnings("rawtypes")
  public Object queryForObject(
      String sql,
      PreparedStatementSetter pss,
      RowMapper rowMapper
  ) throws DataAccessException {
    List result = query(sql, pss, rowMapper);
    if (result.isEmpty()) {
      return null;
    }
    return result.get(0);
  }
}
