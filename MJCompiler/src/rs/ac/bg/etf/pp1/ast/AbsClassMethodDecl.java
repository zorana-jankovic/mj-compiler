// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class AbsClassMethodDecl extends AbsMethDeclListElem {

    private MethodDecl1 MethodDecl1;

    public AbsClassMethodDecl (MethodDecl1 MethodDecl1) {
        this.MethodDecl1=MethodDecl1;
        if(MethodDecl1!=null) MethodDecl1.setParent(this);
    }

    public MethodDecl1 getMethodDecl1() {
        return MethodDecl1;
    }

    public void setMethodDecl1(MethodDecl1 MethodDecl1) {
        this.MethodDecl1=MethodDecl1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodDecl1!=null) MethodDecl1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodDecl1!=null) MethodDecl1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodDecl1!=null) MethodDecl1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AbsClassMethodDecl(\n");

        if(MethodDecl1!=null)
            buffer.append(MethodDecl1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AbsClassMethodDecl]");
        return buffer.toString();
    }
}
