package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormalParamDecl;
import rs.ac.bg.etf.pp1.ast.FormalParamDeclBraces;
import rs.ac.bg.etf.pp1.ast.FormalParamDeclNoBraces;
import rs.ac.bg.etf.pp1.ast.VarDecl;
import rs.ac.bg.etf.pp1.ast.VarDeclElem;
import rs.ac.bg.etf.pp1.ast.VarDeclElemBraces;
import rs.ac.bg.etf.pp1.ast.VarDeclElemNoBraces;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;

	public int getCount() {
		return count;
	}


	public static class FormParamCounter extends CounterVisitor{
		
		public void visit(FormalParamDeclBraces formalParamDecl) {
			count++;
		}
		
		public void visit(FormalParamDeclNoBraces formalParamDecl) {
			count++;
		}
	}
	
	public static class VarCounter extends CounterVisitor{
		
		public void visit(VarDeclElemBraces varDeclElem) {
			count++;
		}
		
		public void visit(VarDeclElemNoBraces varDeclElem) {
			count++;
		}
	}


}
