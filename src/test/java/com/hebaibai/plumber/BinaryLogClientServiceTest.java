package com.hebaibai.plumber;

import com.hebaibai.plumber.core.Auth;
import com.hebaibai.plumber.core.BinaryLogClientService;
import com.hebaibai.plumber.core.handler.TableMapEventHandlerImpl;
import com.hebaibai.plumber.core.handler.UpdateEventHandlerImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

public class BinaryLogClientServiceTest extends PlumberApplicationTests {

    @Autowired
    private BinaryLogClientService binaryLogClientService;

    @Test
    public void name() throws InterruptedException, SQLException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        binaryLogClient();
        countDownLatch.await();
    }

    public void binaryLogClient() throws SQLException {
        Auth auth = new Auth();
        auth.setHostname("172.31.100.51");
        auth.setUsername("pubmidb");
        auth.setPassword("Pubmi@2016");
        binaryLogClientService.create(auth);

        UpdateEventHandlerImpl updateEventHandler = new UpdateEventHandlerImpl();
        TableMapEventHandlerImpl tableMapEventHandler = new TableMapEventHandlerImpl();

        updateEventHandler.setDatabase("pubmidb");
        updateEventHandler.setTable("tbl_member");
        updateEventHandler.setStatus(true);

        binaryLogClientService.registEventHandler(auth, tableMapEventHandler);
        binaryLogClientService.registEventHandler(auth, updateEventHandler);

        updateEventHandler.initMateDate(false);

        binaryLogClientService.start(auth);
    }
}