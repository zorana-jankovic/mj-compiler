// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyDeclList extends DeclList {

    private DeclList DeclList;
    private DeclListElem DeclListElem;

    public MyDeclList (DeclList DeclList, DeclListElem DeclListElem) {
        this.DeclList=DeclList;
        if(DeclList!=null) DeclList.setParent(this);
        this.DeclListElem=DeclListElem;
        if(DeclListElem!=null) DeclListElem.setParent(this);
    }

    public DeclList getDeclList() {
        return DeclList;
    }

    public void setDeclList(DeclList DeclList) {
        this.DeclList=DeclList;
    }

    public DeclListElem getDeclListElem() {
        return DeclListElem;
    }

    public void setDeclListElem(DeclListElem DeclListElem) {
        this.DeclListElem=DeclListElem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DeclList!=null) DeclList.accept(visitor);
        if(DeclListElem!=null) DeclListElem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DeclList!=null) DeclList.traverseTopDown(visitor);
        if(DeclListElem!=null) DeclListElem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DeclList!=null) DeclList.traverseBottomUp(visitor);
        if(DeclListElem!=null) DeclListElem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyDeclList(\n");

        if(DeclList!=null)
            buffer.append(DeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclListElem!=null)
            buffer.append(DeclListElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyDeclList]");
        return buffer.toString();
    }
}
