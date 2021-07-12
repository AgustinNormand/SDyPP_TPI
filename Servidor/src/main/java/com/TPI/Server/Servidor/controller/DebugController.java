package com.TPI.Server.Servidor.controller;

import com.TPI.Server.Servidor.service.debug.DebugService;
import com.TPI.Server.Servidor.service.debug.NodeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/debug", produces = { "application/json" })
public class DebugController {

    @Autowired
    DebugService debugService;

    @GetMapping("/nodes")
    public ResponseEntity<List<NodeInfo>> getNodes() {
        List<NodeInfo> nodesInfo = debugService.getNodesInfo();

        return new ResponseEntity<>(nodesInfo, HttpStatus.OK);
    }

}
