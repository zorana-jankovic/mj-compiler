// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyAbstractClassDecl extends AbstractClassDecl {

    private AbsClassType AbsClassType;
    private ExtendsClass ExtendsClass;
    private DeclListVar DeclListVar;
    private AbsMethDeclClass AbsMethDeclClass;

    public MyAbstractClassDecl (AbsClassType AbsClassType, ExtendsClass ExtendsClass, DeclListVar DeclListVar, AbsMethDeclClass AbsMethDeclClass) {
        this.AbsClassType=AbsClassType;
        if(AbsClassType!=null) AbsClassType.setParent(this);
        this.ExtendsClass=ExtendsClass;
        if(ExtendsClass!=null) ExtendsClass.setParent(this);
        this.DeclListVar=DeclListVar;
        if(DeclListVar!=null) DeclListVar.setParent(this);
        this.AbsMethDeclClass=AbsMethDeclClass;
        if(AbsMethDeclClass!=null) AbsMethDeclClass.setParent(this);
    }

    public AbsClassType getAbsClassType() {
        return AbsClassType;
    }

    public void setAbsClassType(AbsClassType AbsClassType) {
        this.AbsClassType=AbsClassType;
    }

    public ExtendsClass getExtendsClass() {
        return ExtendsClass;
    }

    public void setExtendsClass(ExtendsClass ExtendsClass) {
        this.ExtendsClass=ExtendsClass;
    }

    public DeclListVar getDeclListVar() {
        return DeclListVar;
    }

    public void setDeclListVar(DeclListVar DeclListVar) {
        this.DeclListVar=DeclListVar;
    }

    public AbsMethDeclClass getAbsMethDeclClass() {
        return AbsMethDeclClass;
    }

    public void setAbsMethDeclClass(AbsMethDeclClass AbsMethDeclClass) {
        this.AbsMethDeclClass=AbsMethDeclClass;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AbsClassType!=null) AbsClassType.accept(visitor);
        if(ExtendsClass!=null) ExtendsClass.accept(visitor);
        if(DeclListVar!=null) DeclListVar.accept(visitor);
        if(AbsMethDeclClass!=null) AbsMethDeclClass.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AbsClassType!=null) AbsClassType.traverseTopDown(visitor);
        if(ExtendsClass!=null) ExtendsClass.traverseTopDown(visitor);
        if(DeclListVar!=null) DeclListVar.traverseTopDown(visitor);
        if(AbsMethDeclClass!=null) AbsMethDeclClass.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AbsClassType!=null) AbsClassType.traverseBottomUp(visitor);
        if(ExtendsClass!=null) ExtendsClass.traverseBottomUp(visitor);
        if(DeclListVar!=null) DeclListVar.traverseBottomUp(visitor);
        if(AbsMethDeclClass!=null) AbsMethDeclClass.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyAbstractClassDecl(\n");

        if(AbsClassType!=null)
            buffer.append(AbsClassType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendsClass!=null)
            buffer.append(ExtendsClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DeclListVar!=null)
            buffer.append(DeclListVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AbsMethDeclClass!=null)
            buffer.append(AbsMethDeclClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyAbstractClassDecl]");
        return buffer.toString();
    }
}
