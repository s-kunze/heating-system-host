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
@RequestMapping("/relais")
public class RelaisResource {

	private final RelaisService relaisService;

	public RelaisResource(RelaisService relaisService) {
		this.relaisService = relaisService;
	}
	
	@GetMapping(value = "")
	public List<RelaisTransfer> getRelaiss() {
		return relaisService.getRelaiss();
	}

	@PostMapping(value = "")
	public RelaisTransfer createRelais() {
		return relaisService.createRelais();
	}

	@GetMapping(value = "/{relaisId}")
	public RelaisTransfer getRelais(@PathVariable(required = true) Long relaisId) {
		return relaisService.getRelais(relaisId);
	}

	@PostMapping(value = "/{relaisId}/on")
	public RelaisTransfer on(@PathVariable(required = true)  Long relaisId) {
		return relaisService.on(relaisId);
	}

	@PostMapping(value = "/{relaisId}/off")
	public RelaisTransfer off(@PathVariable(required = true)  Long relaisId) {
		return relaisService.off(relaisId);
	}

}
