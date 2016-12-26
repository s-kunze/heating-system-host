package de.kunze.heating.host.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kunze.heating.host.api.TemperaturSensorResource;
import de.kunze.heating.host.exeception.TemperaturSensorNotFoundException;
import de.kunze.heating.host.service.TemperaturSensorService;
import de.kunze.heating.host.transfer.TemperaturSensorTransfer;
import lombok.SneakyThrows;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TemperaturSensorServiceImpl implements TemperaturSensorService {

	private static final File TEMPERATUR_BASE = Paths.get("/sys", "bus", "w1", "devices").toFile();;
	// private static final String DATA_FILE_NAME = "w1_slave";

	@Override
	public List<TemperaturSensorTransfer> getTemperaturSensor() {
		return getTemperaturSensorNames().map(sensor -> {
			final String temperaturSensorId = sensor.getName();
			log.info("Find sensor: {}", temperaturSensorId);
			val temperaturSensor = new TemperaturSensorTransfer(temperaturSensorId, null);
			temperaturSensor.add(linkTo(TemperaturSensorResource.class).slash(temperaturSensorId).withSelfRel());
			return temperaturSensor;
		}).collect(Collectors.toList());
	}

	@Override
	public TemperaturSensorTransfer getTemperaturSensor(String temperaturSensorId) {
		val result = new TemperaturSensorTransfer(temperaturSensorId, getTemperatur(temperaturSensorId));
		result.add(
				linkTo(methodOn(TemperaturSensorResource.class).getTemperaturSensor(temperaturSensorId)).withSelfRel());

		return result;
	}

	Integer getTemperatur(String temperaturSensorId) {
		final Optional<File> maybeTemperaturSensor = getTemperaturSensorNames()
				.filter(f -> f.getName().equals(temperaturSensorId)).findFirst();

		final File temperaturSensor = maybeTemperaturSensor.orElseThrow(() -> new TemperaturSensorNotFoundException());
		return getTemperaturFromFile(temperaturSensor);
	}

	@SneakyThrows
	Integer getTemperaturFromFile(File dataFile) {
		final List<String> lines = Files.readAllLines(dataFile.toPath(), Charset.forName("UTF-8"));
		final String content = StringUtils.collectionToDelimitedString(lines, "");

		final String temperatur = content.substring(content.lastIndexOf('=') + 1);
		log.info("Temperatur is: {}", temperatur);
		return Integer.valueOf(temperatur);
	}

	Stream<File> getTemperaturSensorNames() {
		return Arrays.stream(TEMPERATUR_BASE.listFiles(f -> !f.getName().contains("bus")));
	}

}
