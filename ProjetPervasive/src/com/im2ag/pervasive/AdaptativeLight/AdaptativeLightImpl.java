package com.im2ag.pervasive.AdaptativeLight;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.light.BinaryLight;
import fr.liglab.adele.icasa.device.light.DimmerLight;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;
import fr.liglab.adele.icasa.device.light.Photometer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdaptativeLightImpl implements DeviceListener<GenericDevice> {
	/** The name of the location for unknown value */
	public static final String LOCATION_PROPERTY_NAME = "Location";
	/** The name of the location for unknown value */
	public static final String LOCATION_UNKNOWN = "unknown";
	/** Field for dimmerLights dependency */
	private DimmerLight[] dimmerLights;
	/** Field for presenceSensors dependency */
	private PresenceSensor[] presenceSensors;
	/** Field for photometers dependency */
	private Photometer[] photometers;
	/** Percentage of luminosity wanted*/
	private int luxRate = 55;

	/** Bind Method for dimmerLight dependency */
	public void bindDimmerLight(DimmerLight dimmerLight, Map properties) {
		System.out.println("Bind Dimmer Light " + dimmerLight.getSerialNumber());
	}

	/** Unbind Method for dimmerLight dependency */
	public void unbindDimmerLight(DimmerLight dimmerLight, Map properties) {
		System.out.println("Unbind Dimmer Light " + dimmerLight.getSerialNumber());
	}

	/** Bind Method for photometer dependency */
	public void bindPhotometer(Photometer photometer, Map properties) {
		System.out.println("Bind photometer " + photometer.getSerialNumber());
	}

	/** Unbind Method for photometer dependency */
	public void unbindPhotometer(Photometer photometer, Map properties) {
		System.out.println("Unbind photometer " + photometer.getSerialNumber());
	}

	/** Bind Method for presenceSensors dependency */
	public void bindPresenceSensors(PresenceSensor presenceSensor, Map properties) {
		presenceSensor.addListener(this);
		System.out.println("Bind presence sensor " + presenceSensor.getSerialNumber());
	}

	/** Unbind Method for presenceSensors dependency */
	public void unbindPresenceSensors(PresenceSensor presenceSensor, Map properties) {
		// remove listener on presence sensor on unbinding
		presenceSensor.removeListener(this);
		System.out.println("Unbind presence sensor " + presenceSensor.getSerialNumber());
	}

	
	/** Component Lifecycle Method */
	public void stop() {
		for (PresenceSensor sensor : presenceSensors) {
			sensor.removeListener(this);
		}
		System.out.println("Adaptative Light s'éteint");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Adaptative Light démarre !");
	}
	
    /**
     * Return all BinaryLight from the given location
     * 
     * @param location
     *            : the given location
     * @return the list of matching BinaryLights
     */
    private synchronized List<DimmerLight> getDimmerLightFromLocation(
        String location) {
      List<DimmerLight> dimmerLightsLocation = new ArrayList<DimmerLight>();
      for (DimmerLight dimLight : dimmerLights) {
        if (dimLight.getPropertyValue(LOCATION_PROPERTY_NAME).equals(
            location)) {
        	dimmerLightsLocation.add(dimLight);
        }
      }
      return dimmerLightsLocation;
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
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {

		// we assume that we listen only to presence sensor events (otherwise there is a
		// bug)
		assert device instanceof PresenceSensor : "device must be a presence sensors only";

		// based on that assumption we can cast the generic device without checking via
		// instanceof
		PresenceSensor changingSensor = (PresenceSensor) device;

		// check the change is related to presence sensing
		if (propertyName.equals(PresenceSensor.PRESENCE_SENSOR_SENSED_PRESENCE)) {
			// get the location of the changing sensor:
			String detectorLocation = (String) changingSensor.getPropertyValue(LOCATION_PROPERTY_NAME);
			System.out.println("The device with the serial number" + changingSensor.getSerialNumber() + " has changed");
			System.out.println("This sensor is in the room :" + detectorLocation);
		
			// if the location is known :
		    if (!detectorLocation.equals(LOCATION_UNKNOWN)) {
		      // get the related binary lights
		      List<DimmerLight> sameLocationLigths = getDimmerLightFromLocation(detectorLocation);
		      int currentlight = 0;
		      double luxValue=100;
		      for (DimmerLight dimmerLight : sameLocationLigths) {
		           // and switch them on/off depending on the sensed presence
		           if(changingSensor.getSensedPresence()){
		        	   //on calcule la luminosité souhaiter en fonction du taux luxRate (en pourcent)
		        	   luxValue = dimmerLight.getMaxPowerLevel()*luxRate/100;
		        	   dimmerLight.setPowerLevel(55.0); //turn on
		        	   currentlight++;       	   
		           }else{
		        	   //turn off the light
		               dimmerLight.setPowerLevel(0);
		           }
		      }
		    }
		}

	}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub
		
	}

}
