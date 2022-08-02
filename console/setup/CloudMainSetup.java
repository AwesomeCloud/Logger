package de.awesomecloud.driver.console.setup;

import de.awesomecloud.driver.Driver;
import de.awesomecloud.driver.configuration.ConfigDriver;
import de.awesomecloud.driver.configuration.configs.ServiceConfiguration;
import de.awesomecloud.driver.configuration.configs.nodes.NodeConfiguration;
import de.awesomecloud.driver.configuration.configs.nodes.NodeProperties;
import de.awesomecloud.driver.console.logger.enums.MSGType;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class CloudMainSetup {

    public CloudMainSetup(String line) {
        switch (Driver.getInstance().getStorageDriver().getSetupStep()) {
            case 0:
                if (line.equalsIgnoreCase("manager")) {
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("MODE", "MANAGER");
                    Driver.getInstance().getStorageDriver().setSetupStep(Driver.getInstance().getStorageDriver().getSetupStep() + 1);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Was ist die IP-Adresse des Managers?");
                }
                break;
            case 1:
                if (line.contains(".")) {
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("managerHostAddress", line);
                    Driver.getInstance().getStorageDriver().setSetupStep(Driver.getInstance().getStorageDriver().getSetupStep() + 1);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Was ist der interne Port? | Normal: §37862");
                } else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Bitte eine numerische Adresse eingeben!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Was ist die IP-Adresse des Managers?");
                }
                break;
            case 2:
                if (line.matches("[0-9]+")) {
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("networkingPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep(Driver.getInstance().getStorageDriver().getSetupStep() + 1);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Welcher Port ist für die RestAPI? | Normal: §38012");
                } else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Bitte eine numerischen Port!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Was ist der interne Port? | Normal: §37862");
                }
                break;
            case 3:
                if (line.matches("[0-9]+")) {
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("restApiPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep(Driver.getInstance().getStorageDriver().getSetupStep() + 1);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Von welchen Port soll der Proxy starten? | Normal: §325565");
                } else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Bitte eine numerischen Port!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Was ist der interne Port? | Normal: §37862");
                }
                break;
            case 4:
                if (line.matches("[0-9]+")) {
                    Driver.getInstance().getStorageDriver().getSetupStorage().put("defaultProxyStartupPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep(Driver.getInstance().getStorageDriver().getSetupStep() + 1);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Von welchem Port aus sollen die Server starten? | Normal: §34000");


                    System.out.println(1);
                } else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Bitte eine numerischen Port!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Von welchen Port soll der Proxy starten? | Normal: §325565");
                }
                break;
            case 5:
                System.out.println(2);

                System.out.println(line);

                if (line.matches("[0-9]+")) {
                    System.out.println(3);

                    Driver.getInstance().getStorageDriver().getSetupStorage().put("defaultServerStartupPort", Integer.valueOf(line));
                    Driver.getInstance().getStorageDriver().setSetupStep(Driver.getInstance().getStorageDriver().getSetupStep() + 1);

                    System.out.println(4);

                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Sie haben die Einrichtung erfolgreich abgeschlossen!");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Die Cloud wird in Kürze neu gestartet werden");

                    System.out.println(4);

                    ServiceConfiguration configuration = new ServiceConfiguration();
                    ServiceConfiguration.Communication communication = new ServiceConfiguration.Communication();

                    communication.setManagerHostAddress(Driver.getInstance().getStorageDriver().getSetupStorage().get("managerHostAddress").toString());
                    communication.setNetworkingPort(Integer.parseInt(Driver.getInstance().getStorageDriver().getSetupStorage().get("networkingPort").toString()));
                    communication.setRestApiPort(Integer.parseInt(Driver.getInstance().getStorageDriver().getSetupStorage().get("restApiPort").toString()));

                    ArrayList<String> addresses = new ArrayList<>();

                    String address = Driver.getInstance().getStorageDriver().getSetupStorage().get("managerHostAddress").toString();
                    if (!address.equalsIgnoreCase("127.0.0.1")) {
                        addresses.add(address);
                    }
                    addresses.add("127.0.0.1");

                    communication.setWhitelistAddresses(addresses);
                    String nodeAuthKey = UUID.randomUUID() + UUID.randomUUID().toString();
                    communication.setNodeAuthKey(nodeAuthKey);
                    communication.setRestApiAuthKey(nodeAuthKey);
                    configuration.setCommunication(communication);

                    ServiceConfiguration.General general = new ServiceConfiguration.General();
                    general.setAutoUpdate(Boolean.getBoolean(Driver.getInstance().getStorageDriver().getSetupStorage().get("autoUpdate").toString()));
                    general.setDefaultProxyStartupPort(Integer.parseInt(Driver.getInstance().getStorageDriver().getSetupStorage().get("defaultProxyStartupPort").toString()));
                    general.setDefaultServerStartupPort(Integer.parseInt(Driver.getInstance().getStorageDriver().getSetupStorage().get("defaultServerStartupPort").toString()));
                    general.setShowPlayerConnections(true);
                    general.setWhitelist(new ArrayList<>());
                    general.setProxyOnlineMode(true);
                    general.setServerSplitter("-");
                    configuration.setGeneral(general);
                    ServiceConfiguration.Messages messages = new ServiceConfiguration.Messages();
                    messages.setPrefix("§3AwesomeCloud §8| §7");

                    messages.setMaintenanceKickMessage("§8» §7The §bNetwork§7 is currently in §bmaintenance");
                    messages.setMaintenanceGroupMessage("§8» §7The §bService§7 is currently in §bmaintenance");
                    messages.setNoFallbackKickMessage("§8» §7No §bfallback§7 can be §bfound");
                    messages.setFullNetworkKickMessage("§8» §7The §bNetwork§7 is full, please buy §ePremium");
                    messages.setFullServiceKickMessage("§8» §7The §bService§7 is currently §bfull");
                    messages.setOnlyProxyJoinKickMessage("§8» §7Please join over §bplay.yourserver.io");
                    messages.setHubCommandNoFallbackFound("§3AwesomeCloud §8| §7No §bfallback§7 can be §bfound");
                    messages.setHubCommandAlreadyOnFallBack("§3AwesomeCloud §8| §7you are already on a §bFallback");
                    messages.setHubCommandSendToAnFallback("§3AwesomeCloud §8| §7ýou have ben send to an §bfallback-server");
                    messages.setServiceStartingNotification("§3AwesomeCloud §8| §7%SERVICE_NAME% is §estarting...");
                    messages.setServiceConnectedToProxyNotification("§3AwesomeCloud §8| §7%SERVICE_NAME% is §bconnected");
                    messages.setServiceStoppingNotification("§3AwesomeCloud §8| §7%SERVICE_NAME% is §cstopping...");

                    configuration.setMessages(messages);
                    new ConfigDriver("./service.json").save(configuration);

                    new File("./local/").mkdirs();
                    new File("./local/storage/jars/").mkdirs();
                    new File("./local/storage/cache/").mkdirs();
                    new File("./local/groups/").mkdirs();
                    new File("./local/global/plugins/").mkdirs();
                    new File("./local/templates/").mkdirs();

                    NodeConfiguration nodes = new NodeConfiguration();

                    NodeProperties properties = new NodeProperties();
                    properties.setNodeName("InternalNode");
                    properties.setNodeHost("127.0.0.1");

                    nodes.getNodes().add(properties);

                    new ConfigDriver("./local/nodes.json").save(nodes);

                    System.exit(0);
                } else {
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Von welchem Port aus sollen die Server starten? | Normal: §34000");
                    Driver.getInstance().getConsoleDriver().getLogger().log(MSGType.MESSAGETYPE_SETUP, "Bitte eine numerischen Port!");
                }
                break;
        }
    }
}
