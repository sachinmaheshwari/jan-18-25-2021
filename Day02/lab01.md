* A Stock broker is interested in fetching the stock prices for specific symbols she is interested in
* INTUIT, HP, FB, GOOG, INFY, TCS, MS, APPL, AZ, RIL


* Stock broker wants to build an akka system where stock prices are fetched by different actors for each symbol
* There's a third party stock service that the broker wants to use. Third party Stock service is a simple class as given below

``` java
public class StockServer {
	
	public static double getPrice(String symbol) {
		//Time consuming operation
		try {
			Thread.sleep((int)Math.random() * 10);
		}
		catch(Exception ex) {}
		double price = Math.random() * 1000;
		return price;
	}

}
```
* The application collects the prices of all the symbols and finally prints the symbol and the price in a pretty format on the console
* Here's a sample output when you run the program

```
Price fetched for 1 stock symbol
Price computed for 1 stock symbol
Price computed for 2 stock symbols
Price computed for 3 stock symbols
Price computed for 4 stock symbols
Price computed for 5 stock symbols
Price computed for 5 stock symbols
Price computed for 6 stock symbols
Price computed for 7 stock symbols
Price computed for 8 stock symbols
Price computed for 9 stock symbols
Price computed for 10 stock symbols
Here's the price list

INTUIT - 876.34
HP - 122.22

...

```
