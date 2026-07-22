package com.fx.api.web;

import com.fx.api.feed.AcceptingPayload;
import com.fx.api.feed.AcceptingState;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AcceptingState state;

    public AdminController(AcceptingState state) {
        this.state = state;
    }

    @GetMapping("/accepting")
    public AcceptingPayload accepting() {
        return new AcceptingPayload(state.isAccepting());
    }

    @PostMapping("/accepting")
    public AcceptingPayload setAccepting(@RequestBody AcceptingPayload body) {
        return new AcceptingPayload(state.set(body.accepting()));
    }
}
