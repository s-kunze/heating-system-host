package de.kunze.heating.host.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import de.kunze.heating.host.model.Temperatur;
import de.kunze.heating.host.model.TemperaturSensor;
import de.kunze.heating.host.service.CommunicationService;
import de.kunze.heating.host.service.TemperaturSensorService;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;
import lombok.val;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TemperaturSensorServiceImplTest {

    @Autowired
    private TemperaturSensorService temperaturSensorService;

    @MockBean
    private CommunicationService communicationService;

    private List<TemperaturSensor> createTemperaturSensors() {
        val result = new ArrayList<TemperaturSensor>();

        result.add(new TemperaturSensor("28-0315743cc7ff"));
        result.add(new TemperaturSensor("28-0315743e46ff"));

        return result;
    }

    @Test
    public void testGetTemperaturFromFile() throws Exception {
        final Temperatur temperatur = new Temperatur(25625);
        when(communicationService.getTemperatur(any(TemperaturSensor.class))).thenReturn(temperatur);

        final TemperaturSensorTransfer temperaturSensor = temperaturSensorService
                .getTemperaturSensor("28-0315743cc7ff");

        assertThat(temperaturSensor).isNotNull();
        assertThat(temperatur.getTemperatur()).isEqualTo(25625);
    }

    @Test
    public void testGetTemperaturSensor() throws Exception {
        when(communicationService.getTemperaturSensors()).thenReturn(createTemperaturSensors());

        final List<TemperaturSensorTransfer> temperaturSensors = temperaturSensorService.getTemperaturSensor();

        assertThat(temperaturSensors).isNotNull();
        assertThat(temperaturSensors.size()).isEqualTo(2);

        final TemperaturSensorTransfer temperaturSensor1 = temperaturSensors.get(0);
        assertThat(temperaturSensor1.getTemperaturSensorId()).isNotNull();
        assertThat(temperaturSensor1.getLinks()).isNotNull();

        final Link link1 = temperaturSensor1.getLink("self");
        assertThat(link1).isNotNull();
        assertThat(link1.getHref()).contains("28-0315743cc7ff");

        final TemperaturSensorTransfer temperaturSensor2 = temperaturSensors.get(1);
        assertThat(temperaturSensor2.getTemperaturSensorId()).isNotNull();
        assertThat(temperaturSensor2.getLinks()).isNotNull();

        final Link link2 = temperaturSensor2.getLink("self");
        assertThat(link2).isNotNull();
        assertThat(link2.getHref()).contains("28-0315743e46ff");
    }

}
