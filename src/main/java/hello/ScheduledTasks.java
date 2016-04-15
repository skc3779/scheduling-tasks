package hello;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties()
@Slf4j
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final Long TIMESLEEP = 5000l;

    @Value("${source.path}")
    String sourcePath;

    @Value("${gojin.path}")
    String gojinPath;

    @Value("${sunin.path}")
    String suninPath;

    @Value("${sunjin.path}")
    String sunjinPath;

    @Value("${ignore.files}")
    String ignoreFiles;

    //@Scheduled(fixedRate = 5000)
    //@Scheduled(cron = "0 10 1 * * * ?")
    @Scheduled(cron = "0/10 * * * * ?")
    public void buildExecute() {
        try {
            // 고진 소스복사 및 톰켓 재시작
            releaseGojin();
            // 선인 소스복사 및 톰켓 재시작
            releaseSunin();
            // 선인 소스복사 및 톰켓 재시작
            releaseSunjin();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }


    /**
     *  고진소스 복사
     */
    private void releaseGojin() throws IOException, InterruptedException {
        //Runtime.getRuntime().exec("cmd /c start d:\\stop.bat");
        Thread.sleep(TIMESLEEP);
        log.info("고진톰켓 정지 완료! ");
        copyFolder(new File(sourcePath), new File(gojinPath));
        log.info("고진톰켓 소스복사 완료! ");
        //Runtime.getRuntime().exec("cmd /c start d:\\start.bat");
        Thread.sleep(TIMESLEEP);
        log.info("고진톰켓 시작 완료! ");
    }

    /**
     *  선인소스 복사
     */
    private void releaseSunin() throws IOException {
        copyFolder(new File(sourcePath), new File(suninPath));
        log.info("선인톰켓 복사 완료! ");
    }

    /**
     *  선진소스 복사
     */
    private void releaseSunjin() throws IOException {
        copyFolder(new File(sourcePath), new File(sunjinPath));
        log.info("선톰진켓 복사 완료! ");
    }

    /**
     *  소파스일복사
     */
    private void copyFolder(File src, File dest) throws IOException {
        // 파일 필터 객체
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String name = pathname.getName();
                List aList = Arrays.asList(ignoreFiles.split(","));
                //log.info("filename : {}, {}", name, !aList.contains(name));
                return !aList.contains(name);
            }
        };
        // 파일 카피
        FileUtils.copyDirectory(src, dest, fileFilter);
    }
}
