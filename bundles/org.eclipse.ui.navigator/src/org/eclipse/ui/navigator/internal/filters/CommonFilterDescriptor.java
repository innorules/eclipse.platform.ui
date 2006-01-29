/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.navigator.internal.filters;

import org.eclipse.core.expressions.Expression;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.navigator.ICommonFilterDescriptor;
import org.eclipse.ui.navigator.internal.CustomAndExpression;
import org.eclipse.ui.navigator.internal.NavigatorPlugin;
import org.eclipse.ui.navigator.internal.extensions.INavigatorContentExtPtConstants;

/**
 * 
 * Describes a <commonFilter /> element under a
 * <b>org.eclipse.ui.navigator.navigatorContent</b> extension.
 * 
 */
public class CommonFilterDescriptor implements ICommonFilterDescriptor,
		INavigatorContentExtPtConstants {

	private IConfigurationElement element;

	private Expression filterExpression;

	private String id;

	protected CommonFilterDescriptor(IConfigurationElement anElement) {

		element = anElement;
		init();
	}

	private void init() {
		id = element.getAttribute(ATT_ID);
		if (id == null)
			id = ""; //$NON-NLS-1$
		IConfigurationElement[] children = element
				.getChildren(TAG_FILTER_EXPRESSION);
		if (children.length == 1)
			filterExpression = new CustomAndExpression(children[0]);
	}

	/**
	 * 
	 * @return An identifier used to determine whether the filter is visible.
	 *         May not be unique.
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return A translated name to identify the filter
	 */
	public String getName() {
		return element.getAttribute(ATT_NAME);
	}

	/**
	 * 
	 * @return A translated description to explain to the user what the defined
	 *         filter will hide from the view.
	 */
	public String getDescription() {
		return element.getAttribute(ATT_DESCRIPTION);
	}

	/**
	 * 
	 * @return Indicates the filter should be in an "Active" state by default.
	 */
	public boolean isActiveByDefault() {
		return Boolean.valueOf(element.getAttribute(ATT_ACTIVE_BY_DEFAULT))
				.booleanValue();
	}

	/**
	 * 
	 * @return An instance of the ViewerFilter defined by the extension. Callers
	 *         of this method are responsible for managing the instantiated
	 *         filter.
	 */
	public ViewerFilter createFilter() {
		try {

			if (filterExpression != null) {

				if (element.getAttribute(ATT_CLASS) != null)
					NavigatorPlugin
							.log(
									IStatus.WARNING,
									0,
									"A <commonFilter /> was specified in " + //$NON-NLS-1$
									element.getDeclaringExtension().getNamespace() + 
									" which specifies a \"class\" attribute and an Core Expression.\n" + //$NON-NLS-1$
									"Only the Core Expression will be respected.", null); //$NON-NLS-1$

				return new CoreExpressionFilter(filterExpression);
			}

			return (ViewerFilter) element.createExecutableExtension(ATT_CLASS);
		} catch (RuntimeException re) {
			NavigatorPlugin.logError(0, re.getMessage(), re);
			return SkeletonViewerFilter.INSTANCE;

		} catch (CoreException e) {
			NavigatorPlugin.logError(0, e.getMessage(), e);
			return SkeletonViewerFilter.INSTANCE;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "CommonFilterDescriptor["+getName()+" ("+getId()+")]";   //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}
}
