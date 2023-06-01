package org.slamperboom.model.processing.predefinedQueries;

public class CancelledFlightsQuery implements PredefinedQuery{
    @Override
    public String getQueryURL(String... conditions) {
        if(conditions.length != 3){
            return "";
        }
        String parameter = conditions[0].toLowerCase();
        String condition = conditions[1];
        String isCount = conditions[2];

        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        boolean isCounting = isCount.equalsIgnoreCase("да");
        if(isCounting){
            builder.append("count(\"FlightID\") ");
        }else{
            builder.append("* ");
        }
        builder.append("from \"FlightsTable\" ");
        if(parameter.contains("маршрут")){
            builder.append("join (select * from \"Cities\")\"Dep\" on (\"Dep\".\"CityID\" = \"Departure\")\n" +
                    "join (select * from \"Cities\")\"Arr\" on (\"Arr\".\"CityID\" = \"Arrival\") ");
        }
        builder.append("where \"IsCancelled\" = TRUE ");
        if(parameter.contains("маршрут")){
            String[] cities = condition.split("->");
            if(cities.length != 2){
                return "";
            }
            builder.append("and \"Dep\".\"CityName\" = '")
                    .append(cities[0].trim())
                    .append("' and \"Arr\".\"CityName\" = '")
                    .append(cities[1].trim())
                    .append("'");
        } else if (parameter.contains("количеству")) {
            builder.append("and \"MinimumTickets\" - \"TicketsSold\"").append(condition);
        } else if (parameter.contains("%")) {
            builder.append("and \"TicketsSold\"*1.0/\"MinimumTickets\"*100").append(condition);
        }else if(!parameter.contains("все")){
            return "";
        }

        if(!isCounting){
            builder.append(" order by \"FlightID\" asc");
        }
        return builder.toString();
    }
}
