package org.slamperboom.model.processing.predefinedQueries;

public class FlightsQuery implements PredefinedQuery{
    @Override
    public String getQueryURL(String... conditions) {
        if(conditions.length != 3){
            return "";
        }
        String parameter = conditions[0];
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
        if(parameter.equalsIgnoreCase("маршрут") || parameter.equalsIgnoreCase("все параметры")){
            builder.append("join (select * from \"Cities\")\"Dep\" on (\"Dep\".\"CityID\" = \"Departure\")\n" +
                    "join (select * from \"Cities\")\"Arr\" on (\"Arr\".\"CityID\" = \"Arrival\") ");
        }
        switch (parameter.toLowerCase()){
            case "маршрут": {
                String[] cities = condition.split("->");
                builder.append("where \"Dep\".\"CityName\" = '")
                        .append(cities[0].trim())
                        .append("' and \"Arr\".\"CityName\" = '")
                        .append(cities[1].trim())
                        .append("'");
                break;
            }
            case "цена":{
                builder.append("where \"Cost\" ").append(condition);
                break;
            }
            case "длительность":{
                if(condition.lastIndexOf("<=") != -1 || condition.lastIndexOf(">=") != -1) {
                    condition = condition.substring(0, 2).concat("'").concat(condition.substring(2)).concat("'");
                }else{
                    condition = condition.substring(0, 1).concat("'").concat(condition.substring(1)).concat("'");
                }
                builder.append("where \"TimeOfFlight\" ").append(condition);
                break;
            }
            case "все параметры":{
                String[] params = condition.split(",");//way, cost, time
                if(params[0].split("->").length != 2){
                    return "";
                }
                params[1] = params[1].trim();
                params[2] = params[2].trim();
                builder.append("where \"Dep\".\"CityName\" ='").append(params[0].split("->")[0].trim())
                        .append("' and \"Arr\".\"CityName\" ='").append(params[0].split("->")[1].trim())
                        .append("' and \"Cost\"").append(params[1])
                        .append(" and \"TimeOfFlight\"").append(
                                params[2].lastIndexOf("<=") != -1 || params[2].lastIndexOf(">=") != -1 ?
                                        params[2].substring(0, 2).concat("'").concat(params[2].substring(2)).concat("'"):
                                        params[2].substring(0, 1).concat("'").concat(params[2].substring(1)).concat("'")
                        );
                break;
            }
            default:{
                return "";
            }
        }
        if(!isCounting){
            builder.append(" order by \"FlightsTable\".\"FlightID\"");
        }
        return builder.toString();
    }
}
