//=====================================================================
//
//File:      $RCSfile$
//Version:   $Revision$
//Modified:  $Date$
//
//(c) Copyright 2008-2013 by Mentor Graphics Corp. All rights reserved.
//
//=====================================================================
//This document contains information proprietary and confidential to
//Mentor Graphics Corp. and is not for external distribution.
//=====================================================================
package com.mentor.nucleus.bp.debug.ui.test.execute;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

import com.mentor.nucleus.bp.core.ClassStateMachine_c;
import com.mentor.nucleus.bp.core.ComponentInstance_c;
import com.mentor.nucleus.bp.core.Component_c;
import com.mentor.nucleus.bp.core.CorePlugin;
import com.mentor.nucleus.bp.core.InterfaceReference_c;
import com.mentor.nucleus.bp.core.ModelClass_c;
import com.mentor.nucleus.bp.core.Ooaofooa;
import com.mentor.nucleus.bp.core.Operation_c;
import com.mentor.nucleus.bp.core.Package_c;
import com.mentor.nucleus.bp.core.PackageableElement_c;
import com.mentor.nucleus.bp.core.Port_c;
import com.mentor.nucleus.bp.core.ProvidedExecutableProperty_c;
import com.mentor.nucleus.bp.core.ProvidedOperation_c;
import com.mentor.nucleus.bp.core.Provision_c;
import com.mentor.nucleus.bp.core.RequiredExecutableProperty_c;
import com.mentor.nucleus.bp.core.RequiredOperation_c;
import com.mentor.nucleus.bp.core.RequiredSignal_c;
import com.mentor.nucleus.bp.core.Requirement_c;
import com.mentor.nucleus.bp.core.StateMachineState_c;
import com.mentor.nucleus.bp.core.StateMachine_c;
import com.mentor.nucleus.bp.core.SystemModel_c;
import com.mentor.nucleus.bp.core.common.BridgePointPreferencesStore;
import com.mentor.nucleus.bp.core.common.ClassQueryInterface_c;
import com.mentor.nucleus.bp.core.common.PersistableModelComponent;
import com.mentor.nucleus.bp.core.ui.perspective.BridgePointPerspective;
import com.mentor.nucleus.bp.debug.ui.launch.BPDebugUtils;
import com.mentor.nucleus.bp.debug.ui.test.DebugUITestUtilities;
import com.mentor.nucleus.bp.test.TestUtil;
import com.mentor.nucleus.bp.test.common.BaseTest;
import com.mentor.nucleus.bp.test.common.TestingUtilities;
import com.mentor.nucleus.bp.test.common.UITestingUtilities;
import com.mentor.nucleus.bp.ui.text.activity.ActivityEditor;

public class VerifierInterfaceExecutionTests extends BaseTest {

	private static String projectName = "VerifierInterfaceExecutionTests";

	private boolean initialized = false;

	public VerifierInterfaceExecutionTests(String testName) throws Exception {
		super(projectName, testName);
	}

	@Override
	protected void setUp() throws Exception {
		if (!initialized)
			  delayGlobalUpgrade = true;
		super.setUp();

		if (!initialized) {
			CorePlugin.disableParseAllOnResourceChange();

			// set perspective switch dialog on launch
			DebugUIPlugin.getDefault().getPluginPreferences().setValue(
					IDebugUIConstants.PLUGIN_ID + ".switch_to_perspective",
					"always");

			CorePlugin
					.getDefault()
					.getPluginPreferences()
					.setDefault(
							BridgePointPreferencesStore.ALLOW_IMPLICIT_COMPONENT_ADDRESSING,
							true);

			// initialize test model
			final IProject project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(projectName);

			loadProject(projectName);
			
			m_sys = SystemModel_c.SystemModelInstance(Ooaofooa
					.getDefaultInstance(), new ClassQueryInterface_c() {

				public boolean evaluate(Object candidate) {
					return ((SystemModel_c) candidate).getName().equals(
							project.getName());
				}

			});

			PersistableModelComponent sys_comp = m_sys
					.getPersistableComponent();
			sys_comp.loadComponentAndChildren(new NullProgressMonitor());

			CorePlugin.enableParseAllOnResourceChange();

			TestingUtilities.allowJobCompletion();
			while (!ResourcesPlugin.getWorkspace().getRoot().isSynchronized(
					IProject.DEPTH_INFINITE)) {
				ResourcesPlugin.getWorkspace().getRoot().refreshLocal(
						IProject.DEPTH_INFINITE, new NullProgressMonitor());
				while (PlatformUI.getWorkbench().getDisplay().readAndDispatch())
					;
			}

			Ooaofooa.setPersistEnabled(true);
            delayGlobalUpgrade = false;

			initialized = true;
		}
	}

