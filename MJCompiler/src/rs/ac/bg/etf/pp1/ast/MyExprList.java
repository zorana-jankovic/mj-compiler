// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyExprList extends ExprList {

    private ExprListElem ExprListElem;
    private ExprList ExprList;

    public MyExprList (ExprListElem ExprListElem, ExprList ExprList) {
        this.ExprListElem=ExprListElem;
        if(ExprListElem!=null) ExprListElem.setParent(this);
        this.ExprList=ExprList;
        if(ExprList!=null) ExprList.setParent(this);
    }

    public ExprListElem getExprListElem() {
        return ExprListElem;
    }

    public void setExprListElem(ExprListElem ExprListElem) {
        this.ExprListElem=ExprListElem;
    }

    public ExprList getExprList() {
        return ExprList;
    }

    public void setExprList(ExprList ExprList) {
        this.ExprList=ExprList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprListElem!=null) ExprListElem.accept(visitor);
        if(ExprList!=null) ExprList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprListElem!=null) ExprListElem.traverseTopDown(visitor);
        if(ExprList!=null) ExprList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprListElem!=null) ExprListElem.traverseBottomUp(visitor);
        if(ExprList!=null) ExprList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyExprList(\n");

        if(ExprListElem!=null)
            buffer.append(ExprListElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprList!=null)
            buffer.append(ExprList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyExprList]");
        return buffer.toString();
    }
}
