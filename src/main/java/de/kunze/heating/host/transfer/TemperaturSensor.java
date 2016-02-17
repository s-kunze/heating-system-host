package de.kunze.heating.host.transfer;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class TemperaturSensor extends ResourceSupport {

	private final String temperaturSensorId;
	private final Integer temperatur;

	@JsonCreator
	public TemperaturSensor(@JsonProperty String temperaturSensorId) {
		this.temperaturSensorId = temperaturSensorId;
		this.temperatur = null;
	}

	@JsonCreator
	public TemperaturSensor(@JsonProperty String temperaturSensorId,
			@JsonProperty Integer temperatur) {
		this.temperaturSensorId = temperaturSensorId;
		this.temperatur = temperatur;
	}

	public String getTemperaturSensorId() {
		return temperaturSensorId;
	}

	public Integer getTemperatur() {
		return temperatur;
	}

}
