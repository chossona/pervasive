package shutter;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.light.Photometer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShutterControlImpl implements DeviceListener {

	/**
	 * The name of the LOCATION property
	 */
	public static final String LOCATION_PROPERTY_NAME = "Location";

	/**
	 * The name of the location for unknown value
	 */
	public static final String LOCATION_UNKNOWN = "unknown";

	public static final String LOCATION_OUTSIDE = "Outside";

	public static final Double TRESHOLD_LUMINANCE_CLOSE = 400.0;

	/** Field for shutter dependency */
	private Shutter[] shutters;
	/** Field for photometer dependency */
	private Photometer[] photometers;

	/** Bind Method for photometer dependency */
	public synchronized void bindPhotometer(Photometer photometer, Map properties) {
		photometer.addListener(this);
	}

	/** Unbind Method for photometer dependency */
	public synchronized void unbindPhotometer(Photometer photometer, Map properties) {
		photometer.removeListener(this);
	}

	/** Bind Method for shutter dependency */
	public void bindShutter(Shutter shutter, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Unbind Method for shutter dependency */
	public void unbindShutter(Shutter shutter, Map properties) {
		// TODO: Add your implementation code here
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Shutter Control starting!");
	}

	/** Component Lifecycle Method */
	public synchronized void stop() {
		// TODO: Add your implementation code here
		for (Photometer p : photometers) {
			p.removeListener(this);
		}
	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {

		// we assume that we listen only to presence sensor events (otherwise there is a
		// bug)
		assert device instanceof Photometer : "device must be a presence sensors only";

		// based on that assumption we can cast the generic device without checking via
		// instanceof
		Photometer changingSensor = (Photometer) device;

		// check the change is related to presence sensing
		if (propertyName.equals(Photometer.PHOTOMETER_CURRENT_ILLUMINANCE)) {
			// get the location of the changing sensor:
			String detectorLocation = (String) changingSensor.getPropertyValue(LOCATION_PROPERTY_NAME);
			System.out.println("The device with the serial number" + changingSensor.getSerialNumber() + " has changed");
			System.out.println("This sensor is in the room :" + detectorLocation);
			System.out.println("luminance :" + changingSensor.getIlluminance());

			if (detectorLocation.equals(LOCATION_OUTSIDE)) {
				if (changingSensor.getIlluminance() < TRESHOLD_LUMINANCE_CLOSE) {
					for (Shutter shutter : shutters) {
						if (shutter.isOpen()) {
							shutter.close();
						}
					}
				} else {
					for (Shutter shutter : shutters) {
						if (!shutter.isOpen()) {
							shutter.open();
						}
					}
				}
			}
		}
	}

	@Override
	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * Return all Shutters from the given location
	 * 
	 * @param location : the given location
	 * @return the list of matching Shutters
	 */
	private synchronized List<Shutter> getShuttersFromLocation(String location) {
		List<Shutter> shuttersLocation = new ArrayList<Shutter>();
		for (Shutter shutter : shutters) {
			if (shutter.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				shuttersLocation.add(shutter);
			}
		}
		return shuttersLocation;
	}

}
