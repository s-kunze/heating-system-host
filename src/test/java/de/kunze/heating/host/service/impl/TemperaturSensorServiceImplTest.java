package de.kunze.heating.host.service.impl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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

	@Test
	public void testGetTemperaturSensor() throws Exception {
		when(communicationService.getTemperaturSensors()).thenReturn(createTemperaturSensors());

		final List<TemperaturSensorTransfer> temperaturSensors = temperaturSensorService.getTemperaturSensor();

		assertNotNull(temperaturSensors);
		assertThat(temperaturSensors.size(), equalTo(2));

		final TemperaturSensorTransfer temperaturSensor1 = temperaturSensors.get(0);
		assertNotNull(temperaturSensor1.getTemperaturSensorId());
		assertNotNull(temperaturSensor1.getLinks());
		final Link link1 = temperaturSensor1.getLink("self");
		assertNotNull(link1);
		assertThat(link1.getHref(), containsString("28-0315743cc7ff"));

		final TemperaturSensorTransfer temperaturSensor2 = temperaturSensors.get(1);
		assertNotNull(temperaturSensor2.getTemperaturSensorId());
		assertNotNull(temperaturSensor2.getLinks());
		final Link link2 = temperaturSensor2.getLink("self");
		assertNotNull(link2);
		assertThat(link2.getHref(), containsString("28-0315743e46ff"));
	}

	@Test
	public void testGetTemperaturFromFile() throws Exception {
		final Temperatur temperatur = new Temperatur(25625);
		when(communicationService.getTemperatur(any(TemperaturSensor.class))).thenReturn(temperatur);

		final TemperaturSensorTransfer temperaturSensor = temperaturSensorService
				.getTemperaturSensor("28-0315743cc7ff");

		assertNotNull(temperaturSensor);
		assertThat(temperaturSensor.getTemperatur(), equalTo(25625));
	}

	private List<TemperaturSensor> createTemperaturSensors() {
		val result = new ArrayList<TemperaturSensor>();

		result.add(new TemperaturSensor("28-0315743cc7ff"));
		result.add(new TemperaturSensor("28-0315743e46ff"));

		return result;
	}

}
