package com.example.statusworker.service;

import com.example.commons.dto.JobStatus;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RedisService {

    private RedisAdvancedClusterCommands<String, String> syncCommands;
    private StatefulRedisClusterConnection<String, String> connection;
    private RedisClusterClient redisClient;

    @Autowired
    public RedisService(Environment env) {
        RedisURI redisUri = RedisURI.Builder.redis(env.getProperty("redis.hostname"))
                .withPassword(env.getProperty("redis.password"))
                .withPort(Integer.parseInt(env.getProperty("redis.port")))
                .withDatabase(Integer.parseInt(env.getProperty("redis.database")))
                .build();
        this.redisClient = RedisClusterClient.create(redisUri);
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }

    public void close(){
        connection.close();
        redisClient.shutdown();
    }

    public void save(String uuid, JobStatus.State step, String body){
        syncCommands.hset(uuid, String.valueOf(step), body);
    }

    public String getStep(String jobId, JobStatus.State state) {
        return syncCommands.hget(jobId, String.valueOf(state));
    }
}
