package id.co.map.spk.startup;

import id.co.map.spk.counters.SpkMonthlyCounter;
import id.co.map.spk.entities.SuratPerintahKerjaEntity;
import id.co.map.spk.repositories.SpkRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class SpkDailyCounterInitiator implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(SpkDailyCounterInitiator.class);
    private final SpkRepository spkRepository;

    public SpkDailyCounterInitiator(SpkRepository spkRepository) {
        this.spkRepository = spkRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("========================== SPK Daily Counter Initiator ==========================");

        initSpkDailyCounter();

        logger.info("Latest SPK Number      : " + SpkMonthlyCounter.numOfMonthlySpk);
        logger.info("========================== SPK Daily Counter Initiator ==========================");
    }

    /**
     * Get latest number of SPK(Daily) from database.
     */
    public void initSpkDailyCounter(){

        Calendar c = Calendar.getInstance();
        Integer month = c.get(Calendar.MONTH) + 1;
        Integer year = c.get(Calendar.YEAR);

        List<SuratPerintahKerjaEntity> spkList = spkRepository.findByCreatedPeriod(month, year);

        if(spkList.size() > 0){

            int last = spkList.size() - 1;
            SuratPerintahKerjaEntity spk = spkList.get(last);

            SpkMonthlyCounter.numOfMonthlySpk = Integer.valueOf(spk.getSpkId().substring(1, 5));
        }
    }

}
