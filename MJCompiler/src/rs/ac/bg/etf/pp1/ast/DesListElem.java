// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class DesListElem extends Expr {

    private DesVar DesVar;
    private DesAssStmtCombOperators DesAssStmtCombOperators;
    private Expr Expr;

    public DesListElem (DesVar DesVar, DesAssStmtCombOperators DesAssStmtCombOperators, Expr Expr) {
        this.DesVar=DesVar;
        if(DesVar!=null) DesVar.setParent(this);
        this.DesAssStmtCombOperators=DesAssStmtCombOperators;
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public DesVar getDesVar() {
        return DesVar;
    }

    public void setDesVar(DesVar DesVar) {
        this.DesVar=DesVar;
    }

    public DesAssStmtCombOperators getDesAssStmtCombOperators() {
        return DesAssStmtCombOperators;
    }

    public void setDesAssStmtCombOperators(DesAssStmtCombOperators DesAssStmtCombOperators) {
        this.DesAssStmtCombOperators=DesAssStmtCombOperators;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesVar!=null) DesVar.accept(visitor);
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesVar!=null) DesVar.traverseTopDown(visitor);
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesVar!=null) DesVar.traverseBottomUp(visitor);
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesListElem(\n");

        if(DesVar!=null)
            buffer.append(DesVar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesAssStmtCombOperators!=null)
            buffer.append(DesAssStmtCombOperators.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesListElem]");
        return buffer.toString();
    }
}
