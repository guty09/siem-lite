package com.guty.siemlite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Simple test controller.
 *
 * Used to verify that the application is running
 * and responding to HTTP requests.
 *
 * Endpoint:
 *
 * GET /hello
 */
@RestController
public class HelloController {

    /*
     * GET /hello
     *
     * Returns a simple message confirming that
     * the SIEM-Lite application is running.
     *
     * Example response:
     *
     * SIEM-Lite is running!
     */
    @GetMapping("/hello")
    public String hello() {

        /*
         * Return health-check message.
         */
        return "SIEM-Lite is running!";
    }

}
