package com.cabinet.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdonnanceResultDTO {
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
