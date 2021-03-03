// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyAbsMethDeclClassList extends AbsMethDeclClassList {

    private AbsMethDeclClassList AbsMethDeclClassList;
    private AbsMethDeclListElem AbsMethDeclListElem;

    public MyAbsMethDeclClassList (AbsMethDeclClassList AbsMethDeclClassList, AbsMethDeclListElem AbsMethDeclListElem) {
        this.AbsMethDeclClassList=AbsMethDeclClassList;
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.setParent(this);
        this.AbsMethDeclListElem=AbsMethDeclListElem;
        if(AbsMethDeclListElem!=null) AbsMethDeclListElem.setParent(this);
    }

    public AbsMethDeclClassList getAbsMethDeclClassList() {
        return AbsMethDeclClassList;
    }

    public void setAbsMethDeclClassList(AbsMethDeclClassList AbsMethDeclClassList) {
        this.AbsMethDeclClassList=AbsMethDeclClassList;
    }

    public AbsMethDeclListElem getAbsMethDeclListElem() {
        return AbsMethDeclListElem;
    }

    public void setAbsMethDeclListElem(AbsMethDeclListElem AbsMethDeclListElem) {
        this.AbsMethDeclListElem=AbsMethDeclListElem;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.accept(visitor);
        if(AbsMethDeclListElem!=null) AbsMethDeclListElem.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.traverseTopDown(visitor);
        if(AbsMethDeclListElem!=null) AbsMethDeclListElem.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.traverseBottomUp(visitor);
        if(AbsMethDeclListElem!=null) AbsMethDeclListElem.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyAbsMethDeclClassList(\n");

        if(AbsMethDeclClassList!=null)
            buffer.append(AbsMethDeclClassList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AbsMethDeclListElem!=null)
            buffer.append(AbsMethDeclListElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyAbsMethDeclClassList]");
        return buffer.toString();
    }
}
