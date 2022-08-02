package de.awesomecloud.driver.console.logger;

public class LoggerQueueInput {

    private final String prefix;
    private final String message;

    public LoggerQueueInput(String prefix, String message) {
        this.prefix = prefix;
        this.message = message;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getMessage() {
        return message;
    }
}
