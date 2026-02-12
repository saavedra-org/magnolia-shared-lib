package com.saavedra.jenkins;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ShellScriptLoader {

    private static final String SCRIPTS_DIR = "resources/scripts";

    public static List<String> listAvailableScripts() throws URISyntaxException {
        File scriptsDir = new File(ShellScriptLoader.class.getClassLoader().getResource(SCRIPTS_DIR).toURI());
        return Arrays.asList(scriptsDir.list());
    }

    public static boolean scriptExists(String scriptName) throws URISyntaxException {
        List<String> availableScripts = listAvailableScripts();
        return availableScripts.contains(scriptName);
    }

    public static String loadScript(String scriptName) throws IOException, URISyntaxException {
        if (!scriptExists(scriptName)) {
            throw new IllegalArgumentException("Script not found: " + scriptName);
        }
        return new String(Files.readAllBytes(Paths.get(SCRIPTS_DIR, scriptName)));
    }
}