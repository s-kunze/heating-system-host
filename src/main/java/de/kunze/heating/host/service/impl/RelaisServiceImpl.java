package de.kunze.heating.host.service.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.jta.JtaTransactionManager;

import de.kunze.heating.host.api.RelaisResource;
import de.kunze.heating.host.model.Relais;
import de.kunze.heating.host.model.Status;
import de.kunze.heating.host.repository.RelaisRepository;
import de.kunze.heating.host.service.RelaisService;
import de.kunze.heating.host.transfer.RelaisTransfer;
import de.kunze.heating.host.transfer.StatusTransfer;

@Service
public class RelaisServiceImpl implements RelaisService, InitializingBean {

    @Autowired
    private RelaisRepository relaisRepository;

    @Autowired
    private JtaTransactionManager jtaTransactionManager;

    @Override
    public void afterPropertiesSet() throws Exception {
	jtaTransactionManager.setAllowCustomIsolationLevels(true);
    }

    @Override
    public List<RelaisTransfer> getRelaiss() {
	List<Relais> relaiss = relaisRepository.findAll();

	return relaiss.stream().map(r -> {
	    RelaisTransfer relaisTransfer = new RelaisTransfer(r.getId(),
		    StatusTransfer.valueOf(r.getStatus().toString()));

	    relaisTransfer.add(linkTo(RelaisResource.class).slash(r.getId()).withSelfRel());

	    return relaisTransfer;
	}).collect(Collectors.toList());
    }

    @Override
    public RelaisTransfer createRelais() {
	Relais relais = new Relais();
	relais.setStatus(Status.UNKNOWN);

	relais = relaisRepository.save(relais);
	RelaisTransfer result = new RelaisTransfer(relais.getId(),
		StatusTransfer.valueOf(relais.getStatus().toString()));

	// result.add(linkTo(methodOn(RelaisResourceImpl.class).getRelais(relais.getId())).withSelfRel());
	// result.add(linkTo(methodOn(RelaisResourceImpl.class).on(relais.getId())).withRel("on"));
	// result.add(linkTo(methodOn(RelaisResourceImpl.class).off(relais.getId())).withRel("off"));

	return result;
    }

    @Override
    public RelaisTransfer getRelais(Long relaisId) {
	Relais relais = relaisRepository.findOne(relaisId);
	RelaisTransfer result = new RelaisTransfer(relais.getId(),
		StatusTransfer.valueOf(relais.getStatus().toString()));

	result.add(linkTo(RelaisResource.class).slash(relais.getId()).withSelfRel());
	result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash("on").withRel("on"));
	result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash("off").withRel("off"));

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, timeout = 20000)
    public RelaisTransfer on(Long relaisId) {
	Relais relais = relaisRepository.findOne(relaisId);
	relais.setStatus(Status.ON);
	relais = relaisRepository.save(relais);
	RelaisTransfer result = new RelaisTransfer(relais.getId(),
		StatusTransfer.valueOf(relais.getStatus().toString()));

	result.add(linkTo(RelaisResource.class).slash(relais.getId()).withSelfRel());
	result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash("off").withRel("off"));

	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE, timeout = 20000)
    public RelaisTransfer off(Long relaisId) {
	Relais relais = relaisRepository.findOne(relaisId);
	relais.setStatus(Status.OFF);
	relais = relaisRepository.save(relais);
	RelaisTransfer result = new RelaisTransfer(relais.getId(),
		StatusTransfer.valueOf(relais.getStatus().toString()));

	result.add(linkTo(RelaisResource.class).slash(relais.getId()).withSelfRel());
	result.add(linkTo(RelaisResource.class).slash(relais.getId()).slash("on").withRel("on"));

	return result;
    }

}
