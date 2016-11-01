/**
 * generated by Xtext 2.9.2
 */
package org.xtuml.bp.xtext.masl.masl.structure.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.xtuml.bp.xtext.masl.masl.structure.StructurePackage;
import org.xtuml.bp.xtext.masl.masl.structure.TerminatorDefinition;
import org.xtuml.bp.xtext.masl.masl.structure.TerminatorServiceDefinition;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Terminator Service Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.xtuml.bp.xtext.masl.masl.structure.impl.TerminatorServiceDefinitionImpl#getTerminator <em>Terminator</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TerminatorServiceDefinitionImpl extends AbstractActionDefinitionImpl implements TerminatorServiceDefinition {
	/**
	 * The cached value of the '{@link #getTerminator() <em>Terminator</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTerminator()
	 * @generated
	 * @ordered
	 */
	protected TerminatorDefinition terminator;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TerminatorServiceDefinitionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return StructurePackage.Literals.TERMINATOR_SERVICE_DEFINITION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TerminatorDefinition getTerminator() {
		if (terminator != null && terminator.eIsProxy()) {
			InternalEObject oldTerminator = (InternalEObject)terminator;
			terminator = (TerminatorDefinition)eResolveProxy(oldTerminator);
			if (terminator != oldTerminator) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, StructurePackage.TERMINATOR_SERVICE_DEFINITION__TERMINATOR, oldTerminator, terminator));
			}
		}
		return terminator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TerminatorDefinition basicGetTerminator() {
		return terminator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTerminator(TerminatorDefinition newTerminator) {
		TerminatorDefinition oldTerminator = terminator;
		terminator = newTerminator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, StructurePackage.TERMINATOR_SERVICE_DEFINITION__TERMINATOR, oldTerminator, terminator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case StructurePackage.TERMINATOR_SERVICE_DEFINITION__TERMINATOR:
				if (resolve) return getTerminator();
				return basicGetTerminator();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case StructurePackage.TERMINATOR_SERVICE_DEFINITION__TERMINATOR:
				setTerminator((TerminatorDefinition)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case StructurePackage.TERMINATOR_SERVICE_DEFINITION__TERMINATOR:
				setTerminator((TerminatorDefinition)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case StructurePackage.TERMINATOR_SERVICE_DEFINITION__TERMINATOR:
				return terminator != null;
		}
		return super.eIsSet(featureID);
	}

} //TerminatorServiceDefinitionImpl
