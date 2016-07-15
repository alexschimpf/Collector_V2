package com.tendersaucer.collector.event;

/**
 * Created by Alex on 5/31/2016.
 */
public class ChainResetEvent extends Event<IChainResetListener> {

    private final String groupId;

    public ChainResetEvent(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void notify(IChainResetListener listener) {
        listener.onChainReset(groupId);
    }
}
