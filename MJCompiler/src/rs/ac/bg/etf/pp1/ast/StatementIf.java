// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class StatementIf extends Statement {

    private BeginingIf BeginingIf;
    private CondIf CondIf;
    private StmtIf StmtIf;

    public StatementIf (BeginingIf BeginingIf, CondIf CondIf, StmtIf StmtIf) {
        this.BeginingIf=BeginingIf;
        if(BeginingIf!=null) BeginingIf.setParent(this);
        this.CondIf=CondIf;
        if(CondIf!=null) CondIf.setParent(this);
        this.StmtIf=StmtIf;
        if(StmtIf!=null) StmtIf.setParent(this);
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BeginingIf!=null) BeginingIf.accept(visitor);
        if(CondIf!=null) CondIf.accept(visitor);
        if(StmtIf!=null) StmtIf.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BeginingIf!=null) BeginingIf.traverseTopDown(visitor);
        if(CondIf!=null) CondIf.traverseTopDown(visitor);
        if(StmtIf!=null) StmtIf.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BeginingIf!=null) BeginingIf.traverseBottomUp(visitor);
        if(CondIf!=null) CondIf.traverseBottomUp(visitor);
        if(StmtIf!=null) StmtIf.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementIf(\n");

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

        buffer.append(tab);
        buffer.append(") [StatementIf]");
        return buffer.toString();
    }
}
