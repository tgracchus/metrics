package com.tgracchus.metrics.endpoints;

import com.tgracchus.metrics.endpoints.dto.IngestRequest;
import com.tgracchus.metrics.endpoints.dto.IngestResponse;
import com.tgracchus.metrics.services.IngestService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class IngestEndpoint  {

    private final IngestService controller;

    public IngestEndpoint(IngestService controller) {
        this.controller = controller;
    }

    @PostMapping(path = "/ingest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<IngestResponse> ingest(@Valid @RequestBody IngestRequest request) {
        controller.ingest(request);
        return ResponseEntity.ok(new IngestResponse());
    }


}
