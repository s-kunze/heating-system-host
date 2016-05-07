package de.kunze.heating.host.service;

import java.util.List;

import de.kunze.heating.host.transfer.TemperaturSensorTransfer;

public interface TemperaturSensorService {

	List<TemperaturSensorTransfer> getTemperaturSensor();

	TemperaturSensorTransfer getTemperaturSensor(String temperaturSensorId);

}