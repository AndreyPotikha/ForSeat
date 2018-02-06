package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@DatabaseTable(tableName = "time")
@ToString
public class Time {

    @DatabaseField(columnName = "i")
    private int i;
    @DatabaseField(columnName = "distance")
    private Double distance;
    @DatabaseField(columnName = "calibration")
    private double calibration;
    @DatabaseField(columnName = "time_to_i")
    private String time_to_i;
    @DatabaseField(columnName = "time_in_i")
    private String time_in_i;

}
