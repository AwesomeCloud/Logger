package de.awesomecloud.driver.console.data;

import de.awesomecloud.driver.Driver;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConsoleCompleter implements Completer {

    private boolean exists;

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        final var input = parsedLine.line();
        List<String> suggestions = null;

        if (input.isEmpty()) {
            final var result = new LinkedList<String>();
            Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                result.add(command.getCommand());
                result.addAll(command.getAliases());
            });
            suggestions = result;
            suggestions.stream().map(Candidate::new).forEach(list::add);

        } else if (!canBeFind(input)) {
            final var result = new LinkedList<String>();
            Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                result.add(command.getCommand());
                result.addAll(command.getAliases());
            });

            suggestions = result;
            suggestions.stream().map(Candidate::new).forEach(list::add);

        } else {
            var arguments = input.split(" ");
            final var consoleInput = Driver.getInstance().getConsoleDriver().getInputs().peek();
            if (input.indexOf(' ') == -1) {
                if (consoleInput == null) {
                    final var result = new LinkedList<String>();
                    final var toTest = arguments[arguments.length - 1];

                    Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                        if (command.getCommand() != null && (toTest.trim().isEmpty() || command.getCommand().toLowerCase().contains(toTest.toLowerCase()))) {
                            result.add(command.getCommand());
                        }

                        command.getAliases().forEach(s -> {
                            if (s.toLowerCase().contains(toTest.toLowerCase())) {
                                result.add(s);
                            }
                        });
                    });

                    if (result.isEmpty() && !Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().isEmpty()) {
                        Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
                            result.add(command.getCommand());
                            result.addAll(command.getAliases());
                        });
                    }

                    suggestions = result;
                } else {
                    suggestions = consoleInput.tabCompletes();
                }
            } else {
                if (consoleInput != null) return;
                final var command = Driver.getInstance().getConsoleDriver().getCommandDriver().getCommand(arguments[0]);
                final var result = new LinkedList<String>();

                if (command == null) {
                    Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command1 -> {
                        result.add(command1.getCommand());
                        result.addAll(command1.getAliases());
                    });

                } else {
                    if (command.tabComplete(null, arguments) != null) {

                        result.addAll(command.tabComplete(null, Driver.getInstance().getStorageDriver().dropFirstString(arguments)));
                        suggestions = result;
                    }
                }
                if (suggestions == null || suggestions.isEmpty()) return;
            }
            suggestions.stream().map(Candidate::new).forEach(list::add);
        }
    }

    private boolean canBeFind(String line) {
        ArrayList<String> commandsAndAliases = new ArrayList<>();
        exists = false;

        if (line.contains(" ")) {
            return true;
        }

        Driver.getInstance().getConsoleDriver().getCommandDriver().getCommands().forEach(command -> {
            commandsAndAliases.add(command.getCommand());

            commandsAndAliases.addAll(command.getAliases());
        });

        commandsAndAliases.forEach(command -> {
            if (command.startsWith(line)) {
                exists = true;
            }
        });
        return exists;
    }
}
