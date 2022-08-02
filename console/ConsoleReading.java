package de.awesomecloud.driver.console;

import de.awesomecloud.driver.Driver;
import de.awesomecloud.driver.console.logger.Logger;
import de.awesomecloud.driver.console.setup.CloudMainSetup;
import org.jline.reader.LineReader;

public final class ConsoleReading extends Thread {

    private final String consolePrompt;
    private final ConsoleDriver consoleDriver;
    private final LineReader lineReader;

    public ConsoleReading(Logger logger, ConsoleDriver consoleDriver) {
        this.consolePrompt = logger.getColoredString("§bAwesomeCloud§f@" + Driver.getInstance().getStorageDriver().getVersion() + " §7» §7");
        this.consoleDriver = consoleDriver;
        this.lineReader = this.consoleDriver.getLineReader();

        this.setPriority(Thread.MAX_PRIORITY);
        this.setName("AwesomeCloud_CONSOLE");
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            final var line = this.lineReader.readLine(consolePrompt);
            if (line != null && !line.isEmpty()) {
                final var input = this.consoleDriver.getInputs().poll();

                if (Driver.getInstance().getStorageDriver().isCloudSetup()) {
                    if (Driver.getInstance().getStorageDriver().getSetupType().equalsIgnoreCase("MAIN_SETUP")) {
                        new CloudMainSetup(line);
                    }
                } else if (input != null) {
                    input.inputs().accept(line);
                } else {
                    this.consoleDriver.getCommandDriver().executeCommand(line);
                }
            }
        }
    }
}
