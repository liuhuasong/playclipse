package org.playframework.playclipse.editors.route;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.playframework.playclipse.editors.ActionCompletionProcessor;

import fr.zenexity.pdt.editors.CompletionProcessor;
import fr.zenexity.pdt.editors.Configuration;

public class RouteConfiguration extends Configuration {

	public RouteConfiguration(RouteEditor editor) {
		super(editor);
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		for(String type : editor.getTypes()) {
			if (!type.equals("action")) {
				CompletionProcessor processor = new CompletionProcessor(type, sourceViewer, editor);
				assistant.setContentAssistProcessor(processor, type);
			}
		}
		ActionCompletionProcessor processor = new ActionCompletionProcessor(sourceViewer, editor);
		assistant.setContentAssistProcessor(processor, "action");
		return assistant;
	}

}
