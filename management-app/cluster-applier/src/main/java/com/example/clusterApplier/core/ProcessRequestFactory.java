package com.example.clusterApplier.core;


import com.example.clusterApplier.core.exceptions.ProcessRequestCreationException;
import com.example.clusterApplier.core.exceptions.UnknownScriptFilename;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProcessRequestFactory {

    private static final int EXPECTED_AMT = 3;



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

    public static ProcessRequest createProcessRequest(String jobId, byte[] splitter, byte[] worker, byte[] joiner, byte[] autoscaler) {
        ProcessRequest request = new ProcessRequest();
        List<Script> pending = new ArrayList<>(Arrays.asList(new Script(splitter), new Script(worker), new Script(joiner), new Script(autoscaler)));
        request.setPending(pending);
        request.setJobId(jobId);
        return request;
    }
}