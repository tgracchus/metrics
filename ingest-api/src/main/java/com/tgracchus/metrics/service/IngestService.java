package com.tgracchus.metrics.service;

import com.tgracchus.metrics.endpoints.dto.IngestRequest;

public interface IngestService {

    void ingest(IngestRequest ingestRequest);
}
