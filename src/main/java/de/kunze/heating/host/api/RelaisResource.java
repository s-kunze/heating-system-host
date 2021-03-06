package de.kunze.heating.host.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.kunze.heating.host.service.RelaisService;
import de.kunze.heating.host.transfer.RelaisTransfer;

@RestController
@RequestMapping()
public class RelaisResource {

    private final RelaisService relaisService;

    public RelaisResource(final RelaisService relaisService) {
        this.relaisService = relaisService;
    }

    @PostMapping(path = "/relais.json")
    public RelaisTransfer createRelais() {
        return relaisService.createRelais();
    }

    @GetMapping(value = "/relais/{relaisId}")
    public RelaisTransfer getRelais(@PathVariable(required = true) final Long relaisId) {
        return relaisService.getRelais(relaisId);
    }

    @GetMapping(path = "/relais.json")
    public List<RelaisTransfer> getRelaiss() {
        return relaisService.getRelaiss();
    }

    @PostMapping(value = "/relais/{relaisId}/off.json")
    public RelaisTransfer off(@PathVariable(required = true) final Long relaisId) {
        return relaisService.off(relaisId);
    }

    @PostMapping(value = "/relais/{relaisId}/on.json")
    public RelaisTransfer on(@PathVariable(required = true) final Long relaisId) {
        return relaisService.on(relaisId);
    }

}
