package com.vmware.sample.istio;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private static final String RESPONSE_STRING_FORMAT = "[%s].Location v1 -> %s : %d\n";
    
    @Value("${app.from.cluster:cluster-name}")
    private String clusterName;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Counter to help us see the lifecycle
     */
    private int count = 0;

    /**
     * Flag for throwing a 503 when enabled
     */
    private boolean misbehave = false;

    private static final String HOSTNAME = parseContainerIdFromHostname(System.getenv().getOrDefault("HOSTNAME", "unknown"));

    static String parseContainerIdFromHostname(String hostname) {
        return hostname.replaceAll("location-v\\d+-", "");
    }

    @RequestMapping("/")
    public ResponseEntity<String> getRecommendations() {
        count++;
        logger.debug(String.format("recommendation request from %s: %d", HOSTNAME, count));
        // timeout();
        logger.debug("LocationController service ready to return");
        if (misbehave) {
            return doMisbehavior();
        }
        return ResponseEntity.ok(String.format(LocationController.RESPONSE_STRING_FORMAT, this.clusterName, HOSTNAME, count));
    }

    @RequestMapping("/mockerror")
    public ResponseEntity<String> flagMisbehave() {
        this.misbehave = true;
        logger.debug("'misbehave' has been set to 'true'");
        return ResponseEntity.ok("Next request to / will return a 503\n");
    }

    @RequestMapping("/fixerror")
    public ResponseEntity<String> flagBehave() {
        this.misbehave = false;
        logger.debug("'misbehave' has been set to 'false'");
        return ResponseEntity.ok("Next request to / will return 200\n");
    }

    private ResponseEntity<String> doMisbehavior() {
        logger.debug(String.format("Misbehaving %d", count));
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(String.format("LocationController misbehavior from '%s'\n", HOSTNAME));
    }

    private void timeout() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.info("Thread interrupted");
        }
    }
    
}