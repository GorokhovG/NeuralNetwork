package com.json.optimization;

import java.util.ArrayList;
import java.util.List;

public class JsonObject {
    private String point;
    private Double pointLat;
    private Double pointLon;
    private Double stopLat;
    private Double stopLon;

    private Stop stop;
    private List<Tasks> tasks;

    JsonObject () {
        this.stop = new Stop();
        this.tasks = new ArrayList<>();
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public void setPointLat(Double pointLat) {
        this.pointLat = pointLat;
    }

    public void setPointLon(Double pointLon) {
        this.pointLon = pointLon;
    }

    public void setStopLat(Double stopLat) {
        this.stopLat = stopLat;
    }

    public void setStopLon(Double stopLon) {
        this.stopLon = stopLon;
    }

    public String getPoint() {
        return point;
    }

    public Double getPointLat() {
        return pointLat;
    }

    public Double getPointLon() {
        return pointLon;
    }

    public Double getStopLat() {
        return stopLat;
    }

    public Double getStopLon() {
        return stopLon;
    }

    public void setStop(String time, Integer duration) {
        stop = new Stop(time, duration);
    }

    public Stop getStop() {
        return stop;
    }

    public void setStop(Stop stop) {
        this.stop = stop;
    }

    public List<Tasks> getTasks() {
        return tasks;
    }

    public void setTasks(List<Tasks> tasks) {
        this.tasks = tasks;
    }

    public int tasksSize() {
        return tasks.size();
    }

    public void setTasks(String taskID, String driverMark, Double valueTotal, Double weightTotal, Double quantityTotal, Double costTotal) {
        Tasks tasksInfo = new Tasks(taskID, driverMark, valueTotal, weightTotal, quantityTotal, costTotal);

        tasks.add(tasksInfo);
    }
}