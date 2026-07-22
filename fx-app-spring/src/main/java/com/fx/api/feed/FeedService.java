package com.fx.api.feed;

import java.util.List;

import com.fx.api.repo.RateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FeedService {

    private static final Logger log = LoggerFactory.getLogger(FeedService.class);

    private final RateRepository rates;
    private final OrchestratorClient orchestrator;
    private final AcceptingState accepting;

    public FeedService(RateRepository rates, OrchestratorClient orchestrator, AcceptingState accepting) {
        this.rates = rates;
        this.orchestrator = orchestrator;
        this.accepting = accepting;
    }

    public void handle(IncomingBatch batch) {
        boolean allow = accepting.isAccepting();
        int stored = 0;
        List<IncomingRate> incoming = batch.rates() == null ? List.of() : batch.rates();

        if (allow) {
            for (IncomingRate rate : incoming) {
                rates.insert(rate.base().toUpperCase(), rate.quote().toUpperCase(), rate.rate());
                stored++;
            }
        }

        String status = allow ? "ACCEPTED" : "DECLINED";
        log.info("Feed batch {} processed: status={}, stored={}", batch.batchId(), status, stored);

        try {
            orchestrator.sendAck(batch.batchId(), status);
        } catch (RuntimeException ex) {
            log.warn("Ack callback failed for batch {}: {}", batch.batchId(), ex.getMessage());
        }
    }
}
