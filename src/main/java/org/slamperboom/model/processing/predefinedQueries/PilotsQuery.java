package org.slamperboom.model.processing.predefinedQueries;

public class PilotsQuery implements PredefinedQuery{
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
            builder.append("count(\"Pilots\".\"PilotID\") ");
        }else{
            builder.append("\"Pilots\".\"PilotID\", \"Employees\".\"EmployeeID\", \"Employees\".\"Name\",\n" +
                    "\"Employees\".\"BrigadeID\", \"Employees\".\"Sex\", \"Employees\".\"Hired\", \"Employees\".\"Salary\", \n" +
                    "\"Employees\".\"Age\", \"Employees\".\"Kids\" ");
        }
        builder.append("from \"Pilots\" join \"Employees\" on (\"Pilots\".\"EmployeeID\" = \"Employees\".\"EmployeeID\") ");
        switch (parameter.toLowerCase()){
            case "не проходил медосмотр в":{
                builder.append("where \"Pilots\".\"PilotID\" not in\n" +
                        "(\n" +
                        "select distinct \"Pilots\".\"PilotID\" \n" +
                        "from \"Pilots\"\n" +
                        "join \"PilotsMedicalControl\" on (\"Pilots\".\"PilotID\" = \"PilotsMedicalControl\".\"PilotID\")\n" +
                        "where date_part('year', \"PilotsMedicalControl\".\"DateOfControl\") =").append(condition).append("\n" +
                        ")");
                break;
            }
            case "проходил медосмотр в":{
                builder.append("join \"PilotsMedicalControl\" on (\"Pilots\".\"PilotID\" = \"PilotsMedicalControl\".\"PilotID\")\n" +
                        "where date_part('year', \"PilotsMedicalControl\".\"DateOfControl\") = ").append(condition);
                break;
            }
            case "возраст":{
                builder.append("where \"Age\"").append(condition);
                break;
            }
            case "пол":{
                builder.append("where \"Sex\" = upper('").append(condition).append("')");
                break;
            }
            case "зарплата":{
                builder.append("where \"Salary\"").append(condition);
                break;
            }
            default:{
                return "";
            }
        }
        if(!isCounting) {
            builder.append(" order by \"PilotID\" asc");
        }
        return builder.toString();
    }
}
