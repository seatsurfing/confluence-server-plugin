package de.seatsurfing.confluence;

import java.util.Map;
import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.confluence.macro.MacroExecutionException;
import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
import com.atlassian.confluence.user.ConfluenceUser;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import javax.inject.Inject;
import javax.inject.Named;

import com.atlassian.sal.api.pluginsettings.PluginSettings;
import com.atlassian.sal.api.pluginsettings.PluginSettingsFactory;
import com.atlassian.sal.api.transaction.TransactionCallback;
import com.atlassian.sal.api.transaction.TransactionTemplate;
import com.atlassian.sal.api.user.UserManager;

import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.sal.api.component.ComponentLocator;

@Named
public class SeatsurfingMacro implements Macro {
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;
    @ComponentImport
    private final TransactionTemplate transactionTemplate;
    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public SeatsurfingMacro(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
                          TransactionTemplate transactionTemplate, ApplicationProperties applicationProperties) {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
        this.applicationProperties = applicationProperties;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Config config = ConfigReader.readConfig(transactionTemplate, pluginSettingsFactory);
        String url = config.getBookingUiUrl();
        if ((url == null) || (url.trim().isEmpty())) {
            url = "https://app.seatsurfing.app/";
        }
        if (!url.endsWith("/")) {
            url += "/";
        }

        if ((config.getSharedSecret() == null) ||
            (config.getOrgId() == null) ||
            (config.getSharedSecret().trim().isEmpty()) ||
            (config.getOrgId().trim().isEmpty())) {
            return "<b>Error:</b> Shared Secret and/or Instance ID are not set. Please go to the Seatsurfing Configuration page in your Confluence Settings and set it.";
        }

        ConfluenceUser user = AuthenticatedUserThreadLocal.get();
        if (user == null) {
            url += "login/confluence/anonymous";
        } else {
            String userName = user.getEmail();
            if ((userName == null) || (userName.trim().equals(""))) {
                userName = user.getName();
            }
            String jwt = JWT
                .create()
                .withClaim("user", userName)
                .withClaim("key", user.getKey().getStringValue())
                .sign(Algorithm.HMAC256(config.getSharedSecret()));
                url += "confluence/" + config.getOrgId() + "/" + jwt;
        }

        String res = "";
        res += "<iframe width=\"100%\" height=\"500px\" src=\""+url+"\" frameborder=\"0\">";
        res += "</iframe>";
        return res;
    }

    public BodyType getBodyType() { return BodyType.NONE; }

    public OutputType getOutputType() { return OutputType.BLOCK; }
}
