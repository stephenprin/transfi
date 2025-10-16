package transfi.web.app.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ExchangeRateScheduleTaskRunner implements CommandLineRunner {
    private final Logger logger= LoggerFactory.getLogger(ExchangeRateScheduleTaskRunner.class);
    private final ExchangeRateService rateService;
    private final ScheduledExecutorService scheduler;

    public ExchangeRateScheduleTaskRunner(ExchangeRateService exchangeRateService, ScheduledExecutorService scheduler) {
        this.rateService = exchangeRateService;
        this.scheduler = scheduler;
    }

    @Override
    public void run(String... args) throws Exception {
            logger.info("Calling the Currency API");
            scheduler.scheduleWithFixedDelay(rateService::getExchangeRate, 0, 24, TimeUnit.HOURS);
            logger.info("The Currency API has ended");

    }
}
