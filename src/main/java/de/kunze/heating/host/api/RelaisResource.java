package de.kunze.heating.host.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.kunze.heating.host.service.RelaisService;
import de.kunze.heating.host.transfer.RelaisTransfer;

@RestController
@RequestMapping("/relais")
public class RelaisResource {

	@Autowired
	private RelaisService relaisService;

	@RequestMapping(method = RequestMethod.GET, value = "")
	public List<RelaisTransfer> getRelaiss() {
		return relaisService.getRelaiss();
	}

	@RequestMapping(method = RequestMethod.POST, value = "")
	public RelaisTransfer createRelais() {
		return relaisService.createRelais();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{relaisId}")
	public RelaisTransfer getRelais(@PathVariable Long relaisId) {
		return relaisService.getRelais(relaisId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{relaisId}/on")
	public RelaisTransfer on(@PathVariable Long relaisId) {
		return relaisService.on(relaisId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{relaisId}/off")
	public RelaisTransfer off(@PathVariable Long relaisId) {
		return relaisService.off(relaisId);
	}

}
