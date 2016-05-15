package com.ticklethepanda.lochistmap.cartograph.googlelocation;

import com.ticklethepanda.lochistmap.cartograph.Point;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;


public class GoogleLocation implements Point {
	private long timestampMs;
	private long latitudeE7;
	private long longitudeE7;
	private long accuracy;

	public LocalDate getDate() {
		return LocalDate.from(
				Instant.ofEpochMilli(this.getTimestampMs())
						.atZone(ZoneId.of("UTC")));
	}

	public long getTimestampMs() {
		return timestampMs;
	}

	public double getY() {
		return latitudeE7;
	}

	public double getX() {
		return longitudeE7;
	}

	public long getAccuracy() {
		return accuracy;
	}
}
