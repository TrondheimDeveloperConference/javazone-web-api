package no.javazone;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class JavaZoneWebApiConfiguration extends Configuration {
    @NotEmpty
    private String emsHost;

    @NotEmpty
    private String devNull;

    @JsonProperty
    public String getEmsHost() {
        return emsHost;
    }

    @JsonProperty
    public void setEmsHost(String emsHost) {
        this.emsHost = emsHost;
    }

    @JsonProperty
    public String getDevNull() {
        return devNull;
    }

    @JsonProperty
    public void setDevNull(String devNull) {
        this.devNull = devNull;
    }
}