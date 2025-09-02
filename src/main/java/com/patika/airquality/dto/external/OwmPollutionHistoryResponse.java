package com.patika.airquality.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OwmPollutionHistoryResponse
{
    private List<Item> list;

    public List<Item> getList() { return list; }
    public void setList(List<Item> list) { this.list = list; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item
    {
    private long dt;
    private Components components;

    public long getDt() { return dt; }
    public void setDt(long dt) { this.dt = dt; }
    public Components getComponents() { return components; }
    public void setComponents(Components components) { this.components = components; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Components
    {
    private Double co;
    private Double so2;
    private Double o3;

    public Double getCo() { return co; }
    public void setCo(Double co) { this.co = co; }
    public Double getSo2() { return so2; }
    public void setSo2(Double so2) { this.so2 = so2; }
    public Double getO3() { return o3; }
    public void setO3(Double o3) { this.o3 = o3; }

    public Map<String, Double> asMap()
    {
    return Map.of(
    "CO", co != null ? co : 0.0,
    "SO2", so2 != null ? so2 : 0.0,
    "O3", o3 != null ? o3 : 0.0
    );
    }
    }
}
