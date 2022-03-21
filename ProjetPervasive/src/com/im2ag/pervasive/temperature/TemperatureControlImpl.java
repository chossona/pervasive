package com.im2ag.pervasive.temperature;

import fr.liglab.adele.icasa.device.temperature.Thermometer;
import fr.liglab.adele.icasa.device.temperature.Heater;
import java.util.Map;

public class TemperatureControlImpl {

	/** Field for Thermometer dependency */
	private Thermometer[] Thermometer;
	/** Field for Heater dependency */
	private Heater[] Heater;
	
	private ThermometerListener thermometer_lstn = new ThermometerListener();
	private HeaterListener heater_lstn = new HeaterListener();

	/** Bind Method for Thermometer dependency */
	public void BindThermometer(Thermometer thermometer, Map properties) {
		thermometer.addListener(thermometer_lstn);
	}

	/** Unbind Method for Thermometer dependency */
	public void UnbindThermometer(Thermometer thermometer, Map properties) {
		thermometer.removeListener(thermometer_lstn);
	}

	/** Bind Method for Heater dependency */
	public void BindHeater(Heater heater, Map properties) {
		heater.addListener(heater_lstn);
	}

	/** Unbind Method for Heater dependency */
	public void UnbindHeater(Heater heater, Map properties) {
		heater.removeListener(heater_lstn);
		}

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("System is stopping !");
		for (Thermometer t : Thermometer) {
			t.removeListener(thermometer_lstn);
		}
		for (Heater h : Heater) {
			h.removeListener(heater_lstn);
		}
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("System is starting ...");
	}

}
