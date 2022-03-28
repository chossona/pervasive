package shutter;

import java.util.List;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.ServiceProperty;
import org.apache.felix.ipojo.annotations.StaticServiceProperty;

import fr.liglab.adele.icasa.device.util.AbstractDevice;
import fr.liglab.adele.icasa.simulator.SimulatedDevice;
import shutter.Shutter;

@Component(name = "iCasa.Shutter")
@Provides(properties = { @StaticServiceProperty(type = "java.lang.Boolean", name = "test") })
public class ShutterImpl extends AbstractDevice implements Shutter, SimulatedDevice {

	@ServiceProperty(name = Shutter.DEVICE_SERIAL_NUMBER, mandatory = true)
	private String m_serialNumber;

	public ShutterImpl() {
		super();

		// Property initialization
		super.setPropertyValue(SimulatedDevice.LOCATION_PROPERTY_NAME, SimulatedDevice.LOCATION_UNKNOWN);

		// Property initialization
		setPropertyValue(Shutter.SHUTTER_OPEN, true);
	}

	@Override
	public String getSerialNumber() {
		return m_serialNumber;
	}

	@Override
	public synchronized boolean isOpen() {
		return (boolean) getPropertyValue(Shutter.SHUTTER_OPEN);
	}

	@Override
	public void open() {
		System.out.println("Open shutter " + getSerialNumber());
		setPropertyValue(Shutter.SHUTTER_OPEN, true);
	}

	@Override
	public void close() {
		System.out.println("Close shutter " + getSerialNumber());
		setPropertyValue(Shutter.SHUTTER_OPEN, false);
	}
}