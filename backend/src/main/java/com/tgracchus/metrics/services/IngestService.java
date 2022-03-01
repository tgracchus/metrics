package com.tgracchus.metrics.services;

import com.tgracchus.metrics.endpoints.dto.IngestRequest;

public interface IngestService {

    void ingest(IngestRequest ingestRequest);
}
