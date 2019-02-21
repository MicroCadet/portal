package com.myproject;

import com.myproject.entity.CuratorClient;
import com.myproject.tools.CuratorTools;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * 服务注册
 * @Author LettleCadet
 * @Date 2019/2/21
 */
@SpringBootApplication
public class RegisterApplication
{
    private static final Logger logger = LoggerFactory.getLogger(RegisterApplication.class);

    /**
     *
     * 通过Curator完成服务注册成功后，zk会立刻同步节点信息到注册的主机上，但是如果直接在linux上，zk不会同步节点信息
     * @param args
     */
    public static void main(String[] args)
    {
        String nodePath = "/prometheus";
        String serviceName = "/prometheus_ftp_20190221_7";

        SpringApplication.run(RegisterApplication.class, args);

        CuratorClient client = new CuratorClient();
        CuratorFramework curatorClient = null;
        ServiceDiscovery serviceDiscovery = null;
        try
        {
            curatorClient = client.init();

            if(logger.isDebugEnabled())
            {
                logger.debug("CuratorFramework was started !");
            }

            serviceDiscovery = CuratorTools.getServiceDiscovery(curatorClient);

            if(logger.isDebugEnabled())
            {
                logger.debug("ServiceDiscovery was started !");
            }

            //除了根节点以外的节点路径
            CuratorTools.register(nodePath + serviceName);

            if(logger.isDebugEnabled())
            {
                logger.debug("register service succeed !,serviceName is" + serviceName );
            }
        }
        catch (Exception e)
        {
            logger.error("something wrong with curator , exception : " + e);
        }
        finally
        {
            try
            {
                CuratorTools.closeResource(curatorClient,serviceDiscovery);

                if(logger.isDebugEnabled())
                {
                    logger.debug("CuratorClient and ServiceDiscovery were closed");
                }
            }
            catch (IOException e)
            {
                logger.error("IO exception when close CuratorClient and ServiceDiscovery, exception : " + e);
            }
        }
    }

}

