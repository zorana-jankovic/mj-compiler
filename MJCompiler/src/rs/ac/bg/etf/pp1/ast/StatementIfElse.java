// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class StatementIfElse extends Statement {

    private BeginingIf BeginingIf;
    private CondIf CondIf;
    private StmtIf StmtIf;
    private Statement Statement;

    public StatementIfElse (BeginingIf BeginingIf, CondIf CondIf, StmtIf StmtIf, Statement Statement) {
        this.BeginingIf=BeginingIf;
        if(BeginingIf!=null) BeginingIf.setParent(this);
        this.CondIf=CondIf;
        if(CondIf!=null) CondIf.setParent(this);
        this.StmtIf=StmtIf;
        if(StmtIf!=null) StmtIf.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public BeginingIf getBeginingIf() {
        return BeginingIf;
    }

    public void setBeginingIf(BeginingIf BeginingIf) {
        this.BeginingIf=BeginingIf;
    }

    public CondIf getCondIf() {
        return CondIf;
    }

    public void setCondIf(CondIf CondIf) {
        this.CondIf=CondIf;
    }

    public StmtIf getStmtIf() {
        return StmtIf;
    }

    public void setStmtIf(StmtIf StmtIf) {
        this.StmtIf=StmtIf;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BeginingIf!=null) BeginingIf.accept(visitor);
        if(CondIf!=null) CondIf.accept(visitor);
        if(StmtIf!=null) StmtIf.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BeginingIf!=null) BeginingIf.traverseTopDown(visitor);
        if(CondIf!=null) CondIf.traverseTopDown(visitor);
        if(StmtIf!=null) StmtIf.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BeginingIf!=null) BeginingIf.traverseBottomUp(visitor);
        if(CondIf!=null) CondIf.traverseBottomUp(visitor);
        if(StmtIf!=null) StmtIf.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIfElse(\n");

        if(BeginingIf!=null)
            buffer.append(BeginingIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondIf!=null)
            buffer.append(CondIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StmtIf!=null)
            buffer.append(StmtIf.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementIfElse]");
        return buffer.toString();
    }
}
