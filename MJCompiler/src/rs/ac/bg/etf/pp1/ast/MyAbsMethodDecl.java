// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyAbsMethodDecl extends AbsMethodDecl {

    private AccessRights AccessRights;
    private AbsMethodSignification AbsMethodSignification;
    private MethodTypeName MethodTypeName;
    private FormPars FormPars;

    public MyAbsMethodDecl (AccessRights AccessRights, AbsMethodSignification AbsMethodSignification, MethodTypeName MethodTypeName, FormPars FormPars) {
        this.AccessRights=AccessRights;
        if(AccessRights!=null) AccessRights.setParent(this);
        this.AbsMethodSignification=AbsMethodSignification;
        if(AbsMethodSignification!=null) AbsMethodSignification.setParent(this);
        this.MethodTypeName=MethodTypeName;
        if(MethodTypeName!=null) MethodTypeName.setParent(this);
        this.FormPars=FormPars;
        if(FormPars!=null) FormPars.setParent(this);
    }

    public AccessRights getAccessRights() {
        return AccessRights;
    }

    public void setAccessRights(AccessRights AccessRights) {
        this.AccessRights=AccessRights;
    }

    public AbsMethodSignification getAbsMethodSignification() {
        return AbsMethodSignification;
    }

    public void setAbsMethodSignification(AbsMethodSignification AbsMethodSignification) {
        this.AbsMethodSignification=AbsMethodSignification;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AccessRights!=null) AccessRights.accept(visitor);
        if(AbsMethodSignification!=null) AbsMethodSignification.accept(visitor);
        if(MethodTypeName!=null) MethodTypeName.accept(visitor);
        if(FormPars!=null) FormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AccessRights!=null) AccessRights.traverseTopDown(visitor);
        if(AbsMethodSignification!=null) AbsMethodSignification.traverseTopDown(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseTopDown(visitor);
        if(FormPars!=null) FormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AccessRights!=null) AccessRights.traverseBottomUp(visitor);
        if(AbsMethodSignification!=null) AbsMethodSignification.traverseBottomUp(visitor);
        if(MethodTypeName!=null) MethodTypeName.traverseBottomUp(visitor);
        if(FormPars!=null) FormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyAbsMethodDecl(\n");

        if(AccessRights!=null)
            buffer.append(AccessRights.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AbsMethodSignification!=null)
            buffer.append(AbsMethodSignification.toString("  "+tab));
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

        buffer.append(tab);
        buffer.append(") [MyAbsMethodDecl]");
        return buffer.toString();
    }
}
