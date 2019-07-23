package com.hebaibai.admin.plumber;

import com.hebaibai.plumber.Config;
import com.hebaibai.plumber.PlumberLancher;
import io.vertx.core.Vertx;
import org.springframework.stereotype.Component;

@Component
public class LancherCache {

    private PlumberLancher plumberLancher = new PlumberLancher() {{
        Vertx vertx = Vertx.vertx();
        setVertx(vertx);
        setContext(vertx.getOrCreateContext());
    }};

    public PlumberLancher getPlumberLancher() {
        return plumberLancher;
    }

    public boolean isRun() {
        return plumberLancher.isRun();
    }

    public void start(Config config) {
        stop();
        plumberLancher.start(config);
    }

    public void stop() {
        if (plumberLancher.isRun()) {
            plumberLancher.stop();
        }
    }
}
