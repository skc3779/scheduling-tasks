package hello;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InterruptedIOException;
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

    @Value("${gojin.target.path}")
    String gojinTargetPath;

    @Value("${sunin.target.path}")
    String suninTargetPath;

    @Value("${sunjin.target.path}")
    String sunjinTargetPath;

    @Value("${ignore.files}")
    String ignoreFiles;

    @Value("${gojin.tomcat.path}")
    String gojinTomcatPath;

    @Value("${sunin.tomcat.path}")
    String suninTomcatPath;

    @Value("${sunjin.tomcat.path}")
    String sunjinTomcatPath;

    //@Scheduled(fixedRate = 5000)
    //@Scheduled(cron = "0/50 * * * * ?")
    //@Scheduled(cron = "0 30 0 * * ?")
    @Scheduled(cron = "${scheduled}")
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


    protected void releaseTest() throws IOException, InterruptedIOException {
        log.info("테스트 스케쥴 실행! ");
    }

    /**
     *  고진소스 복사
     */
    protected void releaseGojin() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("cmd /c start "+gojinTomcatPath+"shutdown.bat");
        Thread.sleep(TIMESLEEP);
        log.info("고진톰켓 정지 완료! ");
        copyFolder(new File(sourcePath), new File(gojinTargetPath));
        log.info("고진톰켓 소스복사 완료! ");
        Runtime.getRuntime().exec("cmd /c start "+gojinTomcatPath+"startup.bat");
        Thread.sleep(TIMESLEEP);
        log.info("고진톰켓 시작 완료! ");

    }

    /**
     *  선인소스 복사
     */
    protected void releaseSunin() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("cmd /c start "+suninTomcatPath+"shutdown.bat");
        Thread.sleep(TIMESLEEP);
        log.info("선인톰켓 정지 완료! ");
        copyFolder(new File(sourcePath), new File(suninTargetPath));
        log.info("선인톰켓 복사 완료! ");
        Runtime.getRuntime().exec("cmd /c start "+suninTomcatPath+"startup.bat");
        Thread.sleep(TIMESLEEP);
        log.info("선인톰켓 시작 완료! ");
    }

    /**
     *  선진소스 복사
     */
    protected void releaseSunjin() throws IOException, InterruptedException {
        Runtime.getRuntime().exec("cmd /c start "+sunjinTomcatPath+"shutdown.bat");
        Thread.sleep(TIMESLEEP);
        log.info("선진톰켓 정지 완료! ");
        copyFolder(new File(sourcePath), new File(sunjinTargetPath));
        log.info("선진톰켓 복사 완료! ");
        Runtime.getRuntime().exec("cmd /c start "+sunjinTomcatPath+"startup.bat");
        Thread.sleep(TIMESLEEP);
        log.info("선진톰켓 시작 완료! ");
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
