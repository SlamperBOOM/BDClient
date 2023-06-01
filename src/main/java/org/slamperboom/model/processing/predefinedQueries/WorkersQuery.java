package org.slamperboom.model.processing.predefinedQueries;

public class WorkersQuery implements PredefinedQuery{
    @Override
    public String getQueryURL(String... conditions) {
        if(conditions.length != 5){
            return "";
        }
        String kind = conditions[0];
        String parameter = conditions[1];
        String condition = conditions[2];
        String department = conditions[3];
        String isCount = conditions[4].toLowerCase();
        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        boolean isCounting = isCount.equalsIgnoreCase("y")
                || isCount.equalsIgnoreCase("yes")
                || isCount.equalsIgnoreCase("да");
        if(isCounting){
            if(department.equals("")){
                //здесь могут быть и главы и работники
                if(kind.equalsIgnoreCase("работники") || kind.equalsIgnoreCase("employees")){
                    builder.append("count(\"EmployeeID\") from \"Employees\" where ");
                }else if(kind.equalsIgnoreCase("главы") || kind.equalsIgnoreCase("heads")){
                    builder.append("count(\"HeadID\") from \"Heads\" where ");
                }
            }else{
                //здесь только работники, т.к. только они приписаны к департаменту
                builder.append("count(\"EmployeeID\") from \"Employees\" ");
                builder.append("join \"Brigades\" on(\"Employees\".\"BrigadeID\" = \"Brigades\".\"BrigadeID\")\n" +
                        "join \"Departments\" on(\"Brigades\".\"DepID\" = \"Departments\".\"DepID\")\n" +
                        "where \"Departments\".\"DepartmentName\"=");
                builder.append("'").append(department).append("' ").append("and ");
            }
        }else{
            if(department.equals("")){
                //здесь могут быть и главы и работники
                builder.append("* from ");
                if(kind.equalsIgnoreCase("работники") || kind.equalsIgnoreCase("employees")){
                    builder.append("\"Employees\" where ");
                }else if(kind.equalsIgnoreCase("главы") || kind.equalsIgnoreCase("heads")){
                    builder.append("\"Heads\" where ");
                }
            }else{
                //здесь только работники, т.к. только они приписаны к департаменту
                builder.append("\"Employees\".\"EmployeeID\", \"Employees\".\"Name\", \"Employees\".\"BrigadeID\", \n" +
                        "\"Employees\".\"Sex\", \"Employees\".\"Hired\", \"Employees\".\"Salary\", \"Employees\".\"Age\",\n" +
                        "\"Employees\".\"Kids\", \"Departments\".\"DepartmentName\" ");
                builder.append("from \"Employees\" ");
                builder.append("join \"Brigades\" on(\"Employees\".\"BrigadeID\" = \"Brigades\".\"BrigadeID\")\n" +
                        "join \"Departments\" on(\"Brigades\".\"DepID\" = \"Departments\".\"DepID\")\n" +
                        "where \"Departments\".\"DepartmentName\"=");
                builder.append("'").append(department).append("' ").append("and ");
            }
        }

        switch (parameter.toLowerCase()){
            case "пол": {
                builder.append("\"Sex\"=upper('").append(condition).append("')");
                break;
            }
            case "трудоустроен": {
                builder.append("date_part('year', age(\"Hired\")) ").append(condition);
                break;
            }
            case "возраст": {
                builder.append("\"Age\" ").append(condition);
                break;
            }
            case "дети":{
                builder.append("\"Kids\" ").append(condition);
                break;
            }
            case "зарплата":{
                builder.append("\"Salary\"").append(condition);
                break;
            }
            default: {
                if(builder.indexOf("and") != -1){
                    builder.replace(builder.indexOf("and"), builder.length(), "");
                } else{
                    builder.replace(builder.indexOf("where"), builder.length(), "");
                }
                break;
            }
        }

        if(!isCounting) {
            if (kind.equalsIgnoreCase("работники") || kind.equalsIgnoreCase("employees")) {
                builder.append(" order by \"EmployeeID\" asc");
            } else if (kind.equalsIgnoreCase("главы") || kind.equalsIgnoreCase("heads")) {
                builder.append(" order by \"HeadID\" asc");
            }
        }
        return builder.toString();
    }
}
