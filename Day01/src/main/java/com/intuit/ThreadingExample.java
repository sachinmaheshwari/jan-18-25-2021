package com.intuit;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThreadingExample {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		List<BigInteger> primes = new ArrayList<BigInteger>();
		
		// ConcurrentModificationException
		// lock (synchronized)
		Thread status = new Thread(() -> {
			while(primes.size() < 100) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("**********Prime numbers calculated so far: " + primes.size());
				primes.forEach(System.out::println);
			}		
		});
	
		status.start();
		
		
		for (int i = 0; i < 100; i++) {
			
			Thread primeThread = new Thread(() -> {
				BigInteger big = new BigInteger(3000, new Random());
				BigInteger prime = big.nextProbablePrime();
				primes.add(prime);
			});
			primeThread.start();
		}

		long end = System.currentTimeMillis();
		System.out.println("Time taken: " + (end - start) + "ms");
	}

}
