// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyAbsMethDeclClass extends AbsMethDeclClass {

    private AbsMethDeclClassList AbsMethDeclClassList;

    public MyAbsMethDeclClass (AbsMethDeclClassList AbsMethDeclClassList) {
        this.AbsMethDeclClassList=AbsMethDeclClassList;
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.setParent(this);
    }

    public AbsMethDeclClassList getAbsMethDeclClassList() {
        return AbsMethDeclClassList;
    }

    public void setAbsMethDeclClassList(AbsMethDeclClassList AbsMethDeclClassList) {
        this.AbsMethDeclClassList=AbsMethDeclClassList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbsMethDeclClassList!=null) AbsMethDeclClassList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyAbsMethDeclClass(\n");

        if(AbsMethDeclClassList!=null)
            buffer.append(AbsMethDeclClassList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyAbsMethDeclClass]");
        return buffer.toString();
    }
}
