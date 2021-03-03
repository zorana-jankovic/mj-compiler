// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class StatementFor extends Statement {

    private DesignatorForBegin DesignatorForBegin;
    private ConditionFor ConditionFor;
    private DesignatorStatementFor DesignatorStatementFor;
    private ForBody ForBody;

    public StatementFor (DesignatorForBegin DesignatorForBegin, ConditionFor ConditionFor, DesignatorStatementFor DesignatorStatementFor, ForBody ForBody) {
        this.DesignatorForBegin=DesignatorForBegin;
        if(DesignatorForBegin!=null) DesignatorForBegin.setParent(this);
        this.ConditionFor=ConditionFor;
        if(ConditionFor!=null) ConditionFor.setParent(this);
        this.DesignatorStatementFor=DesignatorStatementFor;
        if(DesignatorStatementFor!=null) DesignatorStatementFor.setParent(this);
        this.ForBody=ForBody;
        if(ForBody!=null) ForBody.setParent(this);
    }

    public DesignatorForBegin getDesignatorForBegin() {
        return DesignatorForBegin;
    }

    public void setDesignatorForBegin(DesignatorForBegin DesignatorForBegin) {
        this.DesignatorForBegin=DesignatorForBegin;
    }

    public ConditionFor getConditionFor() {
        return ConditionFor;
    }

    public void setConditionFor(ConditionFor ConditionFor) {
        this.ConditionFor=ConditionFor;
    }

    public DesignatorStatementFor getDesignatorStatementFor() {
        return DesignatorStatementFor;
    }

    public void setDesignatorStatementFor(DesignatorStatementFor DesignatorStatementFor) {
        this.DesignatorStatementFor=DesignatorStatementFor;
    }

    public ForBody getForBody() {
        return ForBody;
    }

    public void setForBody(ForBody ForBody) {
        this.ForBody=ForBody;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorForBegin!=null) DesignatorForBegin.accept(visitor);
        if(ConditionFor!=null) ConditionFor.accept(visitor);
        if(DesignatorStatementFor!=null) DesignatorStatementFor.accept(visitor);
        if(ForBody!=null) ForBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorForBegin!=null) DesignatorForBegin.traverseTopDown(visitor);
        if(ConditionFor!=null) ConditionFor.traverseTopDown(visitor);
        if(DesignatorStatementFor!=null) DesignatorStatementFor.traverseTopDown(visitor);
        if(ForBody!=null) ForBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorForBegin!=null) DesignatorForBegin.traverseBottomUp(visitor);
        if(ConditionFor!=null) ConditionFor.traverseBottomUp(visitor);
        if(DesignatorStatementFor!=null) DesignatorStatementFor.traverseBottomUp(visitor);
        if(ForBody!=null) ForBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementFor(\n");

        if(DesignatorForBegin!=null)
            buffer.append(DesignatorForBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConditionFor!=null)
            buffer.append(ConditionFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignatorStatementFor!=null)
            buffer.append(DesignatorStatementFor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForBody!=null)
            buffer.append(ForBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementFor]");
        return buffer.toString();
    }
}
