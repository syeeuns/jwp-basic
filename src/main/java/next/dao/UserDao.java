package next.dao;

import core.jdbc.DataAccessException;
import core.jdbc.JdbcTemplate;
import core.jdbc.PreparedStatementSetter;
import core.jdbc.RowMapper;
import java.util.List;
import next.model.User;

public class UserDao {

    private static final String INSERT_QUERY = "INSERT INTO USERS VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE USERS SET password=?, name=?, email=? WHERE userid=?";
    private static final String FIND_ALL_QUERY = "SELECT userId, password, name, email FROM USERS";
    private static final String FIND_BY_ID_QUERY = "SELECT userId, password, name, email FROM USERS WHERE userid=?";


    public void insert(User user) throws DataAccessException {
        JdbcTemplate insertJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
        };
        insertJdbcTemplate.update(INSERT_QUERY, pss);
    }

    public void update(User user) throws DataAccessException {
        JdbcTemplate updateJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {
            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getUserId());
        };
        updateJdbcTemplate.update(UPDATE_QUERY, pss);
    }

    public List<User> findAll() throws DataAccessException {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> {};
        RowMapper<User> rowMapper = rs -> new User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
        );
        return selectJdbcTemplate.query(FIND_ALL_QUERY, pss, rowMapper);
    }

    public User findByUserId(String userId) throws DataAccessException {
        JdbcTemplate selectJdbcTemplate = new JdbcTemplate();
        PreparedStatementSetter pss = pstmt -> pstmt.setString(1, userId);
        RowMapper<User> rowMapper = rs -> new User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
        );
        return selectJdbcTemplate.queryForObject(FIND_BY_ID_QUERY, pss, rowMapper);
    }
}
