package org.hisp.dhis.analytics.util.sql;

public class SqlConditionJoiner {

    public static String joinSqlConditions(String... conditions) {
        if (conditions == null || conditions.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder("where ");
        boolean firstCondition = true;

        for (String condition : conditions) {
            if (condition == null || condition.trim().isEmpty()) {
                continue;
            }

            // Remove leading "where" or " where" and trim
            String cleanedCondition = condition.trim();
            if (cleanedCondition.toLowerCase().startsWith("where")) {
                cleanedCondition = cleanedCondition.substring(5).trim();
            }

            if (!cleanedCondition.isEmpty()) {
                if (!firstCondition) {
                    result.append(" and ");
                }
                result.append(cleanedCondition);
                firstCondition = false;
            }
        }

        return result.toString();
    }
}
