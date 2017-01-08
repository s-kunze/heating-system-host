package de.kunze.heating.host.service;

import java.util.List;

import de.kunze.heating.host.transfer.RelaisTransfer;

public interface RelaisService {

    RelaisTransfer createRelais();

    RelaisTransfer getRelais(Long relaisId);

    List<RelaisTransfer> getRelaiss();

    RelaisTransfer off(Long relaisId);

    RelaisTransfer on(Long relaisId);

}
