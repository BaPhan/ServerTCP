package org.example.dto;

import java.io.Serializable;

public class WineDTO implements Serializable {
    private Integer id;

    private String name;
    /**
     * Nồng độ
     */
    private String concentration;

    private Integer year;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConcentration() {
        return concentration;
    }

    public void setConcentration(String concentration) {
        this.concentration = concentration;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public WineDTO(Integer id, String name, String concentration, Integer year, String countryName) {
        this.id = id;
        this.name = name;
        this.concentration = concentration;
        this.year = year;
        this.countryName = countryName;
    }

    public WineDTO() {
    }

    private String countryName;
}
