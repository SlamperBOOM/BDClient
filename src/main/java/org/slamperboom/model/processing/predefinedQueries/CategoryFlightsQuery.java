package org.slamperboom.model.processing.predefinedQueries;

public class CategoryFlightsQuery implements PredefinedQuery{
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
            builder.append("\"FlightID\" ");
        }
        builder.append("from \"FlightsTable\" ");

        if(parameter.contains("рейс")){
            builder.append("join \"FlightTypes\" on (\"FlightsTable\".\"FlightType\" = \"FlightTypes\".\"TypeID\")\n" +
                    "where \"Description\" ='").append(condition).append("'");
        } else if (parameter.contains("город")) {
            builder.append("join (select * from \"Cities\")\"Arr\" on (\"Arr\".\"CityID\" = \"Arrival\")\n" +
                    "where \"Arr\".\"CityName\" = '").append(condition).append("'");
        }else{
            return "";
        }

        if(!isCounting){
            builder.append(" order by \"FlightID\" asc");
        }
        return builder.toString();
    }
}
