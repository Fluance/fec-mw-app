package net.fluance.cockpit.core.model.jpa.refdata;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.springframework.util.StringUtils;

public enum LanguageMapper {


    DE("de", "de-DE"),
    FR("fr", "fr-FR"),
    EN("en", "en-US"),
    RU("ru"),
    AR("ar"),
    IT("it");

    private final String languageDatabase;
    // If different then languageDatabase IgnoreCase, you can add arrays of String to match
    private final String[] languageStrings;

    LanguageMapper(@NotNull String languageDatabase, @NotNull String... languageStrings) {
        this.languageDatabase = languageDatabase;
        this.languageStrings = languageStrings;
    }

    public String getLanguageDatabase() {
        return languageDatabase;
    }

    public String[] getLanguageStrings() {
        return languageStrings;
    }

    public static LanguageMapper getLanguageMapper(String language) {
        if (StringUtils.hasText(language)) {
            for (LanguageMapper lm : values()) {
                if (lm.name().equalsIgnoreCase(language)) {
                    return lm;
                }

                if (Arrays.asList(lm.getLanguageStrings()).contains(language)) {
                    return lm;
                }
            }
        }
        return null;
    }
}
