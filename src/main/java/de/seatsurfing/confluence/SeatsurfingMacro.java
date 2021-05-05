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

@Named
public class SeatsurfingMacro implements Macro {
    @ComponentImport
    private final UserManager userManager;
    @ComponentImport
    private final PluginSettingsFactory pluginSettingsFactory;
    @ComponentImport
    private final TransactionTemplate transactionTemplate;

    @Inject
    public SeatsurfingMacro(UserManager userManager, PluginSettingsFactory pluginSettingsFactory,
                          TransactionTemplate transactionTemplate) {
        this.userManager = userManager;
        this.pluginSettingsFactory = pluginSettingsFactory;
        this.transactionTemplate = transactionTemplate;
    }

    public String execute(Map<String, String> map, String s, ConversionContext conversionContext) throws MacroExecutionException {
        Config config = ConfigReader.readConfig(transactionTemplate, pluginSettingsFactory);
        String url = config.getBookingUiUrl();
        if (url == null) {
            url = "https://app.seatsurfing.de/ui/";
        }
        if (!url.endsWith("/")) {
            url += "/";
        }

        ConfluenceUser user = AuthenticatedUserThreadLocal.get();
        if (user == null) {
            url += "login/confluence/anonymous";
        } else {
            String jwt = JWT
                .create()
                .withClaim("user", user.getName())
                .withClaim("key", user.getKey().getStringValue())
                .sign(Algorithm.HMAC256(config.getSharedSecret()));
                url += "login/confluence/server/" + jwt;
        }

        String res = "";
        res += "<iframe width=\"100%\" height=\"500px\" src=\""+url+"\" frameborder=\"0\">";
        res += "</iframe>";
        return res;
    }

    public BodyType getBodyType() { return BodyType.NONE; }

    public OutputType getOutputType() { return OutputType.BLOCK; }
}
