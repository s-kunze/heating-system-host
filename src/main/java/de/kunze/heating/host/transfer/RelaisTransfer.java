package de.kunze.heating.host.transfer;

import org.springframework.hateoas.ResourceSupport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RelaisTransfer extends ResourceSupport {

	private Long relaisId;
	private StatusTransfer status;
	private Long wiringPiId;

}
