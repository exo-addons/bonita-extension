package org.exoplatform.consulting;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

public abstract class AbstractCraUploadProcessVariablesImpl extends
		AbstractConnector {

	protected final static String BASEDESTINATIONURI_INPUT_PARAMETER = "baseDestinationUri";
	protected final static String SALESORDER_INPUT_PARAMETER = "salesOrder";
	protected final static String CUSTOMER_INPUT_PARAMETER = "customer";
	protected final static String YEAR_INPUT_PARAMETER = "year";
	protected final static String WEEKORMONTH_INPUT_PARAMETER = "weekOrMonth";
	protected final static String WEEKORMONTHNUMBER_INPUT_PARAMETER = "weekOrMonthNumber";
	protected final static String PROJECTNAME_INPUT_PARAMETER = "projectName";
	protected final static String USERNAME_INPUT_PARAMETER = "userName";
	protected final static String TYPE_INPUT_PARAMETER = "type";
	protected final static String FILE_INPUT_PARAMETER = "file";
	protected final String INPUTSTREAM_OUTPUT_PARAMETER = "inputStream";
	protected final String DESTINATIONURI_OUTPUT_PARAMETER = "destinationUri";
	protected final String MIMETYPE_OUTPUT_PARAMETER = "mimeType";

	protected final java.lang.String getBaseDestinationUri() {
		return (java.lang.String) getInputParameter(BASEDESTINATIONURI_INPUT_PARAMETER);
	}

	protected final java.lang.String getSalesOrder() {
		return (java.lang.String) getInputParameter(SALESORDER_INPUT_PARAMETER);
	}

	protected final java.lang.String getCustomer() {
		return (java.lang.String) getInputParameter(CUSTOMER_INPUT_PARAMETER);
	}

	protected final java.lang.Integer getYear() {
		return (java.lang.Integer) getInputParameter(YEAR_INPUT_PARAMETER);
	}

	protected final java.lang.String getWeekOrMonth() {
		return (java.lang.String) getInputParameter(WEEKORMONTH_INPUT_PARAMETER);
	}

	protected final java.lang.Integer getWeekOrMonthNumber() {
		return (java.lang.Integer) getInputParameter(WEEKORMONTHNUMBER_INPUT_PARAMETER);
	}

	protected final java.lang.String getProjectName() {
		return (java.lang.String) getInputParameter(PROJECTNAME_INPUT_PARAMETER);
	}

	protected final java.lang.String getUserName() {
		return (java.lang.String) getInputParameter(USERNAME_INPUT_PARAMETER);
	}

	protected final java.lang.String getType() {
		return (java.lang.String) getInputParameter(TYPE_INPUT_PARAMETER);
	}

	protected final org.bonitasoft.engine.bpm.document.DocumentValue getFile() {
		return (org.bonitasoft.engine.bpm.document.DocumentValue) getInputParameter(FILE_INPUT_PARAMETER);
	}

	protected final void setInputStream(java.io.InputStream inputStream) {
		setOutputParameter(INPUTSTREAM_OUTPUT_PARAMETER, inputStream);
	}

	protected final void setDestinationUri(java.lang.String destinationUri) {
		setOutputParameter(DESTINATIONURI_OUTPUT_PARAMETER, destinationUri);
	}

	protected final void setMimeType(java.lang.String mimeType) {
		setOutputParameter(MIMETYPE_OUTPUT_PARAMETER, mimeType);
	}

	@Override
	public void validateInputParameters() throws ConnectorValidationException {
		try {
			getBaseDestinationUri();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"baseDestinationUri type is invalid");
		}
		try {
			getSalesOrder();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("salesOrder type is invalid");
		}
		try {
			getCustomer();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("customer type is invalid");
		}
		try {
			getYear();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("year type is invalid");
		}
		try {
			getWeekOrMonth();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"weekOrMonth type is invalid");
		}
		try {
			getWeekOrMonthNumber();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"weekOrMonthNumber type is invalid");
		}
		try {
			getProjectName();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException(
					"projectName type is invalid");
		}
		try {
			getUserName();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("userName type is invalid");
		}
		try {
			getType();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("type type is invalid");
		}
		try {
			getFile();
		} catch (ClassCastException cce) {
			throw new ConnectorValidationException("file type is invalid");
		}

	}

}
