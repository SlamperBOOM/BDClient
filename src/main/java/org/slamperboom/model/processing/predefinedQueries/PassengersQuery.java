package org.slamperboom.model.processing.predefinedQueries;

public class PassengersQuery implements PredefinedQuery{
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
            builder.append("count(\"PassengerID\") ");
        }else{
            builder.append("\"Passengers\".\"PassengerID\", \"Passengers\".\"Name\", \"Passengers\".\"FlightID\",\n" +
                    "\"Passengers\".\"Age\", \"Passengers\".\"Sex\", \"Passengers\".\"RefundDate\", \"Passengers\".\"Laggage\" ");
        }
        builder.append("from \"Passengers\" ");
        switch (parameter){
            case "рейс":{
                builder.append("where \"FlightID\" = ").append(condition);
                break;
            }
            case "дата вылета":{
                builder.append("join \"FlightsTable\" on (\"FlightsTable\".\"FlightID\" = \"Passengers\".\"FlightID\") ");
                String[] date = condition.split("\\.");
                if(date.length!=3){
                    return "";
                }
                builder.append("where date_part('month', \"DepartureDate\") =").append(date[0]);
                builder.append(" and date_part('day', \"DepartureDate\") =").append(date[1]);
                builder.append(" and date_part('year', \"DepartureDate\") =").append(date[2]);
                break;
            }
            case "дата вылета за границу":{
                builder.append("join \"FlightsTable\" on (\"FlightsTable\".\"FlightID\" = \"Passengers\".\"FlightID\") ");
                String[] date = condition.split("\\.");
                if(date.length!=3){
                    return "";
                }
                builder.append("where date_part('month', \"DepartureDate\") =").append(date[0]);
                builder.append(" and date_part('day', \"DepartureDate\") =").append(date[1]);
                builder.append(" and date_part('year', \"DepartureDate\") =").append(date[2]);
                builder.append(" and \"FlightType\" = 2");
                break;
            }
            case "сдача багажа":{
                if(condition.equalsIgnoreCase("yes") ||
                        condition.equalsIgnoreCase("y") ||
                        condition.equalsIgnoreCase("да")){
                    builder.append("where \"Laggage\" = TRUE");
                } else if (condition.equalsIgnoreCase("no") ||
                        condition.equalsIgnoreCase("n") ||
                        condition.equalsIgnoreCase("нет")) {
                    builder.append("where \"Laggage\" = FALSE");
                }
                break;
            }
            case "пол":{
                builder.append("where \"Sex\" = upper('").append(condition).append("')");
                break;
            }
            case "возраст":{
                builder.append("where \"Age\"").append(condition);
                break;
            }
            default: return "";
        }

        if(!isCounting){
            builder.append(" order by \"PassengerID\" asc");
        }
        return builder.toString();
    }
}
