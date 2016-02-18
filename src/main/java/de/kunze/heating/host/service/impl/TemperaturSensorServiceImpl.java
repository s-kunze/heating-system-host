package de.kunze.heating.host.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.io.Files;

import de.kunze.heating.host.api.TemperaturSensorResource;
import de.kunze.heating.host.exeception.TemperaturSensorNotFoundException;
import de.kunze.heating.host.service.TemperaturSensorService;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;
import de.kunze.heating.host.util.Loggable;
import lombok.SneakyThrows;
import lombok.val;

@Service
public class TemperaturSensorServiceImpl implements Loggable, TemperaturSensorService {

    private static final File TEMPERATUR_BASE = Paths.get("/sys", "bus", "w1", "devices").toFile();;
    private static final String DATA_FILE_NAME = "w1_slave";

    @Override
    public List<TemperaturSensorTransfer> getTemperaturSensor() {
	List<TemperaturSensorTransfer> result = new ArrayList<>();

	File[] sensors = getTemperaturSensorNames();

	if (sensors != null) {
	    for (File sensor : sensors) {
		String temperaturSensorId = sensor.getName();
		logger().info("Find sensor: {}", temperaturSensorId);
		val temperaturSensor = new TemperaturSensorTransfer(temperaturSensorId, null);
		temperaturSensor
			.add(linkTo(TemperaturSensorResource.class).slash(temperaturSensorId).withSelfRel());

		result.add(temperaturSensor);
	    }
	}

	return result;
    }

    @Override
    public TemperaturSensorTransfer getTemperaturSensor(String temperaturSensorId) {
	val result = new TemperaturSensorTransfer(temperaturSensorId, getTemperatur(temperaturSensorId));
	result.add(linkTo(methodOn(TemperaturSensorResource.class).getTemperaturSensor(temperaturSensorId))
		.withSelfRel());

	return result;
    }

    Integer getTemperatur(String temperaturSensorId) {
	File[] possibleTemperaturSensors = TEMPERATUR_BASE.listFiles(f -> f.getName().equals(temperaturSensorId));

	if (possibleTemperaturSensors == null || possibleTemperaturSensors.length != 1) {
	    throw new TemperaturSensorNotFoundException();
	}

	File temperaturSensor = possibleTemperaturSensors[0];

	File[] possibleDataFiles = temperaturSensor.listFiles(f -> f.getName().equals(DATA_FILE_NAME));
	if (possibleDataFiles == null || possibleDataFiles.length != 1) {
	    throw new TemperaturSensorNotFoundException();
	}

	File dataFile = possibleDataFiles[0];

	return getTemperaturFromFile(dataFile);
    }

    @SneakyThrows
    Integer getTemperaturFromFile(File dataFile) {
	List<String> lines = Files.readLines(dataFile, Charset.forName("UTF-8"));
	String content = StringUtils.collectionToDelimitedString(lines, "");

	String temperatur = content.substring(content.lastIndexOf('=') + 1);
	logger().info("Temperatur is: {}", temperatur);
	return Integer.valueOf(temperatur);
    }

    File[] getTemperaturSensorNames() {
	return TEMPERATUR_BASE.listFiles(f -> !f.getName().contains("bus"));
    }

}
