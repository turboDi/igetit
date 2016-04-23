package ru.jconsulting.igetit.postgres;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.TypedValue;

/**
 * @author Dmitriy Borisov
 * @created 4/23/2016
 */
public class TextSearchExpression implements Criterion {

    private final String propertyName;
    private final Object value;

    public TextSearchExpression(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        Dialect dialect = criteriaQuery.getFactory().getDialect();
        String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, propertyName);
        if (columns.length != 1) {
            throw new HibernateException("tsSearch may only be used with single-column properties");
        }

        if (dialect instanceof PostgreSQLDialect) {
            return columns[0] + "_fts @@ to_tsquery('russian', ?)";
        }

        return columns[0] + " like ?";
    }

    @Override
    public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
        Dialect dialect = criteriaQuery.getFactory().getDialect();

        String searchExpression = value.toString().toLowerCase();
        if (dialect instanceof PostgreSQLDialect) {
            searchExpression = searchExpression.replace(' ', '&');
        } else {
            searchExpression = '%' + searchExpression + '%';
        }

        return new TypedValue[] { criteriaQuery.getTypedValue(criteria, propertyName, searchExpression) };
    }

    @Override
    public String toString() {
        return propertyName + " textSearch " + value;
    }
}
