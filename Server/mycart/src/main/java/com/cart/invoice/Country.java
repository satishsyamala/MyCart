package com.cart.invoice;

public class Country implements Comparable<Country>{

	private String name;
	private int population;
	
	public Country()
	{
		
	}
	
	public Country(String name, int population) {
		super();
		this.name = name;
		this.population = population;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	@Override
	public int compareTo(Country o) {
		if(this.population==o.population)
		return this.name.compareTo(o.name);
		else return 
		this.population - o.population;
	}

	

}
