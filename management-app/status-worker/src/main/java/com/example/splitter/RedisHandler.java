package com.example.splitter;

import com.example.splitter.dto.Task;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class RedisHandler {

    private RedisCommands<String, String> syncCommands;
    private StatefulRedisConnection<String, String> connection;
    private RedisClient redisClient;

    @Autowired
    public RedisHandler(Environment env) {
        RedisURI redisUri = RedisURI.Builder.redis(env.getProperty("redis.hostname"))
                .withPassword(env.getProperty("redis.password"))
                .withPort(Integer.parseInt(env.getProperty("redis.port")))
                .withDatabase(Integer.parseInt(env.getProperty("redis.database")))
                .build();
        this.redisClient = RedisClient.create(redisUri);
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }

    public void close(){
        connection.close();
        redisClient.shutdown();
    }

    public void save(String uuid, String subKey, Integer totalTasks){
        syncCommands.hset(uuid, subKey, totalTasks.toString());
    }
}
