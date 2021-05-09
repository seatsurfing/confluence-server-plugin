package de.seatsurfing.confluence;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;

public class ConfigReader {
    public static Config readConfig(final TransactionTemplate transactionTemplate, final PluginSettingsFactory pluginSettingsFactory) {
        return (Config)transactionTemplate.execute(new TransactionCallback() {
            public Object doInTransaction() {
                PluginSettings settings = pluginSettingsFactory.createGlobalSettings();
                Config config = new Config();
                config.setOrgId((String) settings.get(Config.class.getName() + ".orgId"));
                config.setBookingUiUrl((String) settings.get(Config.class.getName() + ".bookingUiUrl"));
                config.setSharedSecret((String) settings.get(Config.class.getName() + ".sharedSecret"));
                return config;
            }
        });
    }
}
