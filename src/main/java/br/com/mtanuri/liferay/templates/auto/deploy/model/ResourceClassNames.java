package br.com.mtanuri.liferay.templates.auto.deploy.model;

public enum ResourceClassNames {

	PortletDisplayTemplate(25403, "com.liferay.portlet.display.template.PortletDisplayTemplate"),
	JournalArticle(20401, "com.liferay.journal.model.JournalArticle");

	private long resourceClassNameId;
	private String value;

	private ResourceClassNames(long resourceClassNameId, String value) {
		this.resourceClassNameId = resourceClassNameId;
		this.value = value;
	}

	public long getResourceClassNameId() {
		return resourceClassNameId;
	}

	public String getValue() {
		return value;
	}
}