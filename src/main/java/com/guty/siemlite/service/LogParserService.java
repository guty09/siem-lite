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
 * Input:
 *
 * Failed password for root from 192.168.1.60
 *
 * Output:
 *
 * timestamp
 * sourceIp
 * username
 * eventType
 * rawLog
 *
 * This process is called parsing.
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
     *
     * null if unsupported
     */
    public SecurityEvent parseLog(String logLine) {

        /*
         * Regex pattern used to recognize
         * failed SSH login attempts.
         *
         * Example:
         *
         * Failed password for root from 192.168.1.60
         */
        Pattern failedPattern =
                Pattern.compile("Failed password for (\\w+) from ([0-9.]+)");

        /*
         * Matcher compares the log line
         * against the regex pattern.
         */
        Matcher failedMatcher = failedPattern.matcher(logLine);

        /*
         * If a match is found,
         * extract username and IP address.
         */
        if (failedMatcher.find()) {

            /*
             * Group(1) = username
             *
             * Example:
             * root
             */
            String username = failedMatcher.group(1);

            /*
             * Group(2) = source IP
             *
             * Example:
             * 192.168.1.60
             */
            String ip = failedMatcher.group(2);

            /*
             * Create a FAILED_LOGIN event.
             */
            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    username,
                    "FAILED_LOGIN",
                    logLine
            );
        }

        /*
         * Regex pattern used to recognize
         * successful SSH logins.
         *
         * Example:
         *
         * Accepted password for root from 192.168.1.60
         */
        Pattern successPattern =
                Pattern.compile("Accepted password for (\\w+) from ([0-9.]+)");

        /*
         * Match against successful login pattern.
         */
        Matcher successMatcher = successPattern.matcher(logLine);

        /*
         * If a successful login is found,
         * create a SUCCESSFUL_LOGIN event.
         */
        if (successMatcher.find()) {

            /*
             * Extract username.
             */
            String username = successMatcher.group(1);

            /*
             * Extract source IP.
             */
            String ip = successMatcher.group(2);

            /*
             * Create a SUCCESSFUL_LOGIN event.
             */
            return new SecurityEvent(
                    LocalDateTime.now(),
                    ip,
                    username,
                    "SUCCESSFUL_LOGIN",
                    logLine
            );
        }

        /*
         * Unknown log format.
         *
         * Returning null tells the caller
         * that parsing failed.
         */
        return null;
    }
}

