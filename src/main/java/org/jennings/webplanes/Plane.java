package org.jennings.webplanes;

public class Plane {
	private final Integer id;
	private final Long ts;
	private final Double speed;
	private final Double dist;
	private final Double bearing;
	private final Integer rtid;
	private final String orig;
	private final String dest;
	private final Integer secsToDep;
	private final Double lon;
	private final Double lat;
	
	public Plane(Integer id, Long ts, Double speed, Double dist, Double bearing, Integer rtid, String orig, String dest,
			Integer secsToDep, Double lon, Double lat) {
		super();
		this.id = id;
		this.ts = ts;
		this.speed = speed;
		this.dist = dist;
		this.bearing = bearing;
		this.rtid = rtid;
		this.orig = orig;
		this.dest = dest;
		this.secsToDep = secsToDep;
		this.lon = lon;
		this.lat = lat;
	}
	@Override
	public String toString() {
		return "Plane [id=" + id + ", ts=" + ts + ", speed=" + speed + ", dist=" + dist + ", bearing=" + bearing
				+ ", rtid=" + rtid + ", orig=" + orig + ", dest=" + dest + ", secsToDep=" + secsToDep + ", lon=" + lon
				+ ", lat=" + lat + "]";
	}
	public Integer getId() {
		return id;
	}
	public Long getTs() {
		return ts;
	}
	public Double getSpeed() {
		return speed;
	}
	public Double getDist() {
		return dist;
	}
	public Double getBearing() {
		return bearing;
	}
	public Integer getRtid() {
		return rtid;
	}
	public String getOrig() {
		return orig;
	}
	public String getDest() {
		return dest;
	}
	public Integer getSecsToDep() {
		return secsToDep;
	}
	public Double getLon() {
		return lon;
	}
	public Double getLat() {
		return lat;
	}
}
