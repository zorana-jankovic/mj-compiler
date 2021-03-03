// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl extends MethodDecl1 {

    private AccessRights AccessRights;
    private MethodTypeName MethodTypeName;
    private FormPars FormPars;
    private DeclListVar DeclListVar;
    private StatementList StatementList;

    public MethodDecl (AccessRights AccessRights, MethodTypeName MethodTypeName, FormPars FormPars, DeclListVar DeclListVar, StatementList StatementList) {
        this.AccessRights=AccessRights;
        if(AccessRights!=null) AccessRights.setParent(this);
        this.MethodTypeName=MethodTypeName;
        if(MethodTypeName!=null) MethodTypeName.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
        this.DeclListVar=DeclListVar;
        if(DeclListVar!=null) DeclListVar.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public AccessRights getAccessRights() {
        return AccessRights;
    }

    public void setAccessRights(AccessRights AccessRights) {
        this.AccessRights=AccessRights;
    }

    public MethodTypeName getMethodTypeName() {
        return MethodTypeName;
    }

    public void setMethodTypeName(MethodTypeName MethodTypeName) {
        this.MethodTypeName=MethodTypeName;
    }

    public FormPars getFormPars() {
        return FormPars;
    }

    public void setFormPars(FormPars FormPars) {
        this.FormPars=FormPars;
    }

    public DeclListVar getDeclListVar() {
        return DeclListVar;
    }

    public void setDeclListVar(DeclListVar DeclListVar) {
        this.DeclListVar=DeclListVar;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AccessRights!=null) AccessRights.accept(visitor);
        if(MethodTypeName!=null) MethodTypeName.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
        if(DeclListVar!=null) DeclListVar.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AccessRights!=null) AccessRights.traverseTopDown(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
        if(DeclListVar!=null) DeclListVar.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AccessRights!=null) AccessRights.traverseBottomUp(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        if(DeclListVar!=null) DeclListVar.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(AccessRights!=null)
            buffer.append(AccessRights.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodTypeName!=null)
            buffer.append(MethodTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormPars!=null)
            buffer.append(FormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclListVar!=null)
            buffer.append(DeclListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
