package de.kunze.heating.host.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.kunze.heating.host.service.TemperaturSensorService;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;

/**
 *
 * @author Stefan
 *
 */
@RestController
@RequestMapping("/temperatursensor")
public class TemperaturSensorResource {

	private final TemperaturSensorService temperaturSensorService;

	public TemperaturSensorResource(TemperaturSensorService temperaturSensorService) {
		this.temperaturSensorService = temperaturSensorService;
	}

	@GetMapping
	public List<TemperaturSensorTransfer> getTemperaturSensors() {
		return temperaturSensorService.getTemperaturSensor();
	}

	@GetMapping(value = "/{temperaturSensorId}")
	public TemperaturSensorTransfer getTemperaturSensor(@PathVariable(required = true) String temperaturSensorId) {
		return temperaturSensorService.getTemperaturSensor(temperaturSensorId);
	}

}
