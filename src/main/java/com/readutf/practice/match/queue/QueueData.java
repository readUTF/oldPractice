package com.readutf.practice.match.queue;

import com.readutf.practice.kits.Kit;
import lombok.Getter;
import lombok.Setter;

public class QueueData {

    public QueueData(long queueStart, Kit queueKit, QueueType queueType) {
        this.queueStart = queueStart;
        this.queueKit = queueKit;
        this.queueType = queueType;
    }

    @Getter long queueStart;
    @Setter @Getter Kit queueKit;
    @Getter @Setter QueueType queueType;

}
