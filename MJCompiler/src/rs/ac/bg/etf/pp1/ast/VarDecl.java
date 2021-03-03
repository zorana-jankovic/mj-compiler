// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private AccessRights AccessRights;
    private VarDeclTypeName VarDeclTypeName;
    private VarDeclElem VarDeclElem;
    private VarDeclList VarDeclList;

    public VarDecl (AccessRights AccessRights, VarDeclTypeName VarDeclTypeName, VarDeclElem VarDeclElem, VarDeclList VarDeclList) {
        this.AccessRights=AccessRights;
        if(AccessRights!=null) AccessRights.setParent(this);
        this.VarDeclTypeName=VarDeclTypeName;
        if(VarDeclTypeName!=null) VarDeclTypeName.setParent(this);
        this.VarDeclElem=VarDeclElem;
        if(VarDeclElem!=null) VarDeclElem.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public AccessRights getAccessRights() {
        return AccessRights;
    }

    public void setAccessRights(AccessRights AccessRights) {
        this.AccessRights=AccessRights;
    }

    public VarDeclTypeName getVarDeclTypeName() {
        return VarDeclTypeName;
    }

    public void setVarDeclTypeName(VarDeclTypeName VarDeclTypeName) {
        this.VarDeclTypeName=VarDeclTypeName;
    }

    public VarDeclElem getVarDeclElem() {
        return VarDeclElem;
    }

    public void setVarDeclElem(VarDeclElem VarDeclElem) {
        this.VarDeclElem=VarDeclElem;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
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
        if(AccessRights!=null) AccessRights.accept(visitor);
        if(VarDeclTypeName!=null) VarDeclTypeName.accept(visitor);
        if(VarDeclElem!=null) VarDeclElem.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AccessRights!=null) AccessRights.traverseTopDown(visitor);
        if(VarDeclTypeName!=null) VarDeclTypeName.traverseTopDown(visitor);
        if(VarDeclElem!=null) VarDeclElem.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AccessRights!=null) AccessRights.traverseBottomUp(visitor);
        if(VarDeclTypeName!=null) VarDeclTypeName.traverseBottomUp(visitor);
        if(VarDeclElem!=null) VarDeclElem.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        if(AccessRights!=null)
            buffer.append(AccessRights.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclTypeName!=null)
            buffer.append(VarDeclTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclElem!=null)
            buffer.append(VarDeclElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
