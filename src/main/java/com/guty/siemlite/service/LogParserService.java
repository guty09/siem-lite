package com.guty.siemlite.service;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogParserService {

    public SecurityEvent parseLog(String logLine) {

        Pattern pattern =
                Pattern.compile("Failed password for (\\w+) from ([0-9.]+)");

        Matcher matcher = pattern.matcher(logLine);

        if (matcher.find()) {

            String username = matcher.group(1);
            String ip = matcher.group(2);

            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    username,
                    "FAILED_LOGIN",
                    logLine
            );
        }

        return null;
    }
}
