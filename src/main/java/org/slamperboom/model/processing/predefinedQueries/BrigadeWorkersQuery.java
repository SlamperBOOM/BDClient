package org.slamperboom.model.processing.predefinedQueries;

public class BrigadeWorkersQuery implements PredefinedQuery{
    @Override
    public String getQueryURL(String... conditions) {
        if(conditions.length != 5){
            return "";
        }
        String belongingParameter = conditions[0];
        String belongingCondition = conditions[1];
        String employeeParameter = conditions[2];
        String employeeCondition = conditions[3];
        String isCount = conditions[4];
        boolean isCounting = isCount.equalsIgnoreCase("да");
        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        if(isCounting){
            builder.append("count(\"EmployeeID\") ");
        }else{
            builder.append("\"Employees\".\"EmployeeID\", \"Employees\".\"Name\", \"Employees\".\"BrigadeID\", \n" +
                    "\"Employees\".\"Sex\", \"Employees\".\"Hired\", \"Employees\".\"Salary\", \"Employees\".\"Age\",\n" +
                    "\"Employees\".\"Kids\" ");
        }
        builder.append("from \"Employees\" ");
        builder.append("join \"Brigades\" on (\"Employees\".\"BrigadeID\"=\"Brigades\".\"BrigadeID\") ");
        boolean isSalaryAvg = employeeParameter.equalsIgnoreCase("salary") && employeeCondition.toLowerCase().contains("avg");

        switch (belongingParameter.toLowerCase()){
            case "brigade":{
                if(isSalaryAvg){
                    builder.append("join (select avg(\"Salary\") as \"avg\" from \"Employees\")\"Sub\"" +
                                    " on (\"Employees\".\"Salary\"")
                            .append(employeeCondition, 0, employeeCondition.lastIndexOf("a"))
                            .append("\"Sub\".\"avg\") ");
                    builder.append("where \"Brigades\".\"Name\" = '").append(belongingCondition).append("' ");
                }else if(employeeParameter.equalsIgnoreCase("age")){
                    builder.append("where \"Brigades\".\"Name\" = '").append(belongingCondition).append("' ");
                    builder.append("and \"Employees\".\"Age\"").append(employeeCondition);
                }else{
                    builder.append("where \"Brigades\".\"Name\" = '").append(belongingCondition).append("' ");
                }
                break;
            }
            case "department":{
                builder.append("join \"Departments\" on (\"Departments\".\"DepID\" = \"Brigades\".\"DepID\")\n");
                if(isSalaryAvg){
                    builder.append("join (select avg(\"Salary\") as \"avg\" from \"Employees\")\"Sub\"" +
                                    " on (\"Employees\".\"Salary\"")
                            .append(employeeCondition, 0, employeeCondition.lastIndexOf("a"))
                            .append("\"Sub\".\"avg\") ");
                    if(!belongingCondition.equalsIgnoreCase("all")){
                        builder.append("where \"Departments\".\"DepartmentName\" = '").append(belongingCondition).append("' ");
                    }
                }else if(employeeParameter.equalsIgnoreCase("age")){
                    if(!belongingCondition.equalsIgnoreCase("all")){
                        builder.append("where \"Departments\".\"DepartmentName\" = '").append(belongingCondition).append("' ");
                        builder.append("and \"Employees\".\"Age\"").append(employeeCondition);
                    }else{
                        builder.append("where \"Employees\".\"Age\"").append(employeeCondition);
                    }
                }else{
                    if(!belongingCondition.equalsIgnoreCase("all")){
                        builder.append("where \"Departments\".\"DepartmentName\" = '").append(belongingCondition).append("' ");
                    }
                }
                break;
            }
            case "flight":{
                builder.append("join \"Planes\" on (\"Planes\".\"PilotBrigade\"=\"Brigades\".\"BrigadeID\" or\n" +
                        "\"Planes\".\"TechnicsBrigade\"=\"Brigades\".\"BrigadeID\" or\n" +
                        "\"Planes\".\"PlaneStaffBrigade\"=\"Brigades\".\"BrigadeID\")\n" +
                        "join \"FlightsTable\" on (\"FlightsTable\".\"PlaneID\" = \"Planes\".\"PlaneID\")\n");
                if(isSalaryAvg){
                    builder.append("join (select avg(\"Salary\") as \"avg\" from \"Employees\")\"Sub\"" +
                                    " on (\"Employees\".\"Salary\"")
                            .append(employeeCondition, 0, employeeCondition.lastIndexOf("a"))
                            .append("\"Sub\".\"avg\") ");
                    builder.append("where \"FlightID\" = ").append(belongingCondition);
                }else if(employeeParameter.equalsIgnoreCase("age")){
                    builder.append("where \"FlightID\" = ").append(belongingCondition);
                    builder.append("and \"Employees\".\"Age\"").append(employeeCondition);
                }else {
                    builder.append("where \"FlightID\" = ").append(belongingCondition);
                }
                break;
            }
            default:{
                return "";
            }
        }
        if(!isCounting){
            builder.append(" order by \"Employees\".\"EmployeeID\" asc");
        }
        return builder.toString();
    }
}
