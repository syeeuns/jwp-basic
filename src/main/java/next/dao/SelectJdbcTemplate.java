package next.dao;

import core.jdbc.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import next.model.User;

public abstract class SelectJdbcTemplate {

  public List<User> query(String sql) throws SQLException {
    try (Connection con = ConnectionManager.getConnection();
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
      List<User> users = new ArrayList<>();
      while (rs.next()) {
        users.add((User) mapRow(rs));
      }
      return users;
    }
  }

  public Object queryForObject(String sql) throws SQLException {
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    try {
      con = ConnectionManager.getConnection();
      pstmt = con.prepareStatement(sql);
      setValues(pstmt);
      rs = pstmt.executeQuery();

      User user = null;
      if (rs.next()) {
        user = (User) mapRow(rs);
      }
      return user;
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

  abstract void setValues(PreparedStatement pstmt) throws SQLException;

  abstract Object mapRow(ResultSet rs) throws SQLException;
}
