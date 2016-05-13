package hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationProperties()
@Slf4j
public class Application implements CommandLineRunner {


    @Autowired
    ScheduledTasks scheduledTasks;

    @Value("${gojin.enabled}")
    String gojinEnabled;

    @Value("${sunin.enabled}")
    String suninEnabled;

    @Value("${sunjin.enabled}")
    String sunjinEnabled;

    @Value("${test.enabled}")
    String testEnabled;

    @Value("${m.gojin.enabled}")
    String mgojinEnabled;

    @Value("${m.sunin.enabled}")
    String msuninEnabled;

    @Value("${m.sunjin.enabled}")
    String msunjinEnabled;


    @Override
    public void run(String... args) throws Exception {

        if(testEnabled.toUpperCase().equals("Y")) {
            scheduledTasks.releaseTest();
        }

        if(gojinEnabled.toUpperCase().equals("Y")) {
            log.info("시작시 고진 톰켓 실행");
            scheduledTasks.releaseGojin();
        }

        if(suninEnabled.toUpperCase().equals("Y")) {
            log.info("시작시 선인 톰켓 실행");
            scheduledTasks.releaseSunin();
        }

        if(sunjinEnabled.toUpperCase().equals("Y")) {
            log.info("시작시 선진 톰켓 실행");
            scheduledTasks.releaseSunjin();
        }

        if(mgojinEnabled.toUpperCase().equals("Y")) {
            log.info("시작시 고진 모바일 실행");
            scheduledTasks.releaseMGojin();
        }

        if(msuninEnabled.toUpperCase().equals("Y")) {
            log.info("시작시 선인 모바일 실행");
            scheduledTasks.releaseMSunin();
        }

        if(msunjinEnabled.toUpperCase().equals("Y")) {
            log.info("시작시 선진 모바일 실행");
            scheduledTasks.releaseMSunjin();
        }

    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class);
    }
}
