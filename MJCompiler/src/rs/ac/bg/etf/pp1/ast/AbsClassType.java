// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class AbsClassType implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String absClassName;

    public AbsClassType (String absClassName) {
        this.absClassName=absClassName;
    }

    public String getAbsClassName() {
        return absClassName;
    }

    public void setAbsClassName(String absClassName) {
        this.absClassName=absClassName;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbsClassType(\n");

        buffer.append(" "+tab+absClassName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbsClassType]");
        return buffer.toString();
    }
}
