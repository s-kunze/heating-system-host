package de.kunze.heating.host.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;

import de.kunze.heating.host.api.RelaisResource;
import de.kunze.heating.host.model.Relais;
import de.kunze.heating.host.model.Status;
import de.kunze.heating.host.repository.RelaisRepository;
import de.kunze.heating.host.service.HeatingConfiguration;
import de.kunze.heating.host.service.RelaisService;
import de.kunze.heating.host.transfer.RelaisTransfer;
import de.kunze.heating.host.transfer.StatusTransfer;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RelaisServiceImpl implements RelaisService, ApplicationListener<ContextRefreshedEvent> {

	private static final String RELAIS_COMMAND_ACTIVATE = "gpio mode %d out";
	private static final String RELAIS_COMMAND_ON = "gpio write %d 0";
	private static final String RELAIS_COMMAND_OFF = "gpio write %d 1";

	private final RelaisRepository relaisRepository;
	private final JtaTransactionManager jtaTransactionManager;
	private final HeatingConfiguration heatingConfiguration;

	public RelaisServiceImpl(RelaisRepository relaisRepository, JtaTransactionManager jtaTransactionManager,
			HeatingConfiguration heatingConfiguration) {
		this.relaisRepository = relaisRepository;
		this.jtaTransactionManager = jtaTransactionManager;
		this.heatingConfiguration = heatingConfiguration;
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		jtaTransactionManager.setAllowCustomIsolationLevels(true);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		StringUtils.commaDelimitedListToSet(heatingConfiguration.getRelaisids()).stream().forEach(relais -> {
			val r = new Relais();

			r.setStatus(Status.UNKNOWN);
			r.setWiringPiId(Long.valueOf(relais));
			relaisRepository.save(r);
		});
	}

	@Override
	public List<RelaisTransfer> getRelaiss() {
		final List<Relais> relaiss = relaisRepository.findAll();

		return relaiss.stream().map(r -> {
			val relaisTransfer = new RelaisTransfer(r.getId(), StatusTransfer.valueOf(r.getStatus().toString()),
					r.getWiringPiId());

			relaisTransfer.add(linkTo(RelaisResource.class).slash(r.getId()).withSelfRel());

			return relaisTransfer;
		}).collect(Collectors.toList());
	}

	@Override
	public RelaisTransfer createRelais() {
		Relais relais = new Relais();
		relais.setStatus(Status.UNKNOWN);

		relais = relaisRepository.save(relais);
		final RelaisTransfer result = new RelaisTransfer(relais.getId(),
				StatusTransfer.valueOf(relais.getStatus().toString()), relais.getWiringPiId());

		return result;
	}

	@Override
	public RelaisTransfer getRelais(Long relaisId) {
		val relais = relaisRepository.findOne(relaisId);
		val result = new RelaisTransfer(relais.getId(), StatusTransfer.valueOf(relais.getStatus().toString()),
				relais.getWiringPiId());

		result.add(linkTo(RelaisResource.class).slash(relais.getId()).withSelfRel());
		result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash("on").withRel("on"));
		result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash("off").withRel("off"));

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, timeout = 20000, rollbackFor = Exception.class)
	public RelaisTransfer on(Long relaisId) {
		return toggle(relaisId, Status.ON);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, timeout = 20000, rollbackFor = Exception.class)
	public RelaisTransfer off(Long relaisId) {
		return toggle(relaisId, Status.OFF);
	}

	private RelaisTransfer toggle(Long relaisId, Status status) {
		Relais relais = relaisRepository.findOne(relaisId);
		relais.setStatus(status);

		setRelais(status, relais.getWiringPiId());

		relais = relaisRepository.save(relais);
		final RelaisTransfer result = new RelaisTransfer(relais.getId(),
				StatusTransfer.valueOf(relais.getStatus().toString()), relais.getWiringPiId());

		final String link = Status.ON == status ? "off" : "on";

		result.add(linkTo(RelaisResource.class).slash(relais.getId()).withSelfRel());
		result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash(link).withRel(link));

		return result;
	}

	private void setRelais(Status status, Long wiringPiId) {
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
