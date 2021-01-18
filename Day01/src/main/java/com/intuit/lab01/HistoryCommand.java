package com.intuit.lab01;

public class HistoryCommand implements WeatherCommand {

	@Override
	public String getData() {
		return "history";
	}

}
