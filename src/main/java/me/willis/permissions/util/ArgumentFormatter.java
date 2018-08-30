package me.willis.permissions.util;

import org.apache.commons.lang.text.StrSubstitutor;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

/**
 * JustRayz :)
 * This class was created on 11/07/2018
 */

/**
 * @NOTE ->
 *
 * This class was not created by my self, as stated above the creator is "JustRayz"
 * You can find more of his work on his Github Page ->
 * https://github.com/JustRayz
 */
public class ArgumentFormatter {

    private final Object[] indexedArguments;
    private String base;
    private Map<String, Object> keyArguments;

    /**
     * @param base         The string to format
     * @param keyArguments A Map where the key is the name e.g. ${player} would be player
     */
    public ArgumentFormatter(String base, Map<String, Object> keyArguments) {
        this.base = base;
        this.keyArguments = keyArguments;
        this.indexedArguments = null;
    }

    /**
     * @param base             The string to format
     * @param indexedArguments A list of items to replace, in order, where ${0}, ${1} = 0 & 1
     */
    public ArgumentFormatter(String base, Object... indexedArguments) {
        this.base = base;
        this.indexedArguments = indexedArguments;
        this.keyArguments = null;
    }

    /**
     * Formats the string depending on whether you used indexed arguments or keyed arguments.
     *
     * @return Formatted string, with placeholders entered.
     */

    public String format() {
        if (this.keyArguments == null) {
            Map<String, Object> arguments = new HashMap<>();

            for (int i = 0; i < this.getIndexedArguments().length; i++) {
                Object argument = this.getIndexedArguments()[i];
                arguments.put(Integer.toString(i), argument);
            }

            this.keyArguments = arguments;
        }

        StrSubstitutor sub = new StrSubstitutor(this.getKeyArguments());
        return ChatColor.translateAlternateColorCodes('&', sub.replace(this.getBase()));
    }

    public Object[] getIndexedArguments() {
        return indexedArguments;
    }

    public Map<String, Object> getKeyArguments() {
        return keyArguments;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
