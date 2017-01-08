package de.kunze.heating.host.service;

import java.util.List;

import de.kunze.heating.host.model.Relais;
import de.kunze.heating.host.model.Status;
import de.kunze.heating.host.model.Temperatur;
import de.kunze.heating.host.model.TemperaturSensor;

public interface CommunicationService {

    List<Relais> getRelaisFromSystem();

    Temperatur getTemperatur(TemperaturSensor temperaturSensor);

    List<TemperaturSensor> getTemperaturSensors();

    void relais(Status status, Long wiringPiId);

}
