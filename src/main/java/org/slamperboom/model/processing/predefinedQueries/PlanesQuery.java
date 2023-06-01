package org.slamperboom.model.processing.predefinedQueries;

public class PlanesQuery implements PredefinedQuery{
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
        if(isCounting && !parameter.toLowerCase().contains("рейсы")){
            builder.append("count(\"Planes\".\"PlaneID\") ");
        }else{
            if(parameter.toLowerCase().contains("список")){
                builder.append("* ");
            } else if (parameter.toLowerCase().contains("нахождение")) {
                builder.append("distinct \"Planes\".\"PlaneID\" ");
            } else if (parameter.toLowerCase().contains("рейсы")) {
                builder.append("\"Planes\".\"PlaneID\", count(\"FlightID\") ");
            }
        }
        builder.append("from \"Planes\" ");
        if (parameter.toLowerCase().contains("нахождение")) {
            builder.append("join \"FlightsTable\" on (\"FlightsTable\".\"PlaneID\" = \"Planes\".\"PlaneID\")\n" +
                    "join \"Cities\" on (\"Departure\" = \"CityID\" OR \"Arrival\" = \"CityID\")\n" +
                    "where (\"CityName\" = 'Moscow')\n" +
                    "and ((\"DepartureDate\" > '").append(condition).append("' and \"Departure\" = \"CityID\")\n" +
                    "\t or (\"ArrivalDate\" < '").append(condition).append("' and \"Arrival\" = \"CityID\"))");
        } else if (parameter.toLowerCase().contains("рейсы")) {
            builder.append("join \"FlightsTable\" on (\"FlightsTable\".\"PlaneID\" = \"Planes\".\"PlaneID\")\n" +
                    "group by \"Planes\".\"PlaneID\"");
        }
        if(!isCounting || parameter.toLowerCase().contains("рейсы")){
            builder.append(" order by \"Planes\".\"PlaneID\" asc");
        }
        return builder.toString();
    }
}
