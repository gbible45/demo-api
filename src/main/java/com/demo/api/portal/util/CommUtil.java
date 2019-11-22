package com.demo.api.portal.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CommUtil {
    private static String defaultEnterChar = "\n";
    public static void checkDirToMakeDirs(String dirPath) {
        try {
            if (!StringUtils.isEmpty(dirPath)) {
                File logDir = new File(dirPath);
                if (!logDir.exists()) {
                    logDir.mkdirs();
                }
            }
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
    }

    public static List<String> readAllLines(String filePath) throws IOException {
        File lineFile = new File(filePath);
        if (lineFile.exists()) {
            return Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } else {
            return null;
        }
    }

    public static String readAllString(String filePath) throws IOException {
        return readAllString(filePath, defaultEnterChar);
    }

    public static String readAllString(String filePath, String enterChar) throws IOException {
        List<String> lines = readAllLines(filePath);
        if (lines != null && lines.size() > 0) {
            return String.join(enterChar, lines);
        } else {
            return null;
        }
    }

    public static void writeFileLines(String filePath, String... lines) throws IOException {
        writeFileLines(filePath, Arrays.asList(lines));
    }

    public static void writeFileLines(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines, StandardCharsets.UTF_8);
    }

    public static void witeFile(String filePath, String... contents) throws IOException {
        witeFile(filePath, false, contents);
    }

    public static void witeFile(String filePath, boolean append, String... contents) throws IOException {
        BufferedWriter bw = null;
        try {
            File outFile = new File(filePath);
            bw = new BufferedWriter(new FileWriter(outFile, append));
            boolean newLine = false;
            for (String content : contents) {
                if (newLine) {
                    bw.newLine();
                }
                bw.write(content);
                bw.flush();
                newLine = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(bw != null) try {bw.close(); } catch (IOException e) {}
        }
   }

    public static String getEthAddressPrefixIpByCidr (String cidr, int lenth) {
        String[] ips = cidr.split("/")[0].split("\\.");
        String prefixIp = "";
        for (int i=0; i<lenth; i++) {
            if (!StringUtils.isEmpty(prefixIp)) prefixIp += ".";
            prefixIp += ips[i];
        }
        return prefixIp;
    }

    public static void sleep (long mills) {
       try {
           Thread.sleep(mills);
       } catch (InterruptedException e) {
       }
   }
}
