/*
* generated by Xtext
*/
package org.argouml.xtuml.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import org.argouml.xtuml.services.OALGrammarAccess;

public class OALParser extends AbstractContentAssistParser {
	
	@Inject
	private OALGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected org.argouml.xtuml.ui.contentassist.antlr.internal.InternalOALParser createParser() {
		org.argouml.xtuml.ui.contentassist.antlr.internal.InternalOALParser result = new org.argouml.xtuml.ui.contentassist.antlr.internal.InternalOALParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getStatementAccess().getAlternatives_0(), "rule__Statement__Alternatives_0");
					put(grammarAccess.getObject_statementAccess().getAlternatives(), "rule__Object_statement__Alternatives");
					put(grammarAccess.getSelect_statementAccess().getAlternatives_1(), "rule__Select_statement__Alternatives_1");
					put(grammarAccess.getSelect_statementAccess().getAlternatives_3(), "rule__Select_statement__Alternatives_3");
					put(grammarAccess.getObject_referenceAccess().getAlternatives(), "rule__Object_reference__Alternatives");
					put(grammarAccess.getFlow_control_statementAccess().getAlternatives(), "rule__Flow_control_statement__Alternatives");
					put(grammarAccess.getExpressionAccess().getAlternatives(), "rule__Expression__Alternatives");
					put(grammarAccess.getExpressionAccess().getAlternatives_1_1_0(), "rule__Expression__Alternatives_1_1_0");
					put(grammarAccess.getExpr2Access().getAlternatives(), "rule__Expr2__Alternatives");
					put(grammarAccess.getExpr2Access().getAlternatives_0_0(), "rule__Expr2__Alternatives_0_0");
					put(grammarAccess.getSumAccess().getAlternatives_1_0(), "rule__Sum__Alternatives_1_0");
					put(grammarAccess.getProductAccess().getAlternatives_1_0(), "rule__Product__Alternatives_1_0");
					put(grammarAccess.getValueAccess().getAlternatives(), "rule__Value__Alternatives");
					put(grammarAccess.getVariableAccess().getAlternatives(), "rule__Variable__Alternatives");
					put(grammarAccess.getStatementAccess().getGroup(), "rule__Statement__Group__0");
					put(grammarAccess.getObject_statementAccess().getGroup_0(), "rule__Object_statement__Group_0__0");
					put(grammarAccess.getObject_statementAccess().getGroup_2(), "rule__Object_statement__Group_2__0");
					put(grammarAccess.getObject_statementAccess().getGroup_3(), "rule__Object_statement__Group_3__0");
					put(grammarAccess.getObject_statementAccess().getGroup_4(), "rule__Object_statement__Group_4__0");
					put(grammarAccess.getCreate_statementAccess().getGroup(), "rule__Create_statement__Group__0");
					put(grammarAccess.getSelect_statementAccess().getGroup(), "rule__Select_statement__Group__0");
					put(grammarAccess.getSelect_statementAccess().getGroup_3_0(), "rule__Select_statement__Group_3_0__0");
					put(grammarAccess.getSelect_statementAccess().getGroup_3_0_4(), "rule__Select_statement__Group_3_0_4__0");
					put(grammarAccess.getSelect_statementAccess().getGroup_3_1(), "rule__Select_statement__Group_3_1__0");
					put(grammarAccess.getSelect_statementAccess().getGroup_3_1_3(), "rule__Select_statement__Group_3_1_3__0");
					put(grammarAccess.getSelect_statementAccess().getGroup_3_1_4(), "rule__Select_statement__Group_3_1_4__0");
					put(grammarAccess.getRelate_statementAccess().getGroup(), "rule__Relate_statement__Group__0");
					put(grammarAccess.getUnrelate_statementAccess().getGroup(), "rule__Unrelate_statement__Group__0");
					put(grammarAccess.getDelete_statementAccess().getGroup(), "rule__Delete_statement__Group__0");
					put(grammarAccess.getRelationAccess().getGroup(), "rule__Relation__Group__0");
					put(grammarAccess.getRelationAccess().getGroup_1(), "rule__Relation__Group_1__0");
					put(grammarAccess.getAssignmentAccess().getGroup(), "rule__Assignment__Group__0");
					put(grammarAccess.getFlow_control_statementAccess().getGroup_0(), "rule__Flow_control_statement__Group_0__0");
					put(grammarAccess.getFlow_control_statementAccess().getGroup_0_5(), "rule__Flow_control_statement__Group_0_5__0");
					put(grammarAccess.getFlow_control_statementAccess().getGroup_0_6(), "rule__Flow_control_statement__Group_0_6__0");
					put(grammarAccess.getFlow_control_statementAccess().getGroup_1(), "rule__Flow_control_statement__Group_1__0");
					put(grammarAccess.getFlow_control_statementAccess().getGroup_2(), "rule__Flow_control_statement__Group_2__0");
					put(grammarAccess.getExpressionAccess().getGroup_0(), "rule__Expression__Group_0__0");
					put(grammarAccess.getExpressionAccess().getGroup_1(), "rule__Expression__Group_1__0");
					put(grammarAccess.getExpressionAccess().getGroup_1_1(), "rule__Expression__Group_1_1__0");
					put(grammarAccess.getExpr2Access().getGroup_0(), "rule__Expr2__Group_0__0");
					put(grammarAccess.getSumAccess().getGroup(), "rule__Sum__Group__0");
					put(grammarAccess.getSumAccess().getGroup_1(), "rule__Sum__Group_1__0");
					put(grammarAccess.getProductAccess().getGroup(), "rule__Product__Group__0");
					put(grammarAccess.getProductAccess().getGroup_1(), "rule__Product__Group_1__0");
					put(grammarAccess.getValueAccess().getGroup_0(), "rule__Value__Group_0__0");
					put(grammarAccess.getValueAccess().getGroup_1(), "rule__Value__Group_1__0");
					put(grammarAccess.getValueAccess().getGroup_2(), "rule__Value__Group_2__0");
					put(grammarAccess.getValueAccess().getGroup_3(), "rule__Value__Group_3__0");
					put(grammarAccess.getValueAccess().getGroup_4(), "rule__Value__Group_4__0");
					put(grammarAccess.getVariableAccess().getGroup_1(), "rule__Variable__Group_1__0");
					put(grammarAccess.getCodeAccess().getStatementsAssignment(), "rule__Code__StatementsAssignment");
					put(grammarAccess.getStatementAccess().getSt1Assignment_0_0(), "rule__Statement__St1Assignment_0_0");
					put(grammarAccess.getStatementAccess().getSt2Assignment_0_1(), "rule__Statement__St2Assignment_0_1");
					put(grammarAccess.getStatementAccess().getSt3Assignment_0_2(), "rule__Statement__St3Assignment_0_2");
					put(grammarAccess.getSelect_statementAccess().getVarAssignment_2(), "rule__Select_statement__VarAssignment_2");
					put(grammarAccess.getAssignmentAccess().getEAssignment_3(), "rule__Assignment__EAssignment_3");
					put(grammarAccess.getFlow_control_statementAccess().getExprAssignment_0_2(), "rule__Flow_control_statement__ExprAssignment_0_2");
					put(grammarAccess.getFlow_control_statementAccess().getSubstatementsAssignment_0_4(), "rule__Flow_control_statement__SubstatementsAssignment_0_4");
					put(grammarAccess.getFlow_control_statementAccess().getElifexprAssignment_0_5_1(), "rule__Flow_control_statement__ElifexprAssignment_0_5_1");
					put(grammarAccess.getFlow_control_statementAccess().getSubstatementsAssignment_0_5_3(), "rule__Flow_control_statement__SubstatementsAssignment_0_5_3");
					put(grammarAccess.getFlow_control_statementAccess().getSubstatementsAssignment_0_6_1(), "rule__Flow_control_statement__SubstatementsAssignment_0_6_1");
					put(grammarAccess.getFlow_control_statementAccess().getListAssignment_1_4(), "rule__Flow_control_statement__ListAssignment_1_4");
					put(grammarAccess.getFlow_control_statementAccess().getSubstatementsAssignment_1_6(), "rule__Flow_control_statement__SubstatementsAssignment_1_6");
					put(grammarAccess.getFlow_control_statementAccess().getExprAssignment_2_2(), "rule__Flow_control_statement__ExprAssignment_2_2");
					put(grammarAccess.getFlow_control_statementAccess().getSubstatementsAssignment_2_4(), "rule__Flow_control_statement__SubstatementsAssignment_2_4");
					put(grammarAccess.getExpressionAccess().getNeAssignment_0_1(), "rule__Expression__NeAssignment_0_1");
					put(grammarAccess.getExpressionAccess().getLsAssignment_1_0(), "rule__Expression__LsAssignment_1_0");
					put(grammarAccess.getExpressionAccess().getRsAssignment_1_1_1(), "rule__Expression__RsAssignment_1_1_1");
					put(grammarAccess.getExpr2Access().getNAssignment_0_1(), "rule__Expr2__NAssignment_0_1");
					put(grammarAccess.getExpr2Access().getSAssignment_1(), "rule__Expr2__SAssignment_1");
					put(grammarAccess.getSumAccess().getLtAssignment_0(), "rule__Sum__LtAssignment_0");
					put(grammarAccess.getSumAccess().getRtAssignment_1_1(), "rule__Sum__RtAssignment_1_1");
					put(grammarAccess.getProductAccess().getLfAssignment_0(), "rule__Product__LfAssignment_0");
					put(grammarAccess.getProductAccess().getRfAssignment_1_1(), "rule__Product__RfAssignment_1_1");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			org.argouml.xtuml.ui.contentassist.antlr.internal.InternalOALParser typedParser = (org.argouml.xtuml.ui.contentassist.antlr.internal.InternalOALParser) parser;
			typedParser.entryRuleCode();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public OALGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(OALGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
