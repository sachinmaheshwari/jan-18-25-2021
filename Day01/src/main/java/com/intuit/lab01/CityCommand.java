package com.intuit.lab01;

public class CityCommand implements WeatherCommand {

	private static final long serialVersionUID = 1L;
	
	private String name;

	public String getData() {
		return name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CityCommand(String name) {
		this.name = name;
	}

	public CityCommand() {
	}
	
	
}
