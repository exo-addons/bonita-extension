package org.exoplatform.bonitasoft.utils;

public class StringUtils {
	public StringUtils() {
	}

	public static String getCommentLeave(String processName, String stepName) {
		String comment = null;
		comment = "has submitted a \"" + stepName + "\" for a \"" + processName+"\"";
		return comment;
	}

	public static String getCommentPublish(String path) {
		String comment = null;
		String[] pathtab = path.split("/");
		String href = System.getProperty("org.exoplatform.runtime.conf.cas.server.name")+ "/portal/private/intranet/editor?"+ "backto=/portal/private/intranet/bonitaXP" + "&path=";
		comment = " has valided the document <a href='" + href + path + "'>"+ pathtab[pathtab.length - 1] + "</a>";
		return comment;
	}

}