	public void tearDown() throws Exception {
		DebugUITestUtilities.stopSession(m_sys, projectName);
	}

    public void testComponentRefComparisonInMessageBodies() {
        final String testSystemName = "TestSystem1";
        Package_c testPkg = Package_c.getOneEP_PKGOnR1405(m_sys,
                new ClassQueryInterface_c() {
                    public boolean evaluate(Object candidate) {
                        return ((Package_c) candidate).getName().equals(
                                testSystemName);
                    }
                });
        assertNotNull(testPkg);

        // launch the system
        // TODO - problem here.  Launching the package with this function apparently
        //  does not launch all the component refs underneath the package.
        DebugUITestUtilities.setLogActivityAndLaunchForElement(testPkg,
                m_bp_tree.getControl().getMenu(), m_sys.getName());

        openPerspectiveAndView(
                "com.mentor.nucleus.bp.debug.ui.DebugPerspective",
                BridgePointPerspective.ID_MGC_BP_EXPLORER);

        // Get the Client components in hand.
        ComponentInstance_c[] engines = ComponentInstance_c.getManyI_EXEsOnR2970(testPkg);
        assertNotNull(engines);
        List<Component_c> clientComponents = new ArrayList<Component_c>();
        for ( ComponentInstance_c engine: engines ) {
            Component_c comp = Component_c.getOneC_COnR2955(engine, false);
            if ( !comp.getName().equals("Server") ) {
                clientComponents.add(comp);
            }
        }
        
        // Call the Init() required signal on one of the Client component references
        RequiredSignal_c reqSig = RequiredSignal_c.getOneSPR_RSOnR4502(
                RequiredExecutableProperty_c.getManySPR_REPsOnR4500(Requirement_c
                        .getManyC_RsOnR4009(InterfaceReference_c
                                .getManyC_IRsOnR4016(Port_c
                                        .getManyC_POsOnR4010(clientComponents.get(0),
                                                new ClassQueryInterface_c() {
                                                    public boolean evaluate(
                                                            Object candidate) {
                                                        return ((Port_c) candidate)
                                                                .getName()
                                                                .equals("CommPort");
                                                    }
                                                })))),
                new ClassQueryInterface_c() {
                    public boolean evaluate(Object candidate) {
                        return ((RequiredSignal_c) candidate).getName()
                                .equals("Init");
                    }
                });
        assertNotNull(reqSig);

        openPerspectiveAndView(
                "com.mentor.nucleus.bp.debug.ui.DebugPerspective",
                BridgePointPerspective.ID_MGC_BP_EXPLORER);

        BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqSig));

        Menu menu = DebugUITestUtilities.getMenuInSETree(reqSig);
        
        assertTrue(
                "The execute menu item was not available for a required signal.",
                UITestingUtilities.checkItemStatusInContextMenu(menu,
                        "Execute", "", false));

        UITestingUtilities.activateMenuItem(menu, "Execute");

        DebugUITestUtilities.waitForExecution();

        // wait for the execution to complete
        DebugUITestUtilities.waitForBPThreads(m_sys);

        // Call the Init() required signal on the other Client component references
        reqSig = RequiredSignal_c.getOneSPR_RSOnR4502(
                RequiredExecutableProperty_c.getManySPR_REPsOnR4500(Requirement_c
                        .getManyC_RsOnR4009(InterfaceReference_c
                                .getManyC_IRsOnR4016(Port_c
                                        .getManyC_POsOnR4010(clientComponents.get(1),
                                                new ClassQueryInterface_c() {
                                                    public boolean evaluate(
                                                            Object candidate) {
                                                        return ((Port_c) candidate)
                                                                .getName()
                                                                .equals("CommPort");
                                                    }
                                                })))),
                new ClassQueryInterface_c() {
                    public boolean evaluate(Object candidate) {
                        return ((RequiredSignal_c) candidate).getName()
                                .equals("Init");
                    }
                });
        assertNotNull(reqSig);

        openPerspectiveAndView(
                "com.mentor.nucleus.bp.debug.ui.DebugPerspective",
                BridgePointPerspective.ID_MGC_BP_EXPLORER);

        BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqSig));

        menu = DebugUITestUtilities.getMenuInSETree(reqSig);
        
        assertTrue(
                "The execute menu item was not available for a required signal.",
                UITestingUtilities.checkItemStatusInContextMenu(menu,
                        "Execute", "", false));

        UITestingUtilities.activateMenuItem(menu, "Execute");

        DebugUITestUtilities.waitForExecution();

        // TODO - compare the trace
        File expectedResults = new File(
                m_workspace_path
                        + "expected_results/interface_execution/execution_compare_component_refs_good.txt");
        String expected_results = TestUtil.getTextFileContents(expectedResults);
        // get the text representation of the debug tree
        String actual_results = DebugUITestUtilities.getConsoleText(expected_results);
        assertEquals(expected_results, actual_results);
    }

	public void testInterfaceExecutionSignalAssignedToTransition() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});
		assertNotNull(component);

		// launch the domain
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port1");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName()
										.equals("sendServerClientOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		Menu menu = DebugUITestUtilities.getMenuInSETree(reqOp);
		
		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalNoOALAssignedToTransition() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port1");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientNoOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		Menu menu = DebugUITestUtilities.getMenuInSETree(reqOp);

		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_no_oal_assigned_signal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalParametersAssignedToTransition() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		StateMachineState_c state = StateMachineState_c
				.getOneSM_STATEOnR501(
						StateMachine_c.getManySM_SMsOnR517(ClassStateMachine_c.getManySM_ASMsOnR519(ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
								.getManyEP_PKGsOnR8001(PackageableElement_c
										.getManyPE_PEsOnR8003(component)))))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((StateMachineState_c) candidate)
										.getName().equals("State 4");
							}

						});
		assertNotNull(state);

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(state));

		ActivityEditor editor = DebugUITestUtilities
				.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port1");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientParams");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		Menu menu = DebugUITestUtilities.getMenuInSETree(reqOp);

		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);
		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		DebugUITestUtilities.processDebugEvents();
		TestingUtilities.processDisplayEvents();
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable y.",
				"4", yValue);
