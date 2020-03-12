package br.com.mtanuri.liferay.templates.auto.deploy.model;

public class Template {

	private String getFullName;
	private String script;

	public Template(String getFullName, String script) {
		super();
		this.getFullName = getFullName;
		this.script = script;
	}

	public String getName() {
		return this.getFullName.split("\\.")[0];
	}

	public String getType() {
		return this.getFullName.split("\\.")[1];
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getGetFullName() {
		return getFullName;
	}

	public void setGetFullName(String getFullName) {
		this.getFullName = getFullName;
	}

	@Override
	public String toString() {
		return "Template [getFullName=" + getFullName + ", script=" + script + "]";
	}

}
