package de.kunze.heating.host.service;

import java.util.List;

import de.kunze.heating.host.transfer.RelaisTransfer;

public interface RelaisService {

	List<RelaisTransfer> getRelaiss();

	RelaisTransfer createRelais();

	RelaisTransfer getRelais(Long relaisId);

	RelaisTransfer on(Long relaisId);

	RelaisTransfer off(Long relaisId);

}
