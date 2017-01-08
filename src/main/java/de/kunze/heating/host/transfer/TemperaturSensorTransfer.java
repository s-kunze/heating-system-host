package de.kunze.heating.host.transfer;

import org.springframework.hateoas.ResourceSupport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TemperaturSensorTransfer extends ResourceSupport {

    private String temperaturSensorId;
    private Integer temperatur;

}
