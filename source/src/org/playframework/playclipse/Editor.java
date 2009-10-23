/*
 * Playclipse - Eclipse plugin for the Play! Framework
 * Copyright 2009 Zenexity
 *
 * This file is licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.playframework.playclipse;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import tk.eclipse.plugin.htmleditor.editors.HTMLSourceEditor;
import tk.eclipse.plugin.htmleditor.views.IPaletteTarget;

/**
 * A helper class to handle the Eclipse-specific heavy lifting to access and
 * manipulate editors and their associated documents.
 *
 */
public final class Editor {

	private ITextEditor textEditor;

	public Editor(ITextEditor textEditor) {
		this.textEditor = textEditor;
	}

	/**
	 * Static Factory Method: Creates an Editor corresponding to the ITextEditor
	 * the user is currently interacting with.
	 *
	 * @param event
	 * @throws ExecutionException
	 */
	public static Editor getCurrent(ExecutionEvent event)
			throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor instanceof ITextEditor) {
			return new Editor((ITextEditor) editor);
		} else if (editor instanceof IPaletteTarget) {
			HTMLSourceEditor sourceEditor = ((IPaletteTarget) editor)
					.getPaletteTarget();
			return new Editor((ITextEditor) sourceEditor);
		} else {
			// Unknown editor... TODO: handle error
			return null;
		}
	}

	public int getCurrentLineNo() {
		return this.getTextSelection().getStartLine();
	}

	public int lineCount() {
		return getDocument().getNumberOfLines();
	}

	public String getTitle() {
		return textEditor.getTitle();
	}

	public String enclosingDirectory() {
		IPath path = getFilePath();
		return path.segment(path.segmentCount() - 2);
	}

	/**
	 *
	 * @return true if the file owned by the editor corresponds to a view
	 */
	public boolean isView() {
		IPath path = getFilePath();
		return path.segment(path.segmentCount() - 3).equals("views");
	}

	/**
	 *
	 * @return true if the file owned by the editor is the conf/routes files
	 */
	public boolean isRoutes() {
		IPath path = getFilePath();
		return path.segment(path.segmentCount() - 1).equals("routes");
	}

	private IPath getFilePath() {
		IFileEditorInput input = (IFileEditorInput) textEditor.getEditorInput();
		return input.getFile().getFullPath();
	}

	public String getLine(int lineNo) {
		IDocument doc = this.getDocument();
		try {
			return doc
					.get(doc.getLineOffset(lineNo), doc.getLineLength(lineNo));
		} catch (BadLocationException e) {
			return null;
		}
	}

	public String getCurrentLine() {
		return getLine(getCurrentLineNo());
	}

	private ITextSelection getTextSelection() {
		return ((ITextSelection) textEditor.getSelectionProvider()
				.getSelection());
	}

	public IDocument getDocument() {
		return textEditor.getDocumentProvider().getDocument(
				textEditor.getEditorInput());
	}

}