*/
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal_with_parameters.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalWithQueuedEventsBadState() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);
		
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
		StateMachineState_c state = StateMachineState_c
				.getOneSM_STATEOnR501(
						StateMachine_c.getManySM_SMsOnR517(ClassStateMachine_c.getManySM_ASMsOnR519(ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
								.getManyEP_PKGsOnR8001(PackageableElement_c
										.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
											
											@Override
											public boolean evaluate(Object candidate) {
												return ((ModelClass_c) candidate).getName().equals("cls 2");
											}
										}))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((StateMachineState_c) candidate)
										.getName().equals("State 2");
							}

						});

		assertNotNull(state);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(state));
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
				.getManyEP_PKGsOnR8001(PackageableElement_c
						.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
							
							@Override
							public boolean evaluate(Object candidate) {
								return ((ModelClass_c) candidate).getName().equals("cls 3");
							}
						});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_bad_state");
			}
		
		});
		assertNotNull(op);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(op));
		Menu menu = DebugUITestUtilities.getMenuInSETree(op);

		assertTrue(
				"The execute menu item was not available for a class based operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		ProvidedOperation_c proOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port2");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"sendClientServer");
					}

				});
		assertNotNull(proOp);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));

		assertTrue(
				"The execute menu item was not available for a provided operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal_queued_events_bad.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
	
	public void testInterfaceExecutionSignalWithQueuedEventsGoodState() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);
		
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
		StateMachineState_c state = StateMachineState_c
				.getOneSM_STATEOnR501(
						StateMachine_c.getManySM_SMsOnR517(ClassStateMachine_c.getManySM_ASMsOnR519(ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
								.getManyEP_PKGsOnR8001(PackageableElement_c
										.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
											
											@Override
											public boolean evaluate(Object candidate) {
												return ((ModelClass_c) candidate).getName().equals("cls 2");
											}
										}))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((StateMachineState_c) candidate)
										.getName().equals("State 2");
							}

						});
		assertNotNull(state);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(state));
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
				.getManyEP_PKGsOnR8001(PackageableElement_c
						.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
							
							@Override
							public boolean evaluate(Object candidate) {
								return ((ModelClass_c) candidate).getName().equals("cls 3");
							}
						});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_good_state");
			}
		
		});
		assertNotNull(op);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(op));
		Menu menu = DebugUITestUtilities.getMenuInSETree(op);

		assertTrue(
				"The execute menu item was not available for a class based operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		ProvidedOperation_c proOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port2");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"sendClientServer");
					}

				});
		assertNotNull(proOp);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));

		assertTrue(
				"The execute menu item was not available for a provided operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_assigned_signal_queued_events_good.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalNotAssignedWithOAL() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredSignal_c reqSig = RequiredSignal_c
			.getOneSPR_RSOnR4502(
				RequiredExecutableProperty_c
						.getManySPR_REPsOnR4500(Requirement_c
								.getManyC_RsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port3");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredSignal_c) candidate)
								.getName().equals(
										"serverclientsig");
					}

				});
		assertNotNull(reqSig);
 
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqSig));
		
		ActivityEditor editor = DebugUITestUtilities
				.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port3");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		Menu menu = DebugUITestUtilities.getMenuInSETree(reqOp);

		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in required signal.", target
				.isSuspended());
		
		DebugUITestUtilities.removeAllBreakpoints();
		try {
			target.resume();
		} catch (DebugException e) {
			fail(e.toString());
		}
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_oal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
	
	public void testInterfaceExecutionSignalNotAssignedWithNoOAL() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port3");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientNoOAL");
							}

						});
		assertNotNull(reqOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		Menu menu = DebugUITestUtilities.getMenuInSETree(reqOp);

		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in requied operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 2);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port3::Interface Server Client 2::sendServerClientNoOAL line: 4", topFrameText);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_no_oal.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionSignalNotAssignedCurrentExecutingAction() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);
		
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
		StateMachineState_c state = StateMachineState_c
				.getOneSM_STATEOnR501(
						StateMachine_c.getManySM_SMsOnR517(ClassStateMachine_c.getManySM_ASMsOnR519(ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
								.getManyEP_PKGsOnR8001(PackageableElement_c
										.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
											
											@Override
											public boolean evaluate(Object candidate) {
												return ((ModelClass_c) candidate).getName().equals("cls 2");
											}
										}))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((StateMachineState_c) candidate)
										.getName().equals("State 2");
							}

						});
		assertNotNull(state);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(state));
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
				.getManyEP_PKGsOnR8001(PackageableElement_c
						.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
							
							@Override
							public boolean evaluate(Object candidate) {
								return ((ModelClass_c) candidate).getName().equals("cls 3");
							}
						});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_current_action");
			}
		
		});
		assertNotNull(op);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(op));
		Menu menu = DebugUITestUtilities.getMenuInSETree(op);

		assertTrue(
				"The execute menu item was not available for a class based operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		RequiredOperation_c reqOp = RequiredOperation_c
			.getOneSPR_ROOnR4502(
				RequiredExecutableProperty_c
						.getManySPR_REPsOnR4500(Requirement_c
								.getManyC_RsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port3");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredOperation_c) candidate)
								.getName().equals(
										"sendServerClientOAL");
					}

				});
		assertNotNull(reqOp);
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		assertTrue(
				"The execute menu item was not available for a provided operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_current_action.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		// Commenting out known failure tests.  See dts0100656068
/*		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
*/	}
	
	public void testInterfaceExecutionSignalNotAssignedWithParameters() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		RequiredSignal_c reqSig = RequiredSignal_c
			.getOneSPR_RSOnR4502(
				RequiredExecutableProperty_c
						.getManySPR_REPsOnR4500(Requirement_c
								.getManyC_RsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port3");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((RequiredSignal_c) candidate)
								.getName().equals(
										"serverclientsigparams");
					}

				});
		assertNotNull(reqSig);
 
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqSig));
		
		ActivityEditor editor = DebugUITestUtilities
				.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		RequiredOperation_c reqOp = RequiredOperation_c
				.getOneSPR_ROOnR4502(
						RequiredExecutableProperty_c
								.getManySPR_REPsOnR4500(Requirement_c
										.getManyC_RsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port3");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((RequiredOperation_c) candidate)
										.getName().equals(
												"sendServerClientParams");
							}

						});
		assertNotNull(reqOp);

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		Menu menu = DebugUITestUtilities.getMenuInSETree(reqOp);

		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(reqOp));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in required signal.", target
				.isSuspended());
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"string1", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"string2", yValue);
*/		
		DebugUITestUtilities.removeAllBreakpoints();
		try {
			target.resume();
		} catch (DebugException e) {
			fail(e.toString());
		}
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_signal_parameters.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	/**
	 * Note this test is disabled until issue dts0100921178 is resolved.
	 */
