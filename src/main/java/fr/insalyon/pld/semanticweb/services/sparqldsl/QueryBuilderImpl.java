package fr.insalyon.pld.semanticweb.services.sparqldsl;

import fr.insalyon.pld.semanticweb.model.persistence.SchemaLinker;
import fr.insalyon.pld.semanticweb.tools.KotlinClass;

public class QueryBuilderImpl implements QueryBuilderWhere, QueryBuilderUnion {

    private String selectClause;
    private String whereClause = null;
    private Integer limit = null;
    private Integer offset = null;
    private String orderBy = null;

    private String union(String a, String b) {
        return "{" + a + "}" + Keywords.UNION + "{" + b + "}";
    }

    @Override
    public QueryBuilderUnion union(Condition condition) {
        String whereClause = union(this.whereClause, condition.toSparqlCondition() + "\n");
        return new QueryBuilderImpl(selectClause, whereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion union(Condition condition0, Condition condition1) {
        String newWhereClause = union(this.whereClause, condition0.toSparqlCondition() + "\n" + condition1.toSparqlCondition() + "\n");
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion union(Condition condition0, Condition condition1, Condition condition2) {
        String newWhereClause = union(this.whereClause, condition0.toSparqlCondition() + "\n" + condition1.toSparqlCondition() + "\n" + condition2.toSparqlCondition() + "\n");
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion union(Condition condition0, Condition condition1, Condition condition2, Condition condition3) {
        String newWhereClause = union(this.whereClause,
                condition0.toSparqlCondition() + "\n" +
                        condition1.toSparqlCondition() + "\n" +
                        condition2.toSparqlCondition() + "\n" +
                        condition3.toSparqlCondition() + "\n"
        );
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion union(Condition... conditions) {
        if(conditions.length == 0) throw new RuntimeException("conditions.legth must be >= 1");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0; i < conditions.length; i++) {
            stringBuilder.append(conditions[i].toSparqlCondition())
                    .append("\n");
        }
        String newWhereClause = union(this.whereClause, stringBuilder.toString());
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion where(Condition condition) {
        String newWhereClause = whereClause + condition.toSparqlCondition() + "\n";
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion where(Condition condition0, Condition condition1) {
        String newWhereClause = whereClause + condition0.toSparqlCondition() + "\n" + condition1.toSparqlCondition() + "\n";
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion where(Condition condition0, Condition condition1, Condition condition2) {
        String newWhereClause = whereClause +
                condition0.toSparqlCondition() + "\n" +
                condition1.toSparqlCondition() + "\n" +
                condition2.toSparqlCondition() + "\n";
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion where(Condition condition0, Condition condition1, Condition condition2, Condition condition3) {
        String newWhereClause = whereClause +
                condition0.toSparqlCondition() + "\n" +
                condition1.toSparqlCondition() + "\n" +
                condition2.toSparqlCondition() + "\n" +
                condition3.toSparqlCondition() + "\n";
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    @Override
    public QueryBuilderUnion where(Condition... conditions) {
        if(conditions.length == 0) throw new RuntimeException("conditions.legth must be >= 1");
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0; i < conditions.length; i++) {
            stringBuilder.append(conditions[i].toSparqlCondition())
                    .append("\n");
        }
        String newWhereClause = this.whereClause + stringBuilder.toString();
        return new QueryBuilderImpl(selectClause, newWhereClause, limit, offset);
    }

    public static class Keywords {
        public static String SELECT = " Select ";
        public static String LIMIT = " Limit ";
        public static String OFFSET = " Offset ";
        public static String DISTINCT = " distinct ";
        public static String WHERE = " Where ";
        public static String UNION = " Union ";
        public static String FILTER = " Filter ";
        public static String ORDER_BY = " Order By ";
    }

    private QueryBuilderImpl(
            String selectClause,
            String whereClause,
            Integer limit,
            Integer offset) {
        this.selectClause = selectClause;
        this.whereClause = whereClause;
        this.limit = limit;
        this.offset = offset;
    }

    private QueryBuilderImpl(
            String selectClause,
            String whereClause,
            Integer limit,
            Integer offset,
            String orderBy) {
        this.selectClause = selectClause;
        this.whereClause = whereClause;
        this.limit = limit;
        this.offset = offset;
        this.orderBy = orderBy;
    }

    public static QueryBuilderWhere select(String select) {
        return new QueryBuilderImpl(select, "", null, null);
    }

    @Override
    public QueryBuilderLimit limit(Integer limit) {
        return new QueryBuilderImpl(selectClause, whereClause, limit, offset, orderBy);
    }

    @Override
    public QueryBuilderLimit offset(Integer offset) {
        return new QueryBuilderImpl(selectClause, whereClause, limit, offset, orderBy);
    }

    @Override
    public QueryBuilderLimit orderAsc(String column) {
        if(orderBy == null) return new QueryBuilderImpl(selectClause, whereClause, limit, offset, column);
        else return new QueryBuilderImpl(selectClause, whereClause, limit, offset, orderBy + " " + column);
    }

    @Override
    public QueryBuilderLimit orderDesc(String column) {
        if(orderBy == null) return new QueryBuilderImpl(selectClause, whereClause, limit, offset, "DESC(" + column + ")");
        else return new QueryBuilderImpl(selectClause, whereClause, limit, offset, orderBy + " " +"DESC(" + column + ")");
    }

    @Override
    public String buildWithPrefix() {
        StringBuilder stringBuilder = new StringBuilder();

        SchemaLinker.get().namespace.forEach((prefix, uri) -> {

            stringBuilder.append("PREFIX ")
                    .append(prefix)
                    .append(": ")
                    .append("<")
                    .append(uri)
                    .append(">\n");

        });

        stringBuilder.append(build());
        return stringBuilder.toString();
    }

    public String build() {
        StringBuilder stringBuilder = new StringBuilder()
                .append(Keywords.SELECT).append(Keywords.DISTINCT).append(selectClause)
                .append(Keywords.WHERE).append("{").append(whereClause).append("}");

        if(orderBy != null) {
            stringBuilder.append(Keywords.ORDER_BY).append(orderBy).append("\n");
        }

        if(limit != null) {
            stringBuilder.append(Keywords.LIMIT).append(limit).append("\n");
        }

        if(offset != null) {
            stringBuilder.append(Keywords.OFFSET).append(offset).append("\n");
        }

        return stringBuilder.toString();
    }

}
