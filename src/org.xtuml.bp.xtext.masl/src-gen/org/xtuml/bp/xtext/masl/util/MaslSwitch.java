/**
 * generated by Xtext 2.9.1
 */
package org.xtuml.bp.xtext.masl.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.xtuml.bp.xtext.masl.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.xtuml.bp.xtext.masl.MaslPackage
 * @generated
 */
public class MaslSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static MaslPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MaslSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = MaslPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case MaslPackage.PROJECT:
      {
        Project project = (Project)theEObject;
        T result = caseProject(project);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PROJECT_ITEM:
      {
        projectItem projectItem = (projectItem)theEObject;
        T result = caseprojectItem(projectItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.DOMAIN:
      {
        Domain domain = (Domain)theEObject;
        T result = caseDomain(domain);
        if (result == null) result = caseprojectItem(domain);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.DOMAIN_PRJ_ITEM:
      {
        domainPrjItem domainPrjItem = (domainPrjItem)theEObject;
        T result = casedomainPrjItem(domainPrjItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PROJECT_NAME:
      {
        ProjectName projectName = (ProjectName)theEObject;
        T result = caseProjectName(projectName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.DOMAIN_NAME:
      {
        DomainName domainName = (DomainName)theEObject;
        T result = caseDomainName(domainName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TERMINATOR_NAME:
      {
        TerminatorName terminatorName = (TerminatorName)theEObject;
        T result = caseTerminatorName(terminatorName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TERMINATOR_DEFINITION:
      {
        TerminatorDefinition terminatorDefinition = (TerminatorDefinition)theEObject;
        T result = caseTerminatorDefinition(terminatorDefinition);
        if (result == null) result = casedomainPrjItem(terminatorDefinition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TERMINATOR_ITEM:
      {
        terminatorItem terminatorItem = (terminatorItem)theEObject;
        T result = caseterminatorItem(terminatorItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TERMINATOR_SERVICE_DECLARATION:
      {
        TerminatorServiceDeclaration terminatorServiceDeclaration = (TerminatorServiceDeclaration)theEObject;
        T result = caseTerminatorServiceDeclaration(terminatorServiceDeclaration);
        if (result == null) result = caseterminatorItem(terminatorServiceDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TERMINATOR_FUNCTION_DECLARATION:
      {
        TerminatorFunctionDeclaration terminatorFunctionDeclaration = (TerminatorFunctionDeclaration)theEObject;
        T result = caseTerminatorFunctionDeclaration(terminatorFunctionDeclaration);
        if (result == null) result = caseterminatorItem(terminatorFunctionDeclaration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PARAMETER_LIST:
      {
        parameterList parameterList = (parameterList)theEObject;
        T result = caseparameterList(parameterList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PARAMETER_DEFINITION:
      {
        ParameterDefinition parameterDefinition = (ParameterDefinition)theEObject;
        T result = caseParameterDefinition(parameterDefinition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.SERVICE_NAME:
      {
        ServiceName serviceName = (ServiceName)theEObject;
        T result = caseServiceName(serviceName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PARAMETER_NAME:
      {
        ParameterName parameterName = (ParameterName)theEObject;
        T result = caseParameterName(parameterName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PARAMETER_TYPE:
      {
        ParameterType parameterType = (ParameterType)theEObject;
        T result = caseParameterType(parameterType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.RETURN_TYPE:
      {
        ReturnType returnType = (ReturnType)theEObject;
        T result = caseReturnType(returnType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TYPE_REFERENCE:
      {
        typeReference typeReference = (typeReference)theEObject;
        T result = casetypeReference(typeReference);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.DEPRECATED_TYPE:
      {
        deprecatedType deprecatedType = (deprecatedType)theEObject;
        T result = casedeprecatedType(deprecatedType);
        if (result == null) result = casetypeReference(deprecatedType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.NAMED_TYPE:
      {
        NamedType namedType = (NamedType)theEObject;
        T result = caseNamedType(namedType);
        if (result == null) result = casetypeReference(namedType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.TYPE_NAME:
      {
        TypeName typeName = (TypeName)theEObject;
        T result = caseTypeName(typeName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PRAGMA_LIST:
      {
        PragmaList pragmaList = (PragmaList)theEObject;
        T result = casePragmaList(pragmaList);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PRAGMA:
      {
        Pragma pragma = (Pragma)theEObject;
        T result = casePragma(pragma);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case MaslPackage.PRAGMA_NAME:
      {
        PragmaName pragmaName = (PragmaName)theEObject;
        T result = casePragmaName(pragmaName);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProject(Project object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>project Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>project Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseprojectItem(projectItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Domain</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Domain</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDomain(Domain object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>domain Prj Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>domain Prj Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casedomainPrjItem(domainPrjItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectName(ProjectName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Domain Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Domain Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseDomainName(DomainName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Terminator Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Terminator Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTerminatorName(TerminatorName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Terminator Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Terminator Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTerminatorDefinition(TerminatorDefinition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>terminator Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>terminator Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseterminatorItem(terminatorItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Terminator Service Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Terminator Service Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTerminatorServiceDeclaration(TerminatorServiceDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Terminator Function Declaration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Terminator Function Declaration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTerminatorFunctionDeclaration(TerminatorFunctionDeclaration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>parameter List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>parameter List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseparameterList(parameterList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameterDefinition(ParameterDefinition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Service Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Service Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseServiceName(ServiceName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameterName(ParameterName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Parameter Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Parameter Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseParameterType(ParameterType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Return Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Return Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseReturnType(ReturnType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>type Reference</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>type Reference</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casetypeReference(typeReference object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>deprecated Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>deprecated Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casedeprecatedType(deprecatedType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Named Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Named Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseNamedType(NamedType object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Type Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Type Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTypeName(TypeName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Pragma List</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Pragma List</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePragmaList(PragmaList object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Pragma</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Pragma</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePragma(Pragma object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Pragma Name</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Pragma Name</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePragmaName(PragmaName object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //MaslSwitch
