// generated with ast extension for cup
// version 0.8
// 19/7/2020 13:59:52


package rs.ac.bg.etf.pp1.ast;

public class ExprListElems extends ExprListElem {

    private ListAddop ListAddop;
    private Term Term;

    public ExprListElems (ListAddop ListAddop, Term Term) {
        this.ListAddop=ListAddop;
        if(ListAddop!=null) ListAddop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public ListAddop getListAddop() {
        return ListAddop;
    }

    public void setListAddop(ListAddop ListAddop) {
        this.ListAddop=ListAddop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ListAddop!=null) ListAddop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ListAddop!=null) ListAddop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ListAddop!=null) ListAddop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprListElems(\n");

        if(ListAddop!=null)
            buffer.append(ListAddop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprListElems]");
        return buffer.toString();
    }
}
