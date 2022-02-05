package next.dao;

import core.jdbc.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {

  public void update(String sql, PreparedStatementSetter pss) throws SQLException {
    Connection con = null;
    PreparedStatement pstmt = null;
    try {
      con = ConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
      pss.setValues(pstmt);
      pstmt.executeUpdate();
    } finally {
      if (pstmt != null) {
        pstmt.close();
      }

      if (con != null) {
        con.close();
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List query(String sql, RowMapper rowMapper) throws SQLException {
    try (Connection con = ConnectionManager.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
      List result = new ArrayList<>();
      while (rs.next()) {
        result.add(rowMapper.mapRow(rs));
      }
      return result;
    }
  }

  public Object queryForObject(
      String sql,
      PreparedStatementSetter pss,
      RowMapper rowMapper
  ) throws SQLException {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = ConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
      pss.setValues(pstmt);
      rs = pstmt.executeQuery();

      Object object = null;
      if (rs.next()) {
        object = rowMapper.mapRow(rs);
      }
      return object;
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (pstmt != null) {
        pstmt.close();
      }
      if (con != null) {
        con.close();
      }
    }
  }
}
