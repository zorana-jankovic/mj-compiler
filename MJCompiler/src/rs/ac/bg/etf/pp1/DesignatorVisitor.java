package rs.ac.bg.etf.pp1;

import java.util.ArrayList;

import rs.ac.bg.etf.pp1.ast.AddLeft;
import rs.ac.bg.etf.pp1.ast.AddRight;
import rs.ac.bg.etf.pp1.ast.AddopLeft;
import rs.ac.bg.etf.pp1.ast.ArrayElem;
import rs.ac.bg.etf.pp1.ast.BoolFactor;
import rs.ac.bg.etf.pp1.ast.CharFactor;
import rs.ac.bg.etf.pp1.ast.ClassField;
import rs.ac.bg.etf.pp1.ast.DesVar;
import rs.ac.bg.etf.pp1.ast.Design;
import rs.ac.bg.etf.pp1.ast.DesignatorIdent;
import rs.ac.bg.etf.pp1.ast.Div;
import rs.ac.bg.etf.pp1.ast.DivR;
import rs.ac.bg.etf.pp1.ast.ExprFactor;
import rs.ac.bg.etf.pp1.ast.ExprList;
import rs.ac.bg.etf.pp1.ast.ExprListElems;
import rs.ac.bg.etf.pp1.ast.FuncCallFactor;
import rs.ac.bg.etf.pp1.ast.FuncCallFactorDesignator;
import rs.ac.bg.etf.pp1.ast.ListAdd;
import rs.ac.bg.etf.pp1.ast.ListMul;
import rs.ac.bg.etf.pp1.ast.Minus;
import rs.ac.bg.etf.pp1.ast.MinusExpr;
import rs.ac.bg.etf.pp1.ast.MinusRight;
import rs.ac.bg.etf.pp1.ast.MinusTerm;
import rs.ac.bg.etf.pp1.ast.Mod;
import rs.ac.bg.etf.pp1.ast.ModR;
import rs.ac.bg.etf.pp1.ast.Mul;
import rs.ac.bg.etf.pp1.ast.MulLeft;
import rs.ac.bg.etf.pp1.ast.MulR;
import rs.ac.bg.etf.pp1.ast.MulRight;
import rs.ac.bg.etf.pp1.ast.MulopLeft;
import rs.ac.bg.etf.pp1.ast.MyDesignatorElemList;
import rs.ac.bg.etf.pp1.ast.MyExprList;
import rs.ac.bg.etf.pp1.ast.MyNoDesignatorElemList;
import rs.ac.bg.etf.pp1.ast.MyNoExprList;
import rs.ac.bg.etf.pp1.ast.MyNoTermList;
import rs.ac.bg.etf.pp1.ast.MyTermList;
import rs.ac.bg.etf.pp1.ast.NewClass;
import rs.ac.bg.etf.pp1.ast.NewFactor;
import rs.ac.bg.etf.pp1.ast.NoMinusExpr;
import rs.ac.bg.etf.pp1.ast.NumberFactor;
import rs.ac.bg.etf.pp1.ast.Plus;
import rs.ac.bg.etf.pp1.ast.PlusRight;
import rs.ac.bg.etf.pp1.ast.TermListElems;
import rs.ac.bg.etf.pp1.ast.Terms;
import rs.ac.bg.etf.pp1.ast.Var;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class DesignatorVisitor extends VisitorAdaptor{
	private int inClass = 0;
	private ArrayList<Obj> myClasses = new ArrayList<>();
	
	
	
	
	public ArrayList<Obj> getMyClasses() {
		return myClasses;
	}


	public void setMyClasses(ArrayList<Obj> myClasses) {
		this.myClasses = myClasses;
	}


	public int getInClass() {
		return inClass;
	}


	public void setInClass(int inClass) {
		this.inClass = inClass;
	}


	public void visit(FuncCallFactorDesignator funcCallFactorDesignator) {
//		Design des = (Design) funcCallFactorDesignator.getDesignator();
//		
//		if (des.getDesignatorElemList() instanceof MyDesignatorElemList) {
//			Code.load(designator);
//		}
	}
	
	
	public void visit(DesignatorIdent designatorIdent) {
		if (designatorIdent.obj.getKind() == Obj.Fld || designatorIdent.obj.getKind() == Obj.Meth && inClass == 1) {
			Code.put(Code.load_n + 0); // ubaci "this" ! // a ne moze se pristupati polju klase van metode? ---> ne moze sa this(nema objekta)
		}
		
		Design design = (Design) designatorIdent.getParent();
		if (design.getDesignatorElemList() instanceof MyNoDesignatorElemList) {
			// nista ne radi ---> ti si jedini(poslednji u listi designatora)
		} 
		else {
			Code.load(designatorIdent.obj);
		}
	}
	
	public void visit(Design design) {
		Obj des = design.obj;		
		//designator = des;
	}
	
	public void visit(ArrayElem arrayElem) {
		MyDesignatorElemList parent = (MyDesignatorElemList) arrayElem.getParent();
		
		if (parent.getDesignatorElemList() instanceof MyDesignatorElemList){
			Code.load(arrayElem.obj);
		}

	}
	
	
	public void visit(ClassField classField) {
		MyDesignatorElemList parent = (MyDesignatorElemList) classField.getParent();
		
		if (parent.getDesignatorElemList() instanceof MyDesignatorElemList){
			Code.load(classField.obj);
		}
		
	}
	
	public void visit(MinusExpr minusExpr) {		
//		Code.loadConst(0);
//		Code.put(Code.dup_x1);
//		Code.put(Code.pop);
//		Code.put(Code.sub);
	}
	
	public void visit(MinusTerm minusTerm) {
		Code.loadConst(0);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.sub);
	}
	
	public void visit(ExprListElems exprListElems) {
		ListAdd listAdd = (ListAdd) exprListElems.getListAddop();
		
		//if(listAdd.getAddop() instanceof AddLeft) {
			AddopLeft addLeft  = (AddopLeft) listAdd.getAddopLeft();
			
			if(addLeft instanceof Plus) {
				Code.put(Code.add);
			}
			
			if(addLeft instanceof Minus) {
				Code.put(Code.sub);
			}
			

		
	}

	public void visit(TermListElems termListElems) {
		ListMul listMulop = (ListMul) termListElems.getListMulop();
		
		//if(listMulop.getMulop() instanceof MulLeft) {

		MulopLeft operator = (MulopLeft) listMulop.getMulopLeft();
			
		if (operator instanceof Mul) {
			Code.put(Code.mul);
		}
		
		if (operator instanceof Div) {
			Code.put(Code.div);
		}
		
		if (operator instanceof Mod) {
			Code.put(Code.rem);
		}
	}

		
		public void visit(NumberFactor numberFactor) {
			Code.loadConst(numberFactor.getValNum());
			//designator = null;
		}
		
		public void visit(CharFactor charFactor) {
			Code.loadConst(charFactor.getValChar());
			//designator = null;
		}
		
		public void visit(BoolFactor boolFactor) { // true - 1 ; else - 0;
			if (boolFactor.getVarBool().equals("true")) {
				Code.loadConst(1);
			}
			else {
				Code.loadConst(0);
			}
		//	designator = null;
		}
		
		public void visit(ExprFactor exprFactor) {
		//	designator = null;
		}
		
		
		public void visit(Var var) {
			//checkAssignOp(var);
			Code.load(var.getDesignator().obj);
		//	designator = var.getDesignator().obj;
		}
		
		public void visit(DesVar var) {
		//	checkAssignOp(var);
		//	Code.load(var.getDesignator().obj);
		//	designator = var.getDesignator().obj;
		}
		
		private void checkAssignOp(DesVar var) {
			if (var.getDesignator().obj.getKind() != Obj.Elem && var.getDesignator().obj.getKind() != Obj.Fld) {
				return;
			}
			
			if (var.getDesignator().obj.getKind() == Obj.Elem ) {
				Code.put(Code.dup2);
			}
			else { // Obj.Fld
				Code.put(Code.dup);
			}
			

			
			
		}
		
		
		
		public void visit(FuncCallFactor factor) {
			Obj des = factor.getFuncCallFactorDesignator().getDesignator().obj;
			if (des.getName().equals("chr") || des.getName().equals("ord")){
				return;
			}
			if(des.getName().equals("len")) {
				Code.put(Code.arraylength);
				return;
			}
			
			Design design = (Design) factor.getFuncCallFactorDesignator().getDesignator();
			
			if (design.getDesignatorElemList() instanceof MyDesignatorElemList) {
				// treba mi this :(
				DesignatorVisitor desVisitor = new DesignatorVisitor();
				desVisitor.setInClass(inClass);
				desVisitor.setMyClasses(myClasses);
				//factor.getFuncCallFactorDesignator().traverseTopDown(desVisitor);
				design.traverseBottomUp(desVisitor);
				// valjda je tu
				Code.put(Code.getfield);
				Code.put2(0);
				Code.put(Code.invokevirtual);
				String name = factor.getFuncCallFactorDesignator().getDesignator().obj.getName();
				for (int i = 0; i < name.length(); i++) {
					Code.put4(name.charAt(i));
				}
				Code.put4(-1);
			}
			else if (design.getDesignatorElemList() instanceof MyNoDesignatorElemList && inClass == 1){
				Code.put(Code.load_n + 0); // ovo ce biti moj this, jer sam u f-ji
				Code.put(Code.getfield);
				Code.put2(0);
				Code.put(Code.invokevirtual);
				String name = factor.getFuncCallFactorDesignator().getDesignator().obj.getName();
				for (int i = 0; i < name.length(); i++) {
					Code.put4(name.charAt(i));
				}
				Code.put4(-1);
				
			}else {
				int offset = factor.getFuncCallFactorDesignator().getDesignator().obj.getAdr() - Code.pc;
				Code.put(Code.call);
				Code.put2(offset);
			}
			
			//designator = null;
		}
		
		public void visit(ListAdd listAdd) {
			//Code.load(designator);
			//listAdd.obj = designator;
		}
		
		public void visit(ListMul listMul) {
			//Code.load(designator);
			//listMul.obj = designator;
		}
	
}