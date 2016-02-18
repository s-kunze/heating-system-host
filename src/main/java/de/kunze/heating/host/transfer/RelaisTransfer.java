package de.kunze.heating.host.transfer;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class RelaisTransfer extends ResourceSupport {

    private final Long relaisId;
    private final StatusTransfer status;

    @JsonCreator
    public RelaisTransfer(@JsonProperty Long relaisId, @JsonProperty StatusTransfer status) {
	this.relaisId = relaisId;
	this.status = status;
    }

    public Long getRelaisId() {
	return relaisId;
    }

    public StatusTransfer getStatus() {
	return status;
    }

}
