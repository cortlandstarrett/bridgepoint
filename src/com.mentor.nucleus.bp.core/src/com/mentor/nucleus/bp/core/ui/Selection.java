//========================================================================
//
//File:      $RCSfile: Selection.java,v $
//Version:   $Revision: 1.20 $
//Modified:  $Date: 2012/06/21 02:38:40 $
//
//(c) Copyright 2004-2012 by Mentor Graphics Corp. All rights reserved.
//
//========================================================================
//This document contains information proprietary and confidential to
//Mentor Graphics Corp., and is not for external distribution.
//======================================================================== 

package com.mentor.nucleus.bp.core.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import com.mentor.nucleus.bp.core.Ooaofooa;
import com.mentor.nucleus.bp.core.common.ModelElementFileFacade;
import com.mentor.nucleus.bp.core.common.NonRootModelElement;

/**
 * Represents which model elements are currently selected in the UI.
 * The set of such elements may span multiple domains.
 */
public class Selection implements ISelectionProvider
{
    /**
     * The singleton instance of this class.
     */
    private static Selection instance = new Selection();
    
    /**
     * Which model elements are currently selected in the UI.
     */
    private List elements = new ArrayList();
    
    /**
     * Those objects wishing to be informed about changes to the selection
     * recorded by this object.
     */
    private ListenerList selectionChangedListeners = new ListenerList(3);
    
    /**
     * See field.
     */
    public static Selection getInstance()
    {
        if (instance == null) instance = new Selection();
        return instance;
    }

    private Object[] getSelectedElements(){
    	Vector result = new Vector(){
    	    public boolean add(Object element){
    	        if(!contains(element)){
    	            return super.add(element);
    	        }
    	        return false;
    	    }
    	};
		for (Iterator iter = elements.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if(element instanceof ModelElementFileFacade)
				result.add(((ModelElementFileFacade)element).getModelElement());
			else
				result.add(element);
		}
		return result.toArray();
    }
	/**
	 * Returns which model elements are currently selected in the UI.
	 * This method <b>should be</b> used from BP source. 
	 */
	public IStructuredSelection getStructuredSelection() 
	{
		return new StructuredSelection(getSelectedElements());
	}
	
	public NonRootModelElement[] getSelectedNonRootModelElements() {
		Object[] selectedObjects = getStructuredSelection().toArray();
		ArrayList<NonRootModelElement> list = new ArrayList<NonRootModelElement>();
		for(int i = 0; i < selectedObjects.length; i++) {
			if(selectedObjects[i] instanceof NonRootModelElement) {
				list.add((NonRootModelElement) selectedObjects[i]);
			}
		}
		return list.toArray(new NonRootModelElement[list.size()]);
	}
	

    /**
     * Returns which model elements and their wrapper are currently selected in the UI.
     * This method <b>should not</b> be used from BP source. Use <code>getStructuredSelection</code>
     */

    public ISelection getSelection() 
    {
    	return new StructuredSelection(elements);
    }

    /**
     * Returns whether this selection includes the given element.
     */
    public boolean contains(Object element)
    {
        return elements.contains(element);
    }

    /**
     * Removes all model elements from this selection.
     */
    public void clear()
    {
        elements.clear();
        StructuredSelection empty = new StructuredSelection();
        fireSelectionChanged(new SelectionChangedEvent(this, empty));
    }

    /**
     * Adds the given listener to this object's collection of such listeners. 
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener)
    {
        selectionChangedListeners.add(listener);
    }

    /**
     * Removes the given listener from this object's collection of such listeners. 
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener)
    {
        selectionChangedListeners.remove(listener);
    }

    /**
     * Makes the given selection the one currently recorded by this object.
     */
    public void setSelection(ISelection selection)
    {
    	setSelection(selection, true);
    }
    
