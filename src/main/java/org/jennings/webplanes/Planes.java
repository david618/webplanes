package org.jennings.webplanes;

import java.util.ArrayList;

public class Planes {

	private ArrayList<Plane> planes;

	public Planes() {
		super();
		planes = new ArrayList<>();
	}

	public ArrayList<Plane> getPlanes() {
		return planes;
	}
	
	public void add(Plane plane) {
		planes.add(plane);
	}

	@Override
	public String toString() {
		return "Planes [planes=" + planes + "]";
	}
	
	
	
}
