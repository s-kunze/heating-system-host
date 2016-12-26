package de.kunze.heating.host.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.kunze.heating.host.model.Relais;
import de.kunze.heating.host.model.Status;
import de.kunze.heating.host.model.Temperatur;
import de.kunze.heating.host.model.TemperaturSensor;
import de.kunze.heating.host.service.CommunicationService;
import de.kunze.heating.host.service.HeatingConfiguration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommunicationServiceImpl implements CommunicationService {

	private static final String RELAIS_COMMAND_ACTIVATE = "gpio mode %d out";
	private static final String RELAIS_COMMAND_ON = "gpio write %d 0";
	private static final String RELAIS_COMMAND_OFF = "gpio write %d 1";
	private static final Path TEMPERATUR = Paths.get("/sys", "bus", "w1", "devices");

	private final HeatingConfiguration heatingConfiguration;

	public CommunicationServiceImpl(HeatingConfiguration heatingConfiguration) {
		this.heatingConfiguration = heatingConfiguration;
	}

	@Override
	public List<TemperaturSensor> getTemperaturSensors() {
		return Arrays.stream(TEMPERATUR.toFile().listFiles(f -> !f.getName().contains("bus")))
				.map(file -> new TemperaturSensor(file.getName())).collect(Collectors.toList());
	}

	@Override
	@SneakyThrows
	public Temperatur getTemperatur(TemperaturSensor temperaturSensor) {
		final File file = TEMPERATUR.resolve(temperaturSensor.getName()).toFile();

		final List<String> lines = Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
		final String content = StringUtils.collectionToDelimitedString(lines, "");

		final String temperatur = content.substring(content.lastIndexOf('=') + 1);
		log.info("Temperatur is: {}", temperatur);
		return new Temperatur(Integer.valueOf(temperatur));
	}

	@Override
	public List<Relais> getRelaisFromSystem() {
		return StringUtils.commaDelimitedListToSet(heatingConfiguration.getRelaisids()).stream()
				.map(id -> new Relais(null, Status.UNKNOWN, Long.valueOf(id))).collect(Collectors.toList());
	}

	@Override
	public void relais(Status status, Long wiringPiId) {
		try {
			String command = String.format(RELAIS_COMMAND_ACTIVATE, wiringPiId);

			log.info("Exec: {}", command);
			final Process activateRelais = Runtime.getRuntime().exec(command);
			activateRelais.waitFor();

			command = null;
			if (Status.ON == status) {
				command = String.format(RELAIS_COMMAND_ON, wiringPiId);
			} else if (Status.OFF == status) {
				command = String.format(RELAIS_COMMAND_OFF, wiringPiId);
			}

			if (command != null) {
				log.info("Exec: {}", command);
				final Process setRelais = Runtime.getRuntime().exec(command);
				setRelais.waitFor();
			}
		} catch (IOException | InterruptedException e) {
			log.error("", e);
			throw new RuntimeException(e);
		}
	}

}
