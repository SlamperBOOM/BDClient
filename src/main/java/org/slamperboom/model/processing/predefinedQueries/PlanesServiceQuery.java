package org.slamperboom.model.processing.predefinedQueries;

public class PlanesServiceQuery implements PredefinedQuery{
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
        if(isCounting && !parameter.equalsIgnoreCase("количество рейсов до ремонта")){
            builder.append("count(\"PlaneID\") from (select ");
        }

        if(parameter.equalsIgnoreCase("период") || parameter.equalsIgnoreCase("дата ремонта")){
            builder.append("distinct \"Planes\".\"PlaneID\" ");
        } else if (parameter.equalsIgnoreCase("количество ремонтов")) {
            builder.append("\"Planes\".\"PlaneID\", count(\"ID\") ");
        } else if (parameter.equalsIgnoreCase("количество рейсов до ремонта")) {
            builder.append("\"Planes\".\"PlaneID\", \"DateOfService\", count(\"FlightID\") ");
        }else {
            return "";
        }
        builder.append("from \"Planes\" ");

        if(parameter.equalsIgnoreCase("количество ремонтов")){
            builder.append("left join \"PlanesService\" on (\"PlanesService\".\"PlaneID\" = \"Planes\".\"PlaneID\")\n" +
                    "group by \"Planes\".\"PlaneID\"\n" +
                    "having count(\"ID\") = ").append(condition);
        }else{
            builder.append("join \"PlanesService\" on (\"PlanesService\".\"PlaneID\" = \"Planes\".\"PlaneID\") ");
            if(parameter.equalsIgnoreCase("период")){
                String[] dates = condition.split("-");
                builder.append("where \"DateOfService\" >= '")
                        .append(dates[0])
                        .append("' and \"DateOfService\" <= '").
                        append(dates[1]).append("'");
            } else if (parameter.equalsIgnoreCase("дата ремонта")) {
                builder.append("where \"DateOfService\" = '").append(condition).append("'");
            }else if (parameter.equalsIgnoreCase("количество рейсов до ремонта")){
                builder.append("join \"FlightsTable\" on (\"Planes\".\"PlaneID\" = \"FlightsTable\".\"PlaneID\")\n" +
                        "where \"ArrivalDate\" < \"DateOfService\"\n" +
                        "group by \"Planes\".\"PlaneID\", \"DateOfService\"");
            }else{
                return "";
            }
        }

        if(isCounting && !parameter.equalsIgnoreCase("количество рейсов до ремонта")){
            builder.append(")\"sub\"");
        }else{
            builder.append(" order by \"Planes\".\"PlaneID\" asc");
        }
        return builder.toString();
    }
}
