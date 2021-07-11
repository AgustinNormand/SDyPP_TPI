package com.TPI.Server.Servidor.core;

import com.TPI.Server.Servidor.exceptions.ProcessRequestCreationException;
import com.TPI.Server.Servidor.exceptions.UnknownScriptFilename;

import java.util.List;
import java.util.Locale;

public class ProcessRequestFactory {

    private static final int EXPECTED_AMT = 3;

    /**
     * Creates a {@link ProcessRequest} from the given {@link Script}s
     * @param scripts
     * @return An instance of {@link ProcessRequest} with the corresponding resources within it
     */
    public static ProcessRequest createProcessRequest(List<Script> scripts) {
        preValidate(scripts);
        ProcessRequest request = new ProcessRequest();
        scripts.forEach(file -> {
            String resource = getResource(file);
            switch (resource) {
                case "SPLITTER" -> request.setSplitter(file);
                case "JOINER" -> request.setJoiner(file);
                case "WORKER" -> request.setWorker(file);
                default -> throw new UnknownScriptFilename(file.getFilename());
            }
        });

        return request;
    }

    /**
     * Tries to split the filename to remove the resource's name based on the "." character.
     * e.g. if the filename is "splitter.yaml" it'll return "splitter", otherwise the same existing filename will be the output.
     * @param file
     * @return the given filename up to the first "." or the same filename if there's no dot at all.
     */
    private static String getResource(Script file) {
        String fullName = file.getFilename().toUpperCase(Locale.ROOT);
        String resource = fullName.split("\\.")[0];
        return resource != null ? resource : fullName;
    }

    private static void preValidate(List<Script> files) {
        if (files.size() != EXPECTED_AMT){
            throw new ProcessRequestCreationException();
        }
    }
}