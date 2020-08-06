package com.sparkle.csvreadapp.events;

import java.util.List;

public class ListEvent {

    public final List<String> topicList;
    public final List<String> messageList;

    public ListEvent(List<String> topicList, List<String> messageList) {
        this.topicList = topicList;
        this.messageList = messageList;
    }

}
