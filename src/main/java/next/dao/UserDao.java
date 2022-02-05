package next.dao;

import java.sql.SQLException;
import java.util.List;
import next.model.User;

public class UserDao {

    private static final String INSERT_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET password=?, name=?, email=? WHERE userid=?";
    private static final String FIND_ALL_QUERY = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";


    public void insert(User user) throws SQLException {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        insertJdbcTemplate.update(INSERT_QUERY, pss);
    }

    public void update(User user) throws SQLException {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };
        updateJdbcTemplate.update(UPDATE_QUERY, pss);
    }

    public List<User> findAll() throws SQLException {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        RowMapper rowMapper = rs -> new User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
        );
        return (List<User>) selectJdbcTemplate.query(FIND_ALL_QUERY, rowMapper);
    }

    public User findByUserId(String userId) throws SQLException {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        RowMapper rowMapper = rs -> new User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
        );
        return (User) selectJdbcTemplate.queryForObject(FIND_BY_ID_QUERY, pss, rowMapper);
    }
}
