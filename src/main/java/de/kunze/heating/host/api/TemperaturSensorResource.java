package de.kunze.heating.host.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

	@Autowired
	private TemperaturSensorService temperaturSensorService;

	@RequestMapping(method = RequestMethod.GET, value = "")
	public List<TemperaturSensorTransfer> getTemperaturSensors() {
		return temperaturSensorService.getTemperaturSensor();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{temperaturSensorId}")
	public TemperaturSensorTransfer getTemperaturSensor(@PathVariable String temperaturSensorId) {
		return temperaturSensorService.getTemperaturSensor(temperaturSensorId);
	}

}
