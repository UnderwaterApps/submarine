package com.submarine.statsreporter.vo;


public class StatsRequestVO {
    public String udid = "";
    public String model = "";
    public String version = "";

    @Override
    public String toString() {
        return "StatsReporterVO{" +
                "udid='" + udid + '\'' +
                ", model='" + model + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
