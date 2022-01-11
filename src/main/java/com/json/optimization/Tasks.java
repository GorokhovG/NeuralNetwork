package com.json.optimization;

import java.util.ArrayList;
import java.util.List;

public class Tasks {
    private String taskID;
    private String driverMark;
    private Double valueTotal;
    private Double weightTotal;
    private Double quantityTotal;
    private Double costTotal;
    private List<Position> positions;

    Tasks() {
        this.positions = new ArrayList<>();
    }

    Tasks(String taskID, String driverMark, Double valueTotal, Double weightTotal, Double quantityTotal, Double costTotal) {
        this.taskID = taskID;
        this.driverMark = driverMark;
        this.valueTotal = valueTotal;
        this.weightTotal = weightTotal;
        this.quantityTotal = quantityTotal;
        this.costTotal = costTotal;
        this.positions = new ArrayList<>();
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getDriverMark() {
        return driverMark;
    }

    public void setDriverMark(String driverMark) {
        this.driverMark = driverMark;
    }

    public Double getValueTotal() {
        return valueTotal;
    }

    public void setValueTotal(Double valueTotal) {
        this.valueTotal = valueTotal;
    }

    public Double getWeightTotal() {
        return weightTotal;
    }

    public void setWeightTotal(Double weightTotal) {
        this.weightTotal = weightTotal;
    }

    public Double getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(Double quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public Double getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(Double costTotal) {
        this.costTotal = costTotal;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public int positionSize() {
        return positions.size();
    }

    public void setTask(String name, Double cost, Double weight, Double value, Double quantity) {
        Position position = new Position(name, cost, weight, value, quantity);

        positions.add(position);
    }
}
