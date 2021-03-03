// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementFuncCall extends DesignatorStatement {

    private DesStmtFuncCallDesignator DesStmtFuncCallDesignator;
    private ActPars ActPars;

    public DesignatorStatementFuncCall (DesStmtFuncCallDesignator DesStmtFuncCallDesignator, ActPars ActPars) {
        this.DesStmtFuncCallDesignator=DesStmtFuncCallDesignator;
        if(DesStmtFuncCallDesignator!=null) DesStmtFuncCallDesignator.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public DesStmtFuncCallDesignator getDesStmtFuncCallDesignator() {
        return DesStmtFuncCallDesignator;
    }

    public void setDesStmtFuncCallDesignator(DesStmtFuncCallDesignator DesStmtFuncCallDesignator) {
        this.DesStmtFuncCallDesignator=DesStmtFuncCallDesignator;
    }

    public ActPars getActPars() {
        return ActPars;
    }

    public void setActPars(ActPars ActPars) {
        this.ActPars=ActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesStmtFuncCallDesignator!=null) DesStmtFuncCallDesignator.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesStmtFuncCallDesignator!=null) DesStmtFuncCallDesignator.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesStmtFuncCallDesignator!=null) DesStmtFuncCallDesignator.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementFuncCall(\n");

        if(DesStmtFuncCallDesignator!=null)
            buffer.append(DesStmtFuncCallDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementFuncCall]");
        return buffer.toString();
    }
}
