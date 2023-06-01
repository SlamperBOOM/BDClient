package org.slamperboom.model.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Requests {
    private final List<RequestInfo> requestsList = new ArrayList<>();
    private boolean caching = true;

    public void setCaching(boolean caching) {
        if(!caching){
            requestsList.clear();
        }
        this.caching = caching;
    }

    public void addRequest(RequestInfo info){
        if(caching) {
            requestsList.add(info);
        }
    }

    public RequestInfo findRequest(String queryURL){
        if(!caching){
            return null;
        }
        List<RequestInfo> requestsToDelete = new ArrayList<>();
        RequestInfo requestInfo = null;
        for(RequestInfo info : requestsList){
            if(info.checkDateTime(LocalDateTime.now())){
                requestsToDelete.add(info);
                continue;
            }
            if(info.checkURL(queryURL)){
                requestInfo = info;
            }
        }
        for(RequestInfo info : requestsToDelete){
            requestsList.remove(info);
        }
        return requestInfo;
    }

    public void deleteRequestsWithTable(String tableName){
        if(!caching){
            return;
        }
        List<RequestInfo> requestsToDelete = new ArrayList<>();
        for(RequestInfo info : requestsList){
            if(info.checkDateTime(LocalDateTime.now())){
                requestsToDelete.add(info);
                continue;
            }
            if(info.checkContains(tableName)){
                requestsToDelete.add(info);
            }
        }
        for(RequestInfo requestInfo : requestsToDelete){
            requestsList.remove(requestInfo);
        }
    }
}
