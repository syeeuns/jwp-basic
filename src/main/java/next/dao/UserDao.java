package next.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import next.model.User;

public class UserDao {

    private static final String INSERT_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET password=?, name=?, email=? WHERE userid=?";
    private static final String FIND_ALL_QUERY = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";


    public void insert(User user) throws SQLException {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());
            }
        };
        insertJdbcTemplate.update(INSERT_QUERY);
    }

    public void update(User user) throws SQLException {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate() {
            @Override
            public void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, user.getPassword());
                pstmt.setString(2, user.getName());
                pstmt.setString(3, user.getEmail());
                pstmt.setString(4, user.getUserId());
            }
        };
        updateJdbcTemplate.update(UPDATE_QUERY);
    }

    public List<User> findAll() throws SQLException {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) {
            }

            @Override
            Object mapRow(ResultSet rs) throws SQLException {
                return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                );
            }
        };
        return selectJdbcTemplate.query(FIND_ALL_QUERY);
    }

    public User findByUserId(String userId) throws SQLException {
        SelectJdbcTemplate selectJdbcTemplate = new SelectJdbcTemplate() {
            @Override
            void setValues(PreparedStatement pstmt) throws SQLException {
                pstmt.setString(1, userId);
            }

            @Override
            Object mapRow(ResultSet rs) throws SQLException {
                return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                );
            }
        };
        return (User) selectJdbcTemplate.queryForObject(FIND_BY_ID_QUERY);
    }
}
