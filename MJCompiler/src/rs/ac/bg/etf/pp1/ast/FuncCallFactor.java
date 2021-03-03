// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class FuncCallFactor extends Factor {

    private FuncCallFactorDesignator FuncCallFactorDesignator;
    private ActPars ActPars;

    public FuncCallFactor (FuncCallFactorDesignator FuncCallFactorDesignator, ActPars ActPars) {
        this.FuncCallFactorDesignator=FuncCallFactorDesignator;
        if(FuncCallFactorDesignator!=null) FuncCallFactorDesignator.setParent(this);
        this.ActPars=ActPars;
        if(ActPars!=null) ActPars.setParent(this);
    }

    public FuncCallFactorDesignator getFuncCallFactorDesignator() {
        return FuncCallFactorDesignator;
    }

    public void setFuncCallFactorDesignator(FuncCallFactorDesignator FuncCallFactorDesignator) {
        this.FuncCallFactorDesignator=FuncCallFactorDesignator;
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
        if(FuncCallFactorDesignator!=null) FuncCallFactorDesignator.accept(visitor);
        if(ActPars!=null) ActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FuncCallFactorDesignator!=null) FuncCallFactorDesignator.traverseTopDown(visitor);
        if(ActPars!=null) ActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FuncCallFactorDesignator!=null) FuncCallFactorDesignator.traverseBottomUp(visitor);
        if(ActPars!=null) ActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FuncCallFactor(\n");

        if(FuncCallFactorDesignator!=null)
            buffer.append(FuncCallFactorDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActPars!=null)
            buffer.append(ActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FuncCallFactor]");
        return buffer.toString();
    }
}