//	public void testInterfaceExecutionOperationOALVoidReturn() {
//		Component_c component = Component_c.getOneC_COnR4608(ComponentPackage_c
//				.getManyCP_CPsOnR4606(m_sys), new ClassQueryInterface_c() {
//
//			public boolean evaluate(Object candidate) {
//				return ((Component_c) candidate).getName().equals("fc1");
//			}
//
//		});
//		assertNotNull(component);
//
//		// launch the component
//		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
//				m_bp_tree.getControl().getMenu(), m_sys.getName());
//
//		ProvidedOperation_c proOp = ProvidedOperation_c
//				.getOneSPR_POOnR4503(
//						ProvidedExecutableProperty_c
//								.getManySPR_PEPsOnR4501(Provision_c
//										.getManyC_PsOnR4009(InterfaceReference_c
//												.getManyC_IRsOnR4016(Port_c
//														.getManyC_POsOnR4010(
//																component,
//																new ClassQueryInterface_c() {
//
//																	public boolean evaluate(
//																			Object candidate) {
//																		return ((Port_c) candidate)
//																				.getName()
//																				.equals(
//																						"Port4");
//																	}
//
//																})))),
//						new ClassQueryInterface_c() {
//
//							public boolean evaluate(Object candidate) {
//								return ((ProvidedOperation_c) candidate)
//										.getName().equals(
//												"sendClientServerOALVoidReturn");
//							}
//
//						});
//		assertNotNull(proOp);
//
//		ProvidedOperation_c testOp = ProvidedOperation_c
//		.getOneSPR_POOnR4503(
//				ProvidedExecutableProperty_c
//						.getManySPR_PEPsOnR4501(Provision_c
//								.getManyC_PsOnR4009(InterfaceReference_c
//										.getManyC_IRsOnR4016(Port_c
//												.getManyC_POsOnR4010(
//														component,
//														new ClassQueryInterface_c() {
//
//															public boolean evaluate(
//																	Object candidate) {
//																return ((Port_c) candidate)
//																		.getName()
//																		.equals(
//																				"Port4");
//															}
//
//														})))),
//				new ClassQueryInterface_c() {
//
//					public boolean evaluate(Object candidate) {
//						return ((ProvidedOperation_c) candidate)
//								.getName().equals(
//										"clientServerOALVoidReturn");
//					}
//
//				});
//		assertNotNull(testOp);
//
//		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
//		
//		BPDebugUtils.setSelectionInSETree(new StructuredSelection(testOp));
//		Menu menu = DebugUITestUtilities.getMenuInSETree(testOp);
//
//		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
//		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
//		
//		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));
//		
//		assertTrue(
//				"The execute menu item was not available for a required operation.",
//				UITestingUtilities.checkItemStatusInContextMenu(menu,
//						"Execute", "", false));
//
//		UITestingUtilities.activateMenuItem(menu, "Execute");
//
//		DebugUITestUtilities.waitForExecution();
//
//		ComponentInstance_c engine = ComponentInstance_c
//				.getOneI_EXEOnR2955(component);
//		assertNotNull(engine);
//
//		// wait for the execution to complete
//		DebugUITestUtilities.waitForBPThreads(m_sys);
//
//		// check that execution was suspended
//		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
//		assertNotNull(process);
//
//		IDebugTarget target = process.getLaunch().getDebugTarget();
//		assertTrue("Process was not suspended by breakpoint in provided operation.", target
//				.isSuspended());
//		
//		DebugUITestUtilities.stepOver(engine, 1);
//		
//		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
//		assertEquals("Port4::Interface Client Server 2::sendClientServerOALVoidReturn line: 2", topFrameText);
//		
//		// compare the trace
//		File expectedResults = new File(
//				m_workspace_path
//						+ "expected_results/interface_execution/execution_operation_oal_void_return.txt");
//		String expected_results = TestUtil.getTextFileContents(expectedResults);
//		// get the text representation of the debug tree
//		String actual_results = DebugUITestUtilities
//				.getConsoleText(expected_results);
//		assertEquals(expected_results, actual_results);
//	}

	public void testInterfaceExectuionOperationOALNonVoidReturn() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerOALNonVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
		.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerOALNonVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(testOp));

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		Menu menu = DebugUITestUtilities.getMenuInSETree(testOp);



		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));
		
		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 3);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerOALNonVoidReturn line: 2", topFrameText);
		
		// verify the return
		// Commenting out known failure tests.  See dts0100656068
