package de.kunze.heating.host.service.impl;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.Link;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import com.google.common.io.Files;

import de.kunze.heating.host.service.impl.TemperaturSensorServiceImpl;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;
import lombok.SneakyThrows;
import lombok.val;

public class TemperaturSensorServiceImplTest {

    @Mock
    private TemperaturSensorServiceImpl temperaturSensorService;

    public TemperaturSensorServiceImplTest() {
	MockitoAnnotations.initMocks(this);
    }

    @Before
    public void before() {
	RequestContextHolder.setRequestAttributes(new ServletWebRequest(new MockHttpServletRequest()));
    }

    @Test
    public void testGetTemperaturSensor() throws Exception {
	when(temperaturSensorService.getTemperaturSensor()).thenCallRealMethod();
	when(temperaturSensorService.getTemperaturSensorNames()).thenReturn(createTemperaturSensors());

	List<TemperaturSensorTransfer> temperaturSensors = temperaturSensorService.getTemperaturSensor();

	assertNotNull(temperaturSensors);
	assertThat(temperaturSensors.size(), equalTo(2));

	TemperaturSensorTransfer temperaturSensor1 = temperaturSensors.get(0);
	assertNotNull(temperaturSensor1.getTemperaturSensorId());
	assertNotNull(temperaturSensor1.getLinks());
	Link link1 = temperaturSensor1.getLink("self");
	assertNotNull(link1);
	assertThat(link1.getHref(), containsString("28-0315743cc7ff"));

	TemperaturSensorTransfer temperaturSensor2 = temperaturSensors.get(1);
	assertNotNull(temperaturSensor2.getTemperaturSensorId());
	assertNotNull(temperaturSensor2.getLinks());
	Link link2 = temperaturSensor2.getLink("self");
	assertNotNull(link2);
	assertThat(link2.getHref(), containsString("28-0315743e46ff"));
    }

    @Test
    public void testGetTemperaturFromFile() throws Exception {
	when(temperaturSensorService.getTemperaturFromFile(any())).thenCallRealMethod();

	Integer temperatur = temperaturSensorService.getTemperaturFromFile(createDataFile());

	assertNotNull(temperatur);
	assertThat(temperatur, equalTo(25625));
    }

    @SneakyThrows
    private File createDataFile() {
	File file = new File("test");

	try (BufferedWriter bufferedWriter = Files.newWriter(file, Charset.forName("UTF-8"))) {
	    bufferedWriter.write("33 00 4b 46 ff ff 02 10 f4 : crc=f4 YES");
	    bufferedWriter.write("\r\n");
	    bufferedWriter.write("33 00 4b 46 ff ff 02 10 f4 t=25625");
	}

	return file;
    }

    private File[] createTemperaturSensors() {
	val result = new ArrayList<File>();

	File temperaturSensor1 = Paths.get("sys", "bus", "w1", "devices", "28-0315743cc7ff").toFile();
	File temperaturSensor2 = Paths.get("sys", "bus", "w1", "devices", "28-0315743e46ff").toFile();

	result.add(temperaturSensor1);
	result.add(temperaturSensor2);

	return result.toArray(new File[result.size()]);
    }

}
