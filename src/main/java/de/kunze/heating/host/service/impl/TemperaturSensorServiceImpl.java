package de.kunze.heating.host.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.kunze.heating.host.api.TemperaturSensorResource;
import de.kunze.heating.host.model.Temperatur;
import de.kunze.heating.host.model.TemperaturSensor;
import de.kunze.heating.host.service.CommunicationService;
import de.kunze.heating.host.service.TemperaturSensorService;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TemperaturSensorServiceImpl implements TemperaturSensorService {

	private final CommunicationService communicationService;

	public TemperaturSensorServiceImpl(CommunicationService communicationService) {
		this.communicationService = communicationService;
	}

	@Override
	public List<TemperaturSensorTransfer> getTemperaturSensor() {
		return communicationService.getTemperaturSensors().stream().map(sensor -> {
			final String temperaturSensorId = sensor.getName();
			log.info("Find sensor: {}", temperaturSensorId);
			val temperaturSensor = new TemperaturSensorTransfer(temperaturSensorId, null);
			temperaturSensor.add(linkTo(TemperaturSensorResource.class).slash(temperaturSensorId).withSelfRel());
			return temperaturSensor;
		}).collect(Collectors.toList());
	}

	@Override
	public TemperaturSensorTransfer getTemperaturSensor(String temperaturSensorId) {
		final Temperatur temperatur = communicationService.getTemperatur(new TemperaturSensor(temperaturSensorId));

		val result = new TemperaturSensorTransfer(temperaturSensorId, temperatur.getTemperatur());
		result.add(
				linkTo(methodOn(TemperaturSensorResource.class).getTemperaturSensor(temperaturSensorId)).withSelfRel());

		return result;
	}

}
