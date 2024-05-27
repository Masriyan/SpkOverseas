package id.co.map.spk.scheduler;

import id.co.map.spk.counters.SpkMonthlyCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpkMonthlyScheduler {

    private static final Logger logger = LogManager.getLogger(SpkMonthlyScheduler.class);

    /**
     * every first day in month at 00:00 set number of monthly spk to 0.
     */
    @Scheduled(cron="0 0 0 1 * *")
    public void resetNumOfSpk(){
        SpkMonthlyCounter.numOfMonthlySpk = 0;
        logger.info("SPK Number reset to 0");
    }
}
