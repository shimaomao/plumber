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

        TableMapEventHandlerImpl tableMapEventHandler = new TableMapEventHandlerImpl();

        UpdateEventHandlerImpl updateEventHandlerMember = new UpdateEventHandlerImpl();
        updateEventHandlerMember.setDatabase("pubmidb");
        updateEventHandlerMember.setTable("tbl_member");
        updateEventHandlerMember.setStatus(true);

        UpdateEventHandlerImpl updateEventHandlerPaymentBill = new UpdateEventHandlerImpl();
        updateEventHandlerPaymentBill.setDatabase("pubmidb");
        updateEventHandlerPaymentBill.setTable("tbl_payment_bill");
        updateEventHandlerPaymentBill.setStatus(true);

        binaryLogClientService.registEventHandler(auth, tableMapEventHandler);
        binaryLogClientService.registEventHandler(auth, updateEventHandlerMember);
        binaryLogClientService.registEventHandler(auth, updateEventHandlerPaymentBill);

        updateEventHandlerMember.initMateDate(false);
        updateEventHandlerPaymentBill.initMateDate(false);


        binaryLogClientService.start(auth);
    }
}