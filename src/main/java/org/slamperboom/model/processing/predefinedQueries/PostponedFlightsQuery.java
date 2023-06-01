package org.slamperboom.model.processing.predefinedQueries;

public class PostponedFlightsQuery implements PredefinedQuery{
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
        if(isCounting && !parameter.contains("билет")){
            builder.append("count(\"PostponesTable\".\"FlightID\") ");
        }else if(parameter.contains("билет")){
            builder.append("\"PostponesTable\".\"FlightID\", count(\"PassengerID\") ");
        }else{
            builder.append("* ");
        }
        builder.append("from \"PostponesTable\" ");
        if(parameter.contains("причин")){
            builder.append("join \"PostponeReasons\" on (\"PostponesTable\".\"PostponeReason\" = \"PostponeReasons\".\"ReasonID\") ")
                    .append("where \"Description\" ='").append(condition).append("'");
        } else if (parameter.contains("маршрут")) {
            builder.append("join \"FlightsTable\" on (\"FlightsTable\".\"FlightID\" = \"PostponesTable\".\"FlightID\")\n" +
                    "join (select * from \"Cities\")\"Dep\" on (\"Dep\".\"CityID\" = \"Departure\")\n" +
                    "join (select * from \"Cities\")\"Arr\" on (\"Arr\".\"CityID\" = \"Arrival\")");
            String[] cities = condition.split("->");
            if(cities.length != 2){
                return "";
            }
            builder.append("where \"Dep\".\"CityName\" = '")
                    .append(cities[0].trim())
                    .append("' and \"Arr\".\"CityName\" = '")
                    .append(cities[1].trim())
                    .append("'");
        } else if (parameter.contains("билет")) {
            builder.append("join \"FlightsTable\" on (\"FlightsTable\".\"FlightID\" = \"PostponesTable\".\"FlightID\")\n" +
                    "join \"Passengers\" on (\"Passengers\".\"FlightID\" = \"FlightsTable\".\"FlightID\")\n" +
                    "where \"RefundDate\" >= \"OldDate\" and \"RefundDate\" <= \"NewDate\"\n" +
                    "group by \"PostponesTable\".\"FlightID\"");
        }else if(!parameter.contains("все")){
            return "";
        }

        if(!isCounting){
            builder.append(" order by \"PostponesTable\".\"FlightID\" asc");
        }
        return builder.toString();
    }
}
