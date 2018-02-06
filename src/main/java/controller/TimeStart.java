package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;
import model.Time;
import service.TimeService;
import service.impl.TimeServiceImpl;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TimeStart implements Initializable{

    @FXML
    private TextField fieldStation;
    @FXML
    private Button btnClear;
    @FXML
    private TableView timeTable;
    @FXML
    private TableColumn columnI;
    @FXML
    private TableColumn columnDistanceToI;
    @FXML
    private TableColumn columnDistanceCalibration;
    @FXML
    private TableColumn columnTimeToI;
    @FXML
    private TableColumn columnTimeInI;
    @FXML
    private TextField fieldDistance;
    @FXML
    private TextField fieldTime;
    @FXML
    private TextField fieldStartTime;
    @FXML
    private TextField fieldCalibration;
    @FXML
    private TextField fieldAverageSpeed;
    @FXML
    private TextField fieldNumberOfLastStation;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStation;

    private ObservableList<Time> timesList = FXCollections.observableArrayList();
    private List<Time> timeList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnStart.setOnAction(event -> start());
        btnClear.setOnAction(event -> clear());
        btnStation.setOnAction(event -> newStation());
        init();
    }


    private void start() {
        String distanceString = fieldDistance.getText();
        String newDistanceString = distanceString.replace(",", ".");
        double distance = Double.parseDouble(newDistanceString);

        int timeInMin = Integer.parseInt(fieldTime.getText());
        timeInMin *= 60;

        double timeForEachSite = timeInMin / (distance * 10);
        LocalTime localTime = LocalTime.of(0, 0, 0);

        String startTime = fieldStartTime.getText();
        String[] startTimeArr = startTime.split(":");
        int[] startTimeArrInt = new int[3];
        for (int i = 0; i < startTimeArr.length; i++) {
            startTimeArrInt[i] = Integer.parseInt(startTimeArr[i]);
        }
        LocalTime localTimeStart = LocalTime.of(startTimeArrInt[0], startTimeArrInt[1], startTimeArrInt[2]);

        int distanceStart = 0;

        int calibration = 0;
        if (!(fieldCalibration.getText().equals(""))) {
            calibration = Integer.parseInt(fieldCalibration.getText());
        }
        int calckCalibration = 0;
        Time time = new Time();
        time.setI(1);
        time.setDistance(Double.valueOf(0));
        time.setCalibration(0);
        time.setTime_to_i(String.valueOf(localTime));
        time.setTime_in_i(String.valueOf(localTimeStart));
        timeList.add(time);
        save(time);
        for (int i = 2; i <= distance * 10 + 1; i++) {
            time.setI(i);
            time.setDistance(Double.valueOf(distanceStart += 100));
            time.setCalibration(calckCalibration += calibration);
            time.setTime_to_i(setTimeToI(time.getDistance()));
            time.setTime_in_i(setTimeInI(time.getDistance()));
            timeList.add(time);
            save(time);
        }

        init();
    }

    private String setTimeInI(double distance) {
        String startTime = fieldStartTime.getText();
        String[] startTimeArr = startTime.split(":");
        int[] startTimeArrInt = new int[3];
        for (int i = 0; i < startTimeArr.length; i++) {
            startTimeArrInt[i] = Integer.parseInt(startTimeArr[i]);
        }
        LocalTime localTime = LocalTime.of(startTimeArrInt[0], startTimeArrInt[1], startTimeArrInt[2]);
        double averageSpeed = Double.parseDouble(fieldAverageSpeed.getText());
        double timeSpeedCalck = distance / (averageSpeed * 1000) * 60;
        int timeSpeedCalclMin = (int) timeSpeedCalck;
        timeSpeedCalck = (timeSpeedCalck - timeSpeedCalclMin) * 60;
        int roundTimeInSec = (int) Math.round(timeSpeedCalck);
        localTime = localTime.plusMinutes(timeSpeedCalclMin);
        localTime = localTime.plusSeconds(roundTimeInSec);
        return String.valueOf(localTime);
    }

    private String setTimeToI(Double distance) {
        LocalTime localTime = LocalTime.of(0, 0, 0);
        double averageSpeed = Double.parseDouble(fieldAverageSpeed.getText());
        double timeSpeedCalck = distance / (averageSpeed * 1000) * 60;
        int timeSpeedCalclMin = (int) timeSpeedCalck;
        timeSpeedCalck = (timeSpeedCalck - timeSpeedCalclMin) * 60;
        int roundTimeInSec = (int) Math.round(timeSpeedCalck);
        localTime = localTime.plusMinutes(timeSpeedCalclMin);
        localTime = localTime.plusSeconds(roundTimeInSec);
        return String.valueOf(localTime);
    }

    private void newStation() {
        String startTime = fieldStartTime.getText();
        String[] startTimeArr = startTime.split(":");
        int[] startTimeArrInt = new int[3];
        for (int i = 0; i < startTimeArr.length; i++) {
            startTimeArrInt[i] = Integer.parseInt(startTimeArr[i]);
        }
        LocalTime localTime = LocalTime.of(startTimeArrInt[0], startTimeArrInt[1], startTimeArrInt[2]);

        double distance = Double.parseDouble(fieldStation.getText());
        double averageSpeed = Double.parseDouble(fieldAverageSpeed.getText());
        double timeSpeedCalck = distance / (averageSpeed * 1000) * 60;
        int timeSpeedCalclMin = (int) timeSpeedCalck;
        timeSpeedCalck = (timeSpeedCalck - timeSpeedCalclMin) * 60;
        int roundTimeInSec = (int) Math.round(timeSpeedCalck);
        localTime = localTime.plusMinutes(timeSpeedCalclMin);
        localTime = localTime.plusSeconds(roundTimeInSec);
        Time time = new Time();
        time.setDistance(distance);
        time.setTime_in_i(String.valueOf(localTime));
        TimeService timeService = new TimeServiceImpl();
        try {
            timeService.save(time);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        init();
    }


    private void save(Time time) {
        TimeService timeService = new TimeServiceImpl();
        try {
            timeService.save(time);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:miracle.sqlite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM time");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        init();
    }

    private void sort() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:miracle.sqlite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM time ORDER BY distance");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        columnI.setCellValueFactory(new PropertyValueFactory<>("i"));
        columnDistanceToI.setCellValueFactory(new PropertyValueFactory<>("distance"));
        columnDistanceCalibration.setCellValueFactory(new PropertyValueFactory<>("calibration"));
        columnTimeToI.setCellValueFactory(new PropertyValueFactory<>("time_to_i"));
        columnTimeInI.setCellValueFactory(new PropertyValueFactory<>("time_in_i"));
        load();

    }

    @SneakyThrows
    private void load() {
        timesList.clear();
        TimeService timeService = new TimeServiceImpl();
        List<Time> times = timeService.findAll();
        timesList.addAll(times);
        timeTable.setItems(timesList);
    }

}
