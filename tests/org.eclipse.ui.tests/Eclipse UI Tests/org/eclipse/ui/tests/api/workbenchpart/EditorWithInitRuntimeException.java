/*******************************************************************************
 * Copyright (c) 2005, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jeanderson Candido <http://jeandersonbc.github.io> - Bug 444070
 *******************************************************************************/
package org.eclipse.ui.tests.api.workbenchpart;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.part.EditorPart;

public class EditorWithInitRuntimeException extends EditorPart {

    @Override
	public void doSave(IProgressMonitor monitor) {

    }

    @Override
	public void doSaveAs() {

    }

    @Override
	public void init(IEditorSite site, IEditorInput input) {

        throw new RuntimeException("This exception was thrown intentionally as part of an error handling test");
    }

    @Override
	public boolean isDirty() {
        return false;
    }

    @Override
	public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
	public void createPartControl(Composite parent) {

        parent.setLayout(new FillLayout());

        Label message = new Label(parent, SWT.NONE);
        message.setText("This editor threw an exception on init. You should not be able to read this");
    }

    @Override
	public void setFocus() {

    }

}
