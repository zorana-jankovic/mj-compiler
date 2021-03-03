// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyDesignatorElemList extends DesignatorElemList {

    private DesignatorElem DesignatorElem;
    private DesignatorElemList DesignatorElemList;

    public MyDesignatorElemList (DesignatorElem DesignatorElem, DesignatorElemList DesignatorElemList) {
        this.DesignatorElem=DesignatorElem;
        if(DesignatorElem!=null) DesignatorElem.setParent(this);
        this.DesignatorElemList=DesignatorElemList;
        if(DesignatorElemList!=null) DesignatorElemList.setParent(this);
    }

    public DesignatorElem getDesignatorElem() {
        return DesignatorElem;
    }

    public void setDesignatorElem(DesignatorElem DesignatorElem) {
        this.DesignatorElem=DesignatorElem;
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
        if(DesignatorElem!=null) DesignatorElem.accept(visitor);
        if(DesignatorElemList!=null) DesignatorElemList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorElem!=null) DesignatorElem.traverseTopDown(visitor);
        if(DesignatorElemList!=null) DesignatorElemList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorElem!=null) DesignatorElem.traverseBottomUp(visitor);
        if(DesignatorElemList!=null) DesignatorElemList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyDesignatorElemList(\n");

        if(DesignatorElem!=null)
            buffer.append(DesignatorElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorElemList!=null)
            buffer.append(DesignatorElemList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyDesignatorElemList]");
        return buffer.toString();
    }
}
