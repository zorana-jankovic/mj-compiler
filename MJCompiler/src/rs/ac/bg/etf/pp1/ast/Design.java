// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class Design extends Designator {

    private DesignatorIdent DesignatorIdent;
    private DesignatorElemList DesignatorElemList;

    public Design (DesignatorIdent DesignatorIdent, DesignatorElemList DesignatorElemList) {
        this.DesignatorIdent=DesignatorIdent;
        if(DesignatorIdent!=null) DesignatorIdent.setParent(this);
        this.DesignatorElemList=DesignatorElemList;
        if(DesignatorElemList!=null) DesignatorElemList.setParent(this);
    }

    public DesignatorIdent getDesignatorIdent() {
        return DesignatorIdent;
    }

    public void setDesignatorIdent(DesignatorIdent DesignatorIdent) {
        this.DesignatorIdent=DesignatorIdent;
    }

    public DesignatorElemList getDesignatorElemList() {
        return DesignatorElemList;
    }

    public void setDesignatorElemList(DesignatorElemList DesignatorElemList) {
        this.DesignatorElemList=DesignatorElemList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorIdent!=null) DesignatorIdent.accept(visitor);
        if(DesignatorElemList!=null) DesignatorElemList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorIdent!=null) DesignatorIdent.traverseTopDown(visitor);
        if(DesignatorElemList!=null) DesignatorElemList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorIdent!=null) DesignatorIdent.traverseBottomUp(visitor);
        if(DesignatorElemList!=null) DesignatorElemList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Design(\n");

        if(DesignatorIdent!=null)
            buffer.append(DesignatorIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorElemList!=null)
            buffer.append(DesignatorElemList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Design]");
        return buffer.toString();
    }
}
