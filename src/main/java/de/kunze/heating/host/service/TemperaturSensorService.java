package de.kunze.heating.host.service;

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
import de.kunze.heating.host.transfer.TemperaturSensor;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.java.Log;

@Service
@Log
public class TemperaturSensorService {

    private static final File TEMPERATUR_BASE = Paths.get("sys", "bus", "w1", "devices").toFile();;
    private static final String DATA_FILE_NAME = "w1_slave";

    public List<TemperaturSensor> getTemperaturSensor() {
	List<TemperaturSensor> result = new ArrayList<>();

	File[] sensors = getTemperaturSensorNames();

	if (sensors != null) {
	    for (File sensor : sensors) {
		String temperaturSensorId = sensor.getName();
		log.info("Find sensor: " + temperaturSensorId);
		val temperaturSensor = new TemperaturSensor(temperaturSensorId, null);
		temperaturSensor.add(linkTo(TemperaturSensorResource.class).slash(temperaturSensorId).withSelfRel());

		result.add(temperaturSensor);
	    }
	}

	return result;
    }

    public TemperaturSensor getTemperaturSensor(String temperaturSensorId) {
	val result = new TemperaturSensor(temperaturSensorId, getTemperatur(temperaturSensorId));
	result.add(
		linkTo(methodOn(TemperaturSensorResource.class).getTemperaturSensor(temperaturSensorId)).withSelfRel());

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
	log.info("Temperatur is: " + temperatur);
	return Integer.valueOf(temperatur);
    }

    File[] getTemperaturSensorNames() {
	String[] list = TEMPERATUR_BASE.list();
	log.info("BasePath: " + TEMPERATUR_BASE.getPath());

	for (String s : list) {
	    log.info("TemperaturSensor: " + s);
	}

	return TEMPERATUR_BASE.listFiles(f -> !f.getName().contains("bus"));
    }

}
