package service;

import model.Time;

import java.sql.SQLException;
import java.util.List;

public interface TimeService {

    List<Time> findAll() throws SQLException;

    int save(Time time) throws SQLException;

    void delete(List<Time> time) throws SQLException;

}
