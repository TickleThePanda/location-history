package co.uk.ticklethepanda.lochistmap.cartograph.googlelocation;

import co.uk.ticklethepanda.lochistmap.cartograph.ecp.EcpPoint;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by panda on 15/05/16.
 */
public class GoogleLocationToEcpConverter {

	public static List<EcpPoint> convertToECPPoints(List<GoogleLocation> locations) {
		return locations.stream()
				.map(l -> EcpPoint.convertLocationToECP(l))
				.collect(Collectors.toList());
	}
}
