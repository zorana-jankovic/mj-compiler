// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class DesAssStmtm extends DesignatorAssignmentStatement {

    private Designator Designator;
    private DesAssStmtCombOperators DesAssStmtCombOperators;
    private Expr Expr;

    public DesAssStmtm (Designator Designator, DesAssStmtCombOperators DesAssStmtCombOperators, Expr Expr) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesAssStmtCombOperators=DesAssStmtCombOperators;
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
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
        if(Designator!=null) Designator.accept(visitor);
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesAssStmtCombOperators!=null) DesAssStmtCombOperators.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesAssStmtm(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
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
        buffer.append(") [DesAssStmtm]");
        return buffer.toString();
    }
}
