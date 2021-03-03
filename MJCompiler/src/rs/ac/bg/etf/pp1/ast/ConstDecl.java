// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDeclTypeName ConstDeclTypeName;
    private ConstDeclElem ConstDeclElem;
    private ConstDeclList ConstDeclList;

    public ConstDecl (ConstDeclTypeName ConstDeclTypeName, ConstDeclElem ConstDeclElem, ConstDeclList ConstDeclList) {
        this.ConstDeclTypeName=ConstDeclTypeName;
        if(ConstDeclTypeName!=null) ConstDeclTypeName.setParent(this);
        this.ConstDeclElem=ConstDeclElem;
        if(ConstDeclElem!=null) ConstDeclElem.setParent(this);
        this.ConstDeclList=ConstDeclList;
        if(ConstDeclList!=null) ConstDeclList.setParent(this);
    }

    public ConstDeclTypeName getConstDeclTypeName() {
        return ConstDeclTypeName;
    }

    public void setConstDeclTypeName(ConstDeclTypeName ConstDeclTypeName) {
        this.ConstDeclTypeName=ConstDeclTypeName;
    }

    public ConstDeclElem getConstDeclElem() {
        return ConstDeclElem;
    }

    public void setConstDeclElem(ConstDeclElem ConstDeclElem) {
        this.ConstDeclElem=ConstDeclElem;
    }

    public ConstDeclList getConstDeclList() {
        return ConstDeclList;
    }

    public void setConstDeclList(ConstDeclList ConstDeclList) {
        this.ConstDeclList=ConstDeclList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclTypeName!=null) ConstDeclTypeName.accept(visitor);
        if(ConstDeclElem!=null) ConstDeclElem.accept(visitor);
        if(ConstDeclList!=null) ConstDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclTypeName!=null) ConstDeclTypeName.traverseTopDown(visitor);
        if(ConstDeclElem!=null) ConstDeclElem.traverseTopDown(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclTypeName!=null) ConstDeclTypeName.traverseBottomUp(visitor);
        if(ConstDeclElem!=null) ConstDeclElem.traverseBottomUp(visitor);
        if(ConstDeclList!=null) ConstDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(ConstDeclTypeName!=null)
            buffer.append(ConstDeclTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclElem!=null)
            buffer.append(ConstDeclElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclList!=null)
            buffer.append(ConstDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
