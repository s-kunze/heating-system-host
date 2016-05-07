package de.kunze.heating.host.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such TemperaturSensor")
public class TemperaturSensorNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

}
