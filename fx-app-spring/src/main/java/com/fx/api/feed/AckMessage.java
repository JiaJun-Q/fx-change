package com.fx.api.feed;

public record AckMessage(long batchId, String status) {}
