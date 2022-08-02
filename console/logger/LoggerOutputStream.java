package de.awesomecloud.driver.console.logger;

import de.awesomecloud.driver.console.logger.enums.MSGType;

import java.io.ByteArrayOutputStream;

public class LoggerOutputStream extends ByteArrayOutputStream {

    private final Logger logger;
    private final MSGType msgType;

    public LoggerOutputStream(Logger logger, MSGType msgType) {
        this.logger = logger;
        this.msgType = msgType;
    }

    @Override
    public void flush() {
        final var input = this.toString();
        this.reset();
        if (input != null && !input.isEmpty()) {
            String[] inputs = input.split("\n");
            for (String string : inputs)
                this.logger.log(this.msgType, string);
        }
    }
}
