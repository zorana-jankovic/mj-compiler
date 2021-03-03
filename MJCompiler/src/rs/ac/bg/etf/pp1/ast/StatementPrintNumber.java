// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class StatementPrintNumber extends Statement {

    private Expr Expr;
    private Integer numConstant;

    public StatementPrintNumber (Expr Expr, Integer numConstant) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.numConstant=numConstant;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public Integer getNumConstant() {
        return numConstant;
    }

    public void setNumConstant(Integer numConstant) {
        this.numConstant=numConstant;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementPrintNumber(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+numConstant);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementPrintNumber]");
        return buffer.toString();
    }
}
