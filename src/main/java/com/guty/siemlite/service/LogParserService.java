package com.guty.siemlite.service;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Responsible for converting raw log lines
 * into structured SecurityEvent objects.
 */
@Service
public class LogParserService {

    public SecurityEvent parseLog(String logLine) {

        /*
         * Failed SSH login.
         */
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
                    logLine,
                    null
            );
        }

        /*
         * Successful SSH login.
         */
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
                    logLine,
                    null
            );
        }

        /*
         * Admin command execution.
         *
         * Supported examples:
         *
         * Admin command executed by root from 192.168.1.100
         * sudo command executed by root from 192.168.1.100
         * Sudo command executed by root from 192.168.1.100
         */
        Pattern adminCommandPattern =
                Pattern.compile("(?i)(admin|sudo) command executed by (\\w+) from ([0-9.]+)");

        Matcher adminCommandMatcher = adminCommandPattern.matcher(logLine);

        if (adminCommandMatcher.find()) {

            String username = adminCommandMatcher.group(2);
            String ip = adminCommandMatcher.group(3);

            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    username,
                    "ADMIN_COMMAND_EXECUTED",
                    logLine,
                    null
            );
        }

        /*
         * Network connection attempt.
         */
        Pattern connectionPattern =
                Pattern.compile("Connection attempt from ([0-9.]+) to port (\\d+)");

        Matcher connectionMatcher = connectionPattern.matcher(logLine);

        if (connectionMatcher.find()) {

            String ip = connectionMatcher.group(1);
            Integer destinationPort =
                    Integer.parseInt(connectionMatcher.group(2));

            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    null,
                    "CONNECTION_ATTEMPT",
                    logLine,
                    destinationPort
            );
        }

        return null;
    }
}