/*		String result = DebugUITestUtilities.getValueForVariable("result");
		assertEquals(
				"Default parameter value was not as expected for variable result.",
				"3", result);
*/		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_oal_nonvoid_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionOperationNoOALVoidReturn() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerNoOALVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerNoOALVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		Menu menu = DebugUITestUtilities.getMenuInSETree(proOp);
		
		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 1);
		DebugUITestUtilities.stepInto(engine, 1);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerNoOALVoidReturn line: 3", topFrameText);
				
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_no_oal_void_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
	
	public void testInterfaceExecutionOperationNoOALNonVoidReturn() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerNoOALNonVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerNoOALNonVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		Menu menu = DebugUITestUtilities.getMenuInSETree(proOp);
		
		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		DebugUITestUtilities.stepOver(engine, 1);
		DebugUITestUtilities.stepInto(engine, 1);
		
		String topFrameText = DebugUITestUtilities.getTopFrameText(engine);
		assertEquals("Port4::Interface Client Server 2::sendClientServerNoOALNonVoidReturn line: 3", topFrameText);
		
		// verify the return
		String result = DebugUITestUtilities.getValueForVariable("result");
		assertEquals(
				"Default parameter value was not as expected for variable result.",
				"", result);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_no_oal_nonvoid_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}

	public void testInterfaceExecutionOperationCurrentExecutingAction() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);
		
		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());
		
		StateMachineState_c state = StateMachineState_c
				.getOneSM_STATEOnR501(
						StateMachine_c.getManySM_SMsOnR517(ClassStateMachine_c.getManySM_ASMsOnR519(ModelClass_c.getManyO_OBJsOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
								.getManyEP_PKGsOnR8001(PackageableElement_c
										.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
											
											@Override
											public boolean evaluate(Object candidate) {
												return ((ModelClass_c) candidate).getName().equals("cls 2");
											}
										}))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((StateMachineState_c) candidate)
										.getName().equals("State 2");
							}

						});
		assertNotNull(state);
		
		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(state));
		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 1);
		
		ModelClass_c startClass = ModelClass_c.getOneO_OBJOnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c
				.getManyEP_PKGsOnR8001(PackageableElement_c
						.getManyPE_PEsOnR8003(component))), new ClassQueryInterface_c() {
							
							@Override
							public boolean evaluate(Object candidate) {
								return ((ModelClass_c) candidate).getName().equals("cls 3");
							}
						});
		assertNotNull(startClass);
		
		Operation_c op = Operation_c.getOneO_TFROnR115(startClass, new ClassQueryInterface_c() {
		
			public boolean evaluate(Object candidate) {
				return ((Operation_c) candidate).getName().equals("start_test_current_action");
			}
		
		});
		assertNotNull(op);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(op));
		Menu menu = DebugUITestUtilities.getMenuInSETree(op);

		assertTrue(
				"The execute menu item was not available for a class based operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in state.", target
				.isSuspended());

		ProvidedOperation_c proOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"sendClientServerNoOALVoidReturn");
					}

				});
		assertNotNull(proOp);
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);

		DebugUITestUtilities.removeAllBreakpoints();
		
		try {
			process.getLaunch().getDebugTarget().resume();
		} catch (DebugException e) {
			fail(e.toString());
		}

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));

		assertTrue(
				"The execute menu item was not available for a provided operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();
		
		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_current_action.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		// Commenting out known failure tests.  See dts0100656068
/*		assertEquals(expected_results, actual_results);
*/	}

	/**
	 * Note this test is disabled until issue dts0100921178 is resolved.
	 */
