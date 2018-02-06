package service.impl;

import dao.TimeDao;
import dao.impl.TimeDaoImpl;
import model.Time;
import service.TimeService;

import java.sql.SQLException;
import java.util.List;

public class TimeServiceImpl implements TimeService {

    @Override
    public List<Time> findAll() throws SQLException {
        TimeDao timeDao = new TimeDaoImpl();
        return timeDao.findAll();
    }

    @Override
    public int save(Time time) throws SQLException {
        TimeDao timeDao = new TimeDaoImpl();
        return timeDao.save(time);
    }

    @Override
    public void delete(List<Time> time) throws SQLException {
        TimeDao timeDao = new TimeDaoImpl();
        timeDao.delete(time);
    }
}
