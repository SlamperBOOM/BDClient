package org.slamperboom.model.processing.predefinedQueries;

public class RefundTicketsQuery implements PredefinedQuery{
    @Override
    public String getQueryURL(String... conditions) {
        if(conditions.length != 2){
            return "";
        }
        String parameter = conditions[0].toLowerCase();
        String condition = conditions[1];

        StringBuilder builder = new StringBuilder();
        if(parameter.equalsIgnoreCase("маршрут")){
            builder.append("select count(\"PassengerID\") from \"Passengers\" ")
                    .append("join \"FlightsTable\" on (\"Passengers\".\"FlightID\" = \"FlightsTable\".\"FlightID\") ")
                    .append("join (select * from \"Cities\")\"Dep\" on (\"Dep\".\"CityID\" = \"Departure\")\n" +
                            "join (select * from \"Cities\")\"Arr\" on (\"Arr\".\"CityID\" = \"Arrival\") ");
        }else{
            builder.append("select count(\"PassengerID\") from \"Passengers\" ")
                    .append("join \"FlightsTable\" on (\"Passengers\".\"FlightID\" = \"FlightsTable\".\"FlightID\") ");
        }
        builder.append("where \"RefundDate\" is not null and ");
        switch (parameter){
            case "рейс":{
                builder.append("\"Passengers\".\"FlightID\" = ").append(condition);
                break;
            }
            case "дата":{
                String[] date = condition.split("\\.");
                if(date.length!=3){
                    return "";
                }
                builder.append("date_part('month', \"RefundDate\") =").append(date[0]);
                builder.append(" and date_part('day', \"RefundDate\") =").append(date[1]);
                builder.append(" and date_part('year', \"RefundDate\") =").append(date[2]);
                break;
            }
            case "маршрут":{
                String[] cities = condition.split("->");
                if(cities.length != 2){
                    return "";
                }
                builder.append("\"Dep\".\"CityName\" = '")
                        .append(cities[0].trim())
                        .append("' and \"Arr\".\"CityName\" = '")
                        .append(cities[1].trim())
                        .append("'");
                break;
            }
            case "цена":{
                builder.append("\"Cost\"").append(condition);
                break;
            }
            case "возраст":{
                builder.append("\"Age\"").append(condition);
                break;
            }
            case "пол":{
                builder.append("\"Sex\"=upper('").append(condition).append("')");
                break;
            }
            default: return "";
        }

        return builder.toString();
    }
}
