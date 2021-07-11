package com.TrabajoPracticoIntegrador.Receptionist.Receptionist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KubectlController {
    public String applyCommand(String command){
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResults(process);
    }

    public String getResults(Process process){
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
