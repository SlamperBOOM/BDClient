package org.slamperboom.model.processing.predefinedQueries;

public class AnotherFlightsQuery implements PredefinedQuery{
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
        if(isCounting && !parameter.contains("маршрут")){
            builder.append("count(\"FlightID\") ");
        }else if(parameter.contains("маршрут")){
            builder.append("avg(\"TicketsSold\") ");
        } else{
            builder.append("\"FlightID\" ");
        }

        if(parameter.contains("модель")){
            builder.append("from \"PlaneModels\"\n" +
                    "join \"Planes\" on (\"PlaneModels\".\"ModelID\" = \"Planes\".\"PlaneModel\")\n" +
                    "join \"FlightsTable\" on (\"FlightsTable\".\"PlaneID\" = \"Planes\".\"PlaneID\")\n" +
                    "where \"PlaneModels\".\"Name\" ='").append(condition).append("'");
        }else if(parameter.contains("маршрут")){
            builder.append("from \"FlightsTable\"\n" +
                    "join (select * from \"Cities\")\"Dep\" on (\"Dep\".\"CityID\" = \"Departure\")\n" +
                    "join (select * from \"Cities\")\"Arr\" on (\"Arr\".\"CityID\" = \"Arrival\") ");
            String[] cities = condition.split("->");
            if(cities.length != 2){
                return "";
            }
            builder.append("where \"Dep\".\"CityName\" = '")
                    .append(cities[0].trim())
                    .append("' and \"Arr\".\"CityName\" = '")
                    .append(cities[1].trim())
                    .append("'");
        } else if (parameter.contains("вылет")) {
            builder.append("from \"FlightsTable\"\n" +
                    "where \"DepartureDate\"");
            if(condition.lastIndexOf("<=") != -1 || condition.lastIndexOf(">=") != -1) {
                condition = condition.substring(0, 2)
                        .concat("timestamp with time zone '")
                        .concat(condition.substring(2)).concat("'");
            }else{
                condition = condition.substring(0, 1)
                        .concat("timestamp with time zone '")
                        .concat(condition.substring(1)).concat("'");
            }
            builder.append(condition);
        }else{
            return "";
        }

        if(!isCounting && !parameter.contains("маршрут")){
            builder.append(" order by \"FlightID\" asc");
        }
        return builder.toString();
    }
}
