package com.TPI.Receptionist.Receptionist.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class ClusterOpResultReader {

    Logger logger = LoggerFactory.getLogger(ClusterOpResultReader.class);

    public static String getProcessResult(Process process){
        return readInputStream(process.getInputStream());
    }

    public static String getProcessError(Process process) {
        return readInputStream(process.getErrorStream());
    }

    private static String readInputStream(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String result = "";
        while (true) {
            String line = "";
            try {
                line = reader.readLine();
                if (line == null)
                    break;
                else
                    result = result + line + "\r\n";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
