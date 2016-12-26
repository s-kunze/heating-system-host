package de.kunze.heating.host.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.kunze.heating.host.model.Temperatur;
import de.kunze.heating.host.model.TemperaturSensor;
import de.kunze.heating.host.service.CommunicationService;
import de.kunze.heating.host.service.TemperaturSensorService;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;
import lombok.val;

@Service
public class TemperaturSensorServiceImpl implements TemperaturSensorService {

	private final CommunicationService communicationService;

	public TemperaturSensorServiceImpl(CommunicationService communicationService) {
		this.communicationService = communicationService;
	}

	@Override
	public List<TemperaturSensorTransfer> getTemperaturSensor() {
		return communicationService.getTemperaturSensors().stream().map(sensor -> getTemperaturSensor(sensor.getName()))
				.collect(Collectors.toList());
	}

	@Override
	public TemperaturSensorTransfer getTemperaturSensor(String temperaturSensorId) {
		final Temperatur temperatur = communicationService.getTemperatur(new TemperaturSensor(temperaturSensorId));

		val result = new TemperaturSensorTransfer(temperaturSensorId, temperatur.getTemperatur());
		// result.add(
		// linkTo(methodOn(TemperaturSensorResource.class).getTemperaturSensor(temperaturSensorId)).withSelfRel());

		return result;
	}

}
