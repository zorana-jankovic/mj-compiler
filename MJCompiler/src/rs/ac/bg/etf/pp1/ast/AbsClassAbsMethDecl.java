// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class AbsClassAbsMethDecl extends AbsMethDeclListElem {

    private AbsMethodDecl AbsMethodDecl;

    public AbsClassAbsMethDecl (AbsMethodDecl AbsMethodDecl) {
        this.AbsMethodDecl=AbsMethodDecl;
        if(AbsMethodDecl!=null) AbsMethodDecl.setParent(this);
    }

    public AbsMethodDecl getAbsMethodDecl() {
        return AbsMethodDecl;
    }

    public void setAbsMethodDecl(AbsMethodDecl AbsMethodDecl) {
        this.AbsMethodDecl=AbsMethodDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbsMethodDecl!=null) AbsMethodDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbsMethodDecl!=null) AbsMethodDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbsMethodDecl!=null) AbsMethodDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbsClassAbsMethDecl(\n");

        if(AbsMethodDecl!=null)
            buffer.append(AbsMethodDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbsClassAbsMethDecl]");
        return buffer.toString();
    }
}
