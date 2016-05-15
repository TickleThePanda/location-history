package com.ticklethepanda.lochistmap.cartograph.ecp;

import com.ticklethepanda.lochistmap.cartograph.Point;
import com.ticklethepanda.lochistmap.cartograph.googlelocation.GoogleLocation;

public class EcpPoint implements Point {
	private final double x;
	private final double y;

	public EcpPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static EcpPoint convertLocationToECP(GoogleLocation location) {
		double x = location.getX() / 360.0;
		double y = -location.getY() / 180.0;
		return new EcpPoint(x, y);
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public double getY() {
		return y;
	}
}
