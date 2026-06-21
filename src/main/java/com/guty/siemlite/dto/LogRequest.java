package com.guty.siemlite.dto;

/*
 * Data Transfer Object (DTO)
 *
 * Represents the JSON request body received
 * by POST /api/logs.
 *
 * Example request:
 *
 * {
 *     "logLine":
 *     "Failed password for root from 192.168.1.60 port 50000 ssh2"
 * }
 *
 * Spring automatically converts the JSON into
 * a LogRequest object.
 */
public class LogRequest {

    /*
     * Stores the raw log line sent by the client.
     */
    private String logLine;

    /*
     * Default constructor required by Spring
     * when converting JSON into Java objects.
     */
    public LogRequest() {
    }

    /*
     * Returns the raw log line.
     */
    public String getLogLine() {
        return logLine;
    }

    /*
     * Sets the raw log line.
     *
     * Spring calls this automatically when
     * deserializing JSON requests.
     */
    public void setLogLine(String logLine) {
        this.logLine = logLine;
    }
}
