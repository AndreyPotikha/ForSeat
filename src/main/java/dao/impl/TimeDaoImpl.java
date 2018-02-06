package dao.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import dao.TimeDao;
import model.Time;

import java.sql.SQLException;
import java.util.List;

public class TimeDaoImpl implements TimeDao {

    private static final String URL = "jdbc:sqlite:miracle.sqlite";
    private Dao<Time, Integer> dao;
    {
        try {
            ConnectionSource source = new JdbcConnectionSource(URL);
            dao = DaoManager.createDao(source, Time.class);
        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    @Override
    public List<Time> findAll() throws SQLException {
        return dao.queryBuilder().orderBy("distance", true).query();
    }

    @Override
    public int save(Time time) throws SQLException {
        return dao.create(time);
    }

    @Override
    public void delete(List<Time> time) throws SQLException {
        dao.delete(time);
    }
}
