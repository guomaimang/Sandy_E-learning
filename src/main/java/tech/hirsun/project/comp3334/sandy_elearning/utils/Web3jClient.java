package tech.hirsun.project.comp3334.sandy_elearning.utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Web3jClient {
    private static final Logger logger = LoggerFactory.getLogger(Web3jClient.class);

    private static String ip = "https://polygon-rpc.com/";

    private volatile static Web3j web3j;

    public static Web3j getClient() {
        if (web3j == null) {
            synchronized (Web3jClient.class) {
                if (web3j == null) {
                    logger.info("Connecting to Web3j with URL: {}", ip);
                    web3j = Web3j.build(new HttpService(ip));
                }
            }
        }
        return web3j;
    }

}

