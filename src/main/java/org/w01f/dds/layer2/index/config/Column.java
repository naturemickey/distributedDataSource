package org.w01f.dds.layer2.index.config;

public class Column {

    public enum Type {
        VARCHAR1000("varchar", 'v', 1000),
        DATATIME("datatime", 'd'),
        NUMERIC20("numeric", 'l', 20, 0);

        private int size = -1, precision = -1;
        private String sqlType;
        private String simpleName;
        private String fullName;

        Type(String sqlType, char simpleName) {
            this.sqlType = sqlType;
            this.simpleName = "" + simpleName;
            this.fullName = sqlType;
        }

        Type(String sqlType, char simpleName, int size) {
            this(sqlType, simpleName);
            this.size = size;
            this.fullName = sqlType + '(' + size + ')';
        }

        Type(String sqlType, char simpleName, int size, int precision) {
            this(sqlType, simpleName, size);
            this.precision = precision;
            this.fullName = sqlType + '(' + size + ", " + precision + ')';
        }

        public String getFullName() {
            return fullName;
        }

        public int getSize() {
            return size;
        }

        public int getPrecision() {
            return precision;
        }

        public String getSqlType() {
            return sqlType;
        }

        public String getSimpleName() {
            return simpleName;
        }
    }

    private String name;
    private Type type;

    public Column(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
