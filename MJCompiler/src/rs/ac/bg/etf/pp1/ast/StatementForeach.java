// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class StatementForeach extends Statement {

    private ForEachIndexName ForEachIndexName;
    private ForEachDesignator ForEachDesignator;
    private ForEachBody ForEachBody;

    public StatementForeach (ForEachIndexName ForEachIndexName, ForEachDesignator ForEachDesignator, ForEachBody ForEachBody) {
        this.ForEachIndexName=ForEachIndexName;
        if(ForEachIndexName!=null) ForEachIndexName.setParent(this);
        this.ForEachDesignator=ForEachDesignator;
        if(ForEachDesignator!=null) ForEachDesignator.setParent(this);
        this.ForEachBody=ForEachBody;
        if(ForEachBody!=null) ForEachBody.setParent(this);
    }

    public ForEachIndexName getForEachIndexName() {
        return ForEachIndexName;
    }

    public void setForEachIndexName(ForEachIndexName ForEachIndexName) {
        this.ForEachIndexName=ForEachIndexName;
    }

    public ForEachDesignator getForEachDesignator() {
        return ForEachDesignator;
    }

    public void setForEachDesignator(ForEachDesignator ForEachDesignator) {
        this.ForEachDesignator=ForEachDesignator;
    }

    public ForEachBody getForEachBody() {
        return ForEachBody;
    }

    public void setForEachBody(ForEachBody ForEachBody) {
        this.ForEachBody=ForEachBody;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ForEachIndexName!=null) ForEachIndexName.accept(visitor);
        if(ForEachDesignator!=null) ForEachDesignator.accept(visitor);
        if(ForEachBody!=null) ForEachBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ForEachIndexName!=null) ForEachIndexName.traverseTopDown(visitor);
        if(ForEachDesignator!=null) ForEachDesignator.traverseTopDown(visitor);
        if(ForEachBody!=null) ForEachBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ForEachIndexName!=null) ForEachIndexName.traverseBottomUp(visitor);
        if(ForEachDesignator!=null) ForEachDesignator.traverseBottomUp(visitor);
        if(ForEachBody!=null) ForEachBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("StatementForeach(\n");

        if(ForEachIndexName!=null)
            buffer.append(ForEachIndexName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForEachDesignator!=null)
            buffer.append(ForEachDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForEachBody!=null)
            buffer.append(ForEachBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [StatementForeach]");
        return buffer.toString();
    }
}
