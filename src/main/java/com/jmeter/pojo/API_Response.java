package com.jmeter.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class API_Response {
    public String transaction;
    public int sampleCount;
    public int errorCount;
    public double errorPct;
    public double meanResTime;
    public double medianResTime;
    public double minResTime;
    public double maxResTime;
    public double pct1ResTime;
    public double pct2ResTime;
    public double pct3ResTime;
    public double throughput;
    public double receivedKBytesPerSec;
    public double sentKBytesPerSec;
}