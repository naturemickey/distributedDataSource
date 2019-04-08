package org.w01f.dds.layer2.sql.parser.mysql.tree;

public class ElementRowNode extends ElementOpFactoryNode implements Cloneable {

    private final ParamListNode paramList;

    @Override
    public ElementRowNode clone() {
        return new ElementRowNode(paramList == null ? null : paramList.clone());
    }

    public ElementRowNode(ParamListNode paramList) {
        this.paramList = paramList;

        super.setParent(paramList);
    }

    @Override
    public String toString() {
        return "row(" + paramList + ")";
    }

}
