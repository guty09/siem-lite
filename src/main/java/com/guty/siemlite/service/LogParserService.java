package com.guty.siemlite.service;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Responsible for converting raw log lines
 * into structured SecurityEvent objects.
 *
 * Supported event types:
 *
 * FAILED_LOGIN
 * SUCCESSFUL_LOGIN
 * CONNECTION_ATTEMPT
 */
@Service
public class LogParserService {

    /*
     * Parses a raw log line and attempts
     * to create a SecurityEvent object.
     *
     * Returns:
     *
     * SecurityEvent if recognized
     * null if unsupported
     */
    public SecurityEvent parseLog(String logLine) {

        /*
         * Failed SSH login pattern.
         *
         * Example:
         * Failed password for root from 192.168.1.60 port 50000 ssh2
         */
        Pattern failedPattern =
                Pattern.compile("Failed password for (\\w+) from ([0-9.]+)");

        Matcher failedMatcher = failedPattern.matcher(logLine);

        if (failedMatcher.find()) {

            String username = failedMatcher.group(1);
            String ip = failedMatcher.group(2);

            /*
             * Authentication events do not use destinationPort,
             * so we pass null.
             */
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
         * Successful SSH login pattern.
         *
         * Example:
         * Accepted password for root from 192.168.1.60 port 50000 ssh2
         */
        Pattern successPattern =
                Pattern.compile("Accepted password for (\\w+) from ([0-9.]+)");

        Matcher successMatcher = successPattern.matcher(logLine);

        if (successMatcher.find()) {

            String username = successMatcher.group(1);
            String ip = successMatcher.group(2);

            /*
             * Authentication events do not use destinationPort,
             * so we pass null.
             */
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
         * Network connection attempt pattern.
         *
         * Example:
         * Connection attempt from 192.168.1.100 to port 22
         *
         * This will be used later for PORT_SCAN detection.
         */
        Pattern connectionPattern =
                Pattern.compile("Connection attempt from ([0-9.]+) to port (\\d+)");

        Matcher connectionMatcher = connectionPattern.matcher(logLine);

        if (connectionMatcher.find()) {

            String ip = connectionMatcher.group(1);
            Integer destinationPort = Integer.parseInt(connectionMatcher.group(2));

            /*
             * Network events do not involve a username,
             * so username is null.
             */
            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    null,
                    "CONNECTION_ATTEMPT",
                    logLine,
                    destinationPort
            );
        }

        /*
         * Unknown log format.
         */
        return null;
    }
}

