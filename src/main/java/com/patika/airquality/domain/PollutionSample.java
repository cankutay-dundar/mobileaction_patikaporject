package com.patika.airquality.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pollution_sample", indexes = {
@Index(name = "idx_city_date", columnList = "cityName,date")
})
public class PollutionSample {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String cityName;

@Column(nullable = false)
private LocalDate date;

private Double co;
private Double so2;
private Double o3;

@Enumerated(EnumType.STRING)
private DataSourceType source;

public PollutionSample() { }

public PollutionSample(String cityName, LocalDate date, Double co, Double so2, Double o3, DataSourceType source)
{
this.cityName = cityName;
this.date = date;
this.co = co;
this.so2 = so2;
this.o3 = o3;
this.source = source;
}

public Long getId() { return id; }
public void setId(Long id) { this.id = id; }
public String getCityName() { return cityName; }
public void setCityName(String cityName) { this.cityName = cityName; }
public LocalDate getDate() { return date; }
public void setDate(LocalDate date) { this.date = date; }
public Double getCo() { return co; }
public void setCo(Double co) { this.co = co; }
public Double getSo2() { return so2; }
public void setSo2(Double so2) { this.so2 = so2; }
public Double getO3() { return o3; }
public void setO3(Double o3) { this.o3 = o3; }
public DataSourceType getSource() { return source; }
public void setSource(DataSourceType source) { this.source = source; }
}
