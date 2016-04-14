package hello;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    // http://blog.cjred.net/267/
    //@Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0/10 * * * * ?")
    public void reportCurrentTime() {
        //System.out.println("The time is now " + dateFormat.format(new Date()));
        log.info("{} : {}", "현재시간", dateFormat.format(new Date()));
    }
}