    public void setSelection(ISelection selection, boolean fireChange) {
    	selection = adaptSelection(selection);
        // if the given selection is different from the current one
        if (elements.size() != ((StructuredSelection)selection).size()
            || !elements.containsAll(((StructuredSelection)selection).toList())) {
            // wipe out the previous selection
            elements.clear();
            
            // set the given selection into this object
            elements.addAll(((IStructuredSelection)selection).toList());
            if(fireChange)
            	fireSelectionChanged(new SelectionChangedEvent(this, selection));
        }
    }

	/**
     * Adds the given element to the current selection.
     */
    public void addToSelection(Object element)
    {
    	addToSelection(element, true);
    }

    public void addToSelection(Object element, boolean fireChange) {
    	if(!(element instanceof NonRootModelElement)) {
    		element = adaptElement(element);
    	}
    	if(element != null) {
        	elements.add(element);
        	if(fireChange)
        		fireSelectionChanged(new SelectionChangedEvent(this, new StructuredSelection(elements)));
    	}
    }
    /**
     * Removes the given element from the current selection.
     */
    public void removeFromSelection(Object element)
    {
    	removeFromSelection(element, true);
    }
    
	public void removeFromSelection(Object element, boolean fireChange) {
		if(!(element instanceof NonRootModelElement))
			element = adaptElement(element);
        // if the current selection does not include the given element,
        // there is nothing to do
        if (!elements.contains(element)) return;
        
        elements.remove(element);
        if(fireChange)
        	fireSelectionChanged(new SelectionChangedEvent(this, new StructuredSelection(elements)));
	}

    /**
     * Notifies any selection-changed listeners that the selection has changed. 
     */
    private void fireSelectionChanged(final SelectionChangedEvent event)
    {
        // for each selection-changed listener
        Object[] listeners = selectionChangedListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ISelectionChangedListener listener = (ISelectionChangedListener) listeners[i];
            
            // in a new thread, to avoid stalling the UI/event thread on which this is 
            // likely being called
            Platform.run(new SafeRunnable() {
                public void run() {
                    // notify the listener
                    listener.selectionChanged(event);
                }

                public void handleException(Throwable e) {
                    super.handleException(e);

                    // remove the offending listener, so there won't be exception from it 
                    // again the next time this is called 
                    removeSelectionChangedListener(listener);
                }
            });
        }
    }
    
    /**
     * Returns the domain associated with the first model element 
     * in the given selection, which is assumed to be non-empty.
     */
    public static Ooaofooa getModelRoot(StructuredSelection selection)
    {
        NonRootModelElement element = (NonRootModelElement)selection.getFirstElement();
        return (Ooaofooa)(element.getModelRoot());
    }
    
    /**
     * Returns the domain associated with the first model element 
     * in this selection, which is assumed to be non-empty. 
     */
    public Ooaofooa getModelRoot()
    {
        NonRootModelElement element = (NonRootModelElement)elements.get(0);
        return (Ooaofooa)(element.getModelRoot());
    }

    private ISelection adaptSelection(ISelection selection) {
    	List<Object> adapted = new ArrayList<Object>();
    	IStructuredSelection ss = (IStructuredSelection) selection;
    	for(Object object : ss.toList()) {
    		if(object instanceof NonRootModelElement)
    			adapted.add(object);
    		else {
    			Object adapter = adaptElement(object);
    			if(adapter != null)
    				adapted.add(adapter);
    			else
    				adapted.add(object);
    		}
    	}
    	return new StructuredSelection(adapted);
	}
    
    private Object adaptElement(Object element) {
    	if(element == null)
    		return null;
    	Object adapter = Platform.getAdapterManager().getAdapter(element, NonRootModelElement.class);
    	if(adapter != null)
    		return adapter;
    	if(element instanceof IAdaptable) {
    		adapter = ((IAdaptable) element).getAdapter(NonRootModelElement.class);
    		if(adapter != null) {
    			return adapter;
    		}
    	}
    	return element;
    }
}
