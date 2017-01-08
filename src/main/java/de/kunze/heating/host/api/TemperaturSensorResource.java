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
@RequestMapping()
public class TemperaturSensorResource {

    private final TemperaturSensorService temperaturSensorService;

    public TemperaturSensorResource(final TemperaturSensorService temperaturSensorService) {
        this.temperaturSensorService = temperaturSensorService;
    }

    @GetMapping(value = "/temperatursensor/{temperaturSensorId}")
    public TemperaturSensorTransfer getTemperaturSensor(
            @PathVariable(required = true) final String temperaturSensorId) {
        return temperaturSensorService.getTemperaturSensor(temperaturSensorId);
    }

    @GetMapping(path = "/temperatursensor.json")
    public List<TemperaturSensorTransfer> getTemperaturSensors() {
        return temperaturSensorService.getTemperaturSensor();
    }

}
