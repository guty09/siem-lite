package com.guty.siemlite.service;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogParserService {

    public SecurityEvent parseLog(String logLine) {

        Pattern failedPattern =
                Pattern.compile("Failed password for (\\w+) from ([0-9.]+)");

        Matcher failedMatcher = failedPattern.matcher(logLine);

        if (failedMatcher.find()) {

            String username = failedMatcher.group(1);
            String ip = failedMatcher.group(2);

            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    username,
                    "FAILED_LOGIN",
                    logLine
            );
        }

        Pattern successPattern =
                Pattern.compile("Accepted password for (\\w+) from ([0-9.]+)");

        Matcher successMatcher = successPattern.matcher(logLine);

        if (successMatcher.find()) {

            String username = successMatcher.group(1);
            String ip = successMatcher.group(2);

            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    username,
                    "SUCCESSFUL_LOGIN",
                    logLine
            );
        }

        return null;
    }
}

