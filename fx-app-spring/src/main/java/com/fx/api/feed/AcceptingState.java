package com.fx.api.feed;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;

@Component
public class AcceptingState {

    private final AtomicBoolean accepting = new AtomicBoolean(true);

    public boolean isAccepting() {
        return accepting.get();
    }

    public boolean set(boolean value) {
        accepting.set(value);
        return value;
    }
}
