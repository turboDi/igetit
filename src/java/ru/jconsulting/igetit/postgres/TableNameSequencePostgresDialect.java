package ru.jconsulting.igetit.postgres;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.SequenceGenerator;
import org.hibernate.type.Type;

import java.util.Properties;

/**
 * @author Dmitriy Borisov
 * @created 25.05.14 11:16
 */
public class TableNameSequencePostgresDialect extends PostgreSQLDialect {

    /**
     * Get the native identifier generator class.
     * @return TableNameSequenceGenerator.
     */
    @Override
    public Class<?> getNativeIdentifierGeneratorClass() {
        return TableNameSequenceGenerator.class;
    }

    /**
     * Creates a sequence per table instead of the default behavior of one sequence.
     */
    public static class TableNameSequenceGenerator extends SequenceGenerator {

        /**
         * {@inheritDoc}
         * If the parameters do not contain a {@link SequenceGenerator#SEQUENCE} name, we
         * assign one based on the table name.
         */
        @Override
        public void configure(final Type type, final Properties params, final Dialect dialect) {
            if (params.getProperty(SEQUENCE) == null || params.getProperty(SEQUENCE).length() == 0) {
                String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
                if (tableName != null) {
                    params.setProperty(SEQUENCE, "seq_" + tableName);
                }
            }
            super.configure(type, params, dialect);
        }
    }
}
