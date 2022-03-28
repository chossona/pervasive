package shutter;

import fr.liglab.adele.icasa.device.GenericDevice;

public interface Shutter extends GenericDevice {

	static String SHUTTER_OPEN = "shutter.open"; // state Property (open or closed)

	boolean isOpen();

	void open();

	void close();

}