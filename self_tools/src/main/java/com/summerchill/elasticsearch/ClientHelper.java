package com.summerchill.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.mapper.Mapping;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tgg on 16-3-17.
 */
public class ClientHelper {
    private static String ip;
    private static int port;

    private Settings setting;
    private Mapping mapping;

    private Map<String, Client> clientMap = new ConcurrentHashMap<String, Client>();

    private Map<String, Integer> ips = new HashMap<String, Integer>(); // hostname port

    private String clusterName = "elasticsearch";

    private ClientHelper(String ip, Integer port) {
        init(ip, port);
        //TO-DO 添加你需要的client到helper
    }

    public static final ClientHelper getInstance(String ipConf, Integer portConf) {
        ip = ipConf;
        port = portConf;
        return ClientHolder.INSTANCE;
    }

    /**
     * 初始化默认的client
     */
    public void init(String ip, int port) {

        ips.put(ip, port);
        setting = Settings.builder()
                .put("client.transport.sniff", true)
                .put("cluster.name", clusterName).build();
        addClient(setting, getAllAddress(ips));
    }

    /**
     * 获得所有的地址端口
     *
     * @return
     */
    public List<InetSocketTransportAddress> getAllAddress(Map<String, Integer> ips) {
        List<InetSocketTransportAddress> addressList = new ArrayList<InetSocketTransportAddress>();
        for (String ip : ips.keySet()) {
            addressList.add(new InetSocketTransportAddress(new InetSocketAddress(ip, ips.get(ip))));
        }
        return addressList;
    }

    public Client getClient() {
        return getClient(clusterName);
    }

    public Client getClient(String clusterName) {
        return clientMap.get(clusterName);//通过集群名称得到一个Client
    }

    public void addClient(Settings setting, List<InetSocketTransportAddress> transportAddress) {
        Client client = new PreBuiltTransportClient(setting)
                .addTransportAddresses(transportAddress.toArray(new InetSocketTransportAddress[transportAddress.size()]));

        clientMap.put(setting.get("cluster.name"), client);
    }

    private static class ClientHolder {
        private static final ClientHelper INSTANCE = new ClientHelper(ip, port);
    }

}