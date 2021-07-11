package com.TPI.Server.Servidor.service.debug;

import com.TPI.Server.Servidor.core.SimpleCommandProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebugService {

    @Autowired
    SimpleCommandProcessor commandProcessor;

    public List<NodeInfo> getNodesInfo() {
        return commandProcessor.executeAndMapResponse("get nodes", result -> Arrays
                .stream(result.split("\r\n"))
                .map(NodeInfo::build)
                .collect(Collectors.toList()));
    }
}