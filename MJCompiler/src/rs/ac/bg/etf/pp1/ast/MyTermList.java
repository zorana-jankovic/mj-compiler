// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class MyTermList extends TermList {

    private TermListElem TermListElem;
    private TermList TermList;

    public MyTermList (TermListElem TermListElem, TermList TermList) {
        this.TermListElem=TermListElem;
        if(TermListElem!=null) TermListElem.setParent(this);
        this.TermList=TermList;
        if(TermList!=null) TermList.setParent(this);
    }

    public TermListElem getTermListElem() {
        return TermListElem;
    }

    public void setTermListElem(TermListElem TermListElem) {
        this.TermListElem=TermListElem;
    }

    public TermList getTermList() {
        return TermList;
    }

    public void setTermList(TermList TermList) {
        this.TermList=TermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermListElem!=null) TermListElem.accept(visitor);
        if(TermList!=null) TermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermListElem!=null) TermListElem.traverseTopDown(visitor);
        if(TermList!=null) TermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermListElem!=null) TermListElem.traverseBottomUp(visitor);
        if(TermList!=null) TermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MyTermList(\n");

        if(TermListElem!=null)
            buffer.append(TermListElem.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TermList!=null)
            buffer.append(TermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MyTermList]");
        return buffer.toString();
    }
}
