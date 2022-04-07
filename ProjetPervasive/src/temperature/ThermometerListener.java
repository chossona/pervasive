package temperature;

import java.util.ArrayList;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.temperature.Thermometer;

public class ThermometerListener implements DeviceListener<GenericDevice> {

	public static double LIM_COEF = 0.5;
	
	ArrayList<WeightedObservedPoint> smoothy = new ArrayList<WeightedObservedPoint>();
	int nb =0;
	
	@Override
	public void deviceAdded(GenericDevice t) {
		for (nb=0; nb<100;nb++) {
			smoothy.add(new WeightedObservedPoint(nb,TemperatureControlImpl.BEST_TEMPERATURE,1));
		}
	}

	@Override
	public void deviceEvent(GenericDevice t, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void devicePropertyAdded(GenericDevice t, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void devicePropertyModified(GenericDevice t, String arg1, Object arg2, Object arg3) {
		Thermometer thermometer = (Thermometer) t;
		Double tempdiff = thermometer.getTemperature() - TemperatureControlImpl.BEST_TEMPERATURE;
		
		smoothy.add(new WeightedObservedPoint(nb++,thermometer.getTemperature(),1));
		smoothy.remove(0);
		
		if(Math.abs(tempdiff)> TemperatureControlImpl.VARIANCE ) {
			final double coef = Math.abs(PolynomialCurveFitter.create(1).fit(smoothy)[0]);
			if(true || Math.abs(tempdiff)> TemperatureControlImpl.CRITICAL ) {
				TemperatureControlImpl.instance.changeTemperatureInRoom(tempdiff,(String)thermometer.getPropertyValue("LOCATION_PROPERTY_NAME"));
			}
		}
		
	}

	@Override
	public void devicePropertyRemoved(GenericDevice t, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deviceRemoved(GenericDevice t) {
		// TODO Auto-generated method stub
		
	}

}
