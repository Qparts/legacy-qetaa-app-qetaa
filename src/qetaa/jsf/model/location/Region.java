package qetaa.jsf.model.location;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;

public class Region implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String nameAr;
	private double latitude;
	private double longitude;
	private int mapZoom;
	private Country country;
	@Transient
	private List<City> cities;
	private String jsonCode;
	/*
	 ['sa-4293', 0],
    ['sa-tb', 1],
    ['sa-jz', 2],
    ['sa-nj', 3],
    ['sa-ri', 4],
    ['sa-md', 5],
    ['sa-ha', 6],
    ['sa-qs', 7],
    ['sa-hs', 8],
    ['sa-jf', 9],
    ['sa-sh', 10],
    ['sa-ba', 11],
    ['sa-as', 12],
    ['sa-mk', 13]
	
	
	*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getMapZoom() {
		return mapZoom;
	}
	public void setMapZoom(int mapZoom) {
		this.mapZoom = mapZoom;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getJsonCode() {
		return jsonCode;
	}
	public void setJsonCode(String jsonCode) {
		this.jsonCode = jsonCode;
	}
	public List<City> getCities() {
		return cities;
	}
	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
}


