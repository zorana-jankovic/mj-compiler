// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class NoRights extends AccessRights {

    public NoRights () {
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
        buffer.append("NoRights(\n");

        buffer.append(tab);
        buffer.append(") [NoRights]");
        return buffer.toString();
    }
}
