package com.json.optimization;

public class Stop {
    private String startTime;
    private Integer duration;

    Stop(String startTime, Integer duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    Stop() {

    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
