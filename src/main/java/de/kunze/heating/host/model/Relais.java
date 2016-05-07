package de.kunze.heating.host.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Relais {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Status status;

	private Long wiringPiId;

}
