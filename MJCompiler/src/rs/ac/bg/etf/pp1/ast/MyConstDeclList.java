// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyConstDeclList extends ConstDeclList {

    private ConstDeclList ConstDeclList;
    private ConstDeclElem ConstDeclElem;

    public MyConstDeclList (ConstDeclList ConstDeclList, ConstDeclElem ConstDeclElem) {
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
        this.ConstDeclElem=ConstDeclElem;
        if(ConstDeclElem!=null) ConstDeclElem.setParent(this);
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public ConstDeclElem getConstDeclElem() {
        return ConstDeclElem;
    }

    public void setConstDeclElem(ConstDeclElem ConstDeclElem) {
        this.ConstDeclElem=ConstDeclElem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
        if(ConstDeclElem!=null) ConstDeclElem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
        if(ConstDeclElem!=null) ConstDeclElem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        if(ConstDeclElem!=null) ConstDeclElem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyConstDeclList(\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclElem!=null)
            buffer.append(ConstDeclElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyConstDeclList]");
        return buffer.toString();
    }
}
