package com.TPI.Receptionist.Receptionist.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ClusterOpResultMapper {

    public static String getProcessResult(Process process){
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = "";
        if(process != null)
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
