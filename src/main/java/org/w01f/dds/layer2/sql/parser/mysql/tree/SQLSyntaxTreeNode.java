package org.w01f.dds.layer2.sql.parser.mysql.tree;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public abstract class SQLSyntaxTreeNode implements Cloneable {

    protected SQLSyntaxTreeNode parent;
    protected final List<SQLSyntaxTreeNode> children = new ArrayList<>();

    protected List<ElementPlaceholderNode> placeholderNodes = null;

    public final List<ElementPlaceholderNode> getPlaceholderNodes() {
        if (placeholderNodes == null) {
            placeholderNodes = new ArrayList<>();

            for (SQLSyntaxTreeNode node : children) {
                if (node != null) {
                    if (node instanceof ElementPlaceholderNode) {
                        placeholderNodes.add((ElementPlaceholderNode) node);
                    } else {
                        placeholderNodes.addAll(node.getPlaceholderNodes());
                    }
                }
            }
        }
        return placeholderNodes;
    }

    @Override
    public SQLSyntaxTreeNode clone() {
        try {
            return (SQLSyntaxTreeNode) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public final void setParent(SQLSyntaxTreeNode... nodes) {
        for (SQLSyntaxTreeNode node : nodes) {
            if (node != null) {
                node.parent = this;
                this.children.add(node);
            }
        }
    }

    public final <T extends SQLSyntaxTreeNode> void setParent(List<T> nodes) {
        if (nodes == null) {
            return;
        }
        for (T node : nodes) {
            if (node != null) {
                node.parent = this;
                this.children.add(node);
            }
        }
    }

    public final String structure() {
        final StringBuilder sb = new StringBuilder();
        for (String s : structureProtected()) {
            sb.append(s).append('\n');
        }
        return sb.toString();
    }

    protected List<String> structureProtected() {
        final List<String> lines = new ArrayList<>();
        final List<String> lines2 = fieldLines();
        if (!lines2.isEmpty()) {
            lines.add("{");
            lines.addAll(lines2);
            lines.add("}");
        }

        return lines;
    }

    private List<String> fieldLines() {
        final List<String> lines = new ArrayList<>();
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    Object value = field.get(this);

                    final StringBuilder sb = new StringBuilder();
                    sb.append("  ").append(field.getName()).append(" : ");

                    if (value instanceof SQLSyntaxTreeNode) {
                        final List<String> strings = ((SQLSyntaxTreeNode) value).structureProtected();


                        for (int i = 0; i < strings.size(); i++) {
                            if (i == 0) {
                                lines.add(sb.toString() + strings.get(i));
                            } else {
                                lines.add("  " + strings.get(i));
                            }
                        }
                    } else if (value instanceof List) {
                        final List lines2 = structureList((List) value);

                        for (int i = 0; i < lines2.size(); i++) {
                            if (i == 0) {
                                sb.append(lines2.get(i));
                                lines.add(sb.toString());
                            } else {
                                lines.add("  " + lines2.get(i));
                            }
                        }
                    } else if (value instanceof Boolean) {
                        if (((Boolean) value) == true) {
                            sb.append(value);

                            lines.add(sb.toString());
                        }
                    } else if (value != null) {
                        sb.append(value);

                        lines.add(sb.toString());
                    }

                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    private List<String> structureList(List<Object> list) {
        List<String> lines = new ArrayList<>();
        lines.add("[");
        for (Object o : list) {
            if (o instanceof SQLSyntaxTreeNode) {
                appendLines(lines, ((SQLSyntaxTreeNode) o).structureProtected(), "  ");
            } else if (o instanceof List) {
                appendLines(lines, structureList((List) o), "  ");
            } else {
                lines.add(o.toString());
            }
        }
        lines.add("]");
        return lines;
    }

    private void appendLines(List<String> lines, List<String> lines2, String tab) {
        for (String s : lines2) {
            lines.add(tab + s);
        }
    }
}
