package org.slamperboom.model.data;

import org.slamperboom.model.processing.QueryRow;

import java.time.LocalDateTime;
import java.util.List;

public class RequestInfo {
    private List<QueryRow> response;
    private String queryURL;
    private LocalDateTime queryTimeStamp;

    public RequestInfo(List<QueryRow> response, String queryURL, LocalDateTime queryTimeStamp) {
        this.response = response;
        this.queryURL = queryURL;
        this.queryTimeStamp = queryTimeStamp;
    }

    public boolean checkURL(String otherURL){
        return queryURL.equals(otherURL);
    }

    public boolean checkContains(String tableName){
        return queryURL.contains(tableName);
    }

    public boolean checkDateTime(LocalDateTime otherDate){
        //сделать сохранение запроса на 5 минут
        return queryTimeStamp.isBefore(otherDate.minusMinutes(5));
    }
    public List<QueryRow> getResponse(){
        return response;
    }
}
