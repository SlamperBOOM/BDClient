package org.slamperboom.model.processing.predefinedQueries;

public class FreeSeatsQuery implements PredefinedQuery {
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
            builder.append("sum(\"PlaneModels\".\"Seats\"-\"TicketsSold\") as \"Free Seats\" ");
        }else{
            builder.append("\"FlightID\", \n" +
                    "\"PlaneModels\".\"Seats\"-\"TicketsSold\" as \"Seats\", \n" +
                    "\"TicketsSold\" ");
        }
        builder.append("from \"FlightsTable\"\n" +
                "join \"Planes\" on (\"Planes\".\"PlaneID\" = \"FlightsTable\".\"PlaneID\")\n" +
                "join \"PlaneModels\" on (\"Planes\".\"PlaneModel\" = \"PlaneModels\".\"ModelID\") ");
        switch (parameter){
            case "рейс":{
                builder.append("where \"FlightID\" = ").append(condition);
                break;
            }
            case "дата":{
                String[] date = condition.split("\\.");
                if(date.length!=3){
                    return "";
                }
                builder.append("where date_part('month', \"DepartureDate\") =").append(date[0]);
                builder.append(" and date_part('day', \"DepartureDate\") =").append(date[1]);
                builder.append(" and date_part('year', \"DepartureDate\") =").append(date[2]);
                break;
            }
            case "маршрут":{
                builder.append("join (select * from \"Cities\")\"Dep\" on (\"Dep\".\"CityID\" = \"Departure\")\n" +
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
                break;
            }
            case "цена":{
                builder.append("where \"Cost\"").append(condition);
                break;
            }
            case "дата вылета":{
                builder.append("where \"DepartureDate\" ");
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
                break;
            }
            default: return "";
        }
        if(!isCounting){
            builder.append(" order by \"FlightID\" asc");
        }
        return builder.toString();
    }
}
