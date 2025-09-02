package com.patika.airquality.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "city")
public class City
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private Double lat;
    private Double lon;

    public City() { }

    public City(String name, Double lat, Double lon)
    {
    this.name = name;
    this.lat = lat;
    this.lon = lon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }
    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }
}
