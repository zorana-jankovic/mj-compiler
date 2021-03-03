// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyClassDecl extends ClassDecl {

    private ClassType ClassType;
    private ExtendsClass ExtendsClass;
    private DeclListVar DeclListVar;
    private MethodDeclClass MethodDeclClass;

    public MyClassDecl (ClassType ClassType, ExtendsClass ExtendsClass, DeclListVar DeclListVar, MethodDeclClass MethodDeclClass) {
        this.ClassType=ClassType;
        if(ClassType!=null) ClassType.setParent(this);
        this.ExtendsClass=ExtendsClass;
        if(ExtendsClass!=null) ExtendsClass.setParent(this);
        this.DeclListVar=DeclListVar;
        if(DeclListVar!=null) DeclListVar.setParent(this);
        this.MethodDeclClass=MethodDeclClass;
        if(MethodDeclClass!=null) MethodDeclClass.setParent(this);
    }

    public ClassType getClassType() {
        return ClassType;
    }

    public void setClassType(ClassType ClassType) {
        this.ClassType=ClassType;
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

    public MethodDeclClass getMethodDeclClass() {
        return MethodDeclClass;
    }

    public void setMethodDeclClass(MethodDeclClass MethodDeclClass) {
        this.MethodDeclClass=MethodDeclClass;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassType!=null) ClassType.accept(visitor);
        if(ExtendsClass!=null) ExtendsClass.accept(visitor);
        if(DeclListVar!=null) DeclListVar.accept(visitor);
        if(MethodDeclClass!=null) MethodDeclClass.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassType!=null) ClassType.traverseTopDown(visitor);
        if(ExtendsClass!=null) ExtendsClass.traverseTopDown(visitor);
        if(DeclListVar!=null) DeclListVar.traverseTopDown(visitor);
        if(MethodDeclClass!=null) MethodDeclClass.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassType!=null) ClassType.traverseBottomUp(visitor);
        if(ExtendsClass!=null) ExtendsClass.traverseBottomUp(visitor);
        if(DeclListVar!=null) DeclListVar.traverseBottomUp(visitor);
        if(MethodDeclClass!=null) MethodDeclClass.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyClassDecl(\n");

        if(ClassType!=null)
            buffer.append(ClassType.toString("  "+tab));
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

        if(MethodDeclClass!=null)
            buffer.append(MethodDeclClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyClassDecl]");
        return buffer.toString();
    }
}