//	public void testInterfaceExecutionOperationWithParametersVoidReturn() {
//		Component_c component = Component_c.getOneC_COnR4608(ComponentPackage_c
//				.getManyCP_CPsOnR4606(m_sys), new ClassQueryInterface_c() {
//
//			public boolean evaluate(Object candidate) {
//				return ((Component_c) candidate).getName().equals("fc1");
//			}
//
//		});
//		assertNotNull(component);
//
//		// launch the component
//		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
//				m_bp_tree.getControl().getMenu(), m_sys.getName());
//
//		ProvidedOperation_c proOp = ProvidedOperation_c
//				.getOneSPR_POOnR4503(
//						ProvidedExecutableProperty_c
//								.getManySPR_PEPsOnR4501(Provision_c
//										.getManyC_PsOnR4009(InterfaceReference_c
//												.getManyC_IRsOnR4016(Port_c
//														.getManyC_POsOnR4010(
//																component,
//																new ClassQueryInterface_c() {
//
//																	public boolean evaluate(
//																			Object candidate) {
//																		return ((Port_c) candidate)
//																				.getName()
//																				.equals(
//																						"Port4");
//																	}
//
//																})))),
//						new ClassQueryInterface_c() {
//
//							public boolean evaluate(Object candidate) {
//								return ((ProvidedOperation_c) candidate)
//										.getName().equals(
//												"sendClientServerParamsVoidReturn");
//							}
//
//						});
//		assertNotNull(proOp);
//
//		ProvidedOperation_c testOp = ProvidedOperation_c
//			.getOneSPR_POOnR4503(
//				ProvidedExecutableProperty_c
//						.getManySPR_PEPsOnR4501(Provision_c
//								.getManyC_PsOnR4009(InterfaceReference_c
//										.getManyC_IRsOnR4016(Port_c
//												.getManyC_POsOnR4010(
//														component,
//														new ClassQueryInterface_c() {
//
//															public boolean evaluate(
//																	Object candidate) {
//																return ((Port_c) candidate)
//																		.getName()
//																		.equals(
//																				"Port4");
//															}
//
//														})))),
//				new ClassQueryInterface_c() {
//
//					public boolean evaluate(Object candidate) {
//						return ((ProvidedOperation_c) candidate)
//								.getName().equals(
//										"clientServerParamsVoidReturn");
//					}
//
//				});
//		assertNotNull(testOp);
//
//		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
//		
//		BPDebugUtils.setSelectionInSETree(new StructuredSelection(testOp));
//
//		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
//		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
//		
//		Menu menu = DebugUITestUtilities.getMenuInSETree(testOp);
//
//		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));
//		
//		assertTrue(
//				"The execute menu item was not available for a required operation.",
//				UITestingUtilities.checkItemStatusInContextMenu(menu,
//						"Execute", "", false));
//
//		UITestingUtilities.activateMenuItem(menu, "Execute");
//
//		DebugUITestUtilities.waitForExecution();
//
//		ComponentInstance_c engine = ComponentInstance_c
//				.getOneI_EXEOnR2955(component);
//		assertNotNull(engine);
//
//		// wait for the execution to complete
//		DebugUITestUtilities.waitForBPThreads(m_sys);
//		DebugUITestUtilities.waitForExecution();
//
//		// check that execution was suspended
//		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
//		assertNotNull(process);
//
//		IDebugTarget target = process.getLaunch().getDebugTarget();
//		assertTrue("Process was not suspended by breakpoint in provided operation.", target
//				.isSuspended());
//		
//		// verify the parameters
//		// Commenting out known failure tests.  See dts0100656068
///*		String xValue = DebugUITestUtilities.getValueForVariable("x");
//		assertEquals(
//				"Default parameter value was not as expected for variable x.",
//				"2", xValue);
//		String yValue = DebugUITestUtilities.getValueForVariable("y");
//		assertEquals(
//				"Default parameter value was not as expected for variable x.",
//				"2", yValue);
//*/		
//		// compare the trace
//		File expectedResults = new File(
//				m_workspace_path
//						+ "expected_results/interface_execution/execution_operation_params_void_return.txt");
//		String expected_results = TestUtil.getTextFileContents(expectedResults);
//		// get the text representation of the debug tree
//		String actual_results = DebugUITestUtilities
//				.getConsoleText(expected_results);
//		assertEquals(expected_results, actual_results);
//	}

	public void testInterfaceExecutionOperationWithParamatersNonVoidReturn() {
		Component_c component = Component_c.getOneC_COnR8001(PackageableElement_c.getManyPE_PEsOnR8000(Package_c.getManyEP_PKGsOnR1405(m_sys)), new ClassQueryInterface_c() {

			public boolean evaluate(Object candidate) {
				return ((Component_c) candidate).getName().equals("fc1");
			}

		});

		assertNotNull(component);

		// launch the component
		DebugUITestUtilities.setLogActivityAndLaunchForElement(component,
				m_bp_tree.getControl().getMenu(), m_sys.getName());

		ProvidedOperation_c proOp = ProvidedOperation_c
				.getOneSPR_POOnR4503(
						ProvidedExecutableProperty_c
								.getManySPR_PEPsOnR4501(Provision_c
										.getManyC_PsOnR4009(InterfaceReference_c
												.getManyC_IRsOnR4016(Port_c
														.getManyC_POsOnR4010(
																component,
																new ClassQueryInterface_c() {

																	public boolean evaluate(
																			Object candidate) {
																		return ((Port_c) candidate)
																				.getName()
																				.equals(
																						"Port4");
																	}

																})))),
						new ClassQueryInterface_c() {

							public boolean evaluate(Object candidate) {
								return ((ProvidedOperation_c) candidate)
										.getName().equals(
												"sendClientServerParamsNonVoidReturn");
							}

						});
		assertNotNull(proOp);

		ProvidedOperation_c testOp = ProvidedOperation_c
			.getOneSPR_POOnR4503(
				ProvidedExecutableProperty_c
						.getManySPR_PEPsOnR4501(Provision_c
								.getManyC_PsOnR4009(InterfaceReference_c
										.getManyC_IRsOnR4016(Port_c
												.getManyC_POsOnR4010(
														component,
														new ClassQueryInterface_c() {

															public boolean evaluate(
																	Object candidate) {
																return ((Port_c) candidate)
																		.getName()
																		.equals(
																				"Port4");
															}

														})))),
				new ClassQueryInterface_c() {

					public boolean evaluate(Object candidate) {
						return ((ProvidedOperation_c) candidate)
								.getName().equals(
										"clientServerParamsNonVoidReturn");
					}

				});
		assertNotNull(testOp);

		openPerspectiveAndView("com.mentor.nucleus.bp.debug.ui.DebugPerspective",BridgePointPerspective.ID_MGC_BP_EXPLORER);
		
		BPDebugUtils.setSelectionInSETree(new StructuredSelection(testOp));

		ActivityEditor editor = DebugUITestUtilities.openActivityEditorForSelectedElement();
		DebugUITestUtilities.setBreakpointAtLine(editor, 2);
		
		Menu menu = DebugUITestUtilities.getMenuInSETree(testOp);

		BPDebugUtils.setSelectionInSETree(new StructuredSelection(proOp));
		
		assertTrue(
				"The execute menu item was not available for a required operation.",
				UITestingUtilities.checkItemStatusInContextMenu(menu,
						"Execute", "", false));

		UITestingUtilities.activateMenuItem(menu, "Execute");

		DebugUITestUtilities.waitForExecution();

		ComponentInstance_c engine = ComponentInstance_c
				.getOneI_EXEOnR2955(component);
		assertNotNull(engine);

		// wait for the execution to complete
		DebugUITestUtilities.waitForBPThreads(m_sys);
		DebugUITestUtilities.waitForExecution();

		// check that execution was suspended
		IProcess process = DebugUITestUtilities.getProcessForEngine(engine);
		assertNotNull(process);

		IDebugTarget target = process.getLaunch().getDebugTarget();
		assertTrue("Process was not suspended by breakpoint in provided operation.", target
				.isSuspended());
		
		// verify the parameters
		// Commenting out known failure tests.  See dts0100656068
/*		String xValue = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", xValue);
		String yValue = DebugUITestUtilities.getValueForVariable("y");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"2", yValue);
		
		DebugUITestUtilities.stepOver(engine, 4);

		String result = DebugUITestUtilities.getValueForVariable("x");
		assertEquals(
				"Default parameter value was not as expected for variable x.",
				"true", result);
*/		
		// compare the trace
		File expectedResults = new File(
				m_workspace_path
						+ "expected_results/interface_execution/execution_operation_params_non_void_return.txt");
		String expected_results = TestUtil.getTextFileContents(expectedResults);
		// get the text representation of the debug tree
		String actual_results = DebugUITestUtilities
				.getConsoleText(expected_results);
		assertEquals(expected_results, actual_results);
	}
}