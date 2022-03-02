package com.tgracchus.metrics.endpoints;

import com.tgracchus.metrics.services.IngestService;
import com.tgracchus.metrics.endpoints.dto.IngestResponse;
import com.tgracchus.metrics.endpoints.dto.IngestRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;

@RestController
public class IngestEndpoint  {

    private final IngestService controller;

    public IngestEndpoint(IngestService controller) {
        this.controller = controller;
    }

    @PostMapping(path = "/ingest", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<IngestResponse> ingest(@Valid @RequestBody IngestRequest request) {
        controller.ingest(request);
        return ResponseEntity.ok(new IngestResponse());
    }


}
