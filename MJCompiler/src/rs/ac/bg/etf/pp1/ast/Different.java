// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class Different extends Relop {

    public Different () {
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
        buffer.append("Different(\n");

        buffer.append(tab);
        buffer.append(") [Different]");
        return buffer.toString();
    }
}
