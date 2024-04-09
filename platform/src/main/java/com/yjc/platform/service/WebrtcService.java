package com.yjc.platform.service;

public interface WebrtcService {
    void call(Long uid, String offer);

    void accept(Long uid, String answer);

    void reject(Long uid);

    void cancel(Long uid);

    void failed(Long uid, String reason);

    void handup(Long uid);

    void candidate(Long uid, String candidate);

    Object iceservers();
}